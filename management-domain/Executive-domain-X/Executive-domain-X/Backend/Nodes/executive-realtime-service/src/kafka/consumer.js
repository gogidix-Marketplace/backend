/**
 * Kafka Consumer Configuration
 *
 * Handles consuming events from Kafka topics and
 * broadcasting to WebSocket clients.
 */

const { Kafka } = require('kafkajs');
const logger = require('../config/logger');
const { getWebSocketServer } = require('../websocket/registry');

let kafkaConsumer = null;
let isRunning = false;

/**
 * Kafka configuration
 */
function getKafkaConfig() {
  const brokers = process.env.KAFKA_BROKERS
    ? process.env.KAFKA_BROKERS.split(',')
    : ['localhost:9092'];

  const clientId = process.env.KAFKA_CLIENT_ID || 'executive-realtime-consumer';
  const groupId = process.env.KAFKA_GROUP_ID || 'executive-realtime-group';

  return {
    clientId,
    brokers,
    groupId,
    sessionTimeout: 30000,
    heartbeatInterval: 3000,
    maxBytesPerPartition: 1048576, // 1MB
  };
}

/**
 * Topics to subscribe to
 */
const consumerTopics = [
  'executive.kpi.events',      // KPI value changes
  'executive.alert.events',     // Alert notifications
  'executive.dashboard.events', // Dashboard updates
  'executive.tenant.events',    // Tenant-wide notifications
];

/**
 * Create and connect Kafka consumer
 */
async function connectKafkaConsumer() {
  if (kafkaConsumer && isRunning) {
    return kafkaConsumer;
  }

  try {
    const kafka = new Kafka(getKafkaConfig());
    kafkaConsumer = kafka.consumer();

    await kafkaConsumer.connect();

    kafkaConsumer.on('consumer.disconnect', () => {
      logger.warn('Kafka consumer disconnected');
    });

    logger.info('Kafka consumer connected successfully');

    return kafkaConsumer;

  } catch (error) {
    logger.error('Failed to connect Kafka consumer:', error);
    throw error;
  }
}

/**
 * Start consuming messages
 */
async function startConsumer() {
  if (isRunning) {
    logger.warn('Kafka consumer is already running');
    return;
  }

  try {
    await connectKafkaConsumer();

    await kafkaConsumer.subscribe({
      topics: consumerTopics,
      fromBeginning: false,
    });

    await kafkaConsumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        await processMessage(topic, partition, message);
      },
    });

    isRunning = true;
    logger.info('Kafka consumer started', { topics: consumerTopics });

  } catch (error) {
    logger.error('Failed to start Kafka consumer:', error);
    throw error;
  }
}

/**
 * Process individual message from Kafka
 */
async function processMessage(topic, partition, message) {
  try {
    const value = message.value?.toString();
    if (!value) {
      logger.warn('Received empty message', { topic, partition });
      return;
    }

    const event = JSON.parse(value);
    const headers = message.headers;

    logger.debug('Processing Kafka message', {
      topic,
      partition,
      offset: message.offset,
      eventType: event.type,
    });

    // Route event based on topic
    await routeEvent(topic, event, headers);

  } catch (error) {
    logger.error('Error processing Kafka message:', {
      topic,
      partition,
      error: error.message,
    });
  }
}

/**
 * Route event to appropriate handler
 */
async function routeEvent(topic, event, headers) {
  const wsServer = getWebSocketServer();
  if (!wsServer) {
    logger.warn('WebSocket server not available for event routing');
    return;
  }

  const tenantId = headers?.tenantId || event.tenantId;

  switch (topic) {
    case 'executive.kpi.events':
      await handleKPIEvent(wsServer, event, tenantId);
      break;

    case 'executive.alert.events':
      await handleAlertEvent(wsServer, event, tenantId);
      break;

    case 'executive.dashboard.events':
      await handleDashboardEvent(wsServer, event, tenantId);
      break;

    case 'executive.tenant.events':
      await handleTenantEvent(wsServer, event, tenantId);
      break;

    default:
      logger.debug('Unknown topic:', topic);
  }
}

/**
 * Handle KPI event
 */
async function handleKPIEvent(wsServer, event, tenantId) {
  const { type, kpiId, executiveLevel, category, data } = event;

  // Broadcast to tenant-wide room
  wsServer.broadcastToRoom(`tenant:${tenantId}`, {
    type: 'kpi_event',
    eventType: type,
    kpiId,
    data,
    timestamp: Date.now(),
  });

  // Broadcast to category room
  if (category) {
    wsServer.broadcastToRoom(`tenant:${tenantId}:${category}`, {
      type: 'kpi_event',
      eventType: type,
      kpiId,
      data,
      timestamp: Date.now(),
    });
  }

  // Broadcast to executive level room
  if (executiveLevel) {
    wsServer.broadcastToRoom(`tenant:${tenantId}:${executiveLevel}`, {
      type: 'kpi_event',
      eventType: type,
      kpiId,
      data,
      timestamp: Date.now(),
    });
  }

  logger.debug(`KPI event broadcasted for tenant ${tenantId}`, { kpiId, type });
}

/**
 * Handle alert event
 */
async function handleAlertEvent(wsServer, event, tenantId) {
  const { type, alertId, level, executiveLevel, message, data } = event;

  const alertMessage = {
    type: 'alert_event',
    eventType: type,
    alertId,
    level,
    message,
    data,
    timestamp: Date.now(),
  };

  // Broadcast to tenant-wide room
  wsServer.broadcastToRoom(`tenant:${tenantId}`, alertMessage);

  // For critical alerts, also broadcast to executive level room
  if (level === 'CRITICAL' && executiveLevel) {
    wsServer.broadcastToRoom(`tenant:${tenantId}:${executiveLevel}`, {
      ...alertMessage,
      priority: 'high',
    });
  }

  logger.debug(`Alert event broadcasted for tenant ${tenantId}`, { alertId, level });
}

/**
 * Handle dashboard event
 */
async function handleDashboardEvent(wsServer, event, tenantId) {
  const { type, dashboardId, dashboardType, data } = event;

  wsServer.broadcastToRoom(`tenant:${tenantId}:dashboard:${dashboardType}`, {
    type: 'dashboard_event',
    eventType: type,
    dashboardId,
    data,
    timestamp: Date.now(),
  });

  logger.debug(`Dashboard event broadcasted for tenant ${tenantId}`, { dashboardId, type });
}

/**
 * Handle tenant-wide event
 */
async function handleTenantEvent(wsServer, event, tenantId) {
  const { type, data } = event;

  wsServer.broadcastToRoom(`tenant:${tenantId}`, {
    type: 'tenant_event',
    eventType: type,
    data,
    timestamp: Date.now(),
  });

  logger.debug(`Tenant event broadcasted for ${tenantId}`, { type });
}

/**
 * Stop consumer
 */
async function stopConsumer() {
  if (!isRunning) {
    return;
  }

  try {
    if (kafkaConsumer) {
      await kafkaConsumer.stop();
      await kafkaConsumer.disconnect();
    }
    isRunning = false;
    logger.info('Kafka consumer stopped');
  } catch (error) {
    logger.error('Error stopping Kafka consumer:', error);
  }
}

/**
 * Get consumer instance
 */
function getConsumer() {
  return kafkaConsumer;
}

/**
 * Check if consumer is running
 */
function isConsumerRunning() {
  return isRunning;
}

module.exports = {
  connectKafkaConsumer,
  startConsumer,
  stopConsumer,
  getConsumer,
  isConsumerRunning,
};
