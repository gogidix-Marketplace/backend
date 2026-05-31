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

/**
 * Async error wrapper
 */
function asyncHandler(fn) {
  return (req, res, next) => {
    Promise.resolve(fn(req, res, next)).catch(next);
  };
}

module.exports = { errorHandler, notFoundHandler, asyncHandler };
