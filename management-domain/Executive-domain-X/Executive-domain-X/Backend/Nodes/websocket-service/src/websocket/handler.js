const config = require('../config');
const logger = require('../config/logger');
const authMiddleware = require('../middleware/auth');
const validationMiddleware = require('../middleware/validation');
const rateLimiter = require('../middleware/rateLimiter');
const errorHandler = require('../middleware/errorHandler');
const roomService = require('../services/RoomService');
const presenceService = require('../services/PresenceService');
const broadcastService = require('../services/BroadcastService');
const kafkaService = require('../services/KafkaService');

class WebSocketHandler {
  constructor() {
    this.io = null;
    this.connections = new Map();
    this.eventHandlers = new Map();
  }

  async initialize(io) {
    this.io = io;

    await this.setupMiddleware();
    await this.setupEventHandlers();

    await broadcastService.initialize(io);
    await presenceService.initialize();
    await kafkaService.initialize(io);

    this.startCleanupInterval();

    logger.info('WebSocket handler initialized');
  }

  setupMiddleware() {
    this.io.use(authMiddleware.authenticate.bind(authMiddleware));
    this.io.use(errorHandler.socketMiddleware());
  }

  setupEventHandlers() {
    this.io.on('connection', this.handleConnection.bind(this));
  }

  async handleConnection(socket) {
    const user = socket.data.user;

    this.connections.set(socket.id, {
      socket,
      user,
      connectedAt: new Date(),
      rooms: new Set(),
      lastActivity: new Date()
    });

    authMiddleware.addConnection(user.userId, user.tenantId, socket.id);

    await presenceService.setOnline(socket);

    await this.joinDefaultRooms(socket);

    this.setupSocketEventHandlers(socket);

    socket.emit('connected', {
      socketId: socket.id,
      userId: user.userId,
      tenantId: user.tenantId,
      timestamp: new Date().toISOString()
    });

    await broadcastService.broadcastPresenceChange(
      user.tenantId,
      user.userId,
      'online'
    );

    logger.info(`User connected: ${user.userId} (${socket.id})`);
  }

  async joinDefaultRooms(socket) {
    const user = socket.data.user;

    const userRoom = roomService.getUserRoom(user.tenantId, user.userId);
    const notificationRoom = roomService.getNotificationRoom(user.tenantId, user.userId);
    const tenantRoom = `tenant:${user.tenantId}:broadcast`;

    await roomService.joinRoom(socket, userRoom, { type: 'user' });
    await roomService.joinRoom(socket, notificationRoom, { type: 'notification' });
    await roomService.joinRoom(socket, tenantRoom, { type: 'tenant' });
  }

  setupSocketEventHandlers(socket) {
    socket.on('disconnect', () => this.handleDisconnect(socket));

    socket.on('room:join', async (data, callback) => {
      await this.handleJoinRoom(socket, data, callback);
    });

    socket.on('room:leave', async (data, callback) => {
      await this.handleLeaveRoom(socket, data, callback);
    });

    socket.on('room:create', async (data, callback) => {
      await this.handleCreateRoom(socket, data, callback);
    });

    socket.on('room:list', async (callback) => {
      await this.handleListRooms(socket, callback);
    });

    socket.on('room:users', async (data, callback) => {
      await this.handleRoomUsers(socket, data, callback);
    });

    socket.on('message:send', async (data, callback) => {
      await this.handleSendMessage(socket, data, callback);
    });

    socket.on('message:broadcast', async (data, callback) => {
      await this.handleBroadcast(socket, data, callback);
    });

    socket.on('presence:update', async (data, callback) => {
      await this.handlePresenceUpdate(socket, data, callback);
    });

    socket.on('presence:typing', async (data, callback) => {
      await this.handleTyping(socket, data, callback);
    });

    socket.on('presence:get', async (data, callback) => {
      await this.handleGetPresence(socket, data, callback);
    });

    socket.on('room:history', async (data, callback) => {
      await this.handleRoomHistory(socket, data, callback);
    });

    socket.on('dashboard:subscribe', async (data, callback) => {
      await this.handleDashboardSubscribe(socket, data, callback);
    });

    socket.on('dashboard:unsubscribe', async (data, callback) => {
      await this.handleDashboardUnsubscribe(socket, data, callback);
    });

    socket.on('kpi:subscribe', async (data, callback) => {
      await this.handleKpiSubscribe(socket, data, callback);
    });

    socket.on('kpi:unsubscribe', async (data, callback) => {
      await this.handleKpiUnsubscribe(socket, data, callback);
    });

    socket.on('ping', (callback) => {
      if (typeof callback === 'function') {
        callback({ pong: Date.now() });
      }
    });

    socket.on('error', (error) => {
      logger.error(`Socket error (${socket.id}):`, error);
    });
  }

  async handleDisconnect(socket) {
    const connection = this.connections.get(socket.id);
    if (!connection) return;

    const { socket: sock, user } = connection;

    await roomService.leaveAllRooms(sock);

    await presenceService.setOffline(sock);

    authMiddleware.removeConnection(user.userId, user.tenantId);

    await broadcastService.broadcastPresenceChange(
      user.tenantId,
      user.userId,
      'offline'
    );

    await kafkaService.publishPresenceEvent(
      user.tenantId,
      user.userId,
      'offline',
      { socketId: socket.id }
    );

    this.connections.delete(socket.id);

    logger.info(`User disconnected: ${user.userId} (${socket.id})`);
  }

  async handleJoinRoom(socket, data, callback) {
    try {
      const validation = validationMiddleware.validateSync('joinRoom', data);
      if (!validation.success) {
        return callback?.(validation);
      }

      const rateCheck = await rateLimiter.checkJoinRoomRate(socket);
      if (!rateCheck.success) {
        return callback?.({
          success: false,
          message: rateCheck.message
        });
      }

      const user = socket.data.user;
      const roomId = roomService.ensureTenantPrefix(validation.data.room, user.tenantId);

      const result = await roomService.joinRoom(socket, roomId, validation.data.metadata);

      this.connections.get(socket.id)?.rooms.add(roomId);

      socket.emit('room:joined', {
        roomId,
        ...result
      });

      await broadcastService.broadcastToRoom(
        roomId,
        'room:user_joined',
        {
          roomId,
          user: {
            userId: user.userId,
            name: user.name || user.email
          },
          timestamp: new Date().toISOString()
        },
        { exclude: [socket.id] }
      });

      callback?.({ success: true, ...result });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'joinRoom' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleLeaveRoom(socket, data, callback) {
    try {
      const validation = validationMiddleware.validateSync('leaveRoom', data);
      if (!validation.success) {
        return callback?.(validation);
      }

      const user = socket.data.user;
      const roomId = roomService.ensureTenantPrefix(validation.data.room, user.tenantId);

      await roomService.leaveRoom(socket, roomId);

      this.connections.get(socket.id)?.rooms.delete(roomId);

      socket.emit('room:left', { roomId });

      await broadcastService.broadcastToRoom(
        roomId,
        'room:user_left',
        {
          roomId,
          user: {
            userId: user.userId,
            name: user.name || user.email
          },
          timestamp: new Date().toISOString()
        }
      );

      callback?.({ success: true, roomId });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'leaveRoom' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleCreateRoom(socket, data, callback) {
    try {
      const validation = validationMiddleware.validateSync('createRoom', data);
      if (!validation.success) {
        return callback?.(validation);
      }

      const user = socket.data.user;
      const room = await roomService.createRoom(
        user.tenantId,
        validation.data.name,
        validation.data.type,
        {
          createdBy: user.userId,
          maxUsers: validation.data.maxUsers,
          metadata: validation.data.metadata
        }
      );

      await roomService.joinRoom(socket, room.id);

      socket.emit('room:created', room);

      await kafkaService.publishDashboardUpdate(
        user.tenantId,
        room.id,
        'room_created',
        room
      );

      callback?.({ success: true, room });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'createRoom' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleListRooms(socket, callback) {
    try {
      const user = socket.data.user;

      const rooms = await roomService.listRooms(user.tenantId);

      callback?.({ success: true, rooms });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'listRooms' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleRoomUsers(socket, data, callback) {
    try {
      const user = socket.data.user;
      const roomId = roomService.ensureTenantPrefix(data.room, user.tenantId);

      const users = await roomService.getRoomUsers(roomId);

      callback?.({ success: true, roomId, users });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'roomUsers' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleSendMessage(socket, data, callback) {
    try {
      const validation = validationMiddleware.validateSync('sendMessage', data);
      if (!validation.success) {
        return callback?.(validation);
      }

      const rateCheck = await rateLimiter.checkMessageRate(socket);
      if (!rateCheck.success) {
        return callback?.({
          success: false,
          message: rateCheck.message
        });
      }

      const user = socket.data.user;
      const { room, to, event, data: messageData, ephemeral } = validation.data;

      const message = {
        from: {
          userId: user.userId,
          name: user.name || user.email
        },
        event,
        data: messageData,
        timestamp: new Date().toISOString()
      };

      if (to) {
        await broadcastService.broadcastToUser(user.tenantId, to, event, message);
      } else if (room) {
        const roomId = roomService.ensureTenantPrefix(room, user.tenantId);
        await broadcastService.broadcastToRoom(roomId, event, message, {
          exclude: ephemeral ? [] : undefined,
          toKafka: !ephemeral
        });
      } else {
        socket.emit(event, message);
      }

      callback?.({ success: true, messageId: message.timestamp });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'sendMessage' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleBroadcast(socket, data, callback) {
    try {
      const validation = validationMiddleware.validateSync('broadcast', data);
      if (!validation.success) {
        return callback?.(validation);
      }

      const rateCheck = await rateLimiter.checkBroadcastRate(socket);
      if (!rateCheck.success) {
        return callback?.({
          success: false,
          message: rateCheck.message
        });
      }

      const user = socket.data.user;
      const { room, event, data: messageData, exclude } = validation.data;

      const message = {
        from: {
          userId: user.userId,
          name: user.name || user.email
        },
        event,
        data: messageData,
        timestamp: new Date().toISOString()
      };

      if (room) {
        const roomId = roomService.ensureTenantPrefix(room, user.tenantId);
        await broadcastService.broadcastToRoom(roomId, event, message, {
          exclude: [...(exclude || []), socket.id]
        });
      } else {
        await broadcastService.broadcastToTenant(user.tenantId, event, message, {
          exclude: [socket.id]
        });
      }

      callback?.({ success: true });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'broadcast' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handlePresenceUpdate(socket, data, callback) {
    try {
      const validation = validationMiddleware.validateSync('presenceUpdate', data);
      if (!validation.success) {
        return callback?.(validation);
      }

      const user = socket.data.user;
      const { state, metadata } = validation.data;

      const presence = await presenceService.updateStatus(socket, state, metadata);

      await kafkaService.publishPresenceEvent(
        user.tenantId,
        user.userId,
        'status_change',
        { state, ...metadata }
      );

      callback?.({ success: true, presence });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'presenceUpdate' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleTyping(socket, data, callback) {
    try {
      const validation = validationMiddleware.validateSync('typingIndicator', data);
      if (!validation.success) {
        return callback?.(validation);
      }

      const rateCheck = await rateLimiter.checkPresenceRate(socket);
      if (!rateCheck.success) {
        return callback?.({
          success: false,
          message: rateCheck.message
        });
      }

      const user = socket.data.user;
      const { room, isTyping } = validation.data;

      const roomId = roomService.ensureTenantPrefix(room, user.tenantId);
      await presenceService.setTyping(socket, roomId, isTyping);

      await broadcastService.broadcastTypingIndicator(
        roomId,
        user.userId,
        isTyping,
        user.name || user.email
      );

      callback?.({ success: true });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'typing' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleGetPresence(socket, data, callback) {
    try {
      const user = socket.data.user;
      const userId = data.userId || user.userId;
      const tenantId = data.tenantId || user.tenantId;

      const presence = await presenceService.getPresence(tenantId, userId);

      callback?.({ success: true, presence });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'getPresence' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleRoomHistory(socket, data, callback) {
    try {
      const user = socket.data.user;
      const roomId = roomService.ensureTenantPrefix(data.room, user.tenantId);
      const limit = data.limit || 50;

      const history = await broadcastService.getRoomHistory(roomId, limit);

      callback?.({ success: true, roomId, history });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'roomHistory' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleDashboardSubscribe(socket, data, callback) {
    try {
      const user = socket.data.user;
      const { dashboardId } = data;

      const roomId = roomService.getDashboardRoom(user.tenantId, dashboardId);

      await roomService.joinRoom(socket, roomId, { type: 'dashboard', dashboardId });

      socket.emit('dashboard:subscribed', { dashboardId, roomId });

      callback?.({ success: true, dashboardId, roomId });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'dashboardSubscribe' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleDashboardUnsubscribe(socket, data, callback) {
    try {
      const user = socket.data.user;
      const { dashboardId } = data;

      const roomId = roomService.getDashboardRoom(user.tenantId, dashboardId);

      await roomService.leaveRoom(socket, roomId);

      socket.emit('dashboard:unsubscribed', { dashboardId, roomId });

      callback?.({ success: true, dashboardId, roomId });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'dashboardUnsubscribe' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleKpiSubscribe(socket, data, callback) {
    try {
      const user = socket.data.user;
      const { kpiId } = data;

      const roomId = roomService.getKpiRoom(user.tenantId, kpiId);

      await roomService.joinRoom(socket, roomId, { type: 'kpi', kpiId });

      socket.emit('kpi:subscribed', { kpiId, roomId });

      callback?.({ success: true, kpiId, roomId });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'kpiSubscribe' });
      callback?.({ success: false, message: error.message });
    }
  }

  async handleKpiUnsubscribe(socket, data, callback) {
    try {
      const user = socket.data.user;
      const { kpiId } = data;

      const roomId = roomService.getKpiRoom(user.tenantId, kpiId);

      await roomService.leaveRoom(socket, roomId);

      socket.emit('kpi:unsubscribed', { kpiId, roomId });

      callback?.({ success: true, kpiId, roomId });
    } catch (error) {
      errorHandler.handleError(socket, error, { action: 'kpiUnsubscribe' });
      callback?.({ success: false, message: error.message });
    }
  }

  startCleanupInterval() {
    setInterval(async () => {
      await roomService.cleanupExpiredRooms();
    }, 300000);
  }

  getStats() {
    return {
      connections: this.connections.size,
      rooms: Array.from(this.connections.values()).reduce((acc, conn) => acc + conn.rooms.size, 0),
      broadcastStats: broadcastService.getStats(),
      kafkaStats: kafkaService.getStats()
    };
  }

  getConnections() {
    return Array.from(this.connections.values()).map(conn => ({
      socketId: conn.socket.id,
      userId: conn.user?.userId,
      tenantId: conn.user?.tenantId,
      connectedAt: conn.connectedAt,
      rooms: Array.from(conn.rooms),
      lastActivity: conn.lastActivity
    }));
  }

  async shutdown() {
    logger.info('Shutting down WebSocket handler...');

    for (const [socketId, connection] of this.connections.entries()) {
      try {
        await this.handleDisconnect(connection.socket);
      } catch (error) {
        logger.error(`Error disconnecting socket ${socketId}:`, error);
      }
    }

    await broadcastService.shutdown();
    await presenceService.shutdown();
    await kafkaService.disconnect();

    this.connections.clear();

    logger.info('WebSocket handler shut down');
  }
}

module.exports = new WebSocketHandler();
