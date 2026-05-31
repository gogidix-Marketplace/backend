require('dotenv').config();
const express = require('express');
const helmet = require('helmet');
const cors = require('cors');
const compression = require('compression');
const logger = require('./config/logger');
const mongodb = require('./config/mongodb');
const redis = require('./config/redis');
const kafka = require('./config/kafka');
const eventProcessor = require('./services/EventProcessor');
const errorHandler = require('./middleware/errorHandler');
const requestLogger = require('./middleware/requestLogger');

const app = express();
const PORT = process.env.PORT || 3003;

// Middleware
app.use(helmet());
app.use(cors({
  origin: process.env.CORS_ORIGIN || '*',
  methods: ['GET', 'POST', 'PUT', 'DELETE'],
  allowedHeaders: ['Content-Type', 'Authorization']
}));
app.use(compression());
app.use(express.json({ limit: '10mb' }));
app.use(express.urlencoded({ extended: true }));
app.use(requestLogger);

// Routes
app.use('/api', require('./routes'));

// Error handling
app.use(errorHandler);

// Graceful shutdown
process.on('SIGTERM', async () => {
  logger.info('SIGTERM received, shutting down gracefully...');
  await shutdown();
});

process.on('SIGINT', async () => {
  logger.info('SIGINT received, shutting down gracefully...');
  await shutdown();
});

// Unhandled promise rejection
process.on('unhandledRejection', (reason, promise) => {
  logger.error('Unhandled Rejection at:', promise, 'reason:', reason);
});

// Uncaught exception
process.on('uncaughtException', (error) => {
  logger.error('Uncaught Exception:', error);
  process.exit(1);
});

async function startKafkaConsumer() {
  try {
    await kafka.connect();
    const consumer = kafka.getConsumer();

    // Subscribe to executive domain events topic
    await consumer.subscribe({
      topic: 'executive-domain-events',
      fromBeginning: true
    });

    logger.info('Kafka consumer subscribed to executive-domain-events');

    // Run consumer
    await consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        try {
          const event = {
            key: message.key.toString(),
            value: JSON.parse(message.value.toString())
          };

          await eventProcessor.handleEvent(event);
        } catch (error) {
          logger.error('Error processing Kafka message:', error);
          // Message will be retried according to Kafka configuration
        }
      }
    });
  } catch (error) {
    logger.error('Failed to start Kafka consumer:', error);
    throw error;
  }
}

async function startServer() {
  try {
    // Connect to databases
    await mongodb.connect();
    await redis.connect();

    // Start Kafka consumer
    await startKafkaConsumer();

    // Start HTTP server
    const server = app.listen(PORT, () => {
      logger.info(`Kafka Consumer Service running on port ${PORT}`);
    });

    server.on('error', (error) => {
      logger.error('Server error:', error);
    });

    return server;
  } catch (error) {
    logger.error('Failed to start server:', error);
    process.exit(1);
  }
}

async function shutdown() {
  try {
    logger.info('Shutting down services...');
    await kafka.disconnect();
    await redis.disconnect();
    await mongodb.disconnect();
    process.exit(0);
  } catch (error) {
    logger.error('Error during shutdown:', error);
    process.exit(1);
  }
}

// Start the server
startServer().catch(error => {
  logger.error('Failed to start Kafka Consumer Service:', error);
  process.exit(1);
});

module.exports = app;