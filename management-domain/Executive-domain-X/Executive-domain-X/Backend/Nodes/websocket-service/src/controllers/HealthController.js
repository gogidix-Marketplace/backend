const config = require('../config');
const logger = require('../config/logger');
const redis = require('../config/redis');
const mongodb = require('../config/mongodb');
const kafka = require('../config/kafka');
const wsServer = require('../websocket/server');

class HealthController {
  async getHealth(req, res) {
    const health = {
      status: 'healthy',
      timestamp: new Date().toISOString(),
      uptime: process.uptime(),
      environment: config.env,
      checks: {}
    };

    let overallStatus = 'healthy';

    if (mongodb.isHealthy()) {
      health.checks.mongodb = { status: 'healthy' };
    } else {
      health.checks.mongodb = { status: 'unhealthy' };
      overallStatus = 'unhealthy';
    }

    if (redis.isHealthy()) {
      health.checks.redis = { status: 'healthy' };
    } else {
      health.checks.redis = { status: 'unhealthy' };
      overallStatus = 'degraded';
    }

    if (kafka.isHealthy()) {
      health.checks.kafka = { status: 'healthy' };
    } else {
      health.checks.kafka = { status: 'unhealthy' };
      overallStatus = 'degraded';
    }

    if (wsServer.isRunning()) {
      health.checks.websocket = { status: 'healthy' };
    } else {
      health.checks.websocket = { status: 'unhealthy' };
      overallStatus = 'unhealthy';
    }

    health.status = overallStatus;
    res.status(overallStatus === 'healthy' ? 200 : 503).json(health);
  }

  async getReady(req, res) {
    const checks = {
      mongodb: mongodb.isHealthy(),
      redis: redis.isHealthy(),
      kafka: kafka.isHealthy()
    };

    const allReady = Object.values(checks).every(check => check === true);

    res.status(allReady ? 200 : 503).json({
      status: allReady ? 'ready' : 'not_ready',
      checks
    });
  }

  async getLive(req, res) {
    res.status(200).json({
      status: 'alive',
      timestamp: new Date().toISOString()
    });
  }

  async getMetrics(req, res) {
    try {
      const wsStats = wsServer.getStats();
      const memory = process.memoryUsage();
      const cpu = process.cpuUsage();

      const metrics = {
        timestamp: new Date().toISOString(),
        uptime: process.uptime(),
        websocket: {
          connections: wsStats.connections,
          rooms: wsStats.rooms,
          messagesBroadcast: wsStats.broadcastStats?.messagesBroadcast || 0,
          messagesToKafka: wsStats.broadcastStats?.messagesToKafka || 0,
          messagesToRedis: wsStats.broadcastStats?.messagesToRedis || 0,
          errors: wsStats.broadcastStats?.errors || 0
        },
        system: {
          memory: {
            rss: Math.round(memory.rss / 1024 / 1024),
            heapTotal: Math.round(memory.heapTotal / 1024 / 1024),
            heapUsed: Math.round(memory.heapUsed / 1024 / 1024),
            external: Math.round(memory.external / 1024 / 1024)
          },
          cpu: {
            user: cpu.user,
            system: cpu.system
          },
          nodeVersion: process.version,
          platform: process.platform,
          arch: process.arch
        },
        kafka: wsStats.kafkaStats
      };

      res.json(metrics);
    } catch (error) {
      logger.error('Error getting metrics:', error);
      res.status(500).json({ error: 'Failed to get metrics' });
    }
  }

  async getConnections(req, res) {
    try {
      const wsStats = wsServer.getStats();
      const connections = wsServer.getConnections();

      res.json({
        total: connections.length,
        connections
      });
    } catch (error) {
      logger.error('Error getting connections:', error);
      res.status(500).json({ error: 'Failed to get connections' });
    }
  }
}

module.exports = new HealthController();
