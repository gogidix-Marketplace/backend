const { v4: uuidv4 } = require('uuid');
const mongodb = require('../config/mongodb');
const logger = require('../config/logger');
const metrics = require('../config/metrics');

/**
 * Dead Letter Queue Model
 * Handles failed events with retry mechanisms
 */
class DeadLetterQueue {
  constructor() {
    this.collection = null;
    this.maxRetryAttempts = parseInt(process.env.DLQ_MAX_RETRY_ATTEMPTS) || 5;
    this.retryBackoffBase = parseInt(process.env.DLQ_RETRY_BACKOFF_BASE) || 1000; // 1 second
  }

  async getCollection() {
    if (!this.collection) {
      this.collection = mongodb.getCollection('dead_letter_queue');
    }
    return this.collection;
  }

  /**
   * Add a failed event to the DLQ
   */
  async addEvent(originalEvent, error, options = {}) {
    try {
      const collection = await this.getCollection();

      const dlqEvent = {
        dlqEventId: uuidv4(),
        originalEventId: originalEvent.eventId || uuidv4(),
        originalTopic: originalEvent.topic || 'executive-domain-events',
        eventType: originalEvent.key || originalEvent.eventType,
        tenantId: originalEvent.tenantId || options.tenantId || 'default',
        payload: originalEvent.value || originalEvent.payload || originalEvent.data,
        headers: originalEvent.headers || {},
        error: {
          message: error.message || String(error),
          stack: error.stack,
          code: error.code,
          type: error.constructor.name
        },
        reason: options.reason || 'PROCESSING_ERROR',
        retryCount: 0,
        maxRetryAttempts: options.maxRetryAttempts || this.maxRetryAttempts,
        nextRetryAt: this.calculateNextRetry(0),
        status: 'PENDING',
        severity: this.calculateSeverity(error, options),
        metadata: options.metadata || {},
        createdAt: new Date(),
        lastAttemptedAt: null
      };

      await collection.insertOne(dlqEvent);

      // Log and metrics
      logger.dlq('Event added to DLQ', {
        dlqEventId: dlqEvent.dlqEventId,
        originalEventId: dlqEvent.originalEventId,
        eventType: dlqEvent.eventType,
        tenantId: dlqEvent.tenantId,
        reason: dlqEvent.reason
      });

      metrics.recordDlqEvent(dlqEvent.eventType, dlqEvent.tenantId, dlqEvent.reason);

      return dlqEvent;
    } catch (err) {
      logger.error('Error adding event to DLQ:', err);
      throw err;
    }
  }

  /**
   * Calculate next retry time with exponential backoff
   */
  calculateNextRetry(retryCount) {
    const delay = this.retryBackoffBase * Math.pow(2, retryCount);
    const jitter = Math.random() * 1000; // Add jitter to avoid thundering herd
    return new Date(Date.now() + delay + jitter);
  }

  /**
   * Calculate severity of the error
   */
  calculateSeverity(error, options) {
    // Check for permanent errors
    if (options.reason === 'SCHEMA_VALIDATION' ||
        options.reason === 'INVALID_EVENT' ||
        error.code === 'VALIDATION_ERROR') {
      return 'FATAL';
    }

    if (options.reason === 'DEPENDENCY_UNAVAILABLE' ||
        error.code === 'ECONNREFUSED' ||
        error.code === 'ETIMEDOUT') {
      return 'HIGH';
    }

    return 'MEDIUM';
  }

  /**
   * Get events ready for retry
   */
  async getRetryableEvents(tenantId = null, limit = 50) {
    try {
      const collection = await this.getCollection();
      const now = new Date();

      const query = {
        status: 'PENDING',
        nextRetryAt: { $lte: now },
        retryCount: { $lt: this.maxRetryAttempts },
        severity: { $ne: 'FATAL' }
      };

      if (tenantId) {
        query.tenantId = tenantId;
      }

      return await collection
        .find(query)
        .sort({ nextRetryAt: 1, createdAt: 1 })
        .limit(limit)
        .toArray();
    } catch (error) {
      logger.error('Error getting retryable events:', error);
      throw error;
    }
  }

  /**
   * Mark event for retry
   */
  async markForRetry(dlqEventId) {
    try {
      const collection = await this.getCollection();

      const event = await collection.findOne({ dlqEventId });
      if (!event) {
        throw new Error(`DLQ event not found: ${dlqEventId}`);
      }

      const update = {
        $inc: { retryCount: 1 },
        $set: {
          nextRetryAt: this.calculateNextRetry(event.retryCount),
          lastAttemptedAt: new Date()
        }
      };

      if (event.retryCount + 1 >= event.maxRetryAttempts) {
        update.$set.status = 'EXHAUSTED';
      }

      const result = await collection.updateOne({ dlqEventId }, update);

      logger.dlq('Event marked for retry', {
        dlqEventId,
        retryCount: event.retryCount + 1,
        status: event.retryCount + 1 >= event.maxRetryAttempts ? 'EXHAUSTED' : 'PENDING'
      });

      metrics.recordDlqRetry(event.eventType, event.tenantId);

      return result.modifiedCount > 0;
    } catch (error) {
      logger.error(`Error marking event ${dlqEventId} for retry:`, error);
      throw error;
    }
  }

  /**
   * Mark event as resolved (successfully reprocessed)
   */
  async markAsResolved(dlqEventId) {
    try {
      const collection = await this.getCollection();
      const result = await collection.updateOne(
        { dlqEventId },
        {
          $set: {
            status: 'RESOLVED',
            resolvedAt: new Date()
          }
        }
      );

      logger.dlq('Event marked as resolved', { dlqEventId });

      return result.modifiedCount > 0;
    } catch (error) {
      logger.error(`Error marking event ${dlqEventId} as resolved:`, error);
      throw error;
    }
  }

  /**
   * Mark event as permanently failed
   */
  async markAsFailed(dlqEventId, notes = null) {
    try {
      const collection = await this.getCollection();
      const update = {
        $set: {
          status: 'FAILED_PERMANENTLY',
          failedAt: new Date()
        }
      };

      if (notes) {
        update.$set.notes = notes;
      }

      const result = await collection.updateOne({ dlqEventId }, update);

      logger.dlq('Event marked as permanently failed', { dlqEventId, notes });

      return result.modifiedCount > 0;
    } catch (error) {
      logger.error(`Error marking event ${dlqEventId} as permanently failed:`, error);
      throw error;
    }
  }

  /**
   * Get DLQ stats
   */
  async getStats(tenantId = null) {
    try {
      const collection = await this.getCollection();
      const matchStage = tenantId ? { $match: { tenantId } } : { $match: {} };

      const statusStats = await collection.aggregate([
        matchStage,
        {
          $group: {
            _id: '$status',
            count: { $sum: 1 }
          }
        }
      ]).toArray();

      const typeStats = await collection.aggregate([
        matchStage,
        {
          $group: {
            _id: '$eventType',
            count: { $sum: 1 }
          }
        }
      ]).toArray();

      const severityStats = await collection.aggregate([
        matchStage,
        {
          $group: {
            _id: '$severity',
            count: { $sum: 1 }
          }
        }
      ]).toArray();

      const total = await collection.countDocuments(tenantId ? { tenantId } : {});

      return {
        total,
        statusCounts: statusStats.reduce((acc, s) => {
          acc[s._id] = s.count;
          return acc;
        }, {}),
        typeCounts: typeStats.reduce((acc, s) => {
          acc[s._id] = s.count;
          return acc;
        }, {}),
        severityCounts: severityStats.reduce((acc, s) => {
          acc[s._id] = s.count;
          return acc;
        }, {})
      };
    } catch (error) {
      logger.error('Error getting DLQ stats:', error);
      throw error;
    }
  }

  /**
   * Get DLQ events with filtering
   */
  async getEvents(filters = {}) {
    try {
      const collection = await this.getCollection();
      const {
        tenantId,
        eventType,
        status,
        severity,
        startDate,
        endDate,
        limit = 100,
        skip = 0
      } = filters;

      const query = {};

      if (tenantId) query.tenantId = tenantId;
      if (eventType) query.eventType = eventType;
      if (status) query.status = status;
      if (severity) query.severity = severity;
      if (startDate || endDate) {
        query.createdAt = {};
        if (startDate) query.createdAt.$gte = new Date(startDate);
        if (endDate) query.createdAt.$lte = new Date(endDate);
      }

      return await collection
        .find(query)
        .sort({ createdAt: -1 })
        .skip(skip)
        .limit(limit)
        .toArray();
    } catch (error) {
      logger.error('Error getting DLQ events:', error);
      throw error;
    }
  }

  /**
   * Get a single DLQ event by ID
   */
  async getEvent(dlqEventId) {
    try {
      const collection = await this.getCollection();
      return await collection.findOne({ dlqEventId });
    } catch (error) {
      logger.error(`Error getting DLQ event ${dlqEventId}:`, error);
      throw error;
    }
  }

  /**
   * Retry a specific DLQ event immediately
   */
  async retryNow(dlqEventId) {
    try {
      const collection = await this.getCollection();
      const event = await collection.findOne({ dlqEventId });

      if (!event) {
        throw new Error(`DLQ event not found: ${dlqEventId}`);
      }

      if (event.status === 'RESOLVED') {
        throw new Error('Event is already resolved');
      }

      await collection.updateOne(
        { dlqEventId },
        {
          $set: {
            nextRetryAt: new Date(),
            lastAttemptedAt: new Date()
          }
        }
      );

      return event;
    } catch (error) {
      logger.error(`Error retrying event ${dlqEventId}:`, error);
      throw error;
    }
  }

  /**
   * Purge old resolved events
   */
  async purgeResolved(olderThanDays = 30) {
    try {
      const collection = await this.getCollection();
      const cutoffDate = new Date();
      cutoffDate.setDate(cutoffDate.getDate() - olderThanDays);

      const result = await collection.deleteMany({
        status: 'RESOLVED',
        resolvedAt: { $lt: cutoffDate }
      });

      logger.info(`Purged ${result.deletedCount} resolved DLQ events older than ${olderThanDays} days`);

      return result.deletedCount;
    } catch (error) {
      logger.error('Error purging resolved DLQ events:', error);
      throw error;
    }
  }
}

module.exports = new DeadLetterQueue();
