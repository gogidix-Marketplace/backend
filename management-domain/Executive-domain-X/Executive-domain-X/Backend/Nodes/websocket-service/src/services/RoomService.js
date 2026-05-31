const config = require('../config');
const logger = require('../config/logger');
const redis = require('../config/redis');
const { v4: uuidv4 } = require('uuid');

class RoomService {
  constructor() {
    this.localRooms = new Map();
    this.roomMetadata = new Map();
  }

  generateRoomId(tenantId, name) {
    const prefix = config.multiTenant.enabled ? `tenant:${tenantId}:` : '';
    return `${prefix}room:${uuidv4()}`;
  }

  async createRoom(tenantId, name, type = 'public', options = {}) {
    const roomId = this.generateRoomId(tenantId, name);
    const metadata = {
      id: roomId,
      tenantId,
      name,
      type,
      createdBy: options.createdBy || null,
      maxUsers: options.maxUsers || config.connections.maxUsersPerRoom,
      currentUsers: 0,
      createdAt: new Date().toISOString(),
      metadata: options.metadata || {},
      persistent: options.persistent || false
    };

    this.roomMetadata.set(roomId, metadata);

    if (metadata.persistent) {
      await this.saveRoomToRedis(roomId, metadata);
    }

    logger.info(`Room created: ${roomId} (${name}) for tenant: ${tenantId}`);
    return metadata;
  }

  async saveRoomToRedis(roomId, metadata) {
    try {
      await redis.hset('rooms', roomId, JSON.stringify(metadata));
      await redis.set(`room:${roomId}:users', '{}', config.presence.ttl);
    } catch (error) {
      logger.error('Error saving room to Redis:', error);
    }
  }

  async getRoom(roomId) {
    let metadata = this.roomMetadata.get(roomId);

    if (!metadata) {
      try {
        const cached = await redis.hget('rooms', roomId);
        if (cached) {
          metadata = JSON.parse(cached);
          this.roomMetadata.set(roomId, metadata);
        }
      } catch (error) {
        logger.error('Error getting room from Redis:', error);
      }
    }

    return metadata;
  }

  async joinRoom(socket, roomId, metadata = {}) {
    const user = socket.data.user;
    if (!user) {
      throw new Error('User not authenticated');
    }

    const room = await this.getRoom(roomId);

    if (room && room.currentUsers >= room.maxUsers) {
      throw new Error(`Room '${roomId}' is full`);
    }

    const tenantRoomId = this.ensureTenantPrefix(roomId, user.tenantId);

    socket.join(tenantRoomId);

    await this.addUserToRoom(tenantRoomId, socket.id, user, metadata);

    if (room) {
      room.currentUsers++;
      this.roomMetadata.set(roomId, room);
    }

    logger.info(`User ${user.userId} joined room: ${tenantRoomId}`);

    return {
      roomId: tenantRoomId,
      userCount: await this.getRoomUserCount(tenantRoomId),
      users: await this.getRoomUsers(tenantRoomId)
    };
  }

  ensureTenantPrefix(roomId, tenantId) {
    if (config.multiTenant.enabled && !roomId.startsWith(`tenant:${tenantId}:`)) {
      if (!roomId.startsWith('tenant:')) {
        return `tenant:${tenantId}:${roomId}`;
      }
    }
    return roomId;
  }

  async leaveRoom(socket, roomId) {
    const user = socket.data.user;
    if (!user) return;

    const tenantRoomId = this.ensureTenantPrefix(roomId, user.tenantId);

    socket.leave(tenantRoomId);

    await this.removeUserFromRoom(tenantRoomId, socket.id);

    const room = await this.getRoom(roomId);
    if (room) {
      room.currentUsers = Math.max(0, room.currentUsers - 1);
      this.roomMetadata.set(roomId, room);
    }

    logger.info(`User ${user.userId} left room: ${tenantRoomId}`);
  }

  async leaveAllRooms(socket) {
    const user = socket.data.user;
    if (!user) return;

    const rooms = Array.from(socket.rooms);
    for (const room of rooms) {
      if (room !== socket.id) {
        await this.leaveRoom(socket, room);
      }
    }
  }

  async addUserToRoom(roomId, socketId, user, metadata = {}) {
    try {
      const key = `room:${roomId}:users`;
      const userData = {
        socketId,
        userId: user.userId,
        tenantId: user.tenantId,
        name: user.name || user.email,
        roles: user.roles || [],
        joinedAt: new Date().toISOString(),
        metadata
      };

      await redis.hset(key, socketId, JSON.stringify(userData));
      await redis.expire(key, config.presence.ttl);

      this.localRooms.set(`${roomId}:${socketId}`, userData);
    } catch (error) {
      logger.error('Error adding user to room:', error);
    }
  }

  async removeUserFromRoom(roomId, socketId) {
    try {
      const key = `room:${roomId}:users`;
      await redis.hdel(key, socketId);
      this.localRooms.delete(`${roomId}:${socketId}`);
    } catch (error) {
      logger.error('Error removing user from room:', error);
    }
  }

  async getRoomUsers(roomId) {
    try {
      const key = `room:${roomId}:users`;
      const users = await redis.hgetall(key);

      return Object.values(users).map(data => {
        try {
          return JSON.parse(data);
        } catch {
          return null;
        }
      }).filter(Boolean);
    } catch (error) {
      logger.error('Error getting room users:', error);
      return [];
    }
  }

  async getRoomUserCount(roomId) {
    try {
      const key = `room:${roomId}:users`;
      return await redis.hlen(key);
    } catch (error) {
      logger.error('Error getting room user count:', error);
      return 0;
    }
  }

  async getUserRooms(userId, tenantId) {
    try {
      const pattern = `room:tenant:${tenantId}:*`;
      const keys = await redis.keys(pattern);

      const userRooms = [];
      for (const key of keys) {
        const users = await redis.hgetall(key);
        const userValues = Object.values(users);
        for (const uv of userValues) {
          try {
            const userData = JSON.parse(uv);
            if (userData.userId === userId) {
              const roomId = key.replace('room:', '').replace(':users', '');
              userRooms.push(roomId);
              break;
            }
          } catch {
            continue;
          }
        }
      }

      return userRooms;
    } catch (error) {
      logger.error('Error getting user rooms:', error);
      return [];
    }
  }

  async listRooms(tenantId, type = null) {
    try {
      const rooms = [];

      for (const [roomId, metadata] of this.roomMetadata.entries()) {
        if (metadata.tenantId === tenantId) {
          if (!type || metadata.type === type) {
            rooms.push(metadata);
          }
        }
      }

      const allRooms = await redis.hgetall('rooms');
      for (const [roomId, data] of Object.entries(allRooms || {})) {
        try {
          const metadata = JSON.parse(data);
          if (metadata.tenantId === tenantId) {
            if (!type || metadata.type === type) {
              const exists = rooms.find(r => r.id === roomId);
              if (!exists) {
                rooms.push(metadata);
              }
            }
          }
        } catch {
          continue;
        }
      }

      return rooms;
    } catch (error) {
      logger.error('Error listing rooms:', error);
      return [];
    }
  }

  async updateRoomMetadata(roomId, metadata) {
    const room = await this.getRoom(roomId);
    if (!room) {
      throw new Error(`Room '${roomId}' not found`);
    }

    Object.assign(room.metadata, metadata);
    room.updatedAt = new Date().toISOString();

    this.roomMetadata.set(roomId, room);

    if (room.persistent) {
      await redis.hset('rooms', roomId, JSON.stringify(room));
    }

    return room;
  }

  async deleteRoom(roomId) {
    const room = await this.getRoom(roomId);
    if (!room) {
      throw new Error(`Room '${roomId}' not found`);
    }

    this.roomMetadata.delete(roomId);

    try {
      await redis.hdel('rooms', roomId);
      await redis.del(`room:${roomId}:users`);
    } catch (error) {
      logger.error('Error deleting room from Redis:', error);
    }

    logger.info(`Room deleted: ${roomId}`);
    return { success: true, roomId };
  }

  async cleanupExpiredRooms() {
    try {
      const rooms = await redis.hgetall('rooms');
      let cleaned = 0;

      for (const [roomId, data] of Object.entries(rooms || {})) {
        try {
          const metadata = JSON.parse(data);

          if (!metadata.persistent) {
            const userCount = await this.getRoomUserCount(roomId);

            if (userCount === 0) {
              await redis.hdel('rooms', roomId);
              this.roomMetadata.delete(roomId);
              cleaned++;
            }
          }
        } catch {
          continue;
        }
      }

      if (cleaned > 0) {
        logger.info(`Cleaned up ${cleaned} expired rooms`);
      }

      return cleaned;
    } catch (error) {
      logger.error('Error cleaning up expired rooms:', error);
      return 0;
    }
  }

  getTenantRoomPrefix(tenantId) {
    return config.multiTenant.enabled ? `tenant:${tenantId}:` : '';
  }

  getSystemRoom(tenantId, roomName) {
    const prefix = this.getTenantRoomPrefix(tenantId);
    return `${prefix}system:${roomName}`;
  }

  getUserRoom(tenantId, userId) {
    const prefix = this.getTenantRoomPrefix(tenantId);
    return `${prefix}user:${userId}`;
  }

  getDashboardRoom(tenantId, dashboardId) {
    const prefix = this.getTenantRoomPrefix(tenantId);
    return `${prefix}dashboard:${dashboardId}`;
  }

  getKpiRoom(tenantId, kpiId) {
    const prefix = this.getTenantRoomPrefix(tenantId);
    return `${prefix}kpi:${kpiId}`;
  }

  getNotificationRoom(tenantId, userId) {
    const prefix = this.getTenantRoomPrefix(tenantId);
    return `${prefix}notifications:${userId}`;
  }

  isTenantRoom(roomId, tenantId) {
    if (!config.multiTenant.enabled) return true;
    return roomId.startsWith(`tenant:${tenantId}:`);
  }

  extractTenantId(roomId) {
    if (!config.multiTenant.enabled) return config.multiTenant.defaultTenantId;

    const match = roomId.match(/^tenant:([^:]+):/);
    return match ? match[1] : config.multiTenant.defaultTenantId;
  }
}

module.exports = new RoomService();
