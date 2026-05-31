/**
 * Scoring Engine Service
 * Core lead scoring logic and rule evaluation
 */

const ScoringRule = require('../models/ScoringRule');
const LeadScore = require('../models/LeadScore');
const Segment = require('../models/Segment');
const { getRedisClient } = require('../config/redis');
const logger = require('../utils/logger');

const CACHE_TTL = parseInt(process.env.SCORE_CACHE_TTL || '3600');
const MAX_SCORE = parseInt(process.env.MAX_LEAD_SCORE || '100');

/**
 * Calculate lead score based on rules
 */
const calculateScore = async (leadId, tenantId, leadData) => {
  try {
    logger.info('Calculating lead score', { leadId, tenantId });

    // Get or create lead score record
    let leadScore = await LeadScore.getOrCreate(leadId, tenantId);

    // Get active scoring rules for tenant
    const rules = await ScoringRule.getActiveByTenant(tenantId);

    if (rules.length === 0) {
      logger.warn('No scoring rules found for tenant', { tenantId });
      return leadScore;
    }

    let totalScore = 0;
    const breakdown = {
      demographic: 0,
      behavioral: 0,
      engagement: 0,
      firmographic: 0,
      custom: 0
    };

    const matchedRules = [];

    // Evaluate each rule
    for (const rule of rules) {
      const result = rule.evaluate(leadData);

      if (result.matched) {
        const ruleScore = Math.min(rule.score, rule.maxScore);
        totalScore += ruleScore;

        if (breakdown.hasOwnProperty(rule.category)) {
          breakdown[rule.category] += ruleScore;
        }

        matchedRules.push({
          ruleId: rule.ruleId,
          name: rule.name,
          score: ruleScore
        });
      }
    }

    // Cap at maximum score
    totalScore = Math.min(totalScore, MAX_SCORE);

    // Update lead score
    const oldScore = leadScore.score;
    leadScore.score = totalScore;
    leadScore.breakdown = breakdown;
    leadScore.lastCalculatedAt = new Date();
    leadScore.lastActivityAt = new Date();

    // Add to history
    const change = totalScore - oldScore;
    if (change !== 0) {
      leadScore.history.push({
        score: totalScore,
        change,
        reason: 'recalculation',
        timestamp: new Date()
      });
    }

    // Update quality classification
    leadScore.updateQuality();

    // Save to database
    await leadScore.save();

    // Cache the score
    await cacheScore(leadId, tenantId, leadScore);

    // Auto-assign segment if enabled
    if (process.env.ENABLE_AUTO_SEGMENTATION === 'true') {
      await assignSegment(leadScore);
    }

    logger.info('Lead score calculated', {
      leadId,
      oldScore,
      newScore: totalScore,
      matchedRules: matchedRules.length
    });

    return leadScore;

  } catch (error) {
    logger.error('Error calculating lead score:', error);
    throw error;
  }
};

/**
 * Recalculate lead score
 */
const recalculateScore = async (leadId, tenantId) => {
  try {
    // Fetch lead data from lead generation service
    const leadData = await fetchLeadData(leadId, tenantId);

    return calculateScore(leadId, tenantId, leadData);

  } catch (error) {
    logger.error('Error recalculating lead score:', error);
    throw error;
  }
};

/**
 * Fetch lead data from lead generation service
 */
const fetchLeadData = async (leadId, tenantId) => {
  try {
    const axios = require('axios');
    const serviceUrl = process.env.LEAD_GENERATION_SERVICE_URL;
    const apiKey = process.env.LEAD_GENERATION_API_KEY;

    if (!serviceUrl) {
      logger.warn('Lead generation service URL not configured');
      return {};
    }

    const response = await axios.get(`${serviceUrl}/api/v1/leads/${leadId}`, {
      headers: {
        'X-API-Key': apiKey,
        'X-Tenant-ID': tenantId
      },
      timeout: 5000
    });

    return response.data.data || {};

  } catch (error) {
    logger.error('Error fetching lead data:', error);
    return {};
  }
};

/**
 * Get cached score
 */
const getCachedScore = async (leadId, tenantId) => {
  try {
    const redis = getRedisClient();
    const key = `score:${tenantId}:${leadId}`;
    const cached = await redis.get(key);

    if (cached) {
      return JSON.parse(cached);
    }

    return null;

  } catch (error) {
    logger.error('Error getting cached score:', error);
    return null;
  }
};

/**
 * Cache score
 */
const cacheScore = async (leadId, tenantId, leadScore) => {
  try {
    const redis = getRedisClient();
    const key = `score:${tenantId}:${leadId}`;
    const value = JSON.stringify(leadScore);

    await redis.setex(key, CACHE_TTL, value);

  } catch (error) {
    logger.error('Error caching score:', error);
  }
};

/**
 * Invalidate cached score
 */
const invalidateScore = async (leadId, tenantId) => {
  try {
    const redis = getRedisClient();
    const key = `score:${tenantId}:${leadId}`;

    await redis.del(key);

  } catch (error) {
    logger.error('Error invalidating cached score:', error);
  }
};

/**
 * Apply time-based decay to scores
 */
const applyDecay = async (tenantId) => {
  try {
    const decayEnabled = process.env.SCORE_DECAY_ENABLED === 'true';
    if (!decayEnabled) {
      return;
    }

    const decayDays = parseInt(process.env.SCORE_DECAY_DAYS || '30');
    const decayRate = parseFloat(process.env.SCORE_DECAY_RATE || '0.1');

    const leads = await LeadScore.getDecayedLeads(tenantId);

    for (const lead of leads) {
      await lead.applyDecay(decayRate, decayDays);
      await invalidateScore(lead.leadId, tenantId);
    }

    logger.info(`Applied decay to ${leads.length} leads`);

  } catch (error) {
    logger.error('Error applying decay:', error);
  }
};

/**
 * Get lead score with caching
 */
const getLeadScore = async (leadId, tenantId) => {
  // Try cache first
  const cached = await getCachedScore(leadId, tenantId);
  if (cached) {
    return cached;
  }

  // Fall back to database
  const leadScore = await LeadScore.findOne({ leadId, tenantId });

  if (leadScore) {
    await cacheScore(leadId, tenantId, leadScore);
  }

  return leadScore;
};

/**
 * Batch calculate scores
 */
const batchCalculateScores = async (tenantId, leadIds) => {
  const results = [];

  for (const leadId of leadIds) {
    try {
      const leadData = await fetchLeadData(leadId, tenantId);
      const score = await calculateScore(leadId, tenantId, leadData);
      results.push({ leadId, success: true, score: score.score });
    } catch (error) {
      results.push({ leadId, success: false, error: error.message });
    }
  }

  return results;
};

/**
 * Assign segment to lead
 */
const assignSegment = async (leadScore) => {
  try {
    const segments = await Segment.getActiveByTenant(leadScore.tenantId);

    // Find matching segment (by priority)
    for (const segment of segments) {
      if (segment.matches(leadScore)) {
        if (leadScore.segmentId !== segment.segmentId) {
          leadScore.segmentId = segment.segmentId;
          leadScore.segmentAssignedAt = new Date();
          await leadScore.save();
          await invalidateScore(leadScore.leadId, leadScore.tenantId);

          logger.info('Lead assigned to segment', {
            leadId: leadScore.leadId,
            segmentId: segment.segmentId
          });
        }
        break;
      }
    }

  } catch (error) {
    logger.error('Error assigning segment:', error);
  }
};

/**
 * Bulk assign segments
 */
const bulkAssignSegments = async (tenantId) => {
  try {
    const segments = await Segment.getActiveByTenant(tenantId);
    const leads = await LeadScore.find({ tenantId });

    for (const lead of leads) {
      await assignSegment(lead);
    }

    // Update segment lead counts
    for (const segment of segments) {
      await segment.updateLeadCount();
    }

    logger.info(`Bulk assigned segments for ${leads.length} leads`);

  } catch (error) {
    logger.error('Error bulk assigning segments:', error);
  }
};

/**
 * Get score distribution
 */
const getScoreDistribution = async (tenantId) => {
  const stats = await LeadScore.aggregate([
    { $match: { tenantId } },
    {
      $bucket: {
        groupBy: '$score',
        boundaries: [0, 20, 40, 60, 80, 100],
        default: '100',
        output: {
          count: { $sum: 1 },
          leads: { $push: '$leadId' }
        }
      }
    }
  ]);

  return stats.map(bucket => ({
    range: `${bucket._id - 20}-${bucket._id}`,
    count: bucket.count
  }));
};

module.exports = {
  calculateScore,
  recalculateScore,
  getLeadScore,
  getCachedScore,
  cacheScore,
  invalidateScore,
  applyDecay,
  batchCalculateScores,
  assignSegment,
  bulkAssignSegments,
  getScoreDistribution,
  fetchLeadData
};
