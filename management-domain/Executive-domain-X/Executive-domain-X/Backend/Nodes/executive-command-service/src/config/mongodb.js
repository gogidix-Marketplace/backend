/**
 * MongoDB Configuration
 */

const { MongoClient } = require('mongodb');
const logger = require('./logger');

let client = null;
let db = null;

/**
 * Connect to MongoDB
 */
async function connectMongo() {
  const mongoUrl = process.env.MONGODB_URL || 'mongodb://localhost:27017';
  const dbName = process.env.MONGODB_DB || 'management_executive';

  client = new MongoClient(mongoUrl, {
    maxPoolSize: 10,
    minPoolSize: 2,
    maxIdleTimeMS: 60000,
    serverSelectionTimeoutMS: 5000,
  });

  try {
    await client.connect();
    db = client.db(dbName);

    // Create indexes
    await createIndexes();

    logger.info(`Connected to MongoDB: ${mongoUrl}/${dbName}`);
    return { client, db };
  } catch (error) {
    logger.error('MongoDB connection failed:', error);
    throw error;
  }
}

/**
 * Create indexes for collections
 */
async function createIndexes() {
  try {
    const kpiCollection = db.collection('kpi_metrics');
    const indexes = [
      { tenantId: 1, category: 1, period: -1 },
      { tenantId: 1, executiveLevel: 1, category: 1, period: -1 },
      { tenantId: 1, period: -1 },
      { tenantId: 1, name: 1 },
    ];

    for (const index of indexes) {
      await kpiCollection.createIndex(index, { background: true });
    }

    logger.info('MongoDB indexes created');
  } catch (error) {
    logger.error('Failed to create indexes:', error);
  }
}

/**
 * Get database instance
 */
function getDb() {
  if (!db) {
    throw new Error('MongoDB not connected. Call connectMongo() first.');
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
 * Get MongoDB status
 */
async function getMongoStatus() {
  if (!client || !db) {
    return { status: 'disconnected' };
  }

  try {
    await db.admin().ping();
    return {
      status: 'connected',
      database: db.databaseName,
      host: client.options.sockets?.[0]?.host || 'unknown',
    };
  } catch (error) {
    return { status: 'error', error: error.message };
  }
}

/**
 * Disconnect from MongoDB
 */
async function disconnectMongo() {
  if (client) {
    await client.close();
    client = null;
    db = null;
    logger.info('MongoDB disconnected');
  }
}

module.exports = {
  connectMongo,
  getDb,
  getCollection,
  getMongoStatus,
  disconnectMongo,
};
