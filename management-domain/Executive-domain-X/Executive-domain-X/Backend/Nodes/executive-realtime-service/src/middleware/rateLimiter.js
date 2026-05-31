/**
 * Rate Limiting Middleware
 *
 * Provides rate limiting for HTTP and WebSocket connections.
 */

const { RateLimiterRedis, RateLimiterMemory } = require('rate-limiter-flexible');
const { getRedisClient } = require('../config/redis');
const logger = require('../config/logger');

/**
 * Rate limiter configuration
 */
const rateLimitConfig = {
  // General API rate limit
  api: {
    points: parseInt(process.env.RATE_LIMIT_API_POINTS) || 100,
    duration: parseInt(process.env.RATE_LIMIT_API_DURATION) || 60,
  },

  // WebSocket message rate limit
  websocket: {
    points: parseInt(process.env.RATE_LIMIT_WS_POINTS) || 100,
    duration: parseInt(process.env.RATE_LIMIT_WS_DURATION) || 60,
  },

  // Authentication rate limit
  auth: {
    points: parseInt(process.env.RATE_LIMIT_AUTH_POINTS) || 5,
    duration: parseInt(process.env.RATE_LIMIT_AUTH_DURATION) || 60,
  },

  // Broadcast rate limit
  broadcast: {
    points: parseInt(process.env.RATE_LIMIT_BROADCAST_POINTS) || 10,
    duration: parseInt(process.env.RATE_LIMIT_BROADCAST_DURATION) || 60,
  },
};

/**
 * Create rate limiter
 */
function createRateLimiter(options) {
  const redis = getRedisClient();

  if (redis) {
    // Use Redis for distributed rate limiting
    return new RateLimiterRedis({
      storeClient: redis,
      keyPrefix: 'rate_limit',
      ...options,
    });
  }

  // Fallback to in-memory rate limiting
  logger.warn('Redis not available, using in-memory rate limiting');
  return new RateLimiterMemory(options);
}

/**
 * Rate limiters
 */
const limiters = {
  api: createRateLimiter(rateLimitConfig.api),
  websocket: createRateLimiter(rateLimitConfig.websocket),
  auth: createRateLimiter(rateLimitConfig.auth),
  broadcast: createRateLimiter(rateLimitConfig.broadcast),
};

/**
 * Express middleware for API rate limiting
 */
function apiRateLimitMiddleware(req, res, next) {
  const key = req.ip || req.connection.remoteAddress;

  limiters.api.consume(key)
    .then(() => next())
    .catch((rej) => {
      const retryAfter = Math.round(rej.msBeforeNext / 1000) || 1;
      res.set('Retry-After', String(retryAfter));
      res.status(429).json({
        error: 'Too many requests',
        retryAfter,
      });
    });
}

/**
 * Express middleware for authentication rate limiting
 */
function authRateLimitMiddleware(req, res, next) {
  const key = req.ip || req.connection.remoteAddress;

  limiters.auth.consume(key)
    .then(() => next())
    .catch((rej) => {
      const retryAfter = Math.round(rej.msBeforeNext / 1000) || 1;
      res.set('Retry-After', String(retryAfter));
      res.status(429).json({
        error: 'Too many authentication attempts',
        retryAfter,
      });
    });
}

/**
 * Check WebSocket message rate limit
 */
async function checkWebSocketRateLimit(clientId) {
  try {
    await limiters.websocket.consume(clientId);
    return { allowed: true };
  } catch (rej) {
    return {
      allowed: false,
      retryAfter: Math.round(rej.msBeforeNext / 1000) || 1,
    };
  }
}

/**
 * Reset rate limit for a key
 */
async function resetRateLimit(limiterType, key) {
  const limiter = limiters[limiterType];
  if (limiter) {
    await limiter.delete(key);
  }
}

/**
 * Get rate limit status for a key
 */
async function getRateLimitStatus(limiterType, key) {
  const limiter = limiters[limiterType];
  if (!limiter) {
    return null;
  }

  try {
    const res = await limiter.get(key);
    return res;
  } catch (error) {
    return null;
  }
}

module.exports = {
  apiRateLimitMiddleware,
  authRateLimitMiddleware,
  checkWebSocketRateLimit,
  resetRateLimit,
  getRateLimitStatus,
  rateLimitConfig,
};
