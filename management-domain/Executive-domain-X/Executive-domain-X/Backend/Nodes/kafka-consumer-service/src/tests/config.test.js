const mongodb = require('../config/mongodb');
const redis = require('../config/redis');
const kafka = require('../config/kafka');
const logger = require('../config/logger');

describe('Configuration Tests', () => {
  beforeAll(async () => {
    // Set test environment variables
    process.env.NODE_ENV = 'test';
    process.env.MONGODB_URI = 'mongodb://localhost:27017/gogidix_executive_test';
    process.env.REDIS_HOST = 'localhost';
    process.env.REDIS_PORT = 6379;
    process.env.KAFKA_BROKERS = 'localhost:9092';
  });

  afterAll(async () => {
    await mongodb.disconnect();
    await redis.disconnect();
  });

  describe('MongoDB Configuration', () => {
    test('should connect to MongoDB', async () => {
      const db = await mongodb.connect();
      expect(db).toBeDefined();
    });

    test('should check MongoDB health', () => {
      expect(mongodb.isHealthy()).toBe(false); // Will be false until connected
    });
  });

  describe('Redis Configuration', () => {
    test('should connect to Redis', async () => {
      await redis.connect();
      expect(redis.isHealthy()).toBe(true);
    });

    test('should perform Redis operations', async () => {
      const client = redis.getClient();
      await client.set('test-key', 'test-value');
      const value = await client.get('test-key');
      expect(value).toBe('test-value');
    });
  });

  describe('Logger Configuration', () => {
    test('should create logger instance', () => {
      expect(logger).toBeDefined();
      expect(typeof logger.info).toBe('function');
      expect(typeof logger.error).toBe('function');
    });

    test('should log messages', () => {
      expect(() => {
        logger.info('Test log message');
        logger.error('Test error message');
      }).not.toThrow();
    });
  });
});