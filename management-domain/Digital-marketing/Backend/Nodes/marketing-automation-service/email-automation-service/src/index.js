/**
 * Email Automation Service
 * Main entry point for the email automation service
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
const emailRoutes = require('./routes/emailRoutes');
const webhookRoutes = require('./routes/webhookRoutes');
const healthRoutes = require('./routes/healthRoutes');
const { startEmailWorker } = require('./workers/emailWorker');
const { initEmailQueue } = require('./services/emailQueue');

const app = express();
const PORT = process.env.PORT || 3001;
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
app.use('/api/v1/emails', authenticateApiKey, emailRoutes);
app.use('/api/v1/templates', authenticateApiKey, emailRoutes);
app.use('/api/v1/queue', authenticateApiKey, emailRoutes);

// Webhook routes (no auth, verified via signature)
app.use('/webhook', webhookRoutes);

// Root endpoint
app.get('/', (req, res) => {
  res.json({
    service: 'Email Automation Service',
    version: '1.0.0',
    status: 'running',
    endpoints: {
      health: '/health',
      emails: '/api/v1/emails',
      templates: '/api/v1/templates',
      queue: '/api/v1/queue',
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

  // Stop accepting new connections
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
      const { closeEmailQueue } = require('./services/emailQueue');
      await closeEmailQueue();
      logger.info('Email queue closed');

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

    // Initialize email queue
    await initEmailQueue();
    logger.info('Email queue initialized');

    // Start email worker
    await startEmailWorker();
    logger.info('Email worker started');

    // Start HTTP server
    const server = app.listen(PORT, HOST, () => {
      logger.info(`Email Automation Service listening on http://${HOST}:${PORT}`);
      logger.info(`Environment: ${process.env.NODE_ENV || 'development'}`);
      logger.info(`Email Provider: ${process.env.EMAIL_PROVIDER || 'sendgrid'}`);
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
