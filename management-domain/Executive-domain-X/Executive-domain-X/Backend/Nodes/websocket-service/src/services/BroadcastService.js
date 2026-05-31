const config = require('../config');
const logger = require('../config/logger');
const redis = require('../config/redis');
const kafka = require('../config/kafka');
const roomService = require('./RoomService');
const presenceService = require('./PresenceService');
const rateLimiter = require('../middleware/rateLimiter');

class BroadcastService {
  constructor() {
    this.messageQueue = [];
    this.processingInterval = null;
    this.stats = {
      messagesBroadcast: 0,
      messagesToKafka: 0,
      messagesToRedis: 0,
      errors: 0
    };
  }

  async initialize(io) {
    this.io = io;

    this.processingInterval = setInterval(() => {
      this.processQueue();
    }, 100);

    await this.subscribeToRedisChannels();

    logger.info('Broadcast service initialized');
  }

  async shutdown() {
    if (this.processingInterval) {
      clearInterval(this.processingInterval);
    }

    await this.processQueue();

    logger.info('Broadcast service shut down');
  }

  async broadcastToRoom(roomId, event, data, options = {}) {
    const {
      exclude = [],
      includeSender = true,
      persist = false,
      toKafka = false,
      toRedis = true
    } = options;

    try {
      const message = {
        event,
        data,
        roomId,
        timestamp: new Date().toISOString(),
        id: this.generateMessageId()
      };

      this.io.to(roomId).except(exclude).emit(event, message);

      this.stats.messagesBroadcast++;

      if (toRedis) {
        await this.publishToRedis(roomId, event, message);
        this.stats.messagesToRedis++;
      }

      if (toKafka) {
        await this.publishToKafka('broadcast', { roomId, event, data: message });
        this.stats.messagesToKafka++;
      }

      if (persist) {
        await this.persistMessage(roomId, message);
      }

      logger.debug(`Broadcast to room ${roomId}: ${event}`);
      return { success: true, messageId: message.id };
    } catch (error) {
      this.stats.errors++;
      logger.error('Error broadcasting to room:', error);
      return { success: false, error: error.message };
    }
  }

  async broadcastToUser(tenantId, userId, event, data, options = {}) {
    try {
      const userRoom = roomService.getUserRoom(tenantId, userId);

      const message = {
        event,
        data,
        userId,
        timestamp: new Date().toISOString(),
        id: this.generateMessageId()
      };

      this.io.to(userRoom).emit(event, message);

      this.stats.messagesBroadcast++;

      if (options.toKafka !== false) {
        await this.publishToKafka('user_message', { tenantId, userId, event, data: message });
        this.stats.messagesToKafka++;
      }

      logger.debug(`Broadcast to user ${userId}: ${event}`);
      return { success: true, messageId: message.id };
    } catch (error) {
      this.stats.errors++;
      logger.error('Error broadcasting to user:', error);
      return { success: false, error: error.message };
    }
  }

  async broadcastToTenant(tenantId, event, data, options = {}) {
    try {
      const tenantRoom = `tenant:${tenantId}:*`;

      const message = {
        event,
        data,
        tenantId,
        timestamp: new Date().toISOString(),
        id: this.generateMessageId()
      };

      this.io.emit(event, message);

      this.stats.messagesBroadcast++;

      await this.publishToRedis(`tenant:${tenantId}`, event, message);
      this.stats.messagesToRedis++;

      if (options.toKafka !== false) {
        await this.publishToKafka('tenant_broadcast', { tenantId, event, data: message });
        this.stats.messagesToKafka++;
      }

      logger.debug(`Broadcast to tenant ${tenantId}: ${event}`);
      return { success: true, messageId: message.id };
    } catch (error) {
      this.stats.errors++;
      logger.error('Error broadcasting to tenant:', error);
      return { success: false, error: error.message };
    }
  }

  async broadcastGlobal(event, data, options = {}) {
    try {
      const message = {
        event,
        data,
        timestamp: new Date().toISOString(),
        id: this.generateMessageId(),
        global: true
      };

      this.io.emit(event, message);

      this.stats.messagesBroadcast++;

      await this.publishToRedis('global', event, message);
      this.stats.messagesToRedis++;

      if (options.toKafka !== false) {
        await this.publishToKafka('global_broadcast', { event, data: message });
        this.stats.messagesToKafka++;
      }

      logger.info(`Global broadcast: ${event}`);
      return { success: true, messageId: message.id };
    } catch (error) {
      this.stats.errors++;
      logger.error('Error broadcasting globally:', error);
      return { success: false, error: error.message };
    }
  }

  async broadcastDashboardUpdate(tenantId, dashboardId, update) {
    const roomId = roomService.getDashboardRoom(tenantId, dashboardId);

    return this.broadcastToRoom(roomId, 'dashboard:update', {
      dashboardId,
      ...update
    }, {
      toKafka: true,
      persist: true
    });
  }

  async broadcastKpiUpdate(tenantId, kpiId, update) {
    const roomId = roomService.getKpiRoom(tenantId, kpiId);

    return this.broadcastToRoom(roomId, 'kpi:update', {
      kpiId,
      ...update
    }, {
      toKafka: true
    });
  }

  async broadcastNotification(tenantId, userId, notification) {
    const notificationRoom = roomService.getNotificationRoom(tenantId, userId);

    return this.broadcastToRoom(notificationRoom, 'notification:new', {
      notificationId: notification.id || this.generateMessageId(),
      ...notification
    }, {
      toKafka: true,
      persist: true
    });
  }

  async sendToSocket(socketId, event, data) {
    try {
      const socket = this.io?.sockets?.sockets?.get(socketId);
      if (!socket) {
        return { success: false, error: 'Socket not found' };
      }

      const message = {
        event,
        data,
        timestamp: new Date().toISOString(),
        id: this.generateMessageId()
      };

      socket.emit(event, message);

      this.stats.messagesBroadcast++;
      return { success: true, messageId: message.id };
    } catch (error) {
      this.stats.errors++;
      logger.error('Error sending to socket:', error);
      return { success: false, error: error.message };
    }
  }

  async publishToRedis(channel, event, message) {
    try {
      const redisChannel = `ws:${channel}`;
      await redis.publish(redisChannel, JSON.stringify({
        event,
        message
      }));
    } catch (error) {
      logger.error('Error publishing to Redis:', error);
    }
  }

  async subscribeToRedisChannels() {
    try {
      const subClient = redis.getSubClient();
      if (!subClient) {
        logger.warn('Redis sub client not available, skipping Redis subscriptions');
        return;
      }

      await redis.psubscribe('ws:*', async (channel, message) => {
        try {
          const parsed = JSON.parse(message);
          const eventChannel = channel.replace('ws:', '');

          this.io?.to(eventChannel)?.emit(parsed.event, parsed.message);
        } catch (error) {
          logger.error('Error processing Redis message:', error);
        }
      });

      logger.info('Subscribed to Redis channels');
    } catch (error) {
      logger.error('Error subscribing to Redis channels:', error);
    }
  }

  async publishToKafka(type, data) {
    try {
      const topic = this.getKafkaTopicForType(type);
      await kafka.publish(topic, {
        type,
        data,
        timestamp: new Date().toISOString()
      }, data.tenantId || data.roomId);
    } catch (error) {
      logger.error('Error publishing to Kafka:', error);
    }
  }

  getKafkaTopicForType(type) {
    const topicMap = {
      'dashboard_update': config.kafka.topics.dashboardUpdates,
      'kpi_update': config.kafka.topics.kpiUpdates,
      'notification': config.kafka.topics.notifications,
      'presence': config.kafka.topics.presence,
      'broadcast': config.kafka.topics.executiveUpdates
    };

    return topicMap[type] || config.kafka.topics.executiveUpdates;
  }

  async persistMessage(roomId, message) {
    try {
      const key = `messages:${roomId}`;
      await redis.lpush(key, JSON.stringify(message));

      const maxMessages = 1000;
      const count = await redis.llen(key);
      if (count > maxMessages) {
        await redis.ltrim(key, 0, maxMessages - 1);
      }

      await redis.expire(key, 86400);
    } catch (error) {
      logger.error('Error persisting message:', error);
    }
  }

  async getRoomHistory(roomId, limit = 50) {
    try {
      const key = `messages:${roomId}`;
      const messages = await redis.lrange(key, 0, limit - 1);

      return messages.map(msg => {
        try {
          return JSON.parse(msg);
        } catch {
          return null;
        }
      }).filter(Boolean);
    } catch (error) {
      logger.error('Error getting room history:', error);
      return [];
    }
  }

  async clearRoomHistory(roomId) {
    try {
      const key = `messages:${roomId}`;
      await redis.del(key);
      logger.info(`Cleared history for room: ${roomId}`);
    } catch (error) {
      logger.error('Error clearing room history:', error);
    }
  }

  queueMessage(broadcastFn) {
    this.messageQueue.push(broadcastFn);
  }

  async processQueue() {
    if (this.messageQueue.length === 0) return;

    const batch = this.messageQueue.splice(0, 100);

    for (const fn of batch) {
      try {
        await fn();
      } catch (error) {
        logger.error('Error processing queued message:', error);
      }
    }
  }

  generateMessageId() {
    return `msg_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`;
  }

  getStats() {
    return {
      ...this.stats,
      queueSize: this.messageQueue.length
    };
  }

  resetStats() {
    this.stats = {
      messagesBroadcast: 0,
      messagesToKafka: 0,
      messagesToRedis: 0,
      errors: 0
    };
  }

  async broadcastPresenceChange(tenantId, userId, status, metadata = {}) {
    return this.broadcastToTenant(tenantId, 'presence:change', {
      userId,
      status,
      metadata,
      timestamp: new Date().toISOString()
    });
  }

  async broadcastTypingIndicator(roomId, userId, isTyping, userName = null) {
    return this.broadcastToRoom(roomId, 'presence:typing', {
      userId,
      userName,
      isTyping,
      timestamp: Date.now()
    }, { toKafka: false, toRedis: false });
  }
}

module.exports = new BroadcastService();
