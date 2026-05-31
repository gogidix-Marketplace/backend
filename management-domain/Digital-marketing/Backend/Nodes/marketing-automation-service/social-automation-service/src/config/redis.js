/**
 * Redis Configuration
 * Redis connection management using ioredis
 */

const Redis = require('ioredis');
const logger = require('../utils/logger');

let redisClient = null;

/**
 * Connect to Redis
 */
const connectRedis = async () => {
  if (redisClient && redisClient.status === 'ready') {
    logger.info('Redis already connected');
    return redisClient;
  }

  const redisHost = process.env.REDIS_HOST || 'localhost';
  const redisPort = parseInt(process.env.REDIS_PORT || '6379');
  const redisPassword = process.env.REDIS_PASSWORD;
  const redisDb = parseInt(process.env.REDIS_DB || '1');

  try {
    redisClient = new Redis({
      host: redisHost,
      port: redisPort,
      password: redisPassword || undefined,
      db: redisDb,
      retryStrategy: (times) => {
        const delay = Math.min(times * 50, 2000);
        return delay;
      },
      maxRetriesPerRequest: 3,
      enableReadyCheck: true,
      enableOfflineQueue: true
    });

    redisClient.on('connect', () => {
      logger.info(`Redis connecting to ${redisHost}:${redisPort}`);
    });

    redisClient.on('ready', () => {
      logger.info(`Redis connected successfully to ${redisHost}:${redisPort}`);
    });

    redisClient.on('error', (error) => {
      logger.error('Redis connection error:', error.message);
    });

    redisClient.on('close', () => {
      logger.warn('Redis connection closed');
    });

    redisClient.on('reconnecting', (delay) => {
      logger.info(`Redis reconnecting in ${delay}ms`);
    });

    return redisClient;

  } catch (error) {
    logger.error('Failed to connect to Redis:', error);
    throw error;
  }
};

/**
 * Disconnect from Redis
 */
const disconnectRedis = async () => {
  if (redisClient) {
    try {
      await redisClient.quit();
      redisClient = null;
      logger.info('Redis disconnected successfully');
    } catch (error) {
      logger.error('Error disconnecting from Redis:', error);
      throw error;
    }
  }
};

/**
 * Get Redis client instance
 */
const getRedisClient = () => {
  if (!redisClient) {
    throw new Error('Redis client not initialized. Call connectRedis() first.');
  }
  return redisClient;
};

/**
 * Get connection status
 */
const getRedisStatus = () => {
  if (!redisClient) {
    return { status: 'not initialized' };
  }
  return {
    status: redisClient.status,
    host: redisClient.options.host,
    port: redisClient.options.port
  };
};

module.exports = {
  connectRedis,
  disconnectRedis,
  getRedisClient,
  getRedisStatus
};
