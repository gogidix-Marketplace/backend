/**
 * Health Check Middleware
 */

const { getMongoStatus } = require('../config/mongodb');
const { getRedisStatus } = require('../config/redis');

/**
 * Health check endpoint
 */
async function healthCheck(req, res) {
  try {
    const [mongoStatus, redisStatus] = await Promise.all([
      getMongoStatus(),
      getRedisStatus(),
    ]);

    const health = {
      status: 'healthy',
      timestamp: new Date().toISOString(),
      uptime: process.uptime(),
      memory: process.memoryUsage(),
      services: {
        mongodb: mongoStatus.status,
        redis: redisStatus.status,
      },
    };

    // Determine overall health
    const isHealthy = mongoStatus.status === 'connected';
    if (!isHealthy) {
      health.status = 'unhealthy';
    }

    const statusCode = isHealthy ? 200 : 503;
    res.status(statusCode).json(health);

  } catch (error) {
    res.status(503).json({
      status: 'unhealthy',
      timestamp: new Date().toISOString(),
      error: error.message,
    });
  }
}

module.exports = { healthCheck };
