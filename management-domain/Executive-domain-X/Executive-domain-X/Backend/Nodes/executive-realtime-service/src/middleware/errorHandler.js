/**
 * Error Handler Middleware
 */

const logger = require('../config/logger');

/**
 * Global error handler
 */
function errorHandler(err, req, res, next) {
  logger.error('Error occurred:', {
    error: err.message,
    stack: err.stack,
    url: req.url,
    method: req.method,
  });

  // WebSocket upgrade errors
  if (err.code === 'ECONNRESET') {
    return;
  }

  const status = err.statusCode || err.status || 500;
  const message = err.message || 'Internal Server Error';

  res.status(status).json({
    error: {
      message: message,
      ...(process.env.NODE_ENV === 'development' && { stack: err.stack }),
    },
    timestamp: new Date().toISOString(),
  });
}

/**
 * Not found handler
 */
function notFoundHandler(req, res) {
  res.status(404).json({
    error: {
      message: 'Not Found',
      path: req.url,
    },
    timestamp: new Date().toISOString(),
  });
}

module.exports = { errorHandler, notFoundHandler };
