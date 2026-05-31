/**
 * Redis Pub/Sub Subscriptions for KPI Updates
 *
 * Subscribes to Redis channels for real-time KPI updates
 * and broadcasts them to connected WebSocket clients.
 */

const { subscribe, unsubscribe, getSubscriber } = require('../config/redis');
const logger = require('../config/logger');

// Subscription channels
const CHANNELS = {
  KPI_UPDATE: 'kpi:update',
  KPI_ALERT: 'kpi:alert',
  EXECUTIVE_DASHBOARD: 'executive:dashboard',
  TENANT_UPDATE: 'tenant:update:*',
};

// Active subscriptions
const activeSubscriptions = new Map();

/**
 * Setup Redis pub/sub subscriptions
 */
async function setupSubscriptions(wsServer) {
  try {
    // Subscribe to KPI updates
    await subscribeToChannel(CHANNELS.KPI_UPDATE, handleKPIUpdate.bind(null, wsServer));

    // Subscribe to KPI alerts
    await subscribeToChannel(CHANNELS.KPI_ALERT, handleKPIAlert.bind(null, wsServer));

    // Subscribe to executive dashboard updates
    await subscribeToChannel(CHANNELS.EXECUTIVE_DASHBOARD, handleDashboardUpdate.bind(null, wsServer));

    logger.info('Redis pub/sub subscriptions established');

  } catch (error) {
    logger.error('Failed to setup subscriptions:', error);
    throw error;
  }
}

/**
 * Subscribe to a Redis channel
 */
async function subscribeToChannel(channel, handler) {
  try {
    await subscribe(channel, (message, channel) => {
      try {
        const data = JSON.parse(message);
        handler(data, channel);
      } catch (error) {
        logger.error(`Error parsing message from channel ${channel}:`, error);
      }
    });

    activeSubscriptions.set(channel, handler);
    logger.debug(`Subscribed to channel: ${channel}`);

  } catch (error) {
    logger.error(`Failed to subscribe to channel ${channel}:`, error);
    throw error;
  }
}

/**
 * Handle KPI update messages
 */
function handleKPIUpdate(wsServer, data, channel) {
  const { tenantId, kpiId, executiveLevel, category } = data;

  logger.debug(`KPI update received: tenant=${tenantId}, kpi=${kpiId}`);

  // Broadcast to tenant-specific room
  wsServer.broadcastToRoom(`tenant:${tenantId}`, {
    type: 'kpi_update',
    data: data,
  });

  // Broadcast to category-specific room
  if (category) {
    wsServer.broadcastToRoom(`tenant:${tenantId}:${category}`, {
      type: 'kpi_update',
      data: data,
    });
  }

  // Broadcast to executive-level specific room
  if (executiveLevel) {
    wsServer.broadcastToRoom(`tenant:${tenantId}:${executiveLevel}`, {
      type: 'kpi_update',
      data: data,
    });
  }
}

/**
 * Handle KPI alert messages
 */
function handleKPIAlert(wsServer, data, channel) {
  const { tenantId, level, executiveLevel } = data;

  logger.debug(`KPI alert received: tenant=${tenantId}, level=${level}`);

  // Broadcast alert to all tenant clients
  wsServer.broadcastToRoom(`tenant:${tenantId}`, {
    type: 'kpi_alert',
    data: data,
    timestamp: Date.now(),
  });

  // If high priority, broadcast to executive level
  if (level === 'CRITICAL' && executiveLevel) {
    wsServer.broadcastToRoom(`tenant:${tenantId}:${executiveLevel}`, {
      type: 'kpi_alert',
      data: data,
      priority: 'high',
      timestamp: Date.now(),
    });
  }
}

/**
 * Handle executive dashboard update messages
 */
function handleDashboardUpdate(wsServer, data, channel) {
  const { tenantId, dashboardType } = data;

  logger.debug(`Dashboard update received: tenant=${tenantId}, type=${dashboardType}`);

  // Broadcast to dashboard-specific room
  wsServer.broadcastToRoom(`tenant:${tenantId}:dashboard:${dashboardType}`, {
    type: 'dashboard_update',
    data: data,
    timestamp: Date.now(),
  });
}

/**
 * Cleanup subscriptions on shutdown
 */
async function cleanupSubscriptions() {
  try {
    for (const channel of activeSubscriptions.keys()) {
      await unsubscribe(channel);
    }
    activeSubscriptions.clear();
    logger.info('Redis pub/sub subscriptions cleaned up');
  } catch (error) {
    logger.error('Error cleaning up subscriptions:', error);
  }
}

module.exports = {
  setupSubscriptions,
  cleanupSubscriptions,
  CHANNELS,
};
