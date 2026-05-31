/**
 * MongoDB Multi-Tenant Middleware for Node.js/Express
 *
 * Provides automatic tenant filtering for MongoDB queries
 * based on X-Tenant-ID and X-Tenant-Type headers.
 *
 * Usage:
 * 1. Import this middleware
 * 2. Add to Express app: app.use(tenantMiddleware)
 * 3. Use in routes: req.tenantId, req.tenantType
 * 4. MongoDB queries automatically filter by tenant
 */

const { MongoClient } = require('mongodb');

// ============================================================================
// CONFIGURATION
// ============================================================================

const CONFIG = {
    MONGO_URI: process.env.MONGO_URI || 'mongodb://localhost:27017',
    DB_NAME: process.env.MONGO_DB || 'foundation_db',
    TENANT_HEADER: 'x-tenant-id',
    TENANT_TYPE_HEADER: 'x-tenant-type',
    DEFAULT_TENANT_ID: 'default',
    DEFAULT_TENANT_TYPE: 'ORGANIZATION'
};

let db = null;
let client = null;

// ============================================================================
// DATABASE CONNECTION
// ============================================================================

async function connectToMongo() {
    if (db) return db;

    try {
        client = new MongoClient(CONFIG.MONGO_URI);
        await client.connect();
        db = client.db(CONFIG.DB_NAME);
        console.log(`✅ Connected to MongoDB: ${CONFIG.DB_NAME}`);
        return db;
    } catch (error) {
        console.error('❌ MongoDB connection error:', error);
        throw error;
    }
}

async function disconnectFromMongo() {
    if (client) {
        await client.close();
        db = null;
        client = null;
        console.log('✅ Disconnected from MongoDB');
    }
}

// ============================================================================
// TENANT MIDDLEWARE
// ============================================================================

/**
 * Express middleware to extract tenant context from headers
 * and add it to the request object.
 *
 * Headers:
 * - X-Tenant-ID: The tenant identifier (e.g., "acme-corp")
 * - X-Tenant-Type: The tenant type (e.g., "ORGANIZATION", "PARTNER")
 */
function tenantMiddleware(req, res, next) {
    // Extract tenant headers
    const tenantId = req.headers[CONFIG.TENANT_HEADER] || CONFIG.DEFAULT_TENANT_ID;
    const tenantType = req.headers[CONFIG.TENANT_TYPE_HEADER] || CONFIG.DEFAULT_TENANT_TYPE;

    // Add tenant context to request
    req.tenantId = tenantId;
    req.tenantType = tenantType;
    req.tenant = { id: tenantId, type: tenantType };

    // Add to response locals for views
    res.locals.tenant = req.tenant;

    // Log tenant context (development only)
    if (process.env.NODE_ENV !== 'production') {
        console.log(`🔑 Tenant: ${tenantId} (${tenantType})`);
    }

    next();
}

/**
 * Async wrapper for tenant-aware route handlers
 */
function asyncHandler(fn) {
    return (req, res, next) => {
        Promise.resolve(fn(req, res, next)).catch(next);
    };
}

// ============================================================================
// TENANT-AWARE MONGODB HELPERS
// ============================================================================

/**
 * Add tenant filter to MongoDB query
 * @param {Object} query - The base query
 * @param {string} tenantId - Tenant ID (optional, defaults to req.tenantId)
 * @returns {Object} Query with tenant filter
 */
function addTenantFilter(query = {}, tenantId = null) {
    return {
        ...query,
        tenantId: tenantId || getCurrentTenantId(),
        deleted: false // Soft delete filter
    };
}

/**
 * Get current tenant ID from request context
 * NOTE: This only works within request context. For background jobs,
 * pass tenant explicitly.
 */
function getCurrentTenantId() {
    // In Express request context, use request.tenantId
    // In background jobs, tenant must be passed explicitly
    if (typeof global.currentTenantId === 'string') {
        return global.currentTenantId;
    }
    return CONFIG.DEFAULT_TENANT_ID;
}

/**
 * Set current tenant context for background jobs
 */
function setCurrentTenant(tenantId) {
    global.currentTenantId = tenantId;
}

/**
 * Clear current tenant context
 */
function clearCurrentTenant() {
    delete global.currentTenantId;
}

// ============================================================================
// MONGODB CRUD OPERATIONS WITH TENANT FILTERING
// ============================================================================

/**
 * Find documents with tenant filtering
 * @param {string} collectionName - Name of MongoDB collection
 * @param {Object} query - Query filter
 * @param {Object} options - Query options (sort, limit, etc.)
 * @returns {Promise<Array>} Array of documents
 */
async function findDocuments(collectionName, query = {}, options = {}) {
    const database = await connectToMongo();
    const collection = database.collection(collectionName);

    // Add tenant filter
    const tenantQuery = addTenantFilter(query);

    // Apply options
    let cursor = collection.find(tenantQuery);

    if (options.sort) {
        cursor = cursor.sort(options.sort);
    }
    if (options.limit) {
        cursor = cursor.limit(options.limit);
    }
    if (options.skip) {
        cursor = cursor.skip(options.skip);
    }

    return await cursor.toArray();
}

/**
 * Find one document with tenant filtering
 * @param {string} collectionName - Name of MongoDB collection
 * @param {Object} query - Query filter
 * @returns {Promise<Object|null>} Single document or null
 */
async function findOneDocument(collectionName, query = {}) {
    const database = await connectToMongo();
    const collection = database.collection(collectionName);

    const tenantQuery = addTenantFilter(query);

    return await collection.findOne(tenantQuery);
}

/**
 * Insert document with tenant context
 * @param {string} collectionName - Name of MongoDB collection
 * @param {Object} document - Document to insert
 * @param {string} tenantId - Tenant ID (optional)
 * @returns {Promise<Object>} Inserted document
 */
async function insertDocument(collectionName, document, tenantId = null) {
    const database = await connectToMongo();
    const collection = database.collection(collectionName);

    // Add tenant context and timestamps
    const docWithTenant = {
        ...document,
        tenantId: tenantId || getCurrentTenantId(),
        tenantType: document.tenantType || null, // Preserve if set
        deleted: false,
        createdAt: new Date(),
        updatedAt: new Date()
    };

    const result = await collection.insertOne(docWithTenant);

    return {
        ...docWithTenant,
        _id: result.insertedId
    };
}

/**
 * Update document with tenant filtering
 * @param {string} collectionName - Name of MongoDB collection
 * @param {Object} filter - Filter to find document
 * @param {Object} update - Update operations
 * @param {string} tenantId - Tenant ID (optional)
 * @returns {Promise<Object>} Update result
 */
async function updateDocument(collectionName, filter, update, tenantId = null) {
    const database = await connectToMongo();
    const collection = database.collection(collectionName);

    // Add tenant filter
    const tenantFilter = addTenantFilter(filter, tenantId);

    // Add updated timestamp
    const updateWithTimestamp = {
        ...update,
        $set: {
            ...(update.$set || {}),
            updatedAt: new Date()
        }
    };

    return await collection.updateOne(tenantFilter, updateWithTimestamp);
}

/**
 * Delete document (soft delete) with tenant filtering
 * @param {string} collectionName - Name of MongoDB collection
 * @param {Object} filter - Filter to find document
 * @param {string} tenantId - Tenant ID (optional)
 * @returns {Promise<Object>} Update result
 */
async function softDeleteDocument(collectionName, filter, tenantId = null) {
    return await updateDocument(
        collectionName,
        filter,
        { $set: { deleted: true } },
        tenantId
    );
}

/**
 * Count documents with tenant filtering
 * @param {string} collectionName - Name of MongoDB collection
 * @param {Object} query - Query filter
 * @returns {Promise<number>} Document count
 */
async function countDocuments(collectionName, query = {}) {
    const database = await connectToMongo();
    const collection = database.collection(collectionName);

    const tenantQuery = addTenantFilter(query);

    return await collection.countDocuments(tenantQuery);
}

// ============================================================================
// INDEX MANAGEMENT
// ============================================================================

/**
 * Create tenant_id index for a collection
 * @param {string} collectionName - Name of MongoDB collection
 */
async function ensureTenantIndex(collectionName) {
    const database = await connectToMongo();
    const collection = database.collection(collectionName);

    await collection.createIndex(
        { tenantId: 1, tenantType: 1 },
        { name: 'idx_tenant', background: true }
    );

    await collection.createIndex(
        { tenantId: 1, deleted: 1 },
        { name: 'idx_tenant_deleted', background: true }
    );

    console.log(`✅ Created tenant indexes for: ${collectionName}`);
}

/**
 * Create all necessary indexes for a collection
 * @param {string} collectionName - Name of MongoDB collection
 * @param {Array} indexes - Array of index specifications
 */
async function ensureIndexes(collectionName, indexes = []) {
    const database = await connectToMongo();
    const collection = database.collection(collectionName);

    // Always ensure tenant indexes
    await ensureTenantIndex(collectionName);

    // Create custom indexes
    for (const index of indexes) {
        await collection.createIndex(index.keys, {
            name: index.name,
            background: true,
            ...index.options
        });
    }

    console.log(`✅ Created ${indexes.length + 2} indexes for: ${collectionName}`);
}

// ============================================================================
// EXPORTS
// ============================================================================

module.exports = {
    // Configuration
    CONFIG,

    // Database connection
    connectToMongo,
    disconnectFromMongo,

    // Middleware
    tenantMiddleware,
    asyncHandler,

    // Tenant context
    getCurrentTenantId,
    setCurrentTenant,
    clearCurrentTenant,

    // CRUD operations
    findDocuments,
    findOneDocument,
    insertDocument,
    updateDocument,
    softDeleteDocument,
    countDocuments,

    // Index management
    ensureTenantIndex,
    ensureIndexes
};
