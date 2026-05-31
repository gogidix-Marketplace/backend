const { RateLimiterRedis, RateLimiterMemory } = require('rate-limiter-flexible');
const config = require('../config');
const logger = require('../config/logger');
const redis = require('../config/redis');

class RateLimiterMiddleware {
  constructor() {
    this.rateLimiters = new Map();
    this.memoryLimiter = new RateLimiterMemory({
      points: 10,
      duration: 1
    });

    this.initializeRateLimiters();
  }

  initializeRateLimiters() {
    const limiterConfig = {
      general: {
        points: config.rateLimit.maxRequests,
        duration: config.rateLimit.windowMs / 1000
      },
      message: {
        points: 100,
        duration: 60
      },
      broadcast: {
        points: 20,
        duration: 60
      },
      joinRoom: {
        points: 30,
        duration: 60
      },
      presence: {
        points: 60,
        duration: 60
      }
    };

    for (const [name, cfg] of Object.entries(limiterConfig)) {
      try {
        this.rateLimiters.set(name, new RateLimiterRedis({
          storeClient: redis.getClient(),
          keyPrefix: `ws_rl_${name}`,
          ...cfg
        }));
      } catch (error) {
        logger.warn(`Failed to create Redis rate limiter for ${name}, using memory`, error);
        this.rateLimiters.set(name, new RateLimiterMemory(cfg));
      }
    }
  }

  async consume(socket, type, key = null) {
    const limiter = this.rateLimiters.get(type) || this.rateLimiters.get('general');

    const rateLimitKey = key || this.getKey(socket, type);

    try {
      const result = await limiter.consume(rateLimitKey);
      return {
        success: true,
        remainingPoints: result.remainingPoints,
        msBeforeNext: result.msBeforeNext
      };
    } catch (rej) {
      return {
        success: false,
        remainingPoints: rej.remainingPoints,
        msBeforeNext: rej.msBeforeNext,
        message: `Rate limit exceeded for ${type}. Try again in ${Math.ceil(rej.msBeforeNext / 1000)} seconds`
      };
    }
  }

  getKey(socket, type) {
    const user = socket.data.user;
    if (user) {
      return `${user.tenantId}:${user.userId}:${type}`;
    }
    return `${socket.id}:${type}`;
  }

  async checkMessageRate(socket) {
    return this.consume(socket, 'message');
  }

  async checkBroadcastRate(socket) {
    return this.consume(socket, 'broadcast');
  }

  async checkJoinRoomRate(socket) {
    return this.consume(socket, 'joinRoom');
  }

  async checkPresenceRate(socket) {
    return this.consume(socket, 'presence');
  }

  async checkGeneralRate(socket) {
    return this.consume(socket, 'general');
  }

  async penalize(socket, type = 'general') {
    const key = this.getKey(socket, type);
    const limiter = this.rateLimiters.get(type) || this.rateLimiters.get('general');

    try {
      await limiter.penalty(key);
      logger.debug(`Penalty applied to ${key} for ${type}`);
    } catch (error) {
      logger.error('Error applying penalty:', error);
    }
  }

  async reset(socket, type = 'general') {
    const key = this.getKey(socket, type);
    const limiter = this.rateLimiters.get(type) || this.rateLimiters.get('general');

    try {
      await limiter.delete(key);
      logger.debug(`Rate limit reset for ${key} for ${type}`);
    } catch (error) {
      logger.error('Error resetting rate limit:', error);
    }
  }

  getMiddleware(type) {
    return async (socket, data, next) => {
      const result = await this.consume(socket, type);

      if (!result.success) {
        socket.emit('error', {
          code: 'RATE_LIMIT_EXCEEDED',
          message: result.message,
          retryAfter: result.msBeforeNext
        });
        return false;
      }

      return next();
    };
  }
}

module.exports = new RateLimiterMiddleware();
