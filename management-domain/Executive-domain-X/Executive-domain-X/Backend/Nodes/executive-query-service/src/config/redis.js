/**
 * Redis Configuration
 * Caching layer for query optimization
 */

const Redis = require('ioredis');
const logger = require('./logger');

let redis = null;
const redisUrl = process.env.REDIS_URL || 'redis://localhost:6379';
const redisDb = parseInt(process.env.REDIS_DB || '3', 10);
const cacheTTL = parseInt(process.env.CACHE_TTL || '300', 10);

/**
 * Initialize Redis connection
 */
async function connect() {
  if (redis) {
    return redis;
  }

  try {
    redis = new Redis(redisUrl, {
      db: redisDb,
      maxRetriesPerRequest: 3,
      retryStrategy: (times) => {
        const delay = Math.min(times * 50, 2000);
        return delay;
      },
    });

    redis.on('connect', () => {
      logger.info('Redis connected successfully', { db: redisDb });
    });

    redis.on('error', (error) => {
      logger.error('Redis connection error', { error: error.message });
    });

    // Test connection
    await redis.ping();

    return redis;
  } catch (error) {
    logger.error('Redis connection failed', { error: error.message });
    throw error;
  }
}

/**
 * Get Redis client
 */
function getClient() {
  if (!redis) {
    throw new Error('Redis not connected. Call connect() first.');
  }
  return redis;
}

/**
 * Get cached value
 */
async function get(key) {
  try {
    const client = getClient();
    const value = await client.get(key);
    if (value) {
      return JSON.parse(value);
    }
    return null;
  } catch (error) {
    logger.warn('Cache get failed', { key, error: error.message });
    return null;
  }
}

/**
 * Set cached value
 */
async function set(key, value, ttl = cacheTTL) {
  try {
    const client = getClient();
    const serialized = JSON.stringify(value);
    await client.setex(key, ttl, serialized);
    return true;
  } catch (error) {
    logger.warn('Cache set failed', { key, error: error.message });
    return false;
  }
}

/**
 * Delete cached value
 */
async function del(key) {
  try {
    const client = getClient();
    await client.del(key);
    return true;
  } catch (error) {
    logger.warn('Cache delete failed', { key, error: error.message });
    return false;
  }
}

/**
 * Delete cache by pattern
 */
async function delPattern(pattern) {
  try {
    const client = getClient();
    const keys = await client.keys(pattern);
    if (keys.length > 0) {
      await client.del(...keys);
    }
    return keys.length;
  } catch (error) {
    logger.warn('Cache pattern delete failed', { pattern, error: error.message });
    return 0;
  }
}

/**
 * Invalidate cache for tenant
 */
async function invalidateTenant(tenantId) {
  const pattern = `kpi:${tenantId}:*`;
  return delPattern(pattern);
}

/**
 * Generate cache key for KPI
 */
function kpiKey(tenantId, kpiId) {
  return `kpi:${tenantId}:${kpiId}`;
}

/**
 * Generate cache key for KPI list
 */
function kpiListKey(tenantId, params) {
  const query = new URLSearchParams(params).toString();
  return `kpi:${tenantId}:list:${query}`;
}

/**
 * Close Redis connection
 */
async function close() {
  if (redis) {
    await redis.quit();
    redis = null;
    logger.info('Redis connection closed');
  }
}

module.exports = {
  connect,
  getClient,
  get,
  set,
  del,
  delPattern,
  invalidateTenant,
  kpiKey,
  kpiListKey,
  close,
  cacheTTL,
};
