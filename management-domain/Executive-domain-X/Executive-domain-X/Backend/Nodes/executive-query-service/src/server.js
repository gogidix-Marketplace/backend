/**
 * Executive Query Service Server
 * Entry point for the Query side of CQRS
 */

require('dotenv').config();
const express = require('express');
const helmet = require('helmet');
const cors = require('cors');
const compression = require('compression');

const logger = require('./config/logger');
const { connect: connectMongo } = require('./config/mongodb');
const { connect: connectRedis } = require('./config/redis');
const { setupRoutes } = require('./routes');

const app = express();
const PORT = process.env.PORT || 3005;
const HOST = process.env.HOST || '0.0.0.0';

/**
 * Initialize connections
 */
async function initialize() {
  try {
    // Connect to MongoDB
    await connectMongo();
    logger.info('MongoDB connected');

    // Connect to Redis
    await connectRedis();
    logger.info('Redis connected');

    // Setup routes
    await setupRoutes(app);
    logger.info('Routes configured');
  } catch (error) {
    logger.error('Initialization failed', { error: error.message });
    process.exit(1);
  }
}

/**
 * Middleware setup
 */
function setupMiddleware() {
  // Security headers
  app.use(helmet());

  // CORS
  const corsOrigins = process.env.CORS_ORIGINS
    ? process.env.CORS_ORIGINS.split(',')
    : ['http://localhost:3000'];
  app.use(cors({
    origin: corsOrigins,
    credentials: true,
  }));

  // Compression
  app.use(compression());

  // Body parsing
  app.use(express.json({ limit: '1mb' }));
  app.use(express.urlencoded({ extended: true, limit: '1mb' }));

  // Request logging
  app.use((req, res, next) => {
    logger.debug('Incoming request', {
      method: req.method,
      path: req.path,
      ip: req.ip,
    });
    next();
  });
}

/**
 * Start server
 */
async function start() {
  await initialize();
  setupMiddleware();

  const server = app.listen(PORT, HOST, () => {
    logger.info(`Executive Query Service listening on http://${HOST}:${PORT}`);
    logger.info(`Environment: ${process.env.NODE_ENV || 'development'}`);
  });

  // Graceful shutdown
  setupGracefulShutdown(server);
}

/**
 * Graceful shutdown handler
 */
function setupGracefulShutdown(server) {
  const shutdown = async (signal) => {
    logger.info(`${signal} received, shutting down gracefully...`);

    server.close(async () => {
      try {
        const { close: closeMongo } = require('./config/mongodb');
        const { close: closeRedis } = require('./config/redis');

        await closeMongo();
        await closeRedis();

        logger.info('Connections closed');
        process.exit(0);
      } catch (error) {
        logger.error('Error during shutdown', { error: error.message });
        process.exit(1);
      }
    });

    // Force shutdown after 10 seconds
    setTimeout(() => {
      logger.error('Forced shutdown after timeout');
      process.exit(1);
    }, 10000);
  };

  process.on('SIGTERM', () => shutdown('SIGTERM'));
  process.on('SIGINT', () => shutdown('SIGINT'));
}

// Start the server
start().catch((error) => {
  logger.error('Failed to start server', { error: error.message });
  process.exit(1);
});

module.exports = app;
