/**
 * Tenant Middleware
 *
 * Multi-tenant support for isolating data and connections.
 */

const logger = require('../config/logger');

/**
 * Extract tenant from request
 */
function extractTenant(req, res, next) {
  // Try to get tenantId from various sources
  const tenantId =
    req.headers['x-tenant-id'] ||
    req.query.tenantId ||
    req.user?.tenantId ||
    null;

  if (!tenantId) {
    return res.status(400).json({
      error: 'Tenant ID required',
      message: 'Please provide X-Tenant-ID header or tenantId query parameter',
    });
  }

  req.tenantId = tenantId;
  next();
}

/**
 * Validate tenant access
 */
function validateTenantAccess(req, res, next) {
  const userTenantId = req.user?.tenantId;
  const requestedTenantId = req.tenantId || req.params.tenantId;

  // If user is not authenticated, they can only access public data
  if (!userTenantId) {
    return res.status(401).json({
      error: 'Authentication required',
    });
  }

  // Users can only access their own tenant data
  if (requestedTenantId && requestedTenantId !== userTenantId) {
    logger.warn('Cross-tenant access attempted', {
      userTenantId,
      requestedTenantId,
      userId: req.user.userId,
    });

    return res.status(403).json({
      error: 'Forbidden',
      message: 'Cannot access other tenant data',
    });
  }

  // Set the verified tenant ID
  req.tenantId = userTenantId;
  next();
}

/**
 * Tenant isolation for WebSocket
 */
function validateTenantSocket(socket, room) {
  const socketTenantId = socket.tenantId || socket.user?.tenantId;

  if (!socketTenantId) {
    return false;
  }

  // Extract tenant from room name
  const roomParts = room.split(':');

  if (roomParts[0] === 'tenant' && roomParts[1]) {
    return roomParts[1] === socketTenantId;
  }

  return false;
}

/**
 * Get tenant-specific room name
 */
function getTenantRoom(tenantId, suffix = '') {
  return suffix ? `tenant:${tenantId}:${suffix}` : `tenant:${tenantId}`;
}

/**
 * Get category-specific room name
 */
function getCategoryRoom(tenantId, category) {
  return `tenant:${tenantId}:${category}`;
}

/**
 * Get executive level room name
 */
function getExecutiveLevelRoom(tenantId, executiveLevel) {
  return `tenant:${tenantId}:${executiveLevel}`;
}

/**
 * Get dashboard room name
 */
function getDashboardRoom(tenantId, dashboardType) {
  return `tenant:${tenantId}:dashboard:${dashboardType}`;
}

module.exports = {
  extractTenant,
  validateTenantAccess,
  validateTenantSocket,
  getTenantRoom,
  getCategoryRoom,
  getExecutiveLevelRoom,
  getDashboardRoom,
};
