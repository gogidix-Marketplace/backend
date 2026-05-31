const EventLog = require('../models/EventLog');
const logger = require('../config/logger');

class EventController {
  constructor() {
    this.eventHandlers = new Map();
  }

  registerEventHandler(eventType, handler) {
    if (typeof handler !== 'function') {
      throw new Error('Handler must be a function');
    }
    this.eventHandlers.set(eventType, handler);
    logger.info(`Registered event handler for: ${eventType}`);
  }

  async handleEvent(event) {
    const eventType = event.key;
    const eventData = event.value;

    logger.info(`Processing event: ${eventType}`);

    try {
      const handler = this.eventHandlers.get(eventType);

      if (!handler) {
        logger.warn(`No handler registered for event type: ${eventType}`);
        return { success: false, message: 'No handler registered' };
      }

      const result = await handler(eventData);

      if (result.success) {
        await EventLog.markAsProcessed(eventData.eventId || uuidv4());
        logger.info(`Successfully processed event: ${eventType}`);
        return result;
      } else {
        throw new Error(result.message || 'Event processing failed');
      }
    } catch (error) {
      logger.error(`Error processing event ${eventType}:`, error);

      const eventId = eventData.eventId || uuidv4();
      await EventLog.markAsFailed(eventId, error.message);

      return {
        success: false,
        message: error.message,
        error: error
      };
    }
  }

  async getEventStats() {
    try {
      const stats = await EventLog.aggregate([
        {
          $group: {
            _id: '$status',
            count: { $sum: 1 }
          }
        }
      ]);

      const totalEvents = await EventLog.countDocuments();
      const recentEvents = await EventLog.find()
        .sort({ timestamp: -1 })
        .limit(10)
        .exec();

      return {
        success: true,
        totalEvents,
        statusCounts: stats.reduce((acc, stat) => {
          acc[stat._id] = stat.count;
          return acc;
        }, {}),
        recentEvents
      };
    } catch (error) {
      logger.error('Error fetching event stats:', error);
      return { success: false, message: error.message };
    }
  }
}

module.exports = EventController;