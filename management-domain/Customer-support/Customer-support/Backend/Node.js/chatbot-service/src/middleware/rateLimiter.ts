/**
 * Rate Limiting Middleware
 */

import rateLimit from 'express-rate-limit';
import { logger } from '../utils/logger';
import config from '../config';
import { redisClient } from '../utils/redis';

// Redis store for rate limiting in production
class RedisStore {
  private prefix = 'ratelimit:';

  async increment(key: string): Promise<{ current: number; resetTime: Date }> {
    const redisKey = `${this.prefix}${key}`;
    const ttl = config.rateLimit.windowMs / 1000;

    const current = await redisClient.incr(redisKey);
    if (current === 1) {
      await redisClient.expire(redisKey, ttl);
    }

    const ttlRemaining = await redisClient.getClient().pttl(redisKey);
    const resetTime = new Date(Date.now() + Math.max(ttlRemaining, ttl * 1000));

    return { current, resetTime };
  }

  async reset(key: string): Promise<void> {
    await redisClient.del(`${this.prefix}${key}`);
  }

  async decrement(key: string): Promise<void> {
    const redisKey = `${this.prefix}${key}`;
    const value = await redisClient.get(redisKey);
    if (value && parseInt(value) > 0) {
      const client = redisClient.getClient();
      await client.decr(redisKey);
    }
  }
}

const redisStore = new RedisStore();

// General API rate limiter
export const apiRateLimiter = rateLimit({
  windowMs: config.rateLimit.windowMs,
  max: config.rateLimit.maxRequests,
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    success: false,
    error: 'Too many requests from this IP, please try again later.',
  },
  handler: (req, res) => {
    logger.warn('Rate limit exceeded', {
      ip: req.ip,
      path: req.path,
      method: req.method,
    });
    res.status(429).json({
      success: false,
      error: 'Rate limit exceeded. Please try again later.',
      retryAfter: Math.ceil(config.rateLimit.windowMs / 1000),
    });
  },
  skip: (req) => {
    // Skip rate limiting for internal services
    return req.headers['x-service-token'] !== undefined;
  },
});

// Stricter rate limiter for chat endpoints
export const chatRateLimiter = rateLimit({
  windowMs: 60000, // 1 minute
  max: 30, // 30 messages per minute
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    success: false,
    error: 'Too many messages, please slow down.',
  },
  handler: (req, res) => {
    logger.warn('Chat rate limit exceeded', {
      ip: req.ip,
      sessionId: req.body?.sessionId,
    });
    res.status(429).json({
      success: false,
      error: 'You are sending messages too quickly. Please wait a moment.',
      retryAfter: 60,
    });
  },
});

// Rate limiter for handoff requests (prevent spam)
export const handoffRateLimiter = rateLimit({
  windowMs: 300000, // 5 minutes
  max: 5, // 5 handoff requests per 5 minutes
  standardHeaders: true,
  legacyHeaders: false,
  message: {
    success: false,
    error: 'Too many handoff requests. Please contact support directly.',
  },
  handler: (req, res) => {
    logger.warn('Handoff rate limit exceeded', {
      ip: req.ip,
      sessionId: req.body?.sessionId,
    });
    res.status(429).json({
      success: false,
      error: 'Too many handoff requests. An agent will be with you shortly.',
      retryAfter: 300,
    });
  },
});

// Key generator for session-based rate limiting
export const sessionKeyGenerator = (req: Request): string => {
  const sessionId = req.body?.sessionId || req.params?.sessionId || req.headers['x-session-id'];
  return sessionId ? `session:${sessionId}` : `ip:${req.ip}`;
};

// Cleanup expired rate limit entries (run periodically)
export const cleanupRateLimits = async (): Promise<void> => {
  // Redis handles TTL automatically, but we can log stats
  try {
    const info = await redisClient.getClient().info('stats');
    logger.debug('Redis stats:', info);
  } catch (error) {
    logger.error('Error cleaning up rate limits:', error);
  }
};
