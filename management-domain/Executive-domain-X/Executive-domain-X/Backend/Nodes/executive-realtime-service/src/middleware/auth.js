/**
 * Authentication Middleware
 *
 * JWT-based authentication for HTTP routes.
 */

const { verifyToken } = require('../auth/jwt');
const logger = require('../config/logger');

/**
 * Authentication middleware for Express routes
 */
function authenticate(req, res, next) {
  // Get token from Authorization header
  const authHeader = req.headers.authorization;

  if (!authHeader) {
    return res.status(401).json({
      error: 'No authorization header',
      message: 'Authorization header is required',
    });
  }

  const parts = authHeader.split(' ');

  if (parts.length !== 2 || parts[0] !== 'Bearer') {
    return res.status(401).json({
      error: 'Invalid authorization format',
      message: 'Authorization header must be: Bearer <token>',
    });
  }

  const token = parts[1];

  verifyToken(token)
    .then((decoded) => {
      // Attach decoded user info to request
      req.user = {
        tenantId: decoded.tenantId,
        userId: decoded.userId,
        executiveLevel: decoded.executiveLevel || 'ALL',
        roles: decoded.roles || [],
      };

      logger.debug('User authenticated', {
        userId: req.user.userId,
        tenantId: req.user.tenantId,
      });

      next();
    })
    .catch((error) => {
      logger.debug('Authentication failed:', error.message);

      return res.status(401).json({
        error: 'Authentication failed',
        message: error.message,
      });
    });
}

/**
 * Optional authentication - doesn't fail if no token
 */
function optionalAuthenticate(req, res, next) {
  const authHeader = req.headers.authorization;

  if (!authHeader) {
    return next();
  }

  const parts = authHeader.split(' ');

  if (parts.length !== 2 || parts[0] !== 'Bearer') {
    return next();
  }

  const token = parts[1];

  verifyToken(token)
    .then((decoded) => {
      req.user = {
        tenantId: decoded.tenantId,
        userId: decoded.userId,
        executiveLevel: decoded.executiveLevel || 'ALL',
        roles: decoded.roles || [],
      };
      next();
    })
    .catch(() => {
      // Continue without authentication on error
      next();
    });
}

/**
 * Check if user has required role
 */
function hasRole(...roles) {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        error: 'Authentication required',
      });
    }

    const userRoles = req.user.roles || [];

    if (roles.some(role => userRoles.includes(role))) {
      return next();
    }

    return res.status(403).json({
      error: 'Forbidden',
      message: `Required role: ${roles.join(' or ')}`,
    });
  };
}

/**
 * Check if user has required executive level
 */
function hasExecutiveLevel(...levels) {
  return (req, res, next) => {
    if (!req.user) {
      return res.status(401).json({
        error: 'Authentication required',
      });
    }

    // Users with ALL level can access everything
    if (req.user.executiveLevel === 'ALL') {
      return next();
    }

    if (levels.includes(req.user.executiveLevel)) {
      return next();
    }

    return res.status(403).json({
      error: 'Forbidden',
      message: `Required executive level: ${levels.join(' or ')}`,
    });
  };
}

/**
 * Verify tenant access
 */
function verifyTenantAccess(req, res, next) {
  if (!req.user) {
    return res.status(401).json({
      error: 'Authentication required',
    });
  }

  const requestedTenantId = req.params.tenantId || req.body.tenantId;

  if (!requestedTenantId) {
    return res.status(400).json({
      error: 'Tenant ID required',
    });
  }

  if (requestedTenantId !== req.user.tenantId) {
    return res.status(403).json({
      error: 'Forbidden',
      message: 'Cannot access other tenant data',
    });
  }

  next();
}

module.exports = {
  authenticate,
  optionalAuthenticate,
  hasRole,
  hasExecutiveLevel,
  verifyTenantAccess,
};
