/**
 * Executive Command Service
 *
 * Handles all write operations (commands) for executive KPIs and metrics.
 * Part of CQRS architecture with separate Query service.
 *
 * Features:
 * - REST API for KPI and metric CRUD operations
 * - MongoDB for persistence
 * - Redis for caching and event publishing
 * - JWT authentication for tenant isolation
 * - Command validation with Joi
 * - Event publishing for real-time updates
 */

require('dotenv').config();
const { createServer } = require('./app');
const logger = require('./config/logger');
const { connectMongo } = require('./config/mongodb');
const { connectRedis } = require('./config/redis');

const PORT = process.env.PORT || 3004;
const HOST = process.env.HOST || '0.0.0.0';

async function startServer() {
  try {
    // Connect to MongoDB
    await connectMongo();
    logger.info('MongoDB connected successfully');

    // Connect to Redis for caching and pub/sub
    await connectRedis();
    logger.info('Redis connected successfully');

    // Create and start the server
    const server = await createServer();

    server.listen(PORT, HOST, () => {
      logger.info(`Executive Command Service listening on ${HOST}:${PORT}`);
      logger.info(`Environment: ${process.env.NODE_ENV || 'development'}`);
      logger.info(`API endpoint: http://${HOST}:${PORT}/api`);
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
