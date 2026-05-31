/**
 * JWT Authentication Middleware
 */

const jwt = require('jsonwebtoken');
const logger = require('../config/logger');

const JWT_SECRET = process.env.JWT_SECRET || 'executive-command-secret';
const JWT_ISSUER = 'executive-command-service';
const JWT_AUDIENCE = 'executive-api';

/**
 * Verify JWT token from request
 */
function authMiddleware(req, res, next) {
  // Get token from header
  const authHeader = req.headers.authorization;

  if (!authHeader || !authHeader.startsWith('Bearer ')) {
    return res.status(401).json({
      error: 'Authentication required',
      message: 'Missing or invalid Authorization header',
    });
  }

  const token = authHeader.substring(7);

  try {
    const decoded = jwt.verify(token, JWT_SECRET, {
      issuer: JWT_ISSUER,
      audience: JWT_AUDIENCE,
    });

    // Attach user info to request
    req.user = {
      tenantId: decoded.tenantId,
      userId: decoded.userId,
      executiveLevel: decoded.executiveLevel || 'ALL',
      email: decoded.email,
    };

    next();
  } catch (error) {
    logger.debug('Authentication failed:', error.message);

    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({
        error: 'Token expired',
        message: 'Please refresh your token',
      });
    }

    return res.status(401).json({
      error: 'Authentication failed',
      message: 'Invalid token',
    });
  }
}

/**
 * Check executive level permission
 */
function requireExecutiveLevel(levels) {
  return (req, res, next) => {
    const userLevel = req.user?.executiveLevel || 'NONE';

    if (levels.includes(userLevel) || userLevel === 'ALL') {
      return next();
    }

    return res.status(403).json({
      error: 'Forbidden',
      message: `Requires executive level: ${levels.join(' or ')}`,
    });
  };
}

module.exports = { authMiddleware, requireExecutiveLevel };
