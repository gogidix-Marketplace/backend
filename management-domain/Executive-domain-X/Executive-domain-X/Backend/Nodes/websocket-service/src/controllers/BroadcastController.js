const config = require('../config');
const logger = require('../config/logger');
const broadcastService = require('../services/BroadcastService');
const roomService = require('../services/RoomService');
const kafkaService = require('../services/KafkaService');
const wsServer = require('../websocket/server');
const Joi = require('joi');

class BroadcastController {
  constructor() {
    this.schemas = {
      broadcastToRoom: Joi.object({
        roomId: Joi.string().required(),
        event: Joi.string().required(),
        data: Joi.object().required(),
        exclude: Joi.array().items(Joi.string()).optional(),
        tenantId: Joi.string().required()
      }),
      broadcastToUser: Joi.object({
        userId: Joi.string().required(),
        event: Joi.string().required(),
        data: Joi.object().required(),
        tenantId: Joi.string().required()
      }),
      broadcastToTenant: Joi.object({
        tenantId: Joi.string().required(),
        event: Joi.string().required(),
        data: Joi.object().required()
      }),
      broadcastGlobal: Joi.object({
        event: Joi.string().required(),
        data: Joi.object().required()
      }),
      kpiUpdate: Joi.object({
        tenantId: Joi.string().required(),
        kpiId: Joi.string().required(),
        value: Joi.alternatives().try(Joi.number(), Joi.object()).required(),
        change: Joi.number().optional()
      }),
      dashboardUpdate: Joi.object({
        tenantId: Joi.string().required(),
        dashboardId: Joi.string().required(),
        type: Joi.string().valid('widget_updated', 'layout_changed', 'data_updated').required(),
        data: Joi.object().required()
      }),
      notification: Joi.object({
        tenantId: Joi.string().required(),
        userId: Joi.string().required(),
        type: Joi.string().required(),
        title: Joi.string().required(),
        message: Joi.string().required(),
        data: Joi.object().optional()
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

  async broadcastToRoom(req, res) {
    try {
      const validation = this.validate('broadcastToRoom', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { roomId, event, data, exclude, tenantId } = validation.data;

      const result = await broadcastService.broadcastToRoom(
        roomService.ensureTenantPrefix(roomId, tenantId),
        event,
        data,
        { exclude, toKafka: true }
      );

      res.json({ success: true, ...result });
    } catch (error) {
      logger.error('Error broadcasting to room:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async broadcastToUser(req, res) {
    try {
      const validation = this.validate('broadcastToUser', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { userId, event, data, tenantId } = validation.data;

      const result = await broadcastService.broadcastToUser(tenantId, userId, event, data, {
        toKafka: true
      });

      res.json({ success: true, ...result });
    } catch (error) {
      logger.error('Error broadcasting to user:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async broadcastToTenant(req, res) {
    try {
      const validation = this.validate('broadcastToTenant', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { tenantId, event, data } = validation.data;

      const result = await broadcastService.broadcastToTenant(tenantId, event, data, {
        toKafka: true
      });

      res.json({ success: true, ...result });
    } catch (error) {
      logger.error('Error broadcasting to tenant:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async broadcastGlobal(req, res) {
    try {
      const validation = this.validate('broadcastGlobal', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { event, data } = validation.data;

      const result = await broadcastService.broadcastGlobal(event, data, {
        toKafka: true
      });

      res.json({ success: true, ...result });
    } catch (error) {
      logger.error('Error broadcasting globally:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async sendKpiUpdate(req, res) {
    try {
      const validation = this.validate('kpiUpdate', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { tenantId, kpiId, value, change } = validation.data;

      await broadcastService.broadcastKpiUpdate(tenantId, kpiId, {
        value,
        change,
        timestamp: new Date().toISOString()
      });

      await kafkaService.publishKpiUpdate(tenantId, kpiId, 'kpi_value_updated', {
        value,
        change,
        timestamp: new Date().toISOString()
      });

      res.json({
        success: true,
        message: 'KPI update broadcast successfully',
        kpiId,
        value
      });
    } catch (error) {
      logger.error('Error sending KPI update:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async sendDashboardUpdate(req, res) {
    try {
      const validation = this.validate('dashboardUpdate', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { tenantId, dashboardId, type, data } = validation.data;

      await broadcastService.broadcastDashboardUpdate(tenantId, dashboardId, {
        type,
        ...data,
        timestamp: new Date().toISOString()
      });

      await kafkaService.publishDashboardUpdate(tenantId, dashboardId, type, data);

      res.json({
        success: true,
        message: 'Dashboard update broadcast successfully',
        dashboardId
      });
    } catch (error) {
      logger.error('Error sending dashboard update:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async sendNotification(req, res) {
    try {
      const validation = this.validate('notification', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { tenantId, userId, type, title, message, data } = validation.data;

      const notification = {
        id: `notif_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`,
        type,
        title,
        message,
        data: data || {},
        timestamp: new Date().toISOString(),
        read: false
      };

      await broadcastService.broadcastNotification(tenantId, userId, notification);

      await kafkaService.publishNotification(tenantId, userId, notification);

      res.json({
        success: true,
        message: 'Notification sent successfully',
        notificationId: notification.id
      });
    } catch (error) {
      logger.error('Error sending notification:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getRoomHistory(req, res) {
    try {
      const { roomId } = req.params;
      const { tenantId } = req.query;
      const limit = parseInt(req.query.limit, 10) || 50;

      if (!tenantId) {
        return res.status(400).json({ success: false, message: 'tenantId is required' });
      }

      const fullRoomId = roomService.ensureTenantPrefix(roomId, tenantId);
      const history = await broadcastService.getRoomHistory(fullRoomId, limit);

      res.json({
        success: true,
        roomId: fullRoomId,
        history,
        count: history.length
      });
    } catch (error) {
      logger.error('Error getting room history:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async clearRoomHistory(req, res) {
    try {
      const { roomId } = req.params;
      const { tenantId } = req.query;

      if (!tenantId) {
        return res.status(400).json({ success: false, message: 'tenantId is required' });
      }

      const fullRoomId = roomService.ensureTenantPrefix(roomId, tenantId);
      await broadcastService.clearRoomHistory(fullRoomId);

      res.json({
        success: true,
        message: 'Room history cleared successfully',
        roomId: fullRoomId
      });
    } catch (error) {
      logger.error('Error clearing room history:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getBroadcastStats(req, res) {
    try {
      const stats = broadcastService.getStats();
      res.json({ success: true, stats });
    } catch (error) {
      logger.error('Error getting broadcast stats:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }
}

module.exports = new BroadcastController();
