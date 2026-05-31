/**
 * MongoDB Configuration
 * Connection management with proper indexing
 */

const { MongoClient } = require('mongodb');
const logger = require('./logger');

let db = null;
let client = null;
const mongoUrl = process.env.MONGODB_URL || 'mongodb://localhost:27017';
const dbName = process.env.MONGODB_DB || 'management_executive';

/**
 * Initialize MongoDB connection
 */
async function connect() {
  if (db) {
    return db;
  }

  try {
    client = new MongoClient(mongoUrl, {
      maxPoolSize: 10,
      minPoolSize: 2,
      maxIdleTimeMS: 60000,
      serverSelectionTimeoutMS: 5000,
    });

    await client.connect();
    db = client.db(dbName);

    // Create indexes
    await createIndexes(db);

    logger.info('MongoDB connected successfully', { dbName });
    return db;
  } catch (error) {
    logger.error('MongoDB connection failed', { error: error.message });
    throw error;
  }
}

/**
 * Create database indexes
 */
async function createIndexes(database) {
  try {
    const kpiCollection = database.collection('kpi');

    // Create indexes for KPI collection
    await kpiCollection.createIndex({ tenantId: 1 });
    await kpiCollection.createIndex({ tenantId: 1, category: 1 });
    await kpiCollection.createIndex({ tenantId: 1, executiveLevel: 1 });
    await kpiCollection.createIndex({ tenantId: 1, period: 1 });
    await kpiCollection.createIndex({ tenantId: 1, status: 1 });
    await kpiCollection.createIndex({ tenantId: 1, name: 1, period: 1 }, { unique: true });
    await kpiCollection.createIndex({ createdAt: 1 });

    logger.info('MongoDB indexes created successfully');
  } catch (error) {
    logger.warn('Some indexes may already exist', { error: error.message });
  }
}

/**
 * Get database instance
 */
function getDb() {
  if (!db) {
    throw new Error('Database not connected. Call connect() first.');
  }
  return db;
}

/**
 * Get collection
 */
function getCollection(name) {
  return getDb().collection(name);
}

/**
 * Close MongoDB connection
 */
async function close() {
  if (client) {
    await client.close();
    db = null;
    client = null;
    logger.info('MongoDB connection closed');
  }
}

module.exports = {
  connect,
  getDb,
  getCollection,
  close,
};
