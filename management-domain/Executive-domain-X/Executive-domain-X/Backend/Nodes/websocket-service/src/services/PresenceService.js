const config = require('../config');
const logger = require('../config/logger');
const redis = require('../config/redis');

class PresenceService {
  constructor() {
    this.presenceCache = new Map();
    this.typingIndicators = new Map();
    this.syncInterval = null;
  }

  async initialize() {
    this.syncInterval = setInterval(() => {
      this.syncPresence();
    }, config.presence.syncInterval);

    logger.info('Presence service initialized');
  }

  async shutdown() {
    if (this.syncInterval) {
      clearInterval(this.syncInterval);
    }
    logger.info('Presence service shut down');
  }

  async setOnline(socket) {
    const user = socket.data.user;
    if (!user) return;

    const presenceKey = this.getPresenceKey(user.tenantId, user.userId);
    const tenantOnlineKey = this.getTenantOnlineKey(user.tenantId);

    const presenceData = {
      userId: user.userId,
      tenantId: user.tenantId,
      executiveId: user.executiveId,
      name: user.name || user.email,
      email: user.email,
      roles: user.roles,
      status: 'online',
      socketId: socket.id,
      lastSeen: new Date().toISOString(),
      metadata: {}
    };

    this.presenceCache.set(socket.id, presenceData);

    try {
      await redis.hset(presenceKey, socket.id, JSON.stringify(presenceData));
      await redis.expire(presenceKey, config.presence.ttl);
      await redis.sadd(tenantOnlineKey, user.userId);
      await redis.expire(tenantOnlineKey, config.presence.ttl * 2);

      await redis.publish(
        `presence:${user.tenantId}`,
        JSON.stringify({
          type: 'user_online',
          data: presenceData
        })
      );

      logger.debug(`User ${user.userId} is now online`);
    } catch (error) {
      logger.error('Error setting user online:', error);
    }

    return presenceData;
  }

  async setOffline(socket) {
    const user = socket.data.user;
    if (!user) return;

    const presenceKey = this.getPresenceKey(user.tenantId, user.userId);
    const socketId = socket.id;

    const cached = this.presenceCache.get(socketId);
    if (cached) {
      cached.status = 'offline';
      cached.lastSeen = new Date().toISOString();
    }

    try {
      const remaining = await redis.hdel(presenceKey, socketId);

      const otherSockets = await redis.hgetall(presenceKey);
      if (Object.keys(otherSockets).length === 0) {
        const tenantOnlineKey = this.getTenantOnlineKey(user.tenantId);
        await redis.srem(tenantOnlineKey, user.userId);

        await redis.publish(
          `presence:${user.tenantId}`,
          JSON.stringify({
            type: 'user_offline',
            data: {
              userId: user.userId,
              tenantId: user.tenantId,
              lastSeen: new Date().toISOString()
            }
          })
        );
      }

      logger.debug(`User ${user.userId} socket ${socketId} is now offline`);
    } catch (error) {
      logger.error('Error setting user offline:', error);
    }

    this.presenceCache.delete(socketId);
    this.clearTypingIndicator(socketId);
  }

  async updateStatus(socket, status, metadata = {}) {
    const user = socket.data.user;
    if (!user) return;

    const validStatuses = ['online', 'offline', 'away', 'busy'];
    if (!validStatuses.includes(status)) {
      throw new Error(`Invalid status: ${status}`);
    }

    const presenceKey = this.getPresenceKey(user.tenantId, user.userId);
    const socketId = socket.id;

    const presenceData = {
      userId: user.userId,
      tenantId: user.tenantId,
      name: user.name || user.email,
      status,
      lastSeen: new Date().toISOString(),
      metadata
    };

    this.presenceCache.set(socketId, presenceData);

    try {
      await redis.hset(presenceKey, socketId, JSON.stringify(presenceData));
      await redis.expire(presenceKey, config.presence.ttl);

      await redis.publish(
        `presence:${user.tenantId}`,
        JSON.stringify({
          type: 'status_change',
          data: presenceData
        })
      );

      logger.debug(`User ${user.userId} status changed to ${status}`);
    } catch (error) {
      logger.error('Error updating status:', error);
    }

    return presenceData;
  }

  async getPresence(tenantId, userId) {
    try {
      const presenceKey = this.getPresenceKey(tenantId, userId);
      const sockets = await redis.hgetall(presenceKey);

      if (!sockets || Object.keys(sockets).length === 0) {
        return {
          userId,
          tenantId,
          status: 'offline',
          lastSeen: null
        };
      }

      const socketData = Object.values(sockets).map(s => {
        try {
          return JSON.parse(s);
        } catch {
          return null;
        }
      }).filter(Boolean);

      const mostRecent = socketData.reduce((latest, current) => {
        return new Date(current.lastSeen) > new Date(latest.lastSeen) ? current : latest;
      }, socketData[0]);

      return {
        userId: mostRecent.userId,
        tenantId: mostRecent.tenantId,
        name: mostRecent.name,
        status: mostRecent.status,
        lastSeen: mostRecent.lastSeen,
        sockets: socketData.length,
        metadata: mostRecent.metadata
      };
    } catch (error) {
      logger.error('Error getting presence:', error);
      return null;
    }
  }

  async getOnlineUsers(tenantId) {
    try {
      const tenantOnlineKey = this.getTenantOnlineKey(tenantId);
      const userIds = await redis.smembers(tenantOnlineKey);

      const presences = [];
      for (const userId of userIds) {
        const presence = await this.getPresence(tenantId, userId);
        if (presence && presence.status !== 'offline') {
          presences.push(presence);
        }
      }

      return presences;
    } catch (error) {
      logger.error('Error getting online users:', error);
      return [];
    }
  }

  async getOnlineCount(tenantId) {
    try {
      const tenantOnlineKey = this.getTenantOnlineKey(tenantId);
      return await redis.scard(tenantOnlineKey);
    } catch (error) {
      logger.error('Error getting online count:', error);
      return 0;
    }
  }

  async isUserOnline(tenantId, userId) {
    try {
      const presenceKey = this.getPresenceKey(tenantId, userId);
      const sockets = await redis.hkeys(presenceKey);
      return sockets.length > 0;
    } catch (error) {
      logger.error('Error checking if user is online:', error);
      return false;
    }
  }

  async setTyping(socket, roomId, isTyping) {
    const user = socket.data.user;
    if (!user) return;

    const typingKey = `typing:${roomId}`;
    const typingData = {
      userId: user.userId,
      name: user.name || user.email,
      isTyping,
      timestamp: Date.now()
    };

    this.typingIndicators.set(`${roomId}:${socket.id}`, typingData);

    try {
      if (isTyping) {
        await redis.hset(typingKey, socket.id, JSON.stringify(typingData));
        await redis.expire(typingKey, config.presence.typingTimeout / 1000);
      } else {
        await redis.hdel(typingKey, socket.id);
      }
    } catch (error) {
      logger.error('Error setting typing indicator:', error);
    }

    return typingData;
  }

  async getTypingUsers(roomId) {
    try {
      const typingKey = `typing:${roomId}`;
      const typingUsers = await redis.hgetall(typingKey);

      const now = Date.now();
      const result = [];

      for (const [socketId, data] of Object.entries(typingUsers || {})) {
        try {
          const parsed = JSON.parse(data);
          if (now - parsed.timestamp < config.presence.typingTimeout) {
            result.push(parsed);
          } else {
            await redis.hdel(typingKey, socketId);
          }
        } catch {
          continue;
        }
      }

      return result;
    } catch (error) {
      logger.error('Error getting typing users:', error);
      return [];
    }
  }

  clearTypingIndicator(socketId) {
    for (const [key] of this.typingIndicators.keys()) {
      if (key.endsWith(`:${socketId}`)) {
        this.typingIndicators.delete(key);
      }
    }
  }

  async broadcastTyping(io, roomId, user, isTyping) {
    const typingUsers = await this.getTypingUsers(roomId);

    io.to(roomId).emit('presence:typing', {
      roomId,
      typingUsers,
      timestamp: Date.now()
    });
  }

  getPresenceKey(tenantId, userId) {
    return `presence:${tenantId}:${userId}`;
  }

  getTenantOnlineKey(tenantId) {
    return `online:${tenantId}`;
  }

  async syncPresence() {
    try {
      const now = Date.now();
      const expired = [];

      for (const [socketId, data] of this.presenceCache.entries()) {
        const lastSeen = new Date(data.lastSeen).getTime();
        if (now - lastSeen > config.presence.ttl * 1000) {
          expired.push(socketId);
        }
      }

      for (const socketId of expired) {
        this.presenceCache.delete(socketId);
      }

      if (expired.length > 0) {
        logger.debug(`Cleaned up ${expired.length} expired presence entries`);
      }
    } catch (error) {
      logger.error('Error syncing presence:', error);
    }
  }

  async getRoomPresence(io, roomId) {
    try {
      const sockets = await io.in(roomId).fetchSockets();
      const users = [];

      for (const socket of sockets) {
        if (socket.data.user) {
          const presence = await this.getPresence(
            socket.data.user.tenantId,
            socket.data.user.userId
          );
          if (presence) {
            users.push(presence);
          }
        }
      }

      return {
        roomId,
        userCount: users.length,
        users
      };
    } catch (error) {
      logger.error('Error getting room presence:', error);
      return { roomId, userCount: 0, users: [] };
    }
  }

  async broadcastRoomPresence(io, roomId) {
    const presence = await this.getRoomPresence(io, roomId);
    io.to(roomId).emit('presence:room', presence);
    return presence;
  }

  async getAllPresenceStats(tenantId) {
    try {
      const onlineCount = await this.getOnlineCount(tenantId);
      const onlineUsers = await this.getOnlineUsers(tenantId);

      const byStatus = {
        online: 0,
        away: 0,
        busy: 0,
        offline: 0
      };

      for (const user of onlineUsers) {
        if (byStatus[user.status] !== undefined) {
          byStatus[user.status]++;
        }
      }

      return {
        total: onlineCount,
        byStatus,
        users: onlineUsers
      };
    } catch (error) {
      logger.error('Error getting presence stats:', error);
      return {
        total: 0,
        byStatus: { online: 0, away: 0, busy: 0, offline: 0 },
        users: []
      };
    }
  }
}

module.exports = new PresenceService();
