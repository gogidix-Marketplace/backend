/**
 * Segment Service
 * Lead segment management and assignment
 */

const Segment = require('../models/Segment');
const LeadScore = require('../models/LeadScore');
const { bulkAssignSegments, assignSegment } = require('./scoringEngine');
const logger = require('../utils/logger');
const { v4: uuidv4 } = require('uuid');

/**
 * Create segment
 */
const createSegment = async (segmentData) => {
  try {
    const segmentId = segmentData.segmentId || uuidv4();

    const segment = new Segment({
      segmentId,
      ...segmentData
    });

    await segment.save();

    logger.info('Segment created', { segmentId, tenantId: segment.tenantId });

    return segment;

  } catch (error) {
    logger.error('Error creating segment:', error);
    throw error;
  }
};

/**
 * Get segment by ID
 */
const getSegment = async (segmentId, tenantId) => {
  return Segment.getBySegmentId(segmentId, tenantId);
};

/**
 * List segments for tenant
 */
const listSegments = async (tenantId, options = {}) => {
  const query = { tenantId };

  if (options.isActive !== undefined) {
    query.isActive = options.isActive;
  }

  const page = parseInt(options.page) || 1;
  const limit = parseInt(options.limit) || 20;
  const skip = (page - 1) * limit;

  const [segments, total] = await Promise.all([
    Segment.find(query)
      .sort({ priority: -1, name: 1 })
      .limit(limit)
      .skip(skip),
    Segment.countDocuments(query)
  ]);

  return {
    segments,
    pagination: {
      page,
      limit,
      total,
      totalPages: Math.ceil(total / limit)
    }
  };
};

/**
 * Update segment
 */
const updateSegment = async (segmentId, tenantId, updates) => {
  try {
    const segment = await Segment.getBySegmentId(segmentId, tenantId);

    if (!segment) {
      throw new Error('Segment not found');
    }

    Object.assign(segment, updates);
    await segment.save();

    // Re-assign leads if criteria changed
    if (updates.criteria || updates.autoAssign !== undefined) {
      if (segment.autoAssign) {
        await segment.assignLeads();
      }
    }

    logger.info('Segment updated', { segmentId, tenantId });

    return segment;

  } catch (error) {
    logger.error('Error updating segment:', error);
    throw error;
  }
};

/**
 * Delete segment
 */
const deleteSegment = async (segmentId, tenantId) => {
  try {
    const segment = await Segment.getBySegmentId(segmentId, tenantId);

    if (!segment) {
      throw new Error('Segment not found');
    }

    // Remove segment assignment from leads
    await LeadScore.updateMany(
      { segmentId, tenantId },
      { $unset: { segmentId: '', segmentAssignedAt: '' } }
    );

    await Segment.deleteOne({ segmentId, tenantId });

    logger.info('Segment deleted', { segmentId, tenantId });

    return { success: true };

  } catch (error) {
    logger.error('Error deleting segment:', error);
    throw error;
  }
};

/**
 * Toggle segment active status
 */
const toggleSegment = async (segmentId, tenantId) => {
  const segment = await Segment.getBySegmentId(segmentId, tenantId);

  if (!segment) {
    throw new Error('Segment not found');
  }

  segment.isActive = !segment.isActive;
  await segment.save();

  return segment;
};

/**
 * Assign leads to segment
 */
const assignLeadsToSegment = async (segmentId, tenantId) => {
  const segment = await Segment.getBySegmentId(segmentId, tenantId);

  if (!segment) {
    throw new Error('Segment not found');
  }

  await segment.assignLeads();

  return segment;
};

/**
 * Get leads in segment
 */
const getSegmentLeads = async (segmentId, tenantId, options = {}) => {
  const page = parseInt(options.page) || 1;
  const limit = parseInt(options.limit) || 20;
  const skip = (page - 1) * limit;

  const [leads, total] = await Promise.all([
    LeadScore.find({ segmentId, tenantId })
      .sort({ score: -1 })
      .limit(limit)
      .skip(skip),
    LeadScore.countDocuments({ segmentId, tenantId })
  ]);

  return {
    leads,
    pagination: {
      page,
      limit,
      total,
      totalPages: Math.ceil(total / limit)
    }
  };
};

/**
 * Update segment lead counts
 */
const updateSegmentCounts = async (tenantId) => {
  const segments = await Segment.getActiveByTenant(tenantId);

  for (const segment of segments) {
    await segment.updateLeadCount();
  }

  logger.info('Updated segment counts', { tenantId, count: segments.length });

  return segments;
};

/**
 * Get segment statistics
 */
const getSegmentStats = async (tenantId) => {
  const overall = await Segment.getStatistics(tenantId);

  const bySegment = await Segment.aggregate([
    { $match: { tenantId } },
    {
      $project: {
        segmentId: 1,
        name: 1,
        leadCount: 1,
        isActive: 1,
        color: 1
      }
    },
    { $sort: { leadCount: -1 } }
  ]);

  return {
    overall: overall[0] || { totalSegments: 0, totalLeads: 0, avgLeadsPerSegment: 0 },
    bySegment
  };
};

/**
 * Move leads between segments
 */
const moveLeads = async (leadIds, fromSegmentId, toSegmentId, tenantId) => {
  try {
    // Update leads
    const result = await LeadScore.updateMany(
      {
        leadId: { $in: leadIds },
        segmentId: fromSegmentId,
        tenantId
      },
      {
        segmentId: toSegmentId,
        segmentAssignedAt: new Date()
      }
    );

    // Update segment counts
    if (fromSegmentId) {
      const fromSegment = await Segment.getBySegmentId(fromSegmentId, tenantId);
      if (fromSegment) {
        await fromSegment.updateLeadCount();
      }
    }

    if (toSegmentId) {
      const toSegment = await Segment.getBySegmentId(toSegmentId, tenantId);
      if (toSegment) {
        await toSegment.updateLeadCount();
      }
    }

    logger.info('Leads moved between segments', {
      count: result.modifiedCount,
      fromSegmentId,
      toSegmentId,
      tenantId
    });

    return { success: true, movedCount: result.modifiedCount };

  } catch (error) {
    logger.error('Error moving leads:', error);
    throw error;
  }
};

/**
 * Clone segment
 */
const cloneSegment = async (segmentId, tenantId, newName) => {
  const original = await Segment.getBySegmentId(segmentId, tenantId);

  if (!original) {
    throw new Error('Segment not found');
  }

  const cloned = new Segment({
    segmentId: uuidv4(),
    tenantId,
    name: newName || `${original.name} (Copy)`,
    description: original.description,
    color: original.color,
    criteria: original.criteria,
    autoAssign: original.autoAssign,
    priority: original.priority,
    tags: original.tags
  });

  await cloned.save();

  logger.info('Segment cloned', { originalSegmentId: segmentId, newSegmentId: cloned.segmentId });

  return cloned;
};

/**
 * Bulk reassign all segments
 */
const reassignAllSegments = async (tenantId) => {
  return bulkAssignSegments(tenantId);
};

module.exports = {
  createSegment,
  getSegment,
  listSegments,
  updateSegment,
  deleteSegment,
  toggleSegment,
  assignLeadsToSegment,
  getSegmentLeads,
  updateSegmentCounts,
  getSegmentStats,
  moveLeads,
  cloneSegment,
  reassignAllSegments
};
