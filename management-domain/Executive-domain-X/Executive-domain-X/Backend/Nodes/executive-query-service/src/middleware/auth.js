/**
 * Authentication Middleware
 * JWT verification and tenant extraction
 */

const jwt = require('jsonwebtoken');
const logger = require('../config/logger');

const JWT_SECRET = process.env.JWT_SECRET || 'your-secret-key';

/**
 * Verify JWT token and extract user info
 */
function authenticateToken(req, res, next) {
  const authHeader = req.headers['authorization'];
  const token = authHeader && authHeader.split(' ')[1];

  if (!token) {
    return res.status(401).json({
      error: 'Access token required',
    });
  }

  try {
    const decoded = jwt.verify(token, JWT_SECRET);

    // Attach user info to request
    req.user = {
      tenantId: decoded.tenantId || decoded.tenant,
      userId: decoded.sub || decoded.userId,
      roles: decoded.roles || [],
    };

    next();
  } catch (error) {
    logger.warn('JWT verification failed', { error: error.message });

    if (error.name === 'TokenExpiredError') {
      return res.status(401).json({
        error: 'Token expired',
      });
    }

    return res.status(403).json({
      error: 'Invalid token',
    });
  }
}

/**
 * Setup authentication middleware
 */
async function setupAuth(app) {
  // Apply to API routes only
  app.use('/api', authenticateToken);

  // Health check bypass
  app.get('/health', (req, res) => {
    res.json({
      success: true,
      service: 'executive-query-service',
      status: 'healthy',
      timestamp: new Date().toISOString(),
    });
  });

  logger.info('Authentication middleware configured');
}

module.exports = {
  authenticateToken,
  setupAuth,
};
