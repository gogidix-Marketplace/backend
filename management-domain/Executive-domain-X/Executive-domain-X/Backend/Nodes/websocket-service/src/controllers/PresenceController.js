const config = require('../config');
const logger = require('../config/logger');
const presenceService = require('../services/PresenceService');
const Joi = require('joi');

class PresenceController {
  constructor() {
    this.schemas = {
      updateStatus: Joi.object({
        userId: Joi.string().required(),
        status: Joi.string().valid('online', 'offline', 'away', 'busy').required(),
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

  async getPresence(req, res) {
    try {
      const { userId, tenantId } = req.params;

      const presence = await presenceService.getPresence(tenantId, userId);

      res.json({
        success: true,
        presence
      });
    } catch (error) {
      logger.error('Error getting presence:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getOnlineUsers(req, res) {
    try {
      const { tenantId } = req.params;

      const users = await presenceService.getOnlineUsers(tenantId);

      res.json({
        success: true,
        users,
        count: users.length
      });
    } catch (error) {
      logger.error('Error getting online users:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getOnlineCount(req, res) {
    try {
      const { tenantId } = req.params;

      const count = await presenceService.getOnlineCount(tenantId);

      res.json({
        success: true,
        tenantId,
        onlineCount: count
      });
    } catch (error) {
      logger.error('Error getting online count:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async getPresenceStats(req, res) {
    try {
      const { tenantId } = req.params;

      const stats = await presenceService.getAllPresenceStats(tenantId);

      res.json({
        success: true,
        stats
      });
    } catch (error) {
      logger.error('Error getting presence stats:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async updateStatus(req, res) {
    try {
      const validation = this.validate('updateStatus', req.body);
      if (!validation.success) {
        return res.status(400).json({ success: false, errors: validation.errors });
      }

      const { userId, status, metadata } = validation.data;
      const { tenantId } = req.params;

      const presence = await presenceService.updateStatus(
        { data: { user: { userId, tenantId } } },
        status,
        metadata
      );

      res.json({
        success: true,
        presence
      });
    } catch (error) {
      logger.error('Error updating status:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }

  async isUserOnline(req, res) {
    try {
      const { userId, tenantId } = req.params;

      const isOnline = await presenceService.isUserOnline(tenantId, userId);

      res.json({
        success: true,
        userId,
        tenantId,
        online: isOnline
      });
    } catch (error) {
      logger.error('Error checking if user is online:', error);
      res.status(500).json({ success: false, message: error.message });
    }
  }
}

module.exports = new PresenceController();
