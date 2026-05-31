/**
 * Social Automation Service
 * Main entry point for the social media automation service
 */

require('dotenv').config();
const express = require('express');
const helmet = require('helmet');
const cors = require('cors');
const { connectDatabase } = require('./config/database');
const { connectRedis } = require('./config/redis');
const logger = require('./utils/logger');
const { errorHandler } = require('./middleware/errorHandler');
const { authenticateApiKey } = require('./middleware/auth');
const socialRoutes = require('./routes/socialRoutes');
const webhookRoutes = require('./routes/webhookRoutes');
const healthRoutes = require('./routes/healthRoutes');
const { startPublisherWorker } = require('./workers/publisherWorker');
const { initPostQueue } = require('./services/postQueue');
const { startScheduler } = require('./services/scheduler');

const app = express();
const PORT = process.env.PORT || 3002;
const HOST = process.env.HOST || '0.0.0.0';

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true, limit: '10mb' }));

// Request logging middleware
app.use((req, res, next) => {
  logger.info({
    method: req.method,
    path: req.path,
    ip: req.ip,
    userAgent: req.get('user-agent')
  });
  next();
});

// Health check routes (no auth required)
app.use('/health', healthRoutes);

// API routes (with authentication)
app.use('/api/v1/social', authenticateApiKey, socialRoutes);
app.use('/api/v1/posts', authenticateApiKey, socialRoutes);
app.use('/api/v1/accounts', authenticateApiKey, socialRoutes);

// Webhook routes (no auth, verified via signature)
app.use('/webhook', webhookRoutes);

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    service: 'Social Automation Service',
    version: '1.0.0',
    status: 'running',
    supportedPlatforms: ['twitter', 'linkedin', 'facebook', 'instagram'],
    endpoints: {
      health: '/health',
      posts: '/api/v1/posts',
      accounts: '/api/v1/accounts',
      webhooks: '/webhook'
    }
  });
});

// 404 handler
app.use((req, res) => {
  res.status(404).json({
    error: 'Not Found',
    path: req.path,
    message: 'The requested resource was not found'
  });
});

// Error handling middleware
app.use(errorHandler);

// Graceful shutdown handler
const gracefulShutdown = async (signal) => {
  logger.info(`Received ${signal}, starting graceful shutdown...`);

  server.close(async () => {
    logger.info('HTTP server closed');

    try {
      // Close database connection
      const { disconnectDatabase } = require('./config/database');
      await disconnectDatabase();
      logger.info('Database connection closed');

      // Close Redis connection
      const { disconnectRedis } = require('./config/redis');
      await disconnectRedis();
      logger.info('Redis connection closed');

      // Close queue
      const { closePostQueue } = require('./services/postQueue');
      await closePostQueue();
      logger.info('Post queue closed');

      // Stop scheduler
      const { stopScheduler } = require('./services/scheduler');
      await stopScheduler();
      logger.info('Scheduler stopped');

      process.exit(0);
    } catch (error) {
      logger.error('Error during shutdown:', error);
      process.exit(1);
    }
  });

  // Force shutdown after 30 seconds
  setTimeout(() => {
    logger.error('Forced shutdown after timeout');
    process.exit(1);
  }, 30000);
};

// Initialize and start server
const startServer = async () => {
  try {
    // Connect to database
    await connectDatabase();
    logger.info('Database connected successfully');

    // Connect to Redis
    await connectRedis();
    logger.info('Redis connected successfully');

    // Initialize post queue
    await initPostQueue();
    logger.info('Post queue initialized');

    // Start publisher worker
    await startPublisherWorker();
    logger.info('Publisher worker started');

    // Start scheduler
    if (process.env.ENABLE_SCHEDULING !== 'false') {
      await startScheduler();
      logger.info('Post scheduler started');
    }

    // Start HTTP server
    const server = app.listen(PORT, HOST, () => {
      logger.info(`Social Automation Service listening on http://${HOST}:${PORT}`);
      logger.info(`Environment: ${process.env.NODE_ENV || 'development'}`);
    });

    // Handle shutdown signals
    process.on('SIGTERM', () => gracefulShutdown('SIGTERM'));
    process.on('SIGINT', () => gracefulShutdown('SIGINT'));

    // Handle uncaught exceptions
    process.on('uncaughtException', (error) => {
      logger.error('Uncaught Exception:', error);
      gracefulShutdown('uncaughtException');
    });

    // Handle unhandled promise rejections
    process.on('unhandledRejection', (reason, promise) => {
      logger.error('Unhandled Rejection at:', promise, 'reason:', reason);
      gracefulShutdown('unhandledRejection');
    });

  } catch (error) {
    logger.error('Failed to start server:', error);
    process.exit(1);
  }
};

// Start the server
startServer();

module.exports = app;
