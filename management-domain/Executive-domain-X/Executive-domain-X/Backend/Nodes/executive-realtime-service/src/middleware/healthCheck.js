/**
 * Health Check Middleware
 */

const { getRedisStatus } = require('../config/redis');

/**
 * Health check endpoint
 */
async function healthCheck(req, res) {
  try {
    const redisStatus = await getRedisStatus();

    const health = {
      status: 'healthy',
      timestamp: new Date().toISOString(),
      uptime: process.uptime(),
      memory: process.memoryUsage(),
      services: {
        redis: redisStatus,
        websocket: 'connected',
      },
    };

    // Determine overall health
    const isHealthy = redisStatus.status === 'connected';
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
