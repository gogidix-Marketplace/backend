/**
 * Health Check Routes
 * Health and readiness endpoints
 */

const express = require('express');
const router = express.Router();
const { getConnectionStatus } = require('../config/database');
const { getRedisStatus } = require('../config/redis');
const { getQueue } = require('../services/postQueue');

/**
 * GET /health
 * Basic health check
 */
router.get('/', async (req, res) => {
  res.status(200).json({
    status: 'healthy',
    timestamp: new Date().toISOString(),
    service: 'social-automation-service'
  });
});

/**
 * GET /health/ready
 * Readiness check
 */
router.get('/ready', async (req, res) => {
  const checks = {
    database: false,
    redis: false,
    queue: false
  };

  try {
    const dbStatus = getConnectionStatus();
    checks.database = dbStatus.isConnected && dbStatus.readyState === 1;
  } catch (error) {
    checks.database = false;
  }

  try {
    const redisStatus = getRedisStatus();
    checks.redis = redisStatus.status === 'ready';
  } catch (error) {
    checks.redis = false;
  }

  try {
    const queue = getQueue();
    checks.queue = !!queue;
  } catch (error) {
    checks.queue = false;
  }

  const isReady = Object.values(checks).every(v => v);

  res.status(isReady ? 200 : 503).json({
    status: isReady ? 'ready' : 'not_ready',
    checks,
    timestamp: new Date().toISOString()
  });
});

/**
 * GET /health/live
 * Liveness check
 */
router.get('/live', (req, res) => {
  res.status(200).json({
    status: 'alive',
    timestamp: new Date().toISOString()
  });
});

/**
 * GET /health/detailed
 * Detailed health information
 */
router.get('/detailed', async (req, res) => {
  const health = {
    service: 'social-automation-service',
    version: '1.0.0',
    timestamp: new Date().toISOString(),
    uptime: process.uptime(),
    memory: process.memoryUsage(),
    environment: process.env.NODE_ENV || 'development'
  };

  try {
    const dbStatus = getConnectionStatus();
    health.database = {
      status: dbStatus.isConnected ? 'connected' : 'disconnected',
      readyState: dbStatus.readyState,
      host: dbStatus.host,
      name: dbStatus.name
    };
  } catch (error) {
    health.database = {
      status: 'error',
      error: error.message
    };
  }

  try {
    const redisStatus = getRedisStatus();
    health.redis = {
      status: redisStatus.status,
      host: redisStatus.host,
      port: redisStatus.port
    };
  } catch (error) {
    health.redis = {
      status: 'error',
      error: error.message
    };
  }

  try {
    const queue = getQueue();
    if (queue) {
      const [waiting, active, completed, failed] = await Promise.all([
        queue.getWaiting().then(jobs => jobs.length).catch(() => 0),
        queue.getActive().then(jobs => jobs.length).catch(() => 0),
        queue.getCompleted().then(jobs => jobs.length).catch(() => 0),
        queue.getFailed().then(jobs => jobs.length).catch(() => 0)
      ]);

      health.queue = {
        status: 'initialized',
        waiting,
        active,
        completed,
        failed
      };
    } else {
      health.queue = {
        status: 'not_initialized'
      };
    }
  } catch (error) {
    health.queue = {
      status: 'error',
      error: error.message
    };
  }

  const isHealthy = health.database?.status === 'connected' &&
                    health.redis?.status === 'ready' &&
                    health.queue?.status === 'initialized';

  res.status(isHealthy ? 200 : 503).json(health);
});

module.exports = router;
