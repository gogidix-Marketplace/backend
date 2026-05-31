/**
 * Database connection configuration
 */

import mongoose from 'mongoose';
import { redisClient } from '../utils/redis';
import { logger } from '../utils/logger';
import config from './index';

export const connectDatabase = async (): Promise<void> => {
  try {
    const options = {
      maxPoolSize: 10,
      minPoolSize: 5,
      socketTimeoutMS: 45000,
      serverSelectionTimeoutMS: 10000,
      heartbeatFrequencyMS: 10000,
      retryWrites: true,
      retryReads: true,
    };

    await mongoose.connect(config.mongoUri, options);
    logger.info('MongoDB connected successfully');

    mongoose.connection.on('error', (error) => {
      logger.error('MongoDB connection error:', error);
    });

    mongoose.connection.on('disconnected', () => {
      logger.warn('MongoDB disconnected');
    });

    mongoose.connection.on('reconnected', () => {
      logger.info('MongoDB reconnected');
    });
  } catch (error) {
    logger.error('Failed to connect to MongoDB:', error);
    throw error;
  }
};

export const connectRedis = async (): Promise<void> => {
  try {
    await redisClient.connect();
    logger.info('Redis connected successfully');
  } catch (error) {
    logger.error('Failed to connect to Redis:', error);
    throw error;
  }
};

export const disconnectDatabase = async (): Promise<void> => {
  try {
    await mongoose.disconnect();
    await redisClient.quit();
    logger.info('Database connections closed');
  } catch (error) {
    logger.error('Error closing database connections:', error);
  }
};

export const healthCheck = async (): Promise<{
  mongodb: string;
  redis: string;
}> => {
  const mongoState = mongoose.connection.readyState;
  const mongoStatus =
    mongoState === 1
      ? 'connected'
      : mongoState === 2
      ? 'connecting'
      : mongoState === 3
      ? 'disconnecting'
      : 'disconnected';

  let redisStatus = 'disconnected';
  try {
    await redisClient.ping();
    redisStatus = 'connected';
  } catch {
    redisStatus = 'disconnected';
  }

  return {
    mongodb: mongoStatus,
    redis: redisStatus,
  };
};
