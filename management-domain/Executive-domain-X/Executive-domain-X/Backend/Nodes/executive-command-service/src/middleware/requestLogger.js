/**
 * Request Logger Middleware
 */

const logger = require('../config/logger');

function requestLogger(req, res, next) {
  const start = Date.now();

  // Log request
  logger.info({
    method: req.method,
    url: req.url,
    ip: req.ip,
    userAgent: req.get('user-agent'),
    tenantId: req.user?.tenantId,
  }, 'Incoming request');

  // Log response
  res.on('finish', () => {
    const duration = Date.now() - start;
    logger.info({
      method: req.method,
      url: req.url,
      status: res.statusCode,
      duration: `${duration}ms`,
      tenantId: req.user?.tenantId,
    }, 'Request completed');
  });

  next();
}

module.exports = { requestLogger };
