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

    this.pubClient = new Redis({
      host: process.env.REDIS_HOST || 'localhost',
      port: process.env.REDIS_PORT || 6379,
      password: process.env.REDIS_PASSWORD || null,
      db: process.env.REDIS_DB || 1,
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

    this.pubClient.on('connect', () => {
      logger.info('Connected to Redis pub client');
    });

    this.pubClient.on('error', (error) => {
      logger.error('Redis pub connection error:', error);
    });
  }

  async connect() {
    if (this.isConnected) {
      return { client: this.client, pubClient: this.pubClient };
    }

    try {
      await this.client.connect();
      await this.pubClient.connect();
      this.isConnected = true;
      return { client: this.client, pubClient: this.pubClient };
    } catch (error) {
      logger.error('Redis connection failed:', error);
      throw error;
    }
  }

  async disconnect() {
    if (this.isConnected) {
      await this.client.quit();
      await this.pubClient.quit();
      this.isConnected = false;
      logger.info('Disconnected from Redis');
    }
  }

  getClient() {
    return this.client;
  }

  getPubClient() {
    return this.pubClient;
  }

  isHealthy() {
    return this.isConnected;
  }
}

module.exports = new RedisClient();