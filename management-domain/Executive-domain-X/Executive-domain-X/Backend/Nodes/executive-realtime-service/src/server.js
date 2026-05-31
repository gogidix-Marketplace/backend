/**
 * Executive Real-time Service
 *
 * Provides WebSocket connections for live KPI updates to executive dashboards.
 * Supports multi-tenant architecture with Redis pub/sub for distributed updates.
 *
 * Features:
 * - WebSocket server for real-time KPI updates
 * - Redis pub/sub for distributed message broadcasting
 * - JWT authentication for tenant isolation
 * - Room-based filtering by executive level and category
 * - Rate limiting and connection management
 * - Health monitoring and metrics
 */

require('dotenv').config();
const { createServer } = require('./app');
const logger = require('./config/logger');
const { connectRedis } = require('./config/redis');

const PORT = process.env.PORT || 3003;
const HOST = process.env.HOST || '0.0.0.0';

async function startServer() {
  try {
    // Connect to Redis for pub/sub
    await connectRedis();
    logger.info('Redis connected successfully');

    // Create and start the server
    const server = await createServer();

    server.listen(PORT, HOST, () => {
      logger.info(`Executive Real-time Service listening on ${HOST}:${PORT}`);
      logger.info(`Environment: ${process.env.NODE_ENV || 'development'}`);
      logger.info(`WebSocket endpoint: ws://${HOST}:${PORT}`);
    });

    // Graceful shutdown
    const shutdown = async (signal) => {
      logger.info(`${signal} received. Starting graceful shutdown...`);

      server.close(() => {
        logger.info('HTTP server closed');
        process.exit(0);
      });

      // Force shutdown after 10 seconds
      setTimeout(() => {
        logger.error('Forced shutdown after timeout');
        process.exit(1);
      }, 10000);
    };

    process.on('SIGTERM', () => shutdown('SIGTERM'));
    process.on('SIGINT', () => shutdown('SIGINT'));

  } catch (error) {
    logger.error('Failed to start server:', error);
    process.exit(1);
  }
}

// Handle uncaught exceptions
process.on('uncaughtException', (error) => {
  logger.error('Uncaught Exception:', error);
  process.exit(1);
});

process.on('unhandledRejection', (reason, promise) => {
  logger.error('Unhandled Rejection at:', promise, 'reason:', reason);
  process.exit(1);
});

startServer();
