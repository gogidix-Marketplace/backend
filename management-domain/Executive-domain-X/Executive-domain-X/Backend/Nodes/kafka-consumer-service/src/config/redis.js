const Redis = require('ioredis');
const logger = require('./logger');

class RedisClient {
  constructor() {
    this.client = new Redis({
      host: process.env.REDIS_HOST || 'localhost',
      port: process.env.REDIS_PORT || 6379,
      password: process.env.REDIS_PASSWORD || null,
      db: process.env.REDIS_DB || 0,
      retryDelayOnFailover: 100,
      enableReadyCheck: false,
      maxRetriesPerRequest: 3
    });

    this.isConnected = false;
    this.setupEventListeners();
  }

  setupEventListeners() {
    this.client.on('connect', () => {
      this.isConnected = true;
      logger.info('Connected to Redis');
    });

    this.client.on('error', (error) => {
      logger.error('Redis connection error:', error);
      this.isConnected = false;
    });

    this.client.on('close', () => {
      this.isConnected = false;
      logger.warn('Redis connection closed');
    });
  }

  async connect() {
    if (this.isConnected) {
      return this.client;
    }

    try {
      await this.client.connect();
      return this.client;
    } catch (error) {
      logger.error('Redis connection failed:', error);
      throw error;
    }
  }

  async disconnect() {
    if (this.isConnected) {
      await this.client.quit();
      this.isConnected = false;
      logger.info('Disconnected from Redis');
    }
  }

  getClient() {
    return this.client;
  }

  isHealthy() {
    return this.isConnected;
  }
}

module.exports = new RedisClient();