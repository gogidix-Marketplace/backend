/**
 * Connection Manager
 *
 * Manages WebSocket connection lifecycle, pooling, and cleanup.
 */

const { v4: uuidv4 } = require('uuid');
const logger = require('../config/logger');

/**
 * Connection states
 */
const ConnectionState = {
  CONNECTING: 'connecting',
  AUTHENTICATED: 'authenticated',
  ACTIVE: 'active',
  IDLE: 'idle',
  DISCONNECTING: 'disconnecting',
  DISCONNECTED: 'disconnected',
};

/**
 * Connection info class
 */
class ConnectionInfo {
  constructor(id, ws, req) {
    this.id = id;
    this.ws = ws;
    this.req = req;

    // Connection metadata
    this.ip = req.socket.remoteAddress;
    this.userAgent = req.headers['user-agent'];
    this.connectedAt = Date.now();
    this.lastActivity = Date.now();
    this.state = ConnectionState.CONNECTING;

    // User info (set after authentication)
    this.tenantId = null;
    this.userId = null;
    this.executiveLevel = null;
    this.roles = [];

    // Room subscriptions
    this.rooms = new Set();

    // Connection metrics
    this.messagesSent = 0;
    this.messagesReceived = 0;
    this.bytesSent = 0;
    this.bytesReceived = 0;

    // Health tracking
    this.isAlive = true;
    this.missedPings = 0;

    // Set connection ID on WebSocket
    ws.clientId = id;
  }

  /**
   * Update activity timestamp
   */
  updateActivity() {
    this.lastActivity = Date.now();
  }

  /**
   * Check if connection is idle
   */
  isIdle(timeoutMs = 300000) { // 5 minutes default
    return Date.now() - this.lastActivity > timeoutMs;
  }

  /**
   * Get connection duration in milliseconds
   */
  getDuration() {
    return Date.now() - this.connectedAt;
  }

  /**
   * Get connection info as plain object
   */
  toJSON() {
    return {
      id: this.id,
      ip: this.ip,
      tenantId: this.tenantId,
      userId: this.userId,
      executiveLevel: this.executiveLevel,
      state: this.state,
      connectedAt: this.connectedAt,
      lastActivity: this.lastActivity,
      duration: this.getDuration(),
      rooms: Array.from(this.rooms),
      messagesSent: this.messagesSent,
      messagesReceived: this.messagesReceived,
    };
  }
}

/**
 * Connection Manager class
 */
class ConnectionManager {
  constructor() {
    this.connections = new Map(); // clientId -> ConnectionInfo
    this.connectionsByTenant = new Map(); // tenantId -> Set of clientIds
    this.connectionsByUser = new Map(); // userId -> Set of clientIds
    this.rooms = new Map(); // roomId -> Set of clientIds
  }

  /**
   * Add a new connection
   */
  addConnection(ws, req) {
    const clientId = uuidv4();
    const connection = new ConnectionInfo(clientId, ws, req);

    this.connections.set(clientId, connection);

    logger.debug('Connection added', {
      clientId,
      ip: connection.ip,
    });

    return connection;
  }

  /**
   * Get connection by ID
   */
  getConnection(clientId) {
    return this.connections.get(clientId);
  }

  /**
   * Remove a connection
   */
  removeConnection(clientId) {
    const connection = this.connections.get(clientId);
    if (!connection) {
      return false;
    }

    // Remove from tenant index
    if (connection.tenantId) {
      const tenantConns = this.connectionsByTenant.get(connection.tenantId);
      if (tenantConns) {
        tenantConns.delete(clientId);
        if (tenantConns.size === 0) {
          this.connectionsByTenant.delete(connection.tenantId);
        }
      }
    }

    // Remove from user index
    if (connection.userId) {
      const userConns = this.connectionsByUser.get(connection.userId);
      if (userConns) {
        userConns.delete(clientId);
        if (userConns.size === 0) {
          this.connectionsByUser.delete(connection.userId);
        }
      }
    }

    // Remove from all rooms
    for (const room of connection.rooms) {
      this.leaveRoom(clientId, room);
    }

    // Remove connection
    this.connections.delete(clientId);

    logger.debug('Connection removed', {
      clientId,
      tenantId: connection.tenantId,
      userId: connection.userId,
    });

    return true;
  }

  /**
   * Authenticate a connection
   */
  authenticateConnection(clientId, userInfo) {
    const connection = this.connections.get(clientId);
    if (!connection) {
      return false;
    }

    connection.tenantId = userInfo.tenantId;
    connection.userId = userInfo.userId;
    connection.executiveLevel = userInfo.executiveLevel;
    connection.roles = userInfo.roles || [];
    connection.state = ConnectionState.AUTHENTICATED;

    // Add to tenant index
    if (!this.connectionsByTenant.has(connection.tenantId)) {
      this.connectionsByTenant.set(connection.tenantId, new Set());
    }
    this.connectionsByTenant.get(connection.tenantId).add(clientId);

    // Add to user index
    if (connection.userId) {
      if (!this.connectionsByUser.has(connection.userId)) {
        this.connectionsByUser.set(connection.userId, new Set());
      }
      this.connectionsByUser.get(connection.userId).add(clientId);
    }

    logger.info('Connection authenticated', {
      clientId,
      tenantId: connection.tenantId,
      userId: connection.userId,
    });

    return true;
  }

  /**
   * Join a room
   */
  joinRoom(clientId, roomId) {
    const connection = this.connections.get(clientId);
    if (!connection) {
      return false;
    }

    if (!this.rooms.has(roomId)) {
      this.rooms.set(roomId, new Set());
    }

    this.rooms.get(roomId).add(clientId);
    connection.rooms.add(roomId);

    logger.debug('Client joined room', {
      clientId,
      roomId,
      roomSize: this.rooms.get(roomId).size,
    });

    return true;
  }

  /**
   * Leave a room
   */
  leaveRoom(clientId, roomId) {
    const connection = this.connections.get(clientId);
    if (!connection) {
      return false;
    }

    if (this.rooms.has(roomId)) {
      this.rooms.get(roomId).delete(clientId);
      if (this.rooms.get(roomId).size === 0) {
        this.rooms.delete(roomId);
      }
    }

    connection.rooms.delete(roomId);

    logger.debug('Client left room', {
      clientId,
      roomId,
    });

    return true;
  }

  /**
   * Get connections in a room
   */
  getRoomConnections(roomId) {
    const clientIds = this.rooms.get(roomId);
    if (!clientIds) {
      return [];
    }

    return Array.from(clientIds)
      .map(id => this.connections.get(id))
      .filter(conn => conn && conn.ws.readyState === 1); // OPEN state
  }

  /**
   * Get all connections for a tenant
   */
  getTenantConnections(tenantId) {
    const clientIds = this.connectionsByTenant.get(tenantId);
    if (!clientIds) {
      return [];
    }

    return Array.from(clientIds)
      .map(id => this.connections.get(id))
      .filter(conn => conn && conn.ws.readyState === 1);
  }

  /**
   * Get all connections for a user
   */
  getUserConnections(userId) {
    const clientIds = this.connectionsByUser.get(userId);
    if (!clientIds) {
      return [];
    }

    return Array.from(clientIds)
      .map(id => this.connections.get(id))
      .filter(conn => conn && conn.ws.readyState === 1);
  }

  /**
   * Get statistics
   */
  getStats() {
    return {
      totalConnections: this.connections.size,
      totalTenants: this.connectionsByTenant.size,
      totalRooms: this.rooms.size,
      connectionsPerTenant: Array.from(this.connectionsByTenant.entries()).map(
        ([tenantId, clients]) => ({ tenantId, count: clients.size })
      ),
    };
  }

  /**
   * Find and clean up idle/dead connections
   */
  cleanup() {
    const now = Date.now();
    const toRemove = [];

    for (const [clientId, connection] of this.connections.entries()) {
      // Check if WebSocket is still open
      if (connection.ws.readyState !== 1) {
        toRemove.push(clientId);
        continue;
      }

      // Check if connection is idle
      if (connection.isIdle(300000)) { // 5 minutes
        toRemove.push(clientId);
        continue;
      }

      // Check missed pings
      if (!connection.isAlive && connection.missedPings > 2) {
        toRemove.push(clientId);
      }
    }

    for (const clientId of toRemove) {
      this.removeConnection(clientId);
    }

    return toRemove.length;
  }

  /**
   * Disconnect all connections for a tenant
   */
  disconnectTenant(tenantId, code = 1000, reason = '') {
    const connections = this.getTenantConnections(tenantId);
    let disconnected = 0;

    for (const connection of connections) {
      try {
        if (connection.ws.readyState === 1) {
          connection.ws.close(code, reason);
          disconnected++;
        }
      } catch (error) {
        logger.error('Error disconnecting connection:', error);
      }
    }

    return disconnected;
  }
}

// Export singleton instance
const connectionManager = new ConnectionManager();

module.exports = {
  ConnectionManager,
  ConnectionState,
  connectionManager,
};
