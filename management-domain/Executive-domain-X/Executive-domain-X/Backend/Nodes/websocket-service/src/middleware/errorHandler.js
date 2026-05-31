const logger = require('../config/logger');
const config = require('../config');

class ErrorHandler {
  constructor() {
    this.errorCodes = {
      AUTHENTICATION_FAILED: 4001,
      AUTHORIZATION_FAILED: 4002,
      RATE_LIMIT_EXCEEDED: 4003,
      VALIDATION_ERROR: 4004,
      ROOM_NOT_FOUND: 4005,
      ROOM_FULL: 4006,
      USER_NOT_FOUND: 4007,
      INTERNAL_ERROR: 5000,
      CONNECTION_CLOSED: 5001,
      TIMEOUT: 5002
    };
  }

  handleError(socket, error, context = {}) {
    const errorInfo = this.parseError(error);

    logger.error('WebSocket error:', {
      socketId: socket.id,
      userId: socket.data.user?.userId,
      tenantId: socket.data.user?.tenantId,
      error: errorInfo,
      context
    });

    if (socket.connected) {
      socket.emit('error', {
        code: errorInfo.code,
        message: errorInfo.message,
        details: config.env === 'development' ? errorInfo.details : undefined,
        timestamp: new Date().toISOString()
      });
    }

    if (errorInfo.critical) {
      this.handleCriticalError(socket, error);
    }
  }

  parseError(error) {
    if (typeof error === 'string') {
      return {
        code: this.errorCodes.INTERNAL_ERROR,
        message: error,
        details: null,
        critical: false
      };
    }

    if (error.name === 'TokenExpiredError') {
      return {
        code: this.errorCodes.AUTHENTICATION_FAILED,
        message: 'Authentication token has expired',
        details: error.message,
        critical: false
      };
    }

    if (error.name === 'JsonWebTokenError') {
      return {
        code: this.errorCodes.AUTHENTICATION_FAILED,
        message: 'Invalid authentication token',
        details: error.message,
        critical: false
      };
    }

    if (error.message?.includes('Rate limit')) {
      return {
        code: this.errorCodes.RATE_LIMIT_EXCEEDED,
        message: error.message,
        details: error.remainingPoints,
        critical: false
      };
    }

    if (error.message?.includes('Validation')) {
      return {
        code: this.errorCodes.VALIDATION_ERROR,
        message: 'Invalid request data',
        details: error.errors || error.message,
        critical: false
      };
    }

    if (error.message?.includes('not found')) {
      return {
        code: this.errorCodes.ROOM_NOT_FOUND,
        message: error.message,
        details: null,
        critical: false
      };
    }

    if (error.code === 'ECONNREFUSED' || error.code === 'ETIMEDOUT') {
      return {
        code: this.errorCodes.TIMEOUT,
        message: 'Connection error',
        details: error.message,
        critical: true
      };
    }

    return {
      code: this.errorCodes.INTERNAL_ERROR,
      message: 'An unexpected error occurred',
      details: config.env === 'development' ? error.message : null,
      critical: false
    };
  }

  handleCriticalError(socket, error) {
    logger.error('Critical error, disconnecting socket:', {
      socketId: socket.id,
      error: error.message
    });

    socket.emit('critical_error', {
      message: 'A critical error occurred. You will be disconnected.',
      reconnect: true
    });

    setTimeout(() => {
      socket.disconnect(true);
    }, 100);
  }

  createError(code, message, details = null) {
    return {
      code,
      message,
      details,
      timestamp: new Date().toISOString()
    };
  }

  authenticationError(message = 'Authentication failed') {
    return this.createError(this.errorCodes.AUTHENTICATION_FAILED, message);
  }

  authorizationError(message = 'Authorization failed') {
    return this.createError(this.errorCodes.AUTHORIZATION_FAILED, message);
  }

  rateLimitError(message, retryAfter) {
    return this.createError(
      this.errorCodes.RATE_LIMIT_EXCEEDED,
      message,
      { retryAfter }
    );
  }

  validationError(errors) {
    return this.createError(
      this.errorCodes.VALIDATION_ERROR,
      'Validation failed',
      { errors }
    );
  }

  roomNotFoundError(room) {
    return this.createError(
      this.errorCodes.ROOM_NOT_FOUND,
      `Room '${room}' not found`
    );
  }

  roomFullError(room) {
    return this.createError(
      this.errorCodes.ROOM_FULL,
      `Room '${room}' is full`
    );
  }

  wrapAsync(fn) {
    return async (...args) => {
      try {
        return await fn(...args);
      } catch (error) {
        const socket = args[0];
        this.handleError(socket, error, { function: fn.name });
        return null;
      }
    };
  }

  socketMiddleware() {
    return (socket, next) => {
      socket.on('error', (error) => {
        this.handleError(socket, error, { event: 'socket_error' });
      });

      const originalEmit = socket.emit.bind(socket);
      socket.emit = (event, ...args) => {
        try {
          return originalEmit(event, ...args);
        } catch (error) {
          this.handleError(socket, error, { event, emit: true });
        }
      };

      next();
    };
  }
}

module.exports = new ErrorHandler();
