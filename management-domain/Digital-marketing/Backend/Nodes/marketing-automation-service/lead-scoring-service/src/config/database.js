/**
 * Database Configuration
 * MongoDB connection management using Mongoose
 */

const mongoose = require('mongoose');
const logger = require('../utils/logger');

let isConnected = false;

/**
 * Connect to MongoDB database
 */
const connectDatabase = async () => {
  if (isConnected) {
    logger.info('Database already connected');
    return;
  }

  const mongoUri = process.env.MONGODB_URI || 'mongodb://localhost:27017/lead_scoring';

  try {
    const options = {
      maxPoolSize: 10,
      minPoolSize: 2,
      serverSelectionTimeoutMS: 5000,
      socketTimeoutMS: 45000,
      family: 4
    };

    await mongoose.connect(mongoUri, options);

    isConnected = true;
    logger.info(`MongoDB connected: ${mongoUri}`);

    // Handle connection events
    mongoose.connection.on('error', (error) => {
      logger.error('MongoDB connection error:', error);
      isConnected = false;
    });

    mongoose.connection.on('disconnected', () => {
      logger.warn('MongoDB disconnected');
      isConnected = false;
    });

    mongoose.connection.on('reconnected', () => {
      logger.info('MongoDB reconnected');
      isConnected = true;
    });

  } catch (error) {
    logger.error('Failed to connect to MongoDB:', error);
    throw error;
  }
};

/**
 * Disconnect from MongoDB
 */
const disconnectDatabase = async () => {
  try {
    await mongoose.disconnect();
    isConnected = false;
    logger.info('MongoDB disconnected successfully');
  } catch (error) {
    logger.error('Error disconnecting from MongoDB:', error);
    throw error;
  }
};

/**
 * Get connection status
 */
const getConnectionStatus = () => ({
  isConnected,
  readyState: mongoose.connection.readyState,
  host: mongoose.connection.host,
  name: mongoose.connection.name
});

module.exports = {
  connectDatabase,
  disconnectDatabase,
  getConnectionStatus
};
