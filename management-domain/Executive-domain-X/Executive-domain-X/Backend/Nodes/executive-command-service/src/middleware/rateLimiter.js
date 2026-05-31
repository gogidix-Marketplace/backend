/**
 * Rate Limiter Middleware
 */

const { RateLimiterMemory } = require('rate-limiter-flexible');
const logger = require('../config/logger');

// Rate limiter for API requests
const apiRateLimiter = new RateLimiterMemory({
  points: 100, // Number of requests
  duration: 60, // Per 60 seconds
});

// Rate limiter for write operations (more strict)
const writeRateLimiter = new RateLimiterMemory({
  points: 20, // Number of write operations
  duration: 60, // Per 60 seconds
});

/**
 * Rate limiting middleware
 */
async function rateLimiter(req, res, next) {
  const key = req.user?.tenantId || req.ip;

  // Use stricter rate limiting for write operations
  const limiter = ['POST', 'PUT', 'DELETE', 'PATCH'].includes(req.method)
    ? writeRateLimiter
    : apiRateLimiter;

  try {
    await limiter.consume(key);
    next();
  } catch (rej) {
    logger.warn('Rate limit exceeded:', { key, method: req.method, url: req.url });

    res.status(429).json({
      error: 'Too many requests',
      message: 'Rate limit exceeded. Please try again later.',
      retryAfter: Math.round(rej.msBeforeNext / 1000),
    });
  }
}

module.exports = { rateLimiter };
