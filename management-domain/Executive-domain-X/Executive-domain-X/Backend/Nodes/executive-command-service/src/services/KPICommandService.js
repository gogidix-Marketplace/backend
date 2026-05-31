/**
 * KPI Command Service
 *
 * Handles all write operations for KPIs
 */

const { ObjectId } = require('mongodb');
const KPI = require('../models/KPI');
const { getCollection } = require('../config/mongodb');
const { publishEvent, invalidatePattern } = require('../config/redis');
const logger = require('../config/logger');

/**
 * Create a new KPI
 */
async function createKPI(tenantId, data) {
  const collection = getCollection(KPI.getCollectionName());

  // Check for duplicate
  const existing = await collection.findOne({
    tenantId,
    name: data.name,
    period: data.period,
  });

  if (existing) {
    throw new Error(`KPI "${data.name}" already exists for period "${data.period}"`);
  }

  // Create KPI
  const kpi = new KPI({
    tenantId,
    ...data,
  });

  // Calculate percent change and status
  if (kpi.previousValue) {
    kpi.calculatePercentChange();
  }
  if (kpi.target) {
    kpi.updateStatus();
  }

  // Save to database
  const result = await collection.insertOne(kpi.toDocument());
  kpi._id = result.insertedId;

  // Publish event
  await publishEvent('kpi:created', {
    kpiId: kpi._id.toString(),
    tenantId,
    name: kpi.name,
    category: kpi.category,
    executiveLevel: kpi.executiveLevel,
  });

  logger.info(`KPI created: ${kpi._id} for tenant ${tenantId}`);

  return kpi;
}

/**
 * Update an existing KPI
 */
async function updateKPI(tenantId, kpiId, data) {
  const collection = getCollection(KPI.getCollectionName());

  // Find existing KPI
  const existing = await collection.findOne({
    _id: new ObjectId(kpiId),
    tenantId,
  });

  if (!existing) {
    throw new Error('KPI not found');
  }

  // Update fields
  const kpi = KPI.fromDocument(existing);
  Object.assign(kpi, data);

  // Recalculate if value or target changed
  if (data.value !== undefined || data.target !== undefined) {
    if (kpi.previousValue || data.previousValue !== undefined) {
      if (data.previousValue !== undefined) {
        kpi.previousValue = data.previousValue;
      }
      kpi.calculatePercentChange();
    }
    if (kpi.target) {
      kpi.updateStatus();
    }
  }

  kpi.updatedAt = new Date();

  // Save to database
  await collection.updateOne(
    { _id: new ObjectId(kpiId) },
    { $set: kpi.toDocument() }
  );

  // Publish event
  await publishEvent('kpi:updated', {
    kpiId: kpi._id.toString(),
    tenantId,
    name: kpi.name,
    value: kpi.value,
    status: kpi.status,
    executiveLevel: kpi.executiveLevel,
    category: kpi.category,
  });

  // Invalidate cache
  await invalidatePattern(`kpi:${tenantId}:*`);

  logger.info(`KPI updated: ${kpiId}`);

  return kpi;
}

/**
 * Delete a KPI
 */
async function deleteKPI(tenantId, kpiId) {
  const collection = getCollection(KPI.getCollectionName());

  const result = await collection.deleteOne({
    _id: new ObjectId(kpiId),
    tenantId,
  });

  if (result.deletedCount === 0) {
    throw new Error('KPI not found');
  }

  // Publish event
  await publishEvent('kpi:deleted', {
    kpiId,
    tenantId,
  });

  // Invalidate cache
  await invalidatePattern(`kpi:${tenantId}:*`);

  logger.info(`KPI deleted: ${kpiId}`);

  return { deleted: true };
}

/**
 * Batch create KPIs
 */
async function batchCreateKPIs(tenantId, kpis) {
  const results = [];
  const errors = [];

  for (const kpiData of kpis) {
    try {
      const kpi = await createKPI(tenantId, kpiData);
      results.push(kpi);
    } catch (error) {
      errors.push({
        name: kpiData.name,
        error: error.message,
      });
    }
  }

  return {
    created: results.length,
    failed: errors.length,
    results,
    errors,
  };
}

/**
 * Recalculate KPI value
 */
async function recalculateKPI(tenantId, kpiId, newValue, metadata = {}) {
  const collection = getCollection(KPI.getCollectionName());

  const existing = await collection.findOne({
    _id: new ObjectId(kpiId),
    tenantId,
  });

  if (!existing) {
    throw new Error('KPI not found');
  }

  const kpi = KPI.fromDocument(existing);

  // Store current value as previous
  kpi.previousValue = kpi.value;
  kpi.value = newValue;

  // Recalculate
  kpi.calculatePercentChange();
  kpi.updateStatus();
  kpi.lastCalculatedAt = new Date();
  kpi.updatedAt = new Date();

  // Add metadata
  Object.entries(metadata).forEach(([key, value]) => {
    kpi.addMetadata(key, value);
  });

  // Save
  await collection.updateOne(
    { _id: new ObjectId(kpiId) },
    { $set: kpi.toDocument() }
  );

  // Publish event
  await publishEvent('kpi:updated', {
    kpiId: kpi._id.toString(),
    tenantId,
    name: kpi.name,
    value: kpi.value,
    previousValue: kpi.previousValue,
    percentChange: kpi.percentChange,
    status: kpi.status,
    executiveLevel: kpi.executiveLevel,
    category: kpi.category,
  });

  return kpi;
}

module.exports = {
  createKPI,
  updateKPI,
  deleteKPI,
  batchCreateKPIs,
  recalculateKPI,
};
