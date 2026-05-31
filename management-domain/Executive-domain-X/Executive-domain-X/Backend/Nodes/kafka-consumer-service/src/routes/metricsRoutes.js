const express = require('express');
const router = express.Router();
const kafka = require('../config/kafka');
const mongodb = require('../config/mongodb');
const redis = require('../config/redis');
const EventStore = require('../models/EventStore');
const DeadLetterQueue = require('../models/DeadLetterQueue');
const EventProcessor = require('../services/EventProcessor');
const RetryService = require('../services/RetryService');
const metrics = require('../config/metrics');

/**
 * @route   GET /api/stats/overview
 * @desc    Get service overview statistics
 */
router.get('/overview', async (req, res) => {
  try {
    const tenantId = req.query.tenantId || null;

    const [eventStats, dlqStats, mongoStats, redisStats, handlerStats, retryStats] =
      await Promise.allSettled([
        EventStore.getStats(tenantId),
        DeadLetterQueue.getStats(tenantId),
        mongodb.getStats().catch(() => null),
        redis.getStats().catch(() => null),
        Promise.resolve(EventProcessor.getHandlerStats()),
        Promise.resolve(RetryService.getStats())
      ]);

    res.json({
      success: true,
      data: {
        connections: {
          kafka: kafka.isHealthy(),
          mongodb: mongodb.isHealthy(),
          redis: redis.isHealthy()
        },
        eventStore: eventStats.status === 'fulfilled' ? eventStats.value : null,
        dlq: dlqStats.status === 'fulfilled' ? dlqStats.value : null,
        mongodb: mongoStats.status === 'fulfilled' ? mongoStats.value : null,
        redis: redisStats.status === 'fulfilled' ? redisStats.value : null,
        handlers: handlerStats.status === 'fulfilled' ? handlerStats.value : null,
        retry: retryStats.status === 'fulfilled' ? retryStats.value : null,
        uptime: process.uptime(),
        memory: process.memoryUsage(),
        cpu: process.cpuUsage()
      }
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/stats/events
 * @desc    Get detailed event statistics
 */
router.get('/events', async (req, res) => {
  try {
    const tenantId = req.query.tenantId || null;
    const stats = await EventStore.getStats(tenantId);

    res.json({
      success: true,
      data: stats
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/stats/dlq
 * @desc    Get detailed DLQ statistics
 */
router.get('/dlq', async (req, res) => {
  try {
    const tenantId = req.query.tenantId || null;
    const stats = await DeadLetterQueue.getStats(tenantId);

    res.json({
      success: true,
      data: stats
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/stats/handlers
 * @desc    Get handler statistics
 */
router.get('/handlers', async (req, res) => {
  try {
    const stats = EventProcessor.getHandlerStats();

    res.json({
      success: true,
      data: stats
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

/**
 * @route   GET /api/stats/prometheus
 * @desc    Get Prometheus metrics
 */
router.get('/prometheus', async (req, res) => {
  try {
    res.set('Content-Type', 'text/plain');
    const metricsData = await metrics.getMetrics();
    res.send(metricsData);
  } catch (error) {
    res.status(500).send(`# Error fetching metrics\n${error.message}`);
  }
});

/**
 * @route   GET /api/stats/health
 * @desc    Get health check for all components
 */
router.get('/health', async (req, res) => {
  const checks = {
    kafka: {
      status: kafka.isHealthy() ? 'UP' : 'DOWN',
      connected: kafka.isHealthy()
    },
    mongodb: {
      status: mongodb.isHealthy() ? 'UP' : 'DOWN',
      connected: mongodb.isHealthy()
    },
    redis: {
      status: redis.isHealthy() ? 'UP' : 'DOWN',
      connected: redis.isHealthy()
    },
    process: {
      uptime: process.uptime(),
      memory: process.memoryUsage(),
      cpu: process.cpuUsage(),
      pid: process.pid
    }
  };

  // Overall status
  const overallStatus =
    checks.kafka.status === 'UP' &&
    checks.mongodb.status === 'UP' &&
    checks.redis.status === 'UP'
      ? 'UP'
      : 'DEGRADED';

  const statusCode = overallStatus === 'UP' ? 200 : 503;

  res.status(statusCode).json({
    status: overallStatus,
    timestamp: new Date().toISOString(),
    checks
  });
});

/**
 * @route   GET /api/stats/ready
 * @desc    Readiness probe
 */
router.get('/ready', async (req, res) => {
  const isReady = kafka.isHealthy() && mongodb.isHealthy() && redis.isHealthy();

  if (isReady) {
    res.status(200).json({ status: 'ready' });
  } else {
    res.status(503).json({
      status: 'not_ready',
      checks: {
        kafka: kafka.isHealthy(),
        mongodb: mongodb.isHealthy(),
        redis: redis.isHealthy()
      }
    });
  }
});

/**
 * @route   GET /api/stats/live
 * @desc    Liveness probe
 */
router.get('/live', (req, res) => {
  res.status(200).json({ status: 'alive' });
});

/**
 * @route   GET /api/stats/metrics/json
 * @desc    Get metrics as JSON
 */
router.get('/metrics/json', async (req, res) => {
  try {
    const metricsData = await metrics.getMetricsAsJson();

    res.json({
      success: true,
      data: metricsData
    });
  } catch (error) {
    res.status(500).json({ success: false, message: error.message });
  }
});

module.exports = router;
