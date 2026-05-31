/**
 * Socket.io Server Implementation
 *
 * Provides Socket.io-based WebSocket server with enhanced features.
 */

const { Server } = require('socket.io');
const { verifyToken } = require('../auth/jwt');
const { checkWebSocketRateLimit } = require('../middleware/rateLimiter');
const { connectionManager, ConnectionState } = require('./connectionManager');
const { registerWebSocketServer, getWebSocketServer } = require('./registry');
const { getTenantRoom, getCategoryRoom, getExecutiveLevelRoom, getDashboardRoom } = require('../middleware/tenant');
const logger = require('../config/logger');

/**
 * Socket.io Events
 */
const SocketEvents = {
  CONNECTION: 'connection',
  DISCONNECT: 'disconnect',
  ERROR: 'error',

  // Client events
  AUTHENTICATE: 'authenticate',
  SUBSCRIBE: 'subscribe',
  UNSUBSCRIBE: 'unsubscribe',
  PING: 'ping',
  GET_SUBSCRIPTIONS: 'get_subscriptions',

  // Server events
  AUTHENTICATED: 'authenticated',
  SUBSCRIBED: 'subscribed',
  UNSUBSCRIBED: 'unsubscribed',
  PONG: 'pong',
  KPI_UPDATE: 'kpi_update',
  ALERT: 'alert',
  DASHBOARD_UPDATE: 'dashboard_update',
};

/**
 * Socket.io Server class
 */
class SocketIOServer {
  constructor(options = {}) {
    this.httpServer = options.server;
    this.io = null;
    this.config = options.config || {};
  }

  /**
   * Initialize the Socket.io server
   */
  async initialize() {
    const corsOrigins = process.env.CORS_ORIGINS
      ? process.env.CORS_ORIGINS.split(',')
      : ['http://localhost:3000', 'http://localhost:8080'];

    this.io = new Server(this.httpServer, {
      path: '/socket.io',
      cors: {
        origin: corsOrigins,
        methods: ['GET', 'POST'],
        credentials: true,
      },
      pingTimeout: 30000,
      pingInterval: 25000,
      maxHttpBufferSize: 1e6, // 1MB
      transports: ['websocket', 'polling'],
      allowUpgrades: true,
    });

    this.setupMiddleware();
    this.setupHandlers();

    // Register with registry
    registerWebSocketServer(this);

    // Start periodic cleanup
    this.startCleanupInterval();

    logger.info('Socket.io Server initialized');
  }

  /**
   * Setup middleware
   */
  setupMiddleware() {
    // Authentication middleware
    this.io.use(async (socket, next) => {
      const token = socket.handshake.auth.token ||
                   socket.handshake.headers.authorization?.replace('Bearer ', '');

      // Store socket in connection manager first
      const connection = connectionManager.addConnection(socket, socket.request);

      if (!token) {
        // Allow unauthenticated connections but limit functionality
        socket.user = null;
        socket.tenantId = null;
        socket.authenticated = false;

        // Set authentication timeout
        socket.authTimeout = setTimeout(() => {
          if (!socket.authenticated) {
            socket.disconnect(4008, 'Authentication timeout');
          }
        }, 30000);

        return next();
      }

      try {
        // Check rate limit first
        const rateLimitResult = await checkWebSocketRateLimit(socket.id);
        if (!rateLimitResult.allowed) {
          return next(new Error(`Rate limit exceeded. Try again in ${rateLimitResult.retryAfter} seconds`));
        }

        // Verify token
        const decoded = await verifyToken(token);

        socket.user = {
          tenantId: decoded.tenantId,
          userId: decoded.userId,
          executiveLevel: decoded.executiveLevel || 'ALL',
          roles: decoded.roles || [],
        };
        socket.tenantId = decoded.tenantId;
        socket.authenticated = true;

        // Update connection info
        connectionManager.authenticateConnection(connection.id, socket.user);

        // Clear auth timeout
        if (socket.authTimeout) {
          clearTimeout(socket.authTimeout);
        }

        logger.debug('Socket.io client authenticated', {
          socketId: socket.id,
          userId: socket.user.userId,
          tenantId: socket.user.tenantId,
        });

        next();

      } catch (error) {
        logger.debug('Socket.io authentication failed:', error.message);
        return next(new Error('Authentication failed'));
      }
    });

    // Tenant validation middleware
    this.io.use((socket, next) => {
      if (!socket.authenticated) {
        return next();
      }

      // Validate tenant exists (placeholder - would check tenant service)
      // For now, just pass through
      next();
    });
  }

  /**
   * Setup event handlers
   */
  setupHandlers() {
    this.io.on(SocketEvents.CONNECTION, (socket) => {
      this.handleConnection(socket);
    });

    this.io.on(SocketEvents.ERROR, (error) => {
      logger.error('Socket.io server error:', error);
    });
  }

  /**
   * Handle client connection
   */
  handleConnection(socket) {
    const connection = connectionManager.getConnection(socket.id);

    logger.debug('Socket.io client connected', {
      socketId: socket.id,
      authenticated: socket.authenticated,
    });

    // Send welcome message
    socket.emit(SocketEvents.CONNECTION, {
      type: 'welcome',
      socketId: socket.id,
      authenticated: socket.authenticated,
      serverTime: Date.now(),
    });

    // Subscribe to tenant room if authenticated
    if (socket.authenticated && socket.tenantId) {
      const tenantRoom = getTenantRoom(socket.tenantId);
      socket.join(tenantRoom);
      connectionManager.joinRoom(socket.id, tenantRoom);
    }

    // Setup event handlers
    socket.on(SocketEvents.AUTHENTICATE, (data) => this.handleAuthenticate(socket, data));
    socket.on(SocketEvents.SUBSCRIBE, (data) => this.handleSubscribe(socket, data));
    socket.on(SocketEvents.UNSUBSCRIBE, (data) => this.handleUnsubscribe(socket, data));
    socket.on(SocketEvents.PING, () => this.handlePing(socket));
    socket.on(SocketEvents.GET_SUBSCRIPTIONS, () => this.handleGetSubscriptions(socket));
    socket.on(SocketEvents.DISCONNECT, (reason) => this.handleDisconnect(socket, reason));
    socket.on('error', (error) => this.handleError(socket, error));
  }

  /**
   * Handle late authentication
   */
  async handleAuthenticate(socket, data) {
    const { token } = data;

    if (!token) {
      socket.emit(SocketEvents.ERROR, {
        code: 4008,
        message: 'Token required',
      });
      return;
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
      socket.authenticated = true;

      // Update connection manager
      connectionManager.authenticateConnection(socket.id, socket.user);

      // Join tenant room
      const tenantRoom = getTenantRoom(socket.tenantId);
      socket.join(tenantRoom);
      connectionManager.joinRoom(socket.id, tenantRoom);

      // Clear auth timeout
      if (socket.authTimeout) {
        clearTimeout(socket.authTimeout);
      }

      socket.emit(SocketEvents.AUTHENTICATED, {
        tenantId: socket.user.tenantId,
        userId: socket.user.userId,
        executiveLevel: socket.user.executiveLevel,
      });

      logger.info('Socket.io client authenticated', {
        socketId: socket.id,
        userId: socket.user.userId,
      });

    } catch (error) {
      socket.emit(SocketEvents.ERROR, {
        code: 4008,
        message: 'Authentication failed',
      });
    }
  }

  /**
   * Handle subscribe request
   */
  handleSubscribe(socket, data) {
    if (!socket.authenticated) {
      socket.emit(SocketEvents.ERROR, {
        code: 4003,
        message: 'Authentication required',
      });
      return;
    }

    const { rooms } = data;

    if (!Array.isArray(rooms) || rooms.length === 0) {
      socket.emit(SocketEvents.ERROR, {
        code: 4002,
        message: 'Invalid rooms parameter',
      });
      return;
    }

    const subscribed = [];
    const denied = [];

    for (const room of rooms) {
      if (this.canAccessRoom(socket, room)) {
        socket.join(room);
        connectionManager.joinRoom(socket.id, room);
        subscribed.push(room);
      } else {
        denied.push(room);
      }
    }

    socket.emit(SocketEvents.SUBSCRIBED, {
      subscribed,
      denied,
    });

    logger.debug('Socket.io client subscribed', {
      socketId: socket.id,
      subscribed,
      denied,
    });
  }

  /**
   * Handle unsubscribe request
   */
  handleUnsubscribe(socket, data) {
    const { rooms } = data;

    if (!Array.isArray(rooms)) {
      return;
    }

    for (const room of rooms) {
      socket.leave(room);
      connectionManager.leaveRoom(socket.id, room);
    }

    socket.emit(SocketEvents.UNSUBSCRIBED, { rooms });

    logger.debug('Socket.io client unsubscribed', {
      socketId: socket.id,
      rooms,
    });
  }

  /**
   * Handle ping
   */
  handlePing(socket) {
    socket.emit(SocketEvents.PONG, {
      timestamp: Date.now(),
    });
  }

  /**
   * Handle get subscriptions request
   */
  handleGetSubscriptions(socket) {
    const connection = connectionManager.getConnection(socket.id);
    const rooms = connection ? Array.from(connection.rooms) : [];

    socket.emit(SocketEvents.GET_SUBSCRIPTIONS, {
      rooms,
    });
  }

  /**
   * Handle disconnect
   */
  handleDisconnect(socket, reason) {
    logger.debug('Socket.io client disconnected', {
      socketId: socket.id,
      reason,
    });

    connectionManager.removeConnection(socket.id);
  }

  /**
   * Handle socket error
   */
  handleError(socket, error) {
    logger.error('Socket.io client error:', {
      socketId: socket.id,
      error: error.message,
    });
  }

  /**
   * Check if socket can access room
   */
  canAccessRoom(socket, room) {
    if (!socket.authenticated || !socket.user) {
      return false;
    }

    // Room format validation
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

  /**
   * Broadcast to room
   */
  broadcastToRoom(roomId, event, data) {
    if (!this.io) {
      logger.warn('Socket.io not initialized');
      return 0;
    }

    this.io.to(roomId).emit(event, data);

    // Get room size
    const room = this.io.sockets.adapter.rooms.get(roomId);
    return room ? room.size : 0;
  }

  /**
   * Broadcast to tenant
   */
  broadcastToTenant(tenantId, event, data) {
    const roomId = getTenantRoom(tenantId);
    return this.broadcastToRoom(roomId, event, data);
  }

  /**
   * Send to specific socket
   */
  sendToSocket(socketId, event, data) {
    if (!this.io) {
      return false;
    }

    const socket = this.io.sockets.sockets.get(socketId);
    if (!socket) {
      return false;
    }

    socket.emit(event, data);
    return true;
  }

  /**
   * Get statistics
   */
  getStats() {
    if (!this.io) {
      return { connected: 0 };
    }

    return {
      connected: this.io.sockets.sockets.size,
      rooms: this.io.sockets.adapter.rooms.size,
      ...connectionManager.getStats(),
    };
  }

  /**
   * Start periodic cleanup
   */
  startCleanupInterval() {
    setInterval(() => {
      const cleaned = connectionManager.cleanup();
      if (cleaned > 0) {
        logger.debug(`Cleaned up ${cleaned} dead connections`);
      }
    }, 60000); // Every minute
  }

  /**
   * Shutdown the server
   */
  async shutdown() {
    logger.info('Shutting down Socket.io server...');

    if (this.io) {
      // Disconnect all clients
      this.io.disconnectSockets(true);
      this.io.close();
    }

    logger.info('Socket.io server shut down');
  }
}

module.exports = {
  SocketIOServer,
  SocketEvents,
};
