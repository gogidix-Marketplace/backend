/**
 * Query Handlers for CQRS Pattern
 *
 * Queries represent requests for data without state changes.
 * All queries are optimized for read performance.
 */

const { getRedisClient } = require('../config/redis');
const logger = require('../config/logger');

/**
 * Query types
 */
const QueryTypes = {
  // KPI Queries
  GET_CURRENT_KPI: 'get_current_kpi',
  GET_KPI_HISTORY: 'get_kpi_history',
  GET_KPI_TRENDS: 'get_kpi_trends',
  LIST_KPIS: 'list_kpis',

  // Dashboard Queries
  GET_DASHBOARD_CONFIG: 'get_dashboard_config',
  GET_DASHBOARD_DATA: 'get_dashboard_data',
  LIST_DASHBOARDS: 'list_dashboards',

  // Connection Queries
  GET_ACTIVE_CONNECTIONS: 'get_active_connections',
  GET_TENANT_CONNECTIONS: 'get_tenant_connections',
  GET_ROOM_SUBSCRIBERS: 'get_room_subscribers',

  // Alert Queries
  GET_ACTIVE_ALERTS: 'get_active_alerts',
  GET_ALERT_HISTORY: 'get_alert_history',
};

/**
 * Cache TTL in seconds
 */
const CacheTTL = {
  SHORT: 30,      // 30 seconds
  MEDIUM: 300,    // 5 minutes
  LONG: 3600,     // 1 hour
};

/**
 * Query handler registry
 */
const queryHandlers = new Map();

/**
 * Register a query handler
 */
function registerQuery(queryType, handler) {
  queryHandlers.set(queryType, handler);
  logger.debug(`Registered query handler: ${queryType}`);
}

/**
 * Execute a query
 */
async function executeQuery(queryType, params, options = {}) {
  const handler = queryHandlers.get(queryType);

  if (!handler) {
    throw new Error(`No handler registered for query type: ${queryType}`);
  }

  try {
    // Check cache if enabled
    if (options.useCache !== false) {
      const cacheKey = buildCacheKey(queryType, params);
      const cached = await getFromCache(cacheKey);
      if (cached) {
        logger.debug(`Cache hit for query: ${queryType}`);
        return cached;
      }
    }

    // Execute query handler
    const result = await handler(params);

    // Cache result if enabled
    if (options.useCache !== false && result) {
      const cacheKey = buildCacheKey(queryType, params);
      const ttl = options.ttl || CacheTTL.MEDIUM;
      await setCache(cacheKey, result, ttl);
    }

    return result;

  } catch (error) {
    logger.error(`Query execution failed: ${queryType}`, error);
    throw error;
  }
}

/**
 * Build cache key from query type and params
 */
function buildCacheKey(queryType, params) {
  const paramStr = Object.keys(params)
    .sort()
    .map(k => `${k}:${params[k]}`)
    .join('|');
  return `query:${queryType}:${paramStr}`;
}

/**
 * Get value from Redis cache
 */
async function getFromCache(key) {
  try {
    const redis = getRedisClient();
    if (!redis) return null;

    const value = await redis.get(key);
    return value ? JSON.parse(value) : null;
  } catch (error) {
    logger.warn('Cache get failed:', error.message);
    return null;
  }
}

/**
 * Set value in Redis cache
 */
async function setCache(key, value, ttl) {
  try {
    const redis = getRedisClient();
    if (!redis) return;

    await redis.setex(key, ttl, JSON.stringify(value));
  } catch (error) {
    logger.warn('Cache set failed:', error.message);
  }
}

/**
 * Invalidate cache for a query type
 */
async function invalidateCache(queryType, params = {}) {
  try {
    const redis = getRedisClient();
    if (!redis) return;

    const pattern = buildCacheKey(queryType, params).replace(/[^:]+:[^:]+:(.+)/, '$1');
    const keys = await redis.keys(`query:${queryType}:${pattern || '*'}`);

    if (keys.length > 0) {
      await redis.del(...keys);
      logger.debug(`Invalidated ${keys.length} cache entries for: ${queryType}`);
    }
  } catch (error) {
    logger.warn('Cache invalidation failed:', error.message);
  }
}

// Register built-in query handlers
registerQuery(QueryTypes.GET_CURRENT_KPI, async (params) => {
  // Placeholder - would fetch from KPI service
  return { kpiId: params.kpiId, value: 0, timestamp: Date.now() };
});

registerQuery(QueryTypes.GET_KPI_HISTORY, async (params) => {
  // Placeholder - would fetch from KPI service
  return { kpiId: params.kpiId, history: [], period: params.period || '24h' };
});

registerQuery(QueryTypes.LIST_KPIS, async (params) => {
  // Placeholder - would fetch from KPI service
  return { tenantId: params.tenantId, kpis: [] };
});

module.exports = {
  executeQuery,
  registerQuery,
  invalidateCache,
  QueryTypes,
  CacheTTL,
};
