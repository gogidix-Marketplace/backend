/**
 * Error Handler Middleware
 * Centralized error handling for the application
 */

const logger = require('../utils/logger');

/**
 * Custom API Error class
 */
class ApiError extends Error {
  constructor(statusCode, message, errors = null) {
    super(message);
    this.statusCode = statusCode;
    this.errors = errors;
    this.name = 'ApiError';
    Error.captureStackTrace(this, this.constructor);
  }
}

/**
 * Validation Error class
 */
class ValidationError extends ApiError {
  constructor(message, errors = null) {
    super(400, message, errors);
    this.name = 'ValidationError';
  }
}

/**
 * Not Found Error class
 */
class NotFoundError extends ApiError {
  constructor(message = 'Resource not found') {
    super(404, message);
    this.name = 'NotFoundError';
  }
}

/**
 * Conflict Error class
 */
class ConflictError extends ApiError {
  constructor(message = 'Resource conflict') {
    super(409, message);
    this.name = 'ConflictError';
  }
}

/**
 * Rate Limit Error class
 */
class RateLimitError extends ApiError {
  constructor(message = 'Rate limit exceeded') {
    super(429, message);
    this.name = 'RateLimitError';
  }
}

/**
 * Error handler middleware
 */
const errorHandler = (err, req, res, next) => {
  // Log error
  logger.error({
    error: err.message,
    stack: err.stack,
    path: req.path,
    method: req.method,
    ip: req.ip
  });

  // Determine status code
  const statusCode = err.statusCode || err.status || 500;

  // Prepare error response
  const errorResponse = {
    error: err.name || 'InternalServerError',
    message: err.message || 'An unexpected error occurred',
    statusCode
  };

  // Include validation errors if present
  if (err.errors) {
    errorResponse.errors = err.errors;
  }

  // Include stack trace in development
  if (process.env.NODE_ENV === 'development') {
    errorResponse.stack = err.stack;
  }

  // Handle specific error types
  if (err.name === 'ValidationError' && err.name !== 'ValidationError') {
    // Mongoose validation error
    errorResponse.error = 'ValidationError';
    errorResponse.errors = Object.values(err.errors).map(e => ({
      field: e.path,
      message: e.message
    }));
  }

  if (err.code === 11000) {
    // MongoDB duplicate key error
    errorResponse.error = 'ConflictError';
    errorResponse.message = 'A resource with this identifier already exists';
    errorResponse.statusCode = 409;
  }

  if (err.name === 'CastError') {
    // MongoDB cast error
    errorResponse.error = 'BadRequest';
    errorResponse.message = 'Invalid ID format';
    errorResponse.statusCode = 400;
  }

  res.status(statusCode).json(errorResponse);
};

/**
 * Not found handler
 */
const notFoundHandler = (req, res) => {
  res.status(404).json({
    error: 'NotFound',
    message: `Route ${req.method} ${req.path} not found`,
    statusCode: 404
  });
};

/**
 * Async handler wrapper to catch errors in async routes
 */
const asyncHandler = (fn) => (req, res, next) => {
  Promise.resolve(fn(req, res, next)).catch(next);
};

module.exports = {
  errorHandler,
  notFoundHandler,
  asyncHandler,
  ApiError,
  ValidationError,
  NotFoundError,
  ConflictError,
  RateLimitError
};
