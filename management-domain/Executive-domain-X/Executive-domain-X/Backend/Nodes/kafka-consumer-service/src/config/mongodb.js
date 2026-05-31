const { MongoClient } = require('mongodb');
const logger = require('./logger');

class MongoDB {
  constructor() {
    this.client = new MongoClient(process.env.MONGODB_URI || 'mongodb://localhost:27017');
    this.dbName = process.env.DB_NAME || 'gogidix_executive';
    this.isConnected = false;
  }

  async connect() {
    try {
      await this.client.connect();
      this.isConnected = true;
      this.db = this.client.db(this.dbName);
      logger.info(`Connected to MongoDB database: ${this.dbName}`);
      return this.db;
    } catch (error) {
      logger.error('MongoDB connection error:', error);
      throw error;
    }
  }

  async disconnect() {
    try {
      if (this.isConnected) {
        await this.client.close();
        this.isConnected = false;
        logger.info('Disconnected from MongoDB');
      }
    } catch (error) {
      logger.error('MongoDB disconnection error:', error);
      throw error;
    }
  }

  getDb() {
    if (!this.isConnected) {
      throw new Error('MongoDB not connected');
    }
    return this.db;
  }

  isHealthy() {
    return this.isConnected;
  }
}

module.exports = new MongoDB();