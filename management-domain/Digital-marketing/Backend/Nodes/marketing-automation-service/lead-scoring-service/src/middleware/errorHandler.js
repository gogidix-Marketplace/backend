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
 * Error handler middleware
 */
const errorHandler = (err, req, res, next) => {
  logger.error({
    error: err.message,
    stack: err.stack,
    path: req.path,
    method: req.method,
    ip: req.ip
  });

  const statusCode = err.statusCode || err.status || 500;

  const errorResponse = {
    error: err.name || 'InternalServerError',
    message: err.message || 'An unexpected error occurred',
    statusCode
  };

  if (err.errors) {
    errorResponse.errors = err.errors;
  }

  if (process.env.NODE_ENV === 'development') {
    errorResponse.stack = err.stack;
  }

  if (err.code === 11000) {
    errorResponse.error = 'ConflictError';
    errorResponse.message = 'A resource with this identifier already exists';
    errorResponse.statusCode = 409;
  }

  res.status(statusCode).json(errorResponse);
};

/**
 * Async handler wrapper
 */
const asyncHandler = (fn) => (req, res, next) => {
  Promise.resolve(fn(req, res, next)).catch(next);
};

module.exports = {
  errorHandler,
  asyncHandler,
  ApiError,
  ValidationError,
  NotFoundError
};
