/**
 * Rate Limiter Middleware
 * Rate limiting using Redis for distributed systems
 */

const rateLimit = require('express-rate-limit');
const { getRedisClient } = require('../config/redis');
const logger = require('../utils/logger');

/**
 * Memory-based rate limiter (for single instance)
 */
const memoryRateLimiter = rateLimit({
  windowMs: parseInt(process.env.RATE_LIMIT_WINDOW || '60000'), // 1 minute
  max: parseInt(process.env.RATE_LIMIT_MAX || '100'), // 100 requests per window
  standardHeaders: true,
  legacyHeaders: false,
  handler: (req, res) => {
    logger.warn('Rate limit exceeded', {
      ip: req.ip,
      path: req.path
    });
    res.status(429).json({
      error: 'TooManyRequests',
      message: 'Too many requests from this IP, please try again later'
    });
  },
  skip: (req) => {
    // Skip rate limiting for health checks
    return req.path === '/health';
  }
});

/**
 * Redis-based rate limiter (for distributed systems)
 */
class RedisRateLimiter {
  constructor(options = {}) {
    this.windowMs = options.windowMs || 60000;
    this.maxRequests = options.maxRequests || 100;
    this.prefix = options.prefix || 'ratelimit';
  }

  /**
   * Check if request is allowed
   */
  async isAllowed(key) {
    const redis = getRedisClient();
    const now = Date.now();
    const windowStart = now - this.windowMs;

    const pipeline = redis.pipeline();
    const redisKey = `${this.prefix}:${key}`;

    // Remove old entries
    pipeline.zremrangebyscore(redisKey, 0, windowStart);
    // Count current entries
    pipeline.zcard(redisKey);
    // Add current request
    pipeline.zadd(redisKey, now, `${now}:${Math.random()}`);
    // Set expiration
    pipeline.expire(redisKey, Math.ceil(this.windowMs / 1000) + 1);

    const results = await pipeline.exec();

    const count = results[1][1];
    const allowed = count < this.maxRequests;

    return {
      allowed,
      count,
      remaining: Math.max(0, this.maxRequests - count),
      reset: now + this.windowMs
    };
  }

  /**
   * Middleware function
   */
  middleware() {
    return async (req, res, next) => {
      try {
        const key = req.ip || req.connection.remoteAddress;
        const result = await this.isAllowed(key);

        // Set rate limit headers
        res.set({
          'X-RateLimit-Limit': this.maxRequests,
          'X-RateLimit-Remaining': result.remaining,
          'X-RateLimit-Reset': new Date(result.reset).toISOString()
        });

        if (!result.allowed) {
          logger.warn('Rate limit exceeded', {
            ip: req.ip,
            path: req.path,
            count: result.count
          });
          return res.status(429).json({
            error: 'TooManyRequests',
            message: 'Rate limit exceeded',
            retryAfter: Math.ceil(this.windowMs / 1000)
          });
        }

        next();
      } catch (error) {
        logger.error('Rate limiter error:', error);
        // Allow request if rate limiter fails
        next();
      }
    };
  }
}

/**
 * Create rate limiter middleware
 */
const createRateLimiter = (options = {}) => {
  const useRedis = process.env.REDIS_ENABLED === 'true';

  if (useRedis) {
    const limiter = new RedisRateLimiter(options);
    return limiter.middleware();
  }

  return memoryRateLimiter;
};

/**
 * API-specific rate limiters
 */
const emailSendLimiter = createRateLimiter({
  windowMs: 60000, // 1 minute
  maxRequests: 10,
  prefix: 'email-send'
});

const templateLimiter = createRateLimiter({
  windowMs: 60000,
  maxRequests: 50,
  prefix: 'template-access'
});

const webhookLimiter = createRateLimiter({
  windowMs: 10000, // 10 seconds
  maxRequests: 100,
  prefix: 'webhook'
});

module.exports = {
  memoryRateLimiter,
  RedisRateLimiter,
  createRateLimiter,
  emailSendLimiter,
  templateLimiter,
  webhookLimiter
};
