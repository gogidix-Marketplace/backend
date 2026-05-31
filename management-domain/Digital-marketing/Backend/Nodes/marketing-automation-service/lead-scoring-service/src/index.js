/**
 * Lead Scoring Service
 * Main entry point for the lead scoring service
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
const scoringRoutes = require('./routes/scoringRoutes');
const eventRoutes = require('./routes/eventRoutes');
const healthRoutes = require('./routes/healthRoutes');
const { startEventProcessor } = require('./events/eventHandlers');

const app = express();
const PORT = process.env.PORT || 3003;
const HOST = process.env.HOST || '0.0.0.0';

// Middleware
app.use(helmet());
app.use(cors());
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true }));

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
app.use('/api/v1/scoring', authenticateApiKey, scoringRoutes);
app.use('/api/v1/rules', authenticateApiKey, scoringRoutes);
app.use('/api/v1/segments', authenticateApiKey, scoringRoutes);
app.use('/api/v1/events', eventRoutes);

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    service: 'Lead Scoring Service',
    version: '1.0.0',
    status: 'running',
    endpoints: {
      health: '/health',
      scoring: '/api/v1/scoring',
      rules: '/api/v1/rules',
      segments: '/api/v1/segments',
      events: '/api/v1/events'
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

      process.exit(0);
    } catch (error) {
      logger.error('Error during shutdown:', error);
      process.exit(1);
    }
  });

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

    // Start event processor
    await startEventProcessor();
    logger.info('Event processor started');

    // Start HTTP server
    const server = app.listen(PORT, HOST, () => {
      logger.info(`Lead Scoring Service listening on http://${HOST}:${PORT}`);
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
