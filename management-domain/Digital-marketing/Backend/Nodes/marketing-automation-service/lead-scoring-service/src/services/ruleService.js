/**
 * Rule Service
 * Management of scoring rules
 */

const ScoringRule = require('../models/ScoringRule');
const LeadScore = require('../models/LeadScore');
const { invalidateScore } = require('./scoringEngine');
const logger = require('../utils/logger');
const { v4: uuidv4 } = require('uuid');

/**
 * Create scoring rule
 */
const createRule = async (ruleData) => {
  try {
    const ruleId = ruleData.ruleId || uuidv4();

    const rule = new ScoringRule({
      ruleId,
      ...ruleData
    });

    await rule.save();

    logger.info('Scoring rule created', { ruleId, tenantId: rule.tenantId });

    return rule;

  } catch (error) {
    logger.error('Error creating scoring rule:', error);
    throw error;
  }
};

/**
 * Get rule by ID
 */
const getRule = async (ruleId, tenantId) => {
  return ScoringRule.getByRuleId(ruleId, tenantId);
};

/**
 * List rules for tenant
 */
const listRules = async (tenantId, options = {}) => {
  const query = { tenantId };

  if (options.category) {
    query.category = options.category;
  }

  if (options.isActive !== undefined) {
    query.isActive = options.isActive;
  }

  const page = parseInt(options.page) || 1;
  const limit = parseInt(options.limit) || 20;
  const skip = (page - 1) * limit;

  const [rules, total] = await Promise.all([
    ScoringRule.find(query)
      .sort({ priority: -1, createdAt: -1 })
      .limit(limit)
      .skip(skip),
    ScoringRule.countDocuments(query)
  ]);

  return {
    rules,
    pagination: {
      page,
      limit,
      total,
      totalPages: Math.ceil(total / limit)
    }
  };
};

/**
 * Update rule
 */
const updateRule = async (ruleId, tenantId, updates) => {
  try {
    const rule = await ScoringRule.getByRuleId(ruleId, tenantId);

    if (!rule) {
      throw new Error('Rule not found');
    }

    Object.assign(rule, updates);
    await rule.save();

    // Invalidate scores for affected segments if rule changed significantly
    if (updates.score !== undefined || updates.conditions !== undefined) {
      await invalidateScoresForRuleChange(ruleId, tenantId);
    }

    logger.info('Scoring rule updated', { ruleId, tenantId });

    return rule;

  } catch (error) {
    logger.error('Error updating scoring rule:', error);
    throw error;
  }
};

/**
 * Delete rule
 */
const deleteRule = async (ruleId, tenantId) => {
  try {
    const rule = await ScoringRule.getByRuleId(ruleId, tenantId);

    if (!rule) {
      throw new Error('Rule not found');
    }

    await ScoringRule.deleteOne({ ruleId, tenantId });

    // Invalidate affected scores
    await invalidateScoresForRuleChange(ruleId, tenantId);

    logger.info('Scoring rule deleted', { ruleId, tenantId });

    return { success: true };

  } catch (error) {
    logger.error('Error deleting scoring rule:', error);
    throw error;
  }
};

/**
 * Toggle rule active status
 */
const toggleRule = async (ruleId, tenantId) => {
  const rule = await ScoringRule.getByRuleId(ruleId, tenantId);

  if (!rule) {
    throw new Error('Rule not found');
  }

  rule.isActive = !rule.isActive;
  await rule.save();

  // If deactivating, invalidate affected scores
  if (!rule.isActive) {
    await invalidateScoresForRuleChange(ruleId, tenantId);
  }

  return rule;
};

/**
 * Bulk update rules
 */
const bulkUpdateRules = async (ruleIds, tenantId, updates) => {
  try {
    await ScoringRule.bulkUpdate(ruleIds, updates, tenantId);

    // Invalidate affected scores
    for (const ruleId of ruleIds) {
      await invalidateScoresForRuleChange(ruleId, tenantId);
    }

    logger.info('Bulk updated scoring rules', { count: ruleIds.length, tenantId });

    return { success: true, count: ruleIds.length };

  } catch (error) {
    logger.error('Error bulk updating scoring rules:', error);
    throw error;
  }
};

/**
 * Test rule against sample data
 */
const testRule = async (ruleId, tenantId, sampleData) => {
  const rule = await ScoringRule.getByRuleId(ruleId, tenantId);

  if (!rule) {
    throw new Error('Rule not found');
  }

  const result = rule.evaluate(sampleData);

  return {
    ruleId: rule.ruleId,
    ruleName: rule.name,
    matched: result.matched,
    score: result.score,
    conditionResults: result.conditionResults
  };
};

/**
 * Validate rule conditions
 */
const validateRuleConditions = (conditions) => {
  const validOperators = ['equals', 'not_equals', 'contains', 'not_contains', 'greater_than', 'less_than', 'in', 'not_in', 'exists', 'not_exists'];
  const validFields = ['email', 'firstName', 'lastName', 'company', 'jobTitle', 'industry', 'companySize', 'revenue', 'country', 'website', 'leadSource', 'leadScore', 'lastActivity', 'created_at'];

  for (const condition of conditions) {
    if (!condition.field) {
      return { valid: false, error: 'Field is required' };
    }

    if (!condition.operator) {
      return { valid: false, error: 'Operator is required' };
    }

    if (!validOperators.includes(condition.operator)) {
      return { valid: false, error: `Invalid operator: ${condition.operator}` };
    }

    // Check if operator requires a value
    if (!['exists', 'not_exists'].includes(condition.operator) && condition.value === undefined) {
      return { valid: false, error: `Value is required for operator: ${condition.operator}` };
    }

    // Validate in/not_in operators have array values
    if (['in', 'not_in'].includes(condition.operator) && !Array.isArray(condition.value)) {
      return { valid: false, error: `Value must be an array for operator: ${condition.operator}` };
    }
  }

  return { valid: true };
};

/**
 * Clone rule
 */
const cloneRule = async (ruleId, tenantId, newName) => {
  const original = await ScoringRule.getByRuleId(ruleId, tenantId);

  if (!original) {
    throw new Error('Rule not found');
  }

  const cloned = new ScoringRule({
    ruleId: uuidv4(),
    tenantId,
    name: newName || `${original.name} (Copy)`,
    description: original.description,
    category: original.category,
    conditions: original.conditions,
    logic: original.logic,
    score: original.score,
    maxScore: original.maxScore,
    priority: original.priority,
    segmentIds: original.segmentIds,
    tags: original.tags
  });

  await cloned.save();

  logger.info('Scoring rule cloned', { originalRuleId: ruleId, newRuleId: cloned.ruleId });

  return cloned;
};

/**
 * Invalidate scores affected by rule change
 */
const invalidateScoresForRuleChange = async (ruleId, tenantId) => {
  try {
    // Get leads that might be affected by this rule
    // For simplicity, we'll invalidate all leads for the tenant
    // In production, you might want to be more selective
    const leads = await LeadScore.find({ tenantId });

    for (const lead of leads) {
      await invalidateScore(lead.leadId, tenantId);
    }

    logger.info(`Invalidated ${leads.length} scores after rule change`, { ruleId, tenantId });

  } catch (error) {
    logger.error('Error invalidating scores:', error);
  }
};

/**
 * Get rule usage statistics
 */
const getRuleStats = async (tenantId) => {
  return ScoringRule.aggregate([
    { $match: { tenantId } },
    {
      $group: {
        _id: '$category',
        count: { $sum: 1 },
        totalUsage: { $sum: '$usageCount' },
        avgScore: { $avg: '$score' }
      }
    }
  ]);
};

module.exports = {
  createRule,
  getRule,
  listRules,
  updateRule,
  deleteRule,
  toggleRule,
  bulkUpdateRules,
  testRule,
  validateRuleConditions,
  cloneRule,
  getRuleStats
};
