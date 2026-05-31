const { v4: uuidv4 } = require('uuid');
const mongodb = require('../config/mongodb');
const logger = require('../config/logger');

/**
 * Event Store Model - CQRS Event Sourcing
 * Stores all domain events for replay and projection
 */
class EventStore {
  constructor() {
    this.collection = null;
  }

  async getCollection() {
    if (!this.collection) {
      this.collection = mongodb.getCollection('event_store');
    }
    return this.collection;
  }

  /**
   * Save an event to the event store
   */
  async saveEvent(eventData) {
    try {
      const collection = await this.getCollection();
      const event = {
        eventId: eventData.eventId || uuidv4(),
        eventType: eventData.eventType,
        eventVersion: eventData.eventVersion || '1.0',
        aggregateId: eventData.aggregateId,
        aggregateType: eventData.aggregateType,
        tenantId: eventData.tenantId || 'default',
        payload: eventData.payload || eventData.data || {},
        metadata: eventData.metadata || {},
        causationId: eventData.causationId || null,
        correlationId: eventData.correlationId || null,
        timestamp: eventData.timestamp || new Date(),
        status: 'PENDING',
        processedAt: null,
        retryCount: 0,
        createdAt: new Date()
      };

      // Check for duplicate event ID
      const existing = await collection.findOne({ eventId: event.eventId });
      if (existing) {
        logger.warn(`Duplicate event detected: ${event.eventId}`);
        return existing;
      }

      // Check aggregate version for optimistic concurrency
      const lastEvent = await collection.findOne(
        { aggregateId: event.aggregateId },
        { sort: { version: -1 } }
      );

      const version = lastEvent ? (lastEvent.version || 0) + 1 : 1;
      event.version = version;

      await collection.insertOne(event);
      logger.events('Event saved to event store', {
        eventId: event.eventId,
        eventType: event.eventType,
        aggregateId: event.aggregateId,
        version,
        tenantId: event.tenantId
      });

      return event;
    } catch (error) {
      logger.error('Error saving event to event store:', error);
      throw error;
    }
  }

  /**
   * Mark event as processed
   */
  async markAsProcessed(eventId, processedAt = new Date()) {
    try {
      const collection = await this.getCollection();
      const result = await collection.updateOne(
        { eventId },
        {
          $set: {
            status: 'COMPLETED',
            processedAt
          }
        }
      );
      return result.modifiedCount > 0;
    } catch (error) {
      logger.error(`Error marking event ${eventId} as processed:`, error);
      throw error;
    }
  }

  /**
   * Mark event as failed
   */
  async markAsFailed(eventId, error, processedAt = new Date()) {
    try {
      const collection = await this.getCollection();
      const result = await collection.updateOne(
        { eventId },
        {
          $set: {
            status: 'FAILED',
            error: typeof error === 'string' ? error : error.message,
            processedAt
          },
          $inc: { retryCount: 1 }
        }
      );
      return result.modifiedCount > 0;
    } catch (error) {
      logger.error(`Error marking event ${eventId} as failed:`, error);
      throw error;
    }
  }

  /**
   * Mark event as processing
   */
  async markAsProcessing(eventId) {
    try {
      const collection = await this.getCollection();
      const result = await collection.updateOne(
        { eventId, status: 'PENDING' },
        { $set: { status: 'PROCESSING' } }
      );
      return result.modifiedCount > 0;
    } catch (error) {
      logger.error(`Error marking event ${eventId} as processing:`, error);
      throw error;
    }
  }

  /**
   * Get events by aggregate ID
   */
  async getEventsByAggregate(aggregateId, tenantId = null) {
    try {
      const collection = await this.getCollection();
      const query = { aggregateId };
      if (tenantId) {
        query.tenantId = tenantId;
      }
      return await collection.find(query).sort({ version: 1 }).toArray();
    } catch (error) {
      logger.error(`Error getting events for aggregate ${aggregateId}:`, error);
      throw error;
    }
  }

  /**
   * Get events by type
   */
  async getEventsByType(eventType, tenantId = null, limit = 100) {
    try {
      const collection = await this.getCollection();
      const query = { eventType };
      if (tenantId) {
        query.tenantId = tenantId;
      }
      return await collection
        .find(query)
        .sort({ timestamp: -1 })
        .limit(limit)
        .toArray();
    } catch (error) {
      logger.error(`Error getting events of type ${eventType}:`, error);
      throw error;
    }
  }

  /**
   * Get events by tenant with multi-tenant isolation
   */
  async getEventsByTenant(tenantId, options = {}) {
    try {
      const collection = await this.getCollection();
      const {
        eventType,
        startDate,
        endDate,
        status,
        limit = 100,
        skip = 0
      } = options;

      const query = { tenantId };

      if (eventType) query.eventType = eventType;
      if (status) query.status = status;
      if (startDate || endDate) {
        query.timestamp = {};
        if (startDate) query.timestamp.$gte = new Date(startDate);
        if (endDate) query.timestamp.$lte = new Date(endDate);
      }

      return await collection
        .find(query)
        .sort({ timestamp: -1 })
        .skip(skip)
        .limit(limit)
        .toArray();
    } catch (error) {
      logger.error(`Error getting events for tenant ${tenantId}:`, error);
      throw error;
    }
  }

  /**
   * Get pending events for retry
   */
  async getPendingEvents(tenantId = null, maxRetryCount = 3) {
    try {
      const collection = await this.getCollection();
      const query = {
        status: { $in: ['FAILED', 'PENDING'] },
        retryCount: { $lt: maxRetryCount }
      };
      if (tenantId) {
        query.tenantId = tenantId;
      }

      return await collection
        .find(query)
        .sort({ timestamp: 1, retryCount: 1 })
        .limit(100)
        .toArray();
    } catch (error) {
      logger.error('Error getting pending events:', error);
      throw error;
    }
  }

  /**
   * Get event stats
   */
  async getStats(tenantId = null) {
    try {
      const collection = await this.getCollection();
      const matchStage = tenantId ? { $match: { tenantId } } : { $match: {} };

      const stats = await collection.aggregate([
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

      const total = await collection.countDocuments(tenantId ? { tenantId } : {});

      return {
        total,
        statusCounts: stats.reduce((acc, s) => {
          acc[s._id] = s.count;
          return acc;
        }, {}),
        typeCounts: typeStats.reduce((acc, s) => {
          acc[s._id] = s.count;
          return acc;
        }, {})
      };
    } catch (error) {
      logger.error('Error getting event stats:', error);
      throw error;
    }
  }

  /**
   * Replay events for an aggregate
   */
  async replayAggregate(aggregateId, tenantId = null) {
    const events = await this.getEventsByAggregate(aggregateId, tenantId);
    let state = null;

    for (const event of events) {
      state = await this.applyEvent(state, event);
    }

    return { state, events };
  }

  /**
   * Apply event to current state (for replay)
   */
  async applyEvent(currentState, event) {
    // This would delegate to specific event handlers based on event type
    // For now, return a basic state structure
    return {
      aggregateId: event.aggregateId,
      version: event.version,
      lastEvent: event.eventType,
      updatedAt: event.timestamp
    };
  }

  /**
   * Create snapshot for aggregate
   */
  async createSnapshot(aggregateId, aggregateType, state, tenantId = 'default') {
    try {
      const snapshots = mongodb.getCollection('event_snapshots');
      const snapshot = {
        snapshotId: uuidv4(),
        aggregateId,
        aggregateType,
        tenantId,
        state,
        createdAt: new Date(),
        version: state.version || 1
      };

      await snapshots.insertOne(snapshot);
      return snapshot;
    } catch (error) {
      logger.error(`Error creating snapshot for aggregate ${aggregateId}:`, error);
      throw error;
    }
  }

  /**
   * Get latest snapshot for aggregate
   */
  async getLatestSnapshot(aggregateId, tenantId = null) {
    try {
      const snapshots = mongodb.getCollection('event_snapshots');
      const query = { aggregateId };
      if (tenantId) {
        query.tenantId = tenantId;
      }

      return await snapshots.findOne(query, { sort: { version: -1 } });
    } catch (error) {
      logger.error(`Error getting snapshot for aggregate ${aggregateId}:`, error);
      throw error;
    }
  }
}

module.exports = new EventStore();
