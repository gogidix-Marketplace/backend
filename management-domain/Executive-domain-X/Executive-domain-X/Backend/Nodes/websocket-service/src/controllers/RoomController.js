const config = require('../config');
const logger = require('../config/logger');
const roomService = require('../services/RoomService');
const presenceService = require('../services/PresenceService');
const Joi = require('joi');

class RoomController {
  constructor() {
    this.schemas = {
      createRoom: Joi.object({
        name: Joi.string().required(),
        type: Joi.string().valid('public', 'private', 'presence').required(),
        maxUsers: Joi.number().integer().min(1).max(1000).optional(),
        metadata: Joi.object().optional(),
        tenantId: Joi.string().required()
      }),
      updateRoom: Joi.object({
        name: Joi.string().optional(),
        maxUsers: Joi.number().integer().min(1).max(1000).optional(),
        metadata: Joi.object().optional()
      })
    };
  }

  validate(schemaName, data) {
    const schema = this.schemas[schemaName];
    if (!schema) {
      return { success: true, data };
    }

    const { error, value } = schema.validate(data, {
      abortEarly: false,
      stripUnknown: true
    });

    if (error) {
      const errors = error.details.map(detail => ({
        field: detail.path.join('.'),
        message: detail.message
      }));
      return { success: false, errors };
    }

    return { success: true, data: value };
  }

  async createRoom(req, res) {
    try {
      const validation = this.validate('createRoom', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { name, type, maxUsers, metadata, tenantId } = validation.data;

      const room = await roomService.createRoom(tenantId, name, type, {
        maxUsers,
        metadata,
        createdBy: req.user?.userId || 'api'
      });

      res.status(201).json({
        success: true,
        room
      });
    } catch (error) {
      logger.error('Error creating room:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getRoom(req, res) {
    try {
      const { roomId } = req.params;
      const { tenantId } = req.query;

      if (!tenantId) {
        return res.status(400).json({ success: false, message: 'tenantId is required' });
      }

      const room = await roomService.getRoom(roomId);

      if (!room) {
        return res.status(404).json({ success: false, message: 'Room not found' });
      }

      if (room.tenantId !== tenantId) {
        return res.status(403).json({ success: false, message: 'Access denied' });
      }

      const users = await roomService.getRoomUsers(roomId);

      res.json({
        success: true,
        room: {
          ...room,
          currentUsers: users.length,
          users
        }
      });
    } catch (error) {
      logger.error('Error getting room:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async listRooms(req, res) {
    try {
      const { tenantId, type } = req.query;

      if (!tenantId) {
        return res.status(400).json({ success: false, message: 'tenantId is required' });
      }

      const rooms = await roomService.listRooms(tenantId, type);

      res.json({
        success: true,
        rooms,
        count: rooms.length
      });
    } catch (error) {
      logger.error('Error listing rooms:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async updateRoom(req, res) {
    try {
      const { roomId } = req.params;
      const validation = this.validate('updateRoom', req.body);

      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const room = await roomService.updateRoomMetadata(roomId, validation.data);

      res.json({
        success: true,
        room
      });
    } catch (error) {
      logger.error('Error updating room:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async deleteRoom(req, res) {
    try {
      const { roomId } = req.params;

      const result = await roomService.deleteRoom(roomId);

      res.json({
        success: true,
        ...result
      });
    } catch (error) {
      logger.error('Error deleting room:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getRoomUsers(req, res) {
    try {
      const { roomId } = req.params;
      const { tenantId } = req.query;

      if (!tenantId) {
        return res.status(400).json({ success: false, message: 'tenantId is required' });
      }

      const fullRoomId = roomService.ensureTenantPrefix(roomId, tenantId);
      const users = await roomService.getRoomUsers(fullRoomId);

      res.json({
        success: true,
        roomId: fullRoomId,
        users,
        count: users.length
      });
    } catch (error) {
      logger.error('Error getting room users:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getUserRooms(req, res) {
    try {
      const { userId, tenantId } = req.params;

      const rooms = await roomService.getUserRooms(userId, tenantId);

      res.json({
        success: true,
        userId,
        tenantId,
        rooms,
        count: rooms.length
      });
    } catch (error) {
      logger.error('Error getting user rooms:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getRoomPresence(req, res) {
    try {
      const { roomId } = req.params;
      const { tenantId } = req.query;

      if (!tenantId) {
        return res.status(400).json({ success: false, message: 'tenantId is required' });
      }

      const wsServer = require('../websocket/server');
      const io = wsServer.getInstance();

      if (!io) {
        return res.status(503).json({ success: false, message: 'WebSocket server not available' });
      }

      const fullRoomId = roomService.ensureTenantPrefix(roomId, tenantId);
      const presence = await presenceService.getRoomPresence(io, fullRoomId);

      res.json({
        success: true,
        presence
      });
    } catch (error) {
      logger.error('Error getting room presence:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }
}

module.exports = new RoomController();
