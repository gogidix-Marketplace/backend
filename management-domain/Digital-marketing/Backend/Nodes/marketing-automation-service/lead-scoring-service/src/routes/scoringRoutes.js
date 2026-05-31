/**
 * Scoring Routes
 * API routes for lead scoring operations
 */

const express = require('express');
const router = express.Router();
const { asyncHandler } = require('../middleware/errorHandler');
const { v4: uuidv4 } = require('uuid');
const Joi = require('joi');

// Import services
const {
  calculateScore,
  recalculateScore,
  getLeadScore,
  batchCalculateScores,
  getScoreDistribution
} = require('../services/scoringEngine');
const {
  createRule,
  getRule,
  listRules,
  updateRule,
  deleteRule,
  toggleRule,
  testRule,
  validateRuleConditions,
  cloneRule
} = require('../services/ruleService');
const {
  createSegment,
  getSegment,
  listSegments,
  updateSegment,
  deleteSegment,
  toggleSegment,
  assignLeadsToSegment,
  getSegmentLeads,
  getSegmentStats,
  moveLeads,
  cloneSegment,
  reassignAllSegments
} = require('../services/segmentService');
const LeadScore = require('../models/LeadScore');

// Validation schemas
const calculateScoreSchema = Joi.object({
  leadId: Joi.string().required(),
  tenantId: Joi.string().required(),
  leadData: Joi.object().required()
});

const createRuleSchema = Joi.object({
  ruleId: Joi.string(),
  tenantId: Joi.string().required(),
  name: Joi.string().required(),
  description: Joi.string(),
  category: Joi.string().valid('demographic', 'behavioral', 'engagement', 'firmographic', 'custom').required(),
  conditions: Joi.array().items(
    Joi.object({
      field: Joi.string().required(),
      operator: Joi.string().valid('equals', 'not_equals', 'contains', 'not_contains', 'greater_than', 'less_than', 'in', 'not_in', 'exists', 'not_exists').required(),
      value: Joi.any(),
      valueType: Joi.string().valid('string', 'number', 'boolean', 'array', 'date')
    })
  ).min(1).required(),
  logic: Joi.string().valid('AND', 'OR').default('AND'),
  score: Joi.number().min(0).max(100).required(),
  maxScore: Joi.number().min(0).max(100).default(100),
  priority: Joi.number().default(0),
  segmentIds: Joi.array().items(Joi.string()),
  tags: Joi.array().items(Joi.string().max(50)).max(10)
});

const createSegmentSchema = Joi.object({
  segmentId: Joi.string(),
  tenantId: Joi.string().required(),
  name: Joi.string().required(),
  description: Joi.string(),
  color: Joi.string().default('#3498db'),
  criteria: Joi.object({
    minScore: Joi.number().min(0).max(100).default(0),
    maxScore: Joi.number().min(0).max(100).default(100),
    qualities: Joi.array().items(Joi.string().valid('hot', 'warm', 'cold')),
    requiredFlags: Joi.array().items(Joi.string()),
    excludedFlags: Joi.array().items(Joi.string())
  }).required(),
  autoAssign: Joi.boolean().default(true),
  priority: Joi.number().default(0)
});

// ==================== Score Routes ====================

/**
 * POST /api/v1/scoring/calculate
 * Calculate lead score
 */
router.post('/calculate', asyncHandler(async (req, res) => {
  const { error, value } = calculateScoreSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => d.message)
    });
  }

  const result = await calculateScore(value.leadId, value.tenantId, value.leadData);

  res.json({
    success: true,
    data: result
  });
}));

/**
 * GET /api/v1/scoring/:leadId
 * Get lead score
 */
router.get('/:leadId', asyncHandler(async (req, res) => {
  const { leadId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  const score = await getLeadScore(leadId, tenantId);

  if (!score) {
    return res.status(404).json({
      error: 'NotFound',
      message: 'Lead score not found'
    });
  }

  res.json({
    success: true,
    data: score
  });
}));

/**
 * POST /api/v1/scoring/:leadId/recalculate
 * Recalculate lead score
 */
router.post('/:leadId/recalculate', asyncHandler(async (req, res) => {
  const { leadId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.body.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  const result = await recalculateScore(leadId, tenantId);

  res.json({
    success: true,
    data: result
  });
}));

/**
 * POST /api/v1/scoring/batch
 * Batch calculate scores
 */
router.post('/batch', asyncHandler(async (req, res) => {
  const { tenantId, leadIds } = req.body;

  if (!tenantId || !leadIds || !Array.isArray(leadIds)) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID and lead IDs array are required'
    });
  }

  const results = await batchCalculateScores(tenantId, leadIds);

  res.json({
    success: true,
    data: results
  });
}));

/**
 * GET /api/v1/scoring/stats/distribution
 * Get score distribution
 */
router.get('/stats/distribution', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  const distribution = await getScoreDistribution(tenantId);

  res.json({
    success: true,
    data: distribution
  });
}));

// ==================== Rule Routes ====================

/**
 * POST /api/v1/rules
 * Create scoring rule
 */
router.post('/rules', asyncHandler(async (req, res) => {
  const { error, value } = createRuleSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => d.message)
    });
  }

  // Validate conditions
  const validation = validateRuleConditions(value.conditions);
  if (!validation.valid) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid conditions',
      errors: [validation.error]
    });
  }

  const rule = await createRule(value);

  res.status(201).json({
    success: true,
    message: 'Scoring rule created',
    data: rule
  });
}));

/**
 * GET /api/v1/rules
 * List scoring rules
 */
router.get('/rules', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  const options = {
    category: req.query.category,
    isActive: req.query.isActive,
    page: parseInt(req.query.page) || 1,
    limit: parseInt(req.query.limit) || 20
  };

  const result = await listRules(tenantId, options);

  res.json({
    success: true,
    data: result
  });
}));

/**
 * GET /api/v1/rules/:ruleId
 * Get scoring rule
 */
router.get('/rules/:ruleId', asyncHandler(async (req, res) => {
  const { ruleId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  const rule = await getRule(ruleId, tenantId);

  if (!rule) {
    return res.status(404).json({
      error: 'NotFound',
      message: 'Rule not found'
    });
  }

  res.json({
    success: true,
    data: rule
  });
}));

/**
 * PUT /api/v1/rules/:ruleId
 * Update scoring rule
 */
router.put('/rules/:ruleId', asyncHandler(async (req, res) => {
  const { ruleId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.body.tenantId;

  const rule = await updateRule(ruleId, tenantId, req.body);

  res.json({
    success: true,
    message: 'Rule updated',
    data: rule
  });
}));

/**
 * DELETE /api/v1/rules/:ruleId
 * Delete scoring rule
 */
router.delete('/rules/:ruleId', asyncHandler(async (req, res) => {
  const { ruleId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  await deleteRule(ruleId, tenantId);

  res.json({
    success: true,
    message: 'Rule deleted'
  });
}));

/**
 * POST /api/v1/rules/:ruleId/toggle
 * Toggle rule active status
 */
router.post('/rules/:ruleId/toggle', asyncHandler(async (req, res) => {
  const { ruleId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  const rule = await toggleRule(ruleId, tenantId);

  res.json({
    success: true,
    message: `Rule ${rule.isActive ? 'activated' : 'deactivated'}`,
    data: rule
  });
}));

/**
 * POST /api/v1/rules/:ruleId/test
 * Test rule with sample data
 */
router.post('/rules/:ruleId/test', asyncHandler(async (req, res) => {
  const { ruleId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.body.tenantId;
  const { sampleData } = req.body;

  if (!sampleData) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Sample data is required'
    });
  }

  const result = await testRule(ruleId, tenantId, sampleData);

  res.json({
    success: true,
    data: result
  });
}));

/**
 * POST /api/v1/rules/:ruleId/clone
 * Clone rule
 */
router.post('/rules/:ruleId/clone', asyncHandler(async (req, res) => {
  const { ruleId } = req.params;
  const tenantId = req.headers['x-tenant-id'] || req.body.tenantId;
  const { name } = req.body;

  const rule = await cloneRule(ruleId, tenantId, name);

  res.status(201).json({
    success: true,
    message: 'Rule cloned',
    data: rule
  });
}));

// ==================== Segment Routes ====================

/**
 * POST /api/v1/segments
 * Create segment
 */
router.post('/segments', asyncHandler(async (req, res) => {
  const { error, value } = createSegmentSchema.validate(req.body);
  if (error) {
    return res.status(400).json({
      error: 'ValidationError',
      message: 'Invalid request data',
      errors: error.details.map(d => d.message)
    });
  }

  const segment = await createSegment(value);

  res.status(201).json({
    success: true,
    message: 'Segment created',
    data: segment
  });
}));

/**
 * GET /api/v1/segments
 * List segments
 */
router.get('/segments', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  const options = {
    isActive: req.query.isActive,
    page: parseInt(req.query.page) || 1,
    limit: parseInt(req.query.limit) || 20
  };

  const result = await listSegments(tenantId, options);

  res.json({
    success: true,
    data: result
  });
}));

/**
 * GET /api/v1/segments/stats
 * Get segment statistics
 */
router.get('/segments/stats', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.query.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  const stats = await getSegmentStats(tenantId);

  res.json({
    success: true,
    data: stats
  });
}));

/**
 * POST /api/v1/segments/reassign
 * Reassign all segments
 */
router.post('/segments/reassign', asyncHandler(async (req, res) => {
  const tenantId = req.headers['x-tenant-id'] || req.body.tenantId;

  if (!tenantId) {
    return res.status(400).json({
      error: 'BadRequest',
      message: 'Tenant ID is required'
    });
  }

  await reassignAllSegments(tenantId);

  res.json({
    success: true,
    message: 'Segments reassigned'
  });
}));

module.exports = router;
