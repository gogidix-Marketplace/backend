/**
 * API Routes
 */

const express = require('express');
const router = express.Router();

/**
 * @route   GET /api/status
 * @desc    Get WebSocket server status
 * @access  Public
 */
router.get('/status', (req, res) => {
  const wsServer = req.app.get('wsServer');

  if (!wsServer) {
    return res.status(503).json({
      error: 'WebSocket server not available',
    });
  }

  const stats = wsServer.getStats();

  res.json({
    status: 'operational',
    websocket: {
      protocol: 'ws',
      endpoint: '/ws',
      ...stats,
    },
    features: {
      authentication: 'JWT',
      subscriptions: 'room-based',
      rateLimit: '100 messages/minute',
    },
  });
});

/**
 * @route   POST /api/broadcast
 * @desc    Broadcast a message to all clients (for testing/internal use)
 * @access  Internal
 */
router.post('/broadcast', (req, res) => {
  const { tenantId, message, room } = req.body;
  const wsServer = req.app.get('wsServer');

  if (!wsServer) {
    return res.status(503).json({
      error: 'WebSocket server not available',
    });
  }

  if (!tenantId || !message) {
    return res.status(400).json({
      error: 'tenantId and message are required',
    });
  }

  let sent = 0;
  if (room) {
    sent = wsServer.broadcastToRoom(`tenant:${tenantId}:${room}`, {
      type: 'broadcast',
      data: message,
      timestamp: Date.now(),
    });
  } else {
    sent = wsServer.broadcastToTenant(tenantId, {
      type: 'broadcast',
      data: message,
      timestamp: Date.now(),
    });
  }

  res.json({
    success: true,
    sentTo: sent,
  });
});

/**
 * @route   GET /api/tenants/:tenantId/clients
 * @desc    Get connected clients for a tenant
 * @access  Private
 */
router.get('/tenants/:tenantId/clients', (req, res) => {
  const { tenantId } = req.params;
  const wsServer = req.app.get('wsServer');

  if (!wsServer) {
    return res.status(503).json({
      error: 'WebSocket server not available',
    });
  }

  const stats = wsServer.getStats();
  const tenantInfo = stats.connectionsPerTenant.find(t => t.tenantId === tenantId);

  if (!tenantInfo) {
    return res.status(404).json({
      error: 'No connected clients for this tenant',
    });
  }

  res.json({
    tenantId: tenantId,
    connectedClients: tenantInfo.count,
  });
});

module.exports = router;
