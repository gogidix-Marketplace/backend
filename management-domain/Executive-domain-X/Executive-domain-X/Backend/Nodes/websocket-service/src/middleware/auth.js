const jwt = require('jsonwebtoken');
const config = require('../config');
const logger = require('../config/logger');
const { RateLimiterRedis } = require('rate-limiter-flexible');
const redis = require('../config/redis');

const rateLimiter = new RateLimiterRedis({
  storeClient: redis.getClient(),
  keyPrefix: 'ws_rate_limit',
  points: config.rateLimit.maxRequests,
  duration: config.rateLimit.windowMs / 1000,
});

class AuthMiddleware {
  constructor() {
    this.connectionTracker = new Map();
  }

  async authenticate(socket, next) {
    try {
      const token = this.extractToken(socket);

      if (!token) {
        return next(new Error('Authentication error: No token provided'));
      }

      const decoded = await this.verifyToken(token);

      if (!decoded) {
        return next(new Error('Authentication error: Invalid token'));
      }

      await this.checkRateLimit(decoded.userId, decoded.tenantId);

      await this.enforceConnectionLimits(decoded.userId, decoded.tenantId);

      socket.data.user = {
        userId: decoded.userId,
        tenantId: decoded.tenantId || config.multiTenant.defaultTenantId,
        executiveId: decoded.executiveId,
        roles: decoded.roles || [],
        permissions: decoded.permissions || [],
        email: decoded.email,
        name: decoded.name
      };

      socket.data.authenticatedAt = new Date();

      logger.info(`User authenticated: ${decoded.userId} (${decoded.tenantId})`);

      next();
    } catch (error) {
      logger.error('Authentication error:', error);
      next(new Error(error.message));
    }
  }

  extractToken(socket) {
    const token = socket.handshake.auth.token ||
                  socket.handshake.headers.authorization?.replace('Bearer ', '') ||
                  socket.handshake.query.token;

    return token;
  }

  async verifyToken(token) {
    try {
      const decoded = jwt.verify(token, config.jwt.secret, {
        issuer: config.jwt.issuer,
        audience: config.jwt.audience
      });

      if (!decoded.userId || !decoded.tenantId && config.multiTenant.enabled) {
        throw new Error('Invalid token payload');
      }

      return decoded;
    } catch (error) {
      if (error.name === 'TokenExpiredError') {
        throw new Error('Token expired');
      } else if (error.name === 'JsonWebTokenError') {
        throw new Error('Invalid token');
      }
      throw error;
    }
  }

  async checkRateLimit(userId, tenantId) {
    try {
      const key = `${tenantId}:${userId}`;
      await rateLimiter.consume(key);
    } catch (error) {
      throw new Error(`Rate limit exceeded: ${error.remainingPoints} points remaining`);
    }
  }

  async enforceConnectionLimits(userId, tenantId) {
    const userConnections = this.connectionTracker.get(`${tenantId}:${userId}`) || 0;
    const tenantConnections = this.countTenantConnections(tenantId);

    if (userConnections >= config.connections.maxPerUser) {
      throw new Error(`Maximum connections per user exceeded (${config.connections.maxPerUser})`);
    }

    if (tenantConnections >= config.connections.maxPerTenant) {
      throw new Error(`Maximum connections per tenant exceeded (${config.connections.maxPerTenant})`);
    }
  }

  countTenantConnections(tenantId) {
    let count = 0;
    for (const [key, value] of this.connectionTracker.entries()) {
      if (key.startsWith(`${tenantId}:`)) {
        count += value;
      }
    }
    return count;
  }

  addConnection(userId, tenantId, socketId) {
    const key = `${tenantId}:${userId}`;
    const current = this.connectionTracker.get(key) || 0;
    this.connectionTracker.set(key, current + 1);
  }

  removeConnection(userId, tenantId) {
    const key = `${tenantId}:${userId}`;
    const current = this.connectionTracker.get(key) || 0;
    if (current <= 1) {
      this.connectionTracker.delete(key);
    } else {
      this.connectionTracker.set(key, current - 1);
    }
  }

  hasPermission(socket, permission) {
    const user = socket.data.user;
    if (!user) return false;

    return user.permissions?.includes(permission) ||
           user.roles?.some(role => this.roleHasPermission(role, permission));
  }

  roleHasPermission(role, permission) {
    const rolePermissions = {
      admin: ['*'],
      executive: ['dashboard:*', 'kpi:*', 'notifications:*'],
      viewer: ['dashboard:view', 'kpi:view'],
      editor: ['dashboard:view', 'dashboard:edit', 'kpi:view', 'kpi:edit']
    };

    const permissions = rolePermissions[role] || [];
    return permissions.includes('*') || permissions.includes(permission);
  }

  hasRole(socket, role) {
    const user = socket.data.user;
    return user?.roles?.includes(role);
  }

  isTenantIsolated(socket, targetTenantId) {
    const user = socket.data.user;
    if (!config.multiTenant.enabled) return true;
    return user?.tenantId === targetTenantId;
  }

  canAccessRoom(socket, room) {
    const user = socket.data.user;
    if (!user) return false;

    const roomTenantId = this.extractTenantIdFromRoom(room);
    if (roomTenantId && roomTenantId !== user.tenantId) {
      return false;
    }

    return true;
  }

  extractTenantIdFromRoom(room) {
    const match = room.match(/^tenant:([^:]+):/);
    return match ? match[1] : null;
  }

  async refreshAuthentication(socket) {
    try {
      const token = this.extractToken(socket);
      if (!token) {
        return { success: false, message: 'No token provided' };
      }

      const decoded = await this.verifyToken(token);
      socket.data.user = {
        userId: decoded.userId,
        tenantId: decoded.tenantId || config.multiTenant.defaultTenantId,
        executiveId: decoded.executiveId,
        roles: decoded.roles || [],
        permissions: decoded.permissions || [],
        email: decoded.email,
        name: decoded.name
      };

      return { success: true, user: socket.data.user };
    } catch (error) {
      logger.error('Token refresh error:', error);
      return { success: false, message: error.message };
    }
  }
}

module.exports = new AuthMiddleware();
