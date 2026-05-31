/**
 * Redis Configuration for Caching and Pub/Sub
 */

const Redis = require('ioredis');
const logger = require('./logger');

let redisClient = null;

/**
 * Connect to Redis
 */
async function connectRedis() {
  const redisUrl = process.env.REDIS_URL || 'redis://localhost:6379';
  const redisDb = process.env.REDIS_DB || 2;

  redisClient = new Redis(redisUrl, {
    db: redisDb,
    retryStrategy: (times) => {
      const delay = Math.min(times * 50, 2000);
      return delay;
    },
  });

  redisClient.on('connect', () => {
    logger.info('Redis connected');
  });

  redisClient.on('error', (err) => {
    logger.error('Redis error:', err);
  });

  // Wait for connection
  await redisClient.connect();

  return redisClient;
}

/**
 * Get Redis client
 */
function getClient() {
  if (!redisClient) {
    throw new Error('Redis not connected. Call connectRedis() first.');
  }
  return redisClient;
}

/**
 * Publish event to Redis channel
 */
async function publishEvent(channel, data) {
  const client = getClient();
  const payload = JSON.stringify(data);
  await client.publish(channel, payload);
  logger.debug(`Published event to ${channel}`);
}

/**
 * Invalidate cache pattern
 */
async function invalidatePattern(pattern) {
  const client = getClient();
  const keys = await client.keys(pattern);

  if (keys.length > 0) {
    await client.del(...keys);
    logger.debug(`Invalidated ${keys.length} cache entries matching ${pattern}`);
  }

  return keys.length;
}

/**
 * Get Redis status
 */
async function getRedisStatus() {
  if (!redisClient) {
    return { status: 'disconnected' };
  }

  try {
    const info = await redisClient.info('server');
    return {
      status: 'connected',
      info: info,
    };
  } catch (error) {
    return { status: 'error', error: error.message };
  }
}

/**
 * Disconnect from Redis
 */
async function disconnectRedis() {
  if (redisClient) {
    await redisClient.quit();
    redisClient = null;
    logger.info('Redis disconnected');
  }
}

module.exports = {
  connectRedis,
  getClient,
  publishEvent,
  invalidatePattern,
  getRedisStatus,
  disconnectRedis,
};
