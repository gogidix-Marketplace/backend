/**
 * WebSocket Server for Executive Real-time Service
 *
 * Manages WebSocket connections, rooms, and message routing.
 * Supports tenant isolation, room-based filtering, and connection management.
 */

const WebSocket = require('ws');
const { v4: uuidv4 } = require('uuid');
const { verifyToken } = require('../auth/jwt');
const { RateLimiterMemory } = require('rate-limiter-flexible');
const logger = require('../config/logger');
const { publishMessage } = require('../config/redis');

// Rate limiter for WebSocket messages
const wsRateLimiter = new RateLimiterMemory({
  points: 100, // Number of messages
  duration: 60, // Per 60 seconds
});

/**
 * WebSocket connection states
 */
const ConnectionState = {
  CONNECTING: 'connecting',
  AUTHENTICATED: 'authenticated',
  SUBSCRIBED: 'subscribed',
  DISCONNECTING: 'disconnecting',
};

/**
 * WebSocket Server class
 */
class WebSocketServer {
  constructor(options = {}) {
    this.httpServer = options.server;
    this.wss = null;
    this.clients = new Map(); // clientId -> ClientInfo
    this.tenantClients = new Map(); // tenantId -> Set of clientIds
    this.rooms = new Map(); // roomId -> Set of clientIds
  }

  /**
   * Initialize the WebSocket server
   */
  async initialize() {
    this.wss = new WebSocket.Server({
      server: this.httpServer,
      path: '/ws',
      clientTracking: true,
    });

    this.wss.on('connection', this.handleConnection.bind(this));
    this.wss.on('error', this.handleError.bind(this));

    // Start periodic cleanup of disconnected clients
    this.startCleanupInterval();

    logger.info('WebSocket Server initialized');
  }

  /**
   * Handle incoming WebSocket connection
   */
  async handleConnection(ws, req) {
    const clientId = uuidv4();
    const clientIp = req.socket.remoteAddress;

    logger.debug(`New WebSocket connection: ${clientId} from ${clientIp}`);

    // Set initial connection state
    ws.clientId = clientId;
    ws.state = ConnectionState.CONNECTING;
    ws.isAlive = true;

    // Add to clients map
    this.clients.set(clientId, {
      id: clientId,
      ws: ws,
      tenantId: null,
      userId: null,
      executiveLevel: null,
      rooms: new Set(),
      connectedAt: Date.now(),
      lastActivity: Date.now(),
    });

    // Setup ping/pong for connection health
    ws.on('pong', () => {
      ws.isAlive = true;
    });

    // Handle incoming messages
    ws.on('message', async (data) => {
      await this.handleMessage(clientId, data);
    });

    // Handle connection close
    ws.on('close', () => {
      this.handleDisconnect(clientId);
    });

    // Handle errors
    ws.on('error', (error) => {
      logger.error(`WebSocket error for client ${clientId}:`, error);
      this.handleDisconnect(clientId);
    });

    // Send welcome message - expect authentication
    this.sendToClient(clientId, {
      type: 'welcome',
      clientId: clientId,
      message: 'Connected to Executive Real-time Service',
      requiresAuth: true,
    });

    // Set authentication timeout (30 seconds)
    ws.authTimeout = setTimeout(() => {
      if (ws.state === ConnectionState.CONNECTING) {
        logger.warn(`Client ${clientId} failed to authenticate in time`);
        this.closeClient(clientId, 4008, 'Authentication timeout');
      }
    }, 30000);
  }

  /**
   * Handle incoming WebSocket message
   */
  async handleMessage(clientId, data) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    // Update last activity
    client.lastActivity = Date.now();

    // Rate limiting
    try {
      await wsRateLimiter.consume(clientId);
    } catch (rej) {
      this.sendToClient(clientId, {
        type: 'error',
        code: 4290,
        message: 'Rate limit exceeded',
      });
      return;
    }

    let message;
    try {
      message = JSON.parse(data);
    } catch (error) {
      this.sendToClient(clientId, {
        type: 'error',
        code: 4000,
        message: 'Invalid JSON',
      });
      return;
    }

    logger.debug(`Message from ${clientId}:`, message.type);

    // Route message based on type
    switch (message.type) {
      case 'auth':
        await this.handleAuth(clientId, message);
        break;

      case 'subscribe':
        await this.handleSubscribe(clientId, message);
        break;

      case 'unsubscribe':
        await this.handleUnsubscribe(clientId, message);
        break;

      case 'ping':
        this.sendToClient(clientId, { type: 'pong', timestamp: Date.now() });
        break;

      case 'get_subscriptions':
        this.sendSubscriptions(clientId);
        break;

      default:
        this.sendToClient(clientId, {
          type: 'error',
          code: 4001,
          message: `Unknown message type: ${message.type}`,
        });
    }
  }

  /**
   * Handle authentication message
   */
  async handleAuth(clientId, message) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    const { token } = message;

    if (!token) {
      this.closeClient(clientId, 4008, 'Authentication token required');
      return;
    }

    try {
      const decoded = await verifyToken(token);

      client.tenantId = decoded.tenantId;
      client.userId = decoded.userId;
      client.executiveLevel = decoded.executiveLevel || 'ALL';
      client.state = ConnectionState.AUTHENTICATED;

      // Clear auth timeout
      if (client.ws.authTimeout) {
        clearTimeout(client.ws.authTimeout);
        client.ws.authTimeout = null;
      }

      // Add to tenant clients map
      if (!this.tenantClients.has(client.tenantId)) {
        this.tenantClients.set(client.tenantId, new Set());
      }
      this.tenantClients.get(client.tenantId).add(clientId);

      this.sendToClient(clientId, {
        type: 'authenticated',
        tenantId: client.tenantId,
        userId: client.userId,
        executiveLevel: client.executiveLevel,
      });

      // Auto-subscribe to tenant-wide room
      await this.joinRoom(clientId, `tenant:${client.tenantId}`);

      logger.info(`Client ${clientId} authenticated for tenant ${client.tenantId}`);

    } catch (error) {
      logger.error(`Authentication failed for client ${clientId}:`, error.message);
      this.closeClient(clientId, 4008, 'Authentication failed');
    }
  }

  /**
   * Handle subscribe message
   */
  async handleSubscribe(clientId, message) {
    const client = this.clients.get(clientId);
    if (!client || client.state !== ConnectionState.AUTHENTICATED) {
      this.sendToClient(clientId, {
        type: 'error',
        code: 4003,
        message: 'Not authenticated',
      });
      return;
    }

    const { rooms } = message;

    if (!Array.isArray(rooms) || rooms.length === 0) {
      this.sendToClient(clientId, {
        type: 'error',
        code: 4002,
        message: 'Invalid rooms parameter',
      });
      return;
    }

    const subscribedRooms = [];

    for (const room of rooms) {
      // Validate room access based on tenant and executive level
      if (this.canAccessRoom(client, room)) {
        await this.joinRoom(clientId, room);
        subscribedRooms.push(room);
      }
    }

    this.sendToClient(clientId, {
      type: 'subscribed',
      rooms: subscribedRooms,
    });

    logger.debug(`Client ${clientId} subscribed to rooms:`, subscribedRooms);
  }

  /**
   * Handle unsubscribe message
   */
  async handleUnsubscribe(clientId, message) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    const { rooms } = message;

    if (!Array.isArray(rooms)) {
      return;
    }

    for (const room of rooms) {
      await this.leaveRoom(clientId, room);
    }

    this.sendToClient(clientId, {
      type: 'unsubscribed',
      rooms: rooms,
    });
  }

  /**
   * Check if client can access a room
   */
  canAccessRoom(client, room) {
    // Room format: "tenant:{tenantId}", "tenant:{tenantId}:{category}", "tenant:{tenantId}:{executiveLevel}", etc.
    const parts = room.split(':');

    if (parts[0] !== 'tenant') {
      return false;
    }

    if (parts[1] !== client.tenantId) {
      return false;
    }

    // Check executive level restrictions
    if (parts.length > 3) {
      const level = parts[2];
      if (level !== 'ALL' && level !== client.executiveLevel && client.executiveLevel !== 'ALL') {
        return false;
      }
    }

    return true;
  }

  /**
   * Add client to a room
   */
  async joinRoom(clientId, roomId) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    if (!this.rooms.has(roomId)) {
      this.rooms.set(roomId, new Set());
    }

    this.rooms.get(roomId).add(clientId);
    client.rooms.add(roomId);
  }

  /**
   * Remove client from a room
   */
  async leaveRoom(clientId, roomId) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    if (this.rooms.has(roomId)) {
      this.rooms.get(roomId).delete(clientId);
      if (this.rooms.get(roomId).size === 0) {
        this.rooms.delete(roomId);
      }
    }

    client.rooms.delete(roomId);
  }

  /**
   * Handle client disconnection
   */
  handleDisconnect(clientId) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    logger.debug(`Client disconnected: ${clientId}`);

    // Clear auth timeout if set
    if (client.ws.authTimeout) {
      clearTimeout(client.ws.authTimeout);
    }

    // Remove from all rooms
    for (const room of client.rooms) {
      this.leaveRoom(clientId, room);
    }

    // Remove from tenant clients
    if (client.tenantId && this.tenantClients.has(client.tenantId)) {
      this.tenantClients.get(client.tenantId).delete(clientId);
      if (this.tenantClients.get(client.tenantId).size === 0) {
        this.tenantClients.delete(client.tenantId);
      }
    }

    // Remove from clients map
    this.clients.delete(clientId);
  }

  /**
   * Send message to a specific client
   */
  sendToClient(clientId, message) {
    const client = this.clients.get(clientId);
    if (!client || client.ws.readyState !== WebSocket.OPEN) {
      return false;
    }

    try {
      client.ws.send(JSON.stringify(message));
      return true;
    } catch (error) {
      logger.error(`Error sending to client ${clientId}:`, error);
      return false;
    }
  }

  /**
   * Broadcast message to all clients in a room
   */
  broadcastToRoom(roomId, message, excludeClientId = null) {
    const clients = this.rooms.get(roomId);
    if (!clients) {
      return 0;
    }

    let sent = 0;
    for (const clientId of clients) {
      if (clientId !== excludeClientId) {
        if (this.sendToClient(clientId, message)) {
          sent++;
        }
      }
    }

    return sent;
  }

  /**
   * Broadcast message to all clients of a tenant
   */
  broadcastToTenant(tenantId, message, excludeClientId = null) {
    const clients = this.tenantClients.get(tenantId);
    if (!clients) {
      return 0;
    }

    let sent = 0;
    for (const clientId of clients) {
      if (clientId !== excludeClientId) {
        if (this.sendToClient(clientId, message)) {
          sent++;
        }
      }
    }

    return sent;
  }

  /**
   * Get client subscriptions
   */
  sendSubscriptions(clientId) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    this.sendToClient(clientId, {
      type: 'subscriptions',
      rooms: Array.from(client.rooms),
    });
  }

  /**
   * Close a client connection
   */
  closeClient(clientId, code, reason) {
    const client = this.clients.get(clientId);
    if (!client) {
      return;
    }

    if (client.ws.readyState === WebSocket.OPEN || client.ws.readyState === WebSocket.CONNECTING) {
      client.ws.close(code, reason);
    }

    this.handleDisconnect(clientId);
  }

  /**
   * Handle WebSocket server error
   */
  handleError(error) {
    logger.error('WebSocket Server error:', error);
  }

  /**
   * Start periodic cleanup of dead connections
   */
  startCleanupInterval() {
    const interval = setInterval(() => {
      this.wss.clients.forEach((ws) => {
        if (!ws.isAlive) {
          logger.debug('Closing dead connection:', ws.clientId);
          return ws.terminate();
        }
        ws.isAlive = false;
        ws.ping();
      });
    }, 30000); // Every 30 seconds

    this.wss.on('close', () => {
      clearInterval(interval);
    });
  }

  /**
   * Get server statistics
   */
  getStats() {
    return {
      totalConnections: this.clients.size,
      totalTenants: this.tenantClients.size,
      totalRooms: this.rooms.size,
      connectionsPerTenant: Array.from(this.tenantClients.entries()).map(
        ([tenantId, clients]) => ({ tenantId, count: clients.size })
      ),
    };
  }

  /**
   * Shutdown the WebSocket server
   */
  async shutdown() {
    logger.info('Shutting down WebSocket Server...');

    // Close all client connections
    for (const [clientId, client] of this.clients.entries()) {
      try {
        client.ws.close(1001, 'Server shutting down');
      } catch (error) {
        logger.error(`Error closing client ${clientId}:`, error);
      }
    }

    // Clear all maps
    this.clients.clear();
    this.tenantClients.clear();
    this.rooms.clear();

    // Close WebSocket server
    if (this.wss) {
      this.wss.close();
    }

    logger.info('WebSocket Server shut down');
  }
}

module.exports = { WebSocketServer };
