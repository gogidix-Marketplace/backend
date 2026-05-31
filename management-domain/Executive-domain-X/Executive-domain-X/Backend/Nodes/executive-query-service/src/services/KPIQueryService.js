/**
 * KPI Query Service
 * Handles all read operations for KPIs with caching
 */

const { ObjectId } = require('mongodb');
const { getCollection } = require('../config/mongodb');
const { get, set, del, kpiKey, kpiListKey, invalidateTenant } = require('../config/redis');
const logger = require('../config/logger');

/**
 * Get KPI by ID with caching
 */
async function getKPIById(tenantId, kpiId) {
  try {
    // Try cache first
    const cacheKey = kpiKey(tenantId, kpiId);
    const cached = await get(cacheKey);
    if (cached) {
      logger.debug('KPI cache hit', { kpiId });
      return cached;
    }

    // Query database
    const collection = getCollection('kpi');
    const kpi = await collection.findOne({
      _id: new ObjectId(kpiId),
      tenantId,
    });

    if (!kpi) {
      throw new Error('KPI not found');
    }

    // Cache the result
    await set(cacheKey, kpi);

    return kpi;
  } catch (error) {
    logger.error('Failed to get KPI', { kpiId, error: error.message });
    throw error;
  }
}

/**
 * Get KPIs with pagination and filtering
 */
async function getKPIs(tenantId, options = {}) {
  try {
    const {
      page = 1,
      limit = 20,
      category,
      executiveLevel,
      status,
      period,
      sortBy = 'createdAt',
      sortOrder = 'desc',
    } = options;

    const skip = (page - 1) * limit;

    // Build query
    const query = { tenantId };

    if (category) {
      query.category = category;
    }
    if (executiveLevel) {
      query.executiveLevel = { $in: [executiveLevel, 'ALL'] };
    }
    if (status) {
      query.status = status;
    }
    if (period) {
      query.period = period;
    }

    // Build sort
    const sort = {};
    sort[sortBy] = sortOrder === 'asc' ? 1 : -1;

    // Check cache for list
    const cacheKey = kpiListKey(tenantId, options);
    const cached = await get(cacheKey);
    if (cached) {
      logger.debug('KPI list cache hit', { tenantId });
      return cached;
    }

    // Query database
    const collection = getCollection('kpi');
    const [kpis, total] = await Promise.all([
      collection.find(query).sort(sort).skip(skip).limit(limit).toArray(),
      collection.countDocuments(query),
    ]);

    const result = {
      data: kpis,
      pagination: {
        page,
        limit,
        total,
        pages: Math.ceil(total / limit),
      },
    };

    // Cache the result
    await set(cacheKey, result);

    return result;
  } catch (error) {
    logger.error('Failed to get KPIs', { tenantId, error: error.message });
    throw error;
  }
}

/**
 * Get KPI summary by category
 */
async function getKPISummary(tenantId) {
  try {
    const cacheKey = `kpi:${tenantId}:summary`;
    const cached = await get(cacheKey);
    if (cached) {
      return cached;
    }

    const collection = getCollection('kpi');

    const pipeline = [
      { $match: { tenantId } },
      {
        $group: {
          _id: '$category',
          total: { $sum: 1 },
          ahead: {
            $sum: { $cond: [{ $eq: ['$status', 'AHEAD'] }, 1, 0] },
          },
          onTrack: {
            $sum: { $cond: [{ $eq: ['$status', 'ON_TRACK'] }, 1, 0] },
          },
          atRisk: {
            $sum: { $cond: [{ $eq: ['$status', 'AT_RISK'] }, 1, 0] },
          },
          behind: {
            $sum: { $cond: [{ $eq: ['$status', 'BEHIND'] }, 1, 0] },
          },
        },
      },
    ];

    const results = await collection.aggregate(pipeline).toArray();

    const summary = {};
    for (const result of results) {
      summary[result._id] = {
        total: result.total,
        ahead: result.ahead,
        onTrack: result.onTrack,
        atRisk: result.atRisk,
        behind: result.behind,
      };
    }

    await set(cacheKey, summary);

    return summary;
  } catch (error) {
    logger.error('Failed to get KPI summary', { tenantId, error: error.message });
    throw error;
  }
}

/**
 * Get dashboard KPIs for executive level
 */
async function getDashboardKPIs(tenantId, level = 'CEO') {
  try {
    const cacheKey = `kpi:${tenantId}:dashboard:${level}`;
    const cached = await get(cacheKey);
    if (cached) {
      return cached;
    }

    const collection = getCollection('kpi');

    // For CEO, get all KPIs; for others, filter by level
    const query = level === 'CEO'
      ? { tenantId }
      : {
          $or: [
            { tenantId, executiveLevel: level },
            { tenantId, executiveLevel: 'ALL' },
          ],
        };

    const kpis = await collection.find(query).toArray();

    // Calculate summary
    const summary = {
      total: kpis.length,
      ahead: 0,
      onTrack: 0,
      atRisk: 0,
      behind: 0,
    };

    for (const kpi of kpis) {
      summary[kpi.status.toLowerCase()]++;
    }

    const result = {
      kpi: kpis,
      summary,
    };

    await set(cacheKey, result);

    return result;
  } catch (error) {
    logger.error('Failed to get dashboard KPIs', { tenantId, level, error: error.message });
    throw error;
  }
}

/**
 * Get KPI trends over time
 */
async function getKPITrends(tenantId, kpiId, periods = 12) {
  try {
    const cacheKey = `kpi:${tenantId}:trends:${kpiId}:${periods}`;
    const cached = await get(cacheKey);
    if (cached) {
      return cached;
    }

    const collection = getCollection('kpi');

    // Get KPI to determine name and category
    const kpi = await collection.findOne({
      _id: new ObjectId(kpiId),
      tenantId,
    });

    if (!kpi) {
      throw new Error('KPI not found');
    }

    // Get historical data for same KPI name
    const historical = await collection.find({
      tenantId,
      name: kpi.name,
      period: { $ne: kpi.period },
    })
      .sort({ period: -1 })
      .limit(periods)
      .toArray();

    const trends = {
      current: kpi,
      historical: historical.reverse(),
      trend: calculateTrend([kpi, ...historical]),
    };

    await set(cacheKey, trends);

    return trends;
  } catch (error) {
    logger.error('Failed to get KPI trends', { tenantId, kpiId, error: error.message });
    throw error;
  }
}

/**
 * Calculate trend direction
 */
function calculateTrend(dataPoints) {
  if (dataPoints.length < 2) {
    return 'STABLE';
  }

  const sorted = [...dataPoints].sort((a, b) => a.period.localeCompare(b.period));
  let upCount = 0;
  let downCount = 0;

  for (let i = 1; i < sorted.length; i++) {
    if (sorted[i].value > sorted[i - 1].value) upCount++;
    else if (sorted[i].value < sorted[i - 1].value) downCount++;
  }

  if (upCount > downCount * 1.5) return 'UP';
  if (downCount > upCount * 1.5) return 'DOWN';
  return 'STABLE';
}

/**
 * Search KPIs by name
 */
async function searchKPIs(tenantId, searchTerm, options = {}) {
  try {
    const { limit = 10 } = options;

    const collection = getCollection('kpi');
    const kpis = await collection.find({
      tenantId,
      name: { $regex: searchTerm, $options: 'i' },
    })
      .limit(limit)
      .toArray();

    return kpis;
  } catch (error) {
    logger.error('Failed to search KPIs', { tenantId, searchTerm, error: error.message });
    throw error;
  }
}

/**
 * Invalidate cache for KPI
 */
async function invalidateKPI(tenantId, kpiId) {
  await del(kpiKey(tenantId, kpiId));
  await invalidateTenant(tenantId);
}

/**
 * Invalidate all cache for tenant
 */
async function invalidateAll(tenantId) {
  await invalidateTenant(tenantId);
}

module.exports = {
  getKPIById,
  getKPIs,
  getKPISummary,
  getDashboardKPIs,
  getKPITrends,
  searchKPIs,
  invalidateKPI,
  invalidateAll,
};
