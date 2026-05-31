/**
 * Authentication Middleware
 * API key authentication for secure endpoints
 */

const logger = require('../utils/logger');

/**
 * Authenticate API key middleware
 */
const authenticateApiKey = (req, res, next) => {
  const apiKeyHeader = process.env.API_KEY_HEADER || 'X-API-Key';
  const apiKey = req.headers[apiKeyHeader.toLowerCase()] || req.headers[apiKeyHeader];

  if (!apiKey) {
    logger.warn('Authentication failed: No API key provided', {
      ip: req.ip,
      path: req.path
    });
    return res.status(401).json({
      error: 'Unauthorized',
      message: 'API key is required'
    });
  }

  const validApiKeys = process.env.API_KEYS
    ? process.env.API_KEYS.split(',').map(k => k.trim())
    : [];

  if (validApiKeys.length === 0) {
    logger.warn('Authentication bypassed: No API keys configured');
    return next();
  }

  if (!validApiKeys.includes(apiKey)) {
    logger.warn('Authentication failed: Invalid API key', {
      ip: req.ip,
      path: req.path
    });
    return res.status(403).json({
      error: 'Forbidden',
      message: 'Invalid API key'
    });
  }

  req.apiKey = apiKey;
  next();
};

/**
 * Optional authentication
 */
const optionalAuth = (req, res, next) => {
  const apiKeyHeader = process.env.API_KEY_HEADER || 'X-API-Key';
  const apiKey = req.headers[apiKeyHeader.toLowerCase()] || req.headers[apiKeyHeader];

  if (apiKey) {
    const validApiKeys = process.env.API_KEYS
      ? process.env.API_KEYS.split(',').map(k => k.trim())
      : [];

    if (validApiKeys.includes(apiKey)) {
      req.apiKey = apiKey;
      req.isAuthenticated = true;
    }
  }

  req.isAuthenticated = req.isAuthenticated || false;
  next();
};

module.exports = {
  authenticateApiKey,
  optionalAuth
};
