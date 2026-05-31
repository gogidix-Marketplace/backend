/**
 * WebSocket Authentication Middleware
 *
 * Validates JWT tokens for WebSocket connections.
 */

const { verifyToken } = require('../auth/jwt');
const { checkWebSocketRateLimit } = require('./rateLimiter');
const logger = require('../config/logger');

/**
 * WebSocket authentication middleware
 */
async function socketAuthenticate(socket, next) {
  const token = socket.handshake.auth.token || socket.handshake.headers.authorization?.replace('Bearer ', '');

  if (!token) {
    return next(new Error('Authentication token missing'));
  }

  try {
    // Check rate limit first
    const rateLimitResult = await checkWebSocketRateLimit(socket.id);
    if (!rateLimitResult.allowed) {
      return next(new Error(`Rate limit exceeded. Try again in ${rateLimitResult.retryAfter} seconds`));
    }

    // Verify token
    const decoded = await verifyToken(token);

    // Attach user info to socket
    socket.user = {
      tenantId: decoded.tenantId,
      userId: decoded.userId,
      executiveLevel: decoded.executiveLevel || 'ALL',
      roles: decoded.roles || [],
    };

    socket.tenantId = decoded.tenantId;

    logger.debug('WebSocket authenticated', {
      socketId: socket.id,
      userId: socket.user.userId,
      tenantId: socket.user.tenantId,
    });

    next();

  } catch (error) {
    logger.debug('WebSocket authentication failed:', error.message);
    return next(new Error('Authentication failed: ' + error.message));
  }
}

/**
 * Optional WebSocket authentication - doesn't fail if no token
 */
async function optionalSocketAuthenticate(socket, next) {
  const token = socket.handshake.auth.token || socket.handshake.headers.authorization?.replace('Bearer ', '');

  if (!token) {
    socket.user = null;
    socket.tenantId = null;
    return next();
  }

  try {
    const decoded = await verifyToken(token);

    socket.user = {
      tenantId: decoded.tenantId,
      userId: decoded.userId,
      executiveLevel: decoded.executiveLevel || 'ALL',
      roles: decoded.roles || [],
    };

    socket.tenantId = decoded.tenantId;

  } catch (error) {
    // Continue without authentication
    socket.user = null;
    socket.tenantId = null;
  }

  next();
}

/**
 * Verify room access for WebSocket
 */
function canAccessRoom(socket, room) {
  if (!socket.user) {
    return false;
  }

  // Room format: "tenant:{tenantId}", "tenant:{tenantId}:{category}", etc.
  const parts = room.split(':');

  if (parts[0] !== 'tenant') {
    return false;
  }

  if (parts[1] !== socket.user.tenantId) {
    return false;
  }

  // Check executive level restrictions
  if (parts.length > 3) {
    const level = parts[2];
    if (level !== 'ALL' && level !== socket.user.executiveLevel && socket.user.executiveLevel !== 'ALL') {
      return false;
    }
  }

  return true;
}

module.exports = {
  socketAuthenticate,
  optionalSocketAuthenticate,
  canAccessRoom,
};
