const config = require('../config');
const logger = require('../config/logger');
const kafka = require('../config/kafka');
const broadcastService = require('./BroadcastService');
const presenceService = require('./PresenceService');

class KafkaService {
  constructor() {
    this.isConsuming = false;
    this.messageHandlers = new Map();
    this.consumerGroup = 'websocket-service-group';
  }

  async initialize(io) {
    this.io = io;

    await this.connect();

    await this.setupDefaultHandlers();

    await this.startConsuming();

    logger.info('Kafka service initialized');
  }

  async connect() {
    try {
      await kafka.connect();
      logger.info('Connected to Kafka');
    } catch (error) {
      logger.error('Failed to connect to Kafka:', error);
      throw error;
    }
  }

  async disconnect() {
    try {
      await this.stopConsuming();
      await kafka.disconnect();
      logger.info('Disconnected from Kafka');
    } catch (error) {
      logger.error('Error disconnecting from Kafka:', error);
    }
  }

  setupDefaultHandlers() {
    this.on(config.kafka.topics.executiveUpdates, this.handleExecutiveUpdate.bind(this));
    this.on(config.kafka.topics.kpiUpdates, this.handleKpiUpdate.bind(this));
    this.on(config.kafka.topics.dashboardUpdates, this.handleDashboardUpdate.bind(this));
    this.on(config.kafka.topics.notifications, this.handleNotification.bind(this));
    this.on(config.kafka.topics.presence, this.handlePresenceEvent.bind(this));
  }

  async handleExecutiveUpdate(message) {
    try {
      const { tenantId, type, data } = message.value;

      logger.debug(`Received executive update: ${type} for tenant: ${tenantId}`);

      switch (type) {
        case 'executive_created':
        case 'executive_updated':
          await broadcastService.broadcastToTenant(
            tenantId,
            'executive:update',
            data
          );
          break;

        case 'executive_deleted':
          await broadcastService.broadcastToTenant(
            tenantId,
            'executive:delete',
            { executiveId: data.executiveId }
          );
          break;

        default:
          await broadcastService.broadcastToTenant(
            tenantId,
            'executive:update',
            data
          );
      }
    } catch (error) {
      logger.error('Error handling executive update:', error);
    }
  }

  async handleKpiUpdate(message) {
    try {
      const { tenantId, kpiId, type, data } = message.value;

      logger.debug(`Received KPI update: ${type} for KPI: ${kpiId}`);

      switch (type) {
        case 'kpi_value_updated':
          await broadcastService.broadcastKpiUpdate(
            tenantId,
            kpiId,
            {
              value: data.value,
              change: data.change,
              timestamp: data.timestamp
            }
          );
          break;

        case 'kpi_threshold_breached':
          await broadcastService.broadcastToTenant(
            tenantId,
            'kpi:alert',
            {
              kpiId,
              threshold: data.threshold,
              actualValue: data.value,
              severity: data.severity
            }
          );
          break;

        case 'kpi_created':
        case 'kpi_updated':
        case 'kpi_deleted':
          await broadcastService.broadcastToTenant(
            tenantId,
            'kpi:change',
            { kpiId, type, data }
          );
          break;

        default:
          await broadcastService.broadcastKpiUpdate(
            tenantId,
            kpiId,
            data
          );
      }
    } catch (error) {
      logger.error('Error handling KPI update:', error);
    }
  }

  async handleDashboardUpdate(message) {
    try {
      const { tenantId, dashboardId, type, data } = message.value;

      logger.debug(`Received dashboard update: ${type} for dashboard: ${dashboardId}`);

      switch (type) {
        case 'widget_updated':
          await broadcastService.broadcastDashboardUpdate(
            tenantId,
            dashboardId,
            {
              widgetId: data.widgetId,
              widgetType: data.widgetType,
              data: data.widgetData
            }
          );
          break;

        case 'dashboard_layout_changed':
          await broadcastService.broadcastDashboardUpdate(
            tenantId,
            dashboardId,
            {
              type: 'layout_change',
              layout: data.layout
            }
          );
          break;

        case 'dashboard_shared':
          await broadcastService.broadcastToUser(
            tenantId,
            data.sharedWith,
            'dashboard:shared',
            {
              dashboardId,
              sharedBy: data.sharedBy,
              permissions: data.permissions
            }
          );
          break;

        case 'dashboard_created':
        case 'dashboard_deleted':
          await broadcastService.broadcastToTenant(
            tenantId,
            'dashboard:change',
            { dashboardId, type, data }
          );
          break;

        default:
          await broadcastService.broadcastDashboardUpdate(
            tenantId,
            dashboardId,
            data
          );
      }
    } catch (error) {
      logger.error('Error handling dashboard update:', error);
    }
  }

  async handleNotification(message) {
    try {
      const { tenantId, userId, notification } = message.value;

      logger.debug(`Received notification for user: ${userId}`);

      await broadcastService.broadcastNotification(
        tenantId,
        userId,
        notification
      );
    } catch (error) {
      logger.error('Error handling notification:', error);
    }
  }

  async handlePresenceEvent(message) {
    try {
      const { tenantId, userId, type, data } = message.value;

      logger.debug(`Received presence event: ${type} for user: ${userId}`);

      await broadcastService.broadcastPresenceChange(
        tenantId,
        userId,
        data.status || type,
        data.metadata || {}
      );
    } catch (error) {
      logger.error('Error handling presence event:', error);
    }
  }

  async publishExecutiveUpdate(tenantId, type, data) {
    try {
      await kafka.publish(
        config.kafka.topics.executiveUpdates,
        {
          tenantId,
          type,
          data,
          timestamp: new Date().toISOString(),
          source: 'websocket-service'
        },
        tenantId
      );

      logger.debug(`Published executive update: ${type}`);
    } catch (error) {
      logger.error('Error publishing executive update:', error);
    }
  }

  async publishKpiUpdate(tenantId, kpiId, type, data) {
    try {
      await kafka.publish(
        config.kafka.topics.kpiUpdates,
        {
          tenantId,
          kpiId,
          type,
          data,
          timestamp: new Date().toISOString(),
          source: 'websocket-service'
        },
        kpiId
      );

      logger.debug(`Published KPI update: ${type} for KPI: ${kpiId}`);
    } catch (error) {
      logger.error('Error publishing KPI update:', error);
    }
  }

  async publishDashboardUpdate(tenantId, dashboardId, type, data) {
    try {
      await kafka.publish(
        config.kafka.topics.dashboardUpdates,
        {
          tenantId,
          dashboardId,
          type,
          data,
          timestamp: new Date().toISOString(),
          source: 'websocket-service'
        },
        dashboardId
      );

      logger.debug(`Published dashboard update: ${type} for dashboard: ${dashboardId}`);
    } catch (error) {
      logger.error('Error publishing dashboard update:', error);
    }
  }

  async publishNotification(tenantId, userId, notification) {
    try {
      await kafka.publish(
        config.kafka.topics.notifications,
        {
          tenantId,
          userId,
          notification,
          timestamp: new Date().toISOString(),
          source: 'websocket-service'
        },
        userId
      );

      logger.debug(`Published notification for user: ${userId}`);
    } catch (error) {
      logger.error('Error publishing notification:', error);
    }
  }

  async publishPresenceEvent(tenantId, userId, type, data) {
    try {
      await kafka.publish(
        config.kafka.topics.presence,
        {
          tenantId,
          userId,
          type,
          data,
          timestamp: new Date().toISOString(),
          source: 'websocket-service'
        },
        userId
      );

      logger.debug(`Published presence event: ${type} for user: ${userId}`);
    } catch (error) {
      logger.error('Error publishing presence event:', error);
    }
  }

  on(topic, handler) {
    if (!this.messageHandlers.has(topic)) {
      this.messageHandlers.set(topic, []);
    }
    this.messageHandlers.get(topic).push(handler);
  }

  off(topic, handler) {
    if (!this.messageHandlers.has(topic)) return;

    const handlers = this.messageHandlers.get(topic);
    const index = handlers.indexOf(handler);
    if (index !== -1) {
      handlers.splice(index, 1);
    }

    if (handlers.length === 0) {
      this.messageHandlers.delete(topic);
    }
  }

  async startConsuming() {
    if (this.isConsuming) {
      logger.warn('Kafka consumer already running');
      return;
    }

    try {
      const topics = Array.from(this.messageHandlers.keys());

      if (topics.length === 0) {
        logger.warn('No Kafka topics to subscribe to');
        return;
      }

      await kafka.subscribe(topics, async (message) => {
        await this.handleMessage(message);
      }, {
        fromBeginning: false
      });

      this.isConsuming = true;
      logger.info(`Started consuming Kafka topics: ${topics.join(', ')}`);
    } catch (error) {
      logger.error('Error starting Kafka consumer:', error);
      throw error;
    }
  }

  async stopConsuming() {
    if (!this.isConsuming) return;

    try {
      const topics = Array.from(this.messageHandlers.keys());
      await kafka.unsubscribe(topics);

      this.isConsuming = false;
      logger.info('Stopped consuming Kafka topics');
    } catch (error) {
      logger.error('Error stopping Kafka consumer:', error);
    }
  }

  async handleMessage(message) {
    const handlers = this.messageHandlers.get(message.topic);

    if (!handlers || handlers.length === 0) {
      logger.debug(`No handler for Kafka topic: ${message.topic}`);
      return;
    }

    for (const handler of handlers) {
      try {
        await handler(message);
      } catch (error) {
        logger.error(`Error in Kafka handler for topic ${message.topic}:`, error);
      }
    }
  }

  async publishBatch(events) {
    const messages = events.map(event => ({
      key: event.key || event.tenantId,
      value: {
        tenantId: event.tenantId,
        type: event.type,
        data: event.data,
        timestamp: new Date().toISOString(),
        source: 'websocket-service'
      }
    }));

    try {
      const topic = config.kafka.topics.executiveUpdates;
      await kafka.publishBatch(topic, messages);
      logger.info(`Published batch of ${events.length} events to Kafka`);
    } catch (error) {
      logger.error('Error publishing batch to Kafka:', error);
    }
  }

  async getConsumerLag() {
    try {
      const topics = Array.from(this.messageHandlers.keys());
      const lagInfo = {};

      for (const topic of topics) {
        const offsets = await kafka.getTopicOffsets(topic);
        lagInfo[topic] = offsets;
      }

      return lagInfo;
    } catch (error) {
      logger.error('Error getting consumer lag:', error);
      return {};
    }
  }

  getTopics() {
    return {
      executiveUpdates: config.kafka.topics.executiveUpdates,
      kpiUpdates: config.kafka.topics.kpiUpdates,
      dashboardUpdates: config.kafka.topics.dashboardUpdates,
      notifications: config.kafka.topics.notifications,
      presence: config.kafka.topics.presence
    };
  }

  getStats() {
    return {
      isConsuming: this.isConsuming,
      subscribedTopics: Array.from(this.messageHandlers.keys()),
      handlersCount: this.messageHandlers.size
    };
  }
}

module.exports = new KafkaService();
