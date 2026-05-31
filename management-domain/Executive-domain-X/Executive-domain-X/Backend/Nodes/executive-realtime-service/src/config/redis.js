/**
 * Redis Configuration for Pub/Sub and Caching
 */

const Redis = require('ioredis');
const logger = require('./logger');

// Redis clients
let pubClient = null;
let subClient = null;

// Message handlers by channel
const channelHandlers = new Map();

/**
 * Connect to Redis
 */
async function connectRedis() {
  const redisUrl = process.env.REDIS_URL || 'redis://localhost:6379';
  const redisDb = process.env.REDIS_DB || 1;

  // Publisher client
  pubClient = new Redis(redisUrl, {
    db: redisDb,
    retryStrategy: (times) => {
      const delay = Math.min(times * 50, 2000);
      return delay;
    },
  });

  pubClient.on('connect', () => {
    logger.info('Redis publisher connected');
  });

  pubClient.on('error', (err) => {
    logger.error('Redis publisher error:', err);
  });

  // Subscriber client
  subClient = new Redis(redisUrl, {
    db: redisDb,
    retryStrategy: (times) => {
      const delay = Math.min(times * 50, 2000);
      return delay;
    },
  });

  subClient.on('connect', () => {
    logger.info('Redis subscriber connected');
  });

  subClient.on('error', (err) => {
    logger.error('Redis subscriber error:', err);
  });

  // Handle incoming messages
  subClient.on('message', (channel, message) => {
    const handler = channelHandlers.get(channel);
    if (handler) {
      handler(message, channel);
    } else {
      logger.debug(`No handler for channel: ${channel}`);
    }
  });

  // Wait for connections
  await pubClient.connect();
  await subClient.connect();

  return { pubClient, subClient };
}

/**
 * Subscribe to a Redis channel
 */
async function subscribe(channel, handler) {
  if (!subClient) {
    throw new Error('Redis subscriber not connected');
  }

  await subClient.subscribe(channel);
  channelHandlers.set(channel, handler);
  logger.debug(`Subscribed to channel: ${channel}`);
}

/**
 * Unsubscribe from a Redis channel
 */
async function unsubscribe(channel) {
  if (!subClient) {
    return;
  }

  await subClient.unsubscribe(channel);
  channelHandlers.delete(channel);
  logger.debug(`Unsubscribed from channel: ${channel}`);
}

/**
 * Publish a message to a Redis channel
 */
async function publishMessage(channel, message) {
  if (!pubClient) {
    throw new Error('Redis publisher not connected');
  }

  const payload = typeof message === 'string' ? message : JSON.stringify(message);
  await pubClient.publish(channel, payload);
  logger.debug(`Published to channel ${channel}`);
}

/**
 * Get Redis status
 */
async function getRedisStatus() {
  const status = {
    publisher: 'disconnected',
    subscriber: 'disconnected',
    status: 'disconnected',
  };

  if (pubClient && pubClient.status === 'ready') {
    status.publisher = 'connected';
  }

  if (subClient && subClient.status === 'ready') {
    status.subscriber = 'connected';
  }

  if (status.publisher === 'connected' && status.subscriber === 'connected') {
    status.status = 'connected';
  }

  return status;
}

/**
 * Get subscriber client (for external use)
 */
function getSubscriber() {
  return subClient;
}

/**
 * Disconnect from Redis
 */
async function disconnectRedis() {
  if (pubClient) {
    await pubClient.quit();
    pubClient = null;
  }

  if (subClient) {
    await subClient.quit();
    subClient = null;
  }

  channelHandlers.clear();
  logger.info('Redis disconnected');
}

module.exports = {
  connectRedis,
  subscribe,
  unsubscribe,
  publishMessage,
  getRedisStatus,
  getSubscriber,
  disconnectRedis,
};
