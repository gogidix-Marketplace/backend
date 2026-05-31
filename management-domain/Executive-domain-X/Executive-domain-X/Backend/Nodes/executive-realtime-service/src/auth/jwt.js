/**
 * JWT Authentication Utilities
 */

const jwt = require('jsonwebtoken');
const logger = require('../config/logger');

const JWT_SECRET = process.env.JWT_SECRET || 'executive-realtime-secret-key';
const JWT_ISSUER = 'executive-realtime-service';
const JWT_AUDIENCE = 'executive-dashboard';

/**
 * Verify JWT token
 */
async function verifyToken(token) {
  return new Promise((resolve, reject) => {
    jwt.verify(token, JWT_SECRET, {
      issuer: JWT_ISSUER,
      audience: JWT_AUDIENCE,
    }, (err, decoded) => {
      if (err) {
        logger.debug('JWT verification failed:', err.message);
        return reject(err);
      }
      resolve(decoded);
    });
  });
}

/**
 * Sign JWT token (for testing)
 */
function signToken(payload) {
  return jwt.sign(payload, JWT_SECRET, {
    issuer: JWT_ISSUER,
    audience: JWT_AUDIENCE,
    expiresIn: '1h',
  });
}

module.exports = {
  verifyToken,
  signToken,
};
