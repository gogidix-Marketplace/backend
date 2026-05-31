/**
 * KPI Query Controller
 * Handles HTTP requests for KPI read operations
 */

const { param, query, validationResult } = require('express-validator');
const KPIQueryService = require('../services/KPIQueryService');
const { asyncHandler } = require('../middleware/errorHandler');
const logger = require('../config/logger');

/**
 * Validation rules
 */
const kpiIdValidation = [
  param('id').isMongoId().withMessage('Invalid KPI ID'),
];

const getKPIsValidation = [
  query('page').optional().isInt({ min: 1 }).withMessage('Page must be a positive integer'),
  query('limit').optional().isInt({ min: 1, max: 100 }).withMessage('Limit must be between 1 and 100'),
  query('category').optional().isIn(['FINANCIAL', 'OPERATIONAL', 'CUSTOMER', 'EMPLOYEE', 'ALL'])
    .withMessage('Invalid category'),
  query('executiveLevel').optional().isIn(['CEO', 'CFO', 'CTO', 'COO', 'ALL'])
    .withMessage('Invalid executive level'),
  query('status').optional().isIn(['ON_TRACK', 'AT_RISK', 'BEHIND', 'AHEAD'])
    .withMessage('Invalid status'),
];

const dashboardValidation = [
  param('level').optional().isIn(['CEO', 'CFO', 'CTO', 'COO'])
    .withMessage('Invalid executive level'),
];

/**
 * Handle validation errors
 */
function handleValidation(req, res) {
  const errors = validationResult(req);
  if (!errors.isEmpty()) {
    res.status(400).json({
      error: 'Validation failed',
      details: errors.array(),
    });
    return true;
  }
  return false;
}

/**
 * Get KPI by ID
 */
async function getKPIById(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const kpiId = req.params.id;

  try {
    const kpi = await KPIQueryService.getKPIById(tenantId, kpiId);

    res.json({
      success: true,
      data: kpi,
    });
  } catch (error) {
    res.status(404).json({
      error: error.message,
    });
  }
}

/**
 * Get all KPIs with pagination and filtering
 */
async function getKPIs(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const options = {
    page: parseInt(req.query.page) || 1,
    limit: parseInt(req.query.limit) || 20,
    category: req.query.category,
    executiveLevel: req.query.executiveLevel,
    status: req.query.status,
    period: req.query.period,
    sortBy: req.query.sortBy || 'createdAt',
    sortOrder: req.query.sortOrder || 'desc',
  };

  try {
    const result = await KPIQueryService.getKPIs(tenantId, options);

    res.json({
      success: true,
      ...result,
    });
  } catch (error) {
    res.status(500).json({
      error: error.message,
    });
  }
}

/**
 * Get KPI summary by category
 */
async function getKPISummary(req, res) {
  const tenantId = req.user.tenantId;

  try {
    const summary = await KPIQueryService.getKPISummary(tenantId);

    res.json({
      success: true,
      data: summary,
    });
  } catch (error) {
    res.status(500).json({
      error: error.message,
    });
  }
}

/**
 * Get dashboard KPIs for executive level
 */
async function getDashboardKPIs(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const level = req.params.level || 'CEO';

  try {
    const result = await KPIQueryService.getDashboardKPIs(tenantId, level);

    res.json({
      success: true,
      data: result,
    });
  } catch (error) {
    res.status(500).json({
      error: error.message,
    });
  }
}

/**
 * Get KPI trends over time
 */
async function getKPITrends(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const kpiId = req.params.id;
  const periods = parseInt(req.query.periods) || 12;

  try {
    const trends = await KPIQueryService.getKPITrends(tenantId, kpiId, periods);

    res.json({
      success: true,
      data: trends,
    });
  } catch (error) {
    res.status(404).json({
      error: error.message,
    });
  }
}

/**
 * Search KPIs by name
 */
async function searchKPIs(req, res) {
  const tenantId = req.user.tenantId;
  const searchTerm = req.query.q;

  if (!searchTerm) {
    return res.status(400).json({
      error: 'Search query (q) is required',
    });
  }

  try {
    const kpis = await KPIQueryService.searchKPIs(tenantId, searchTerm, {
      limit: parseInt(req.query.limit) || 10,
    });

    res.json({
      success: true,
      data: kpis,
    });
  } catch (error) {
    res.status(500).json({
      error: error.message,
    });
  }
}

/**
 * Health check endpoint
 */
async function healthCheck(req, res) {
  res.json({
    success: true,
    service: 'executive-query-service',
    status: 'healthy',
    timestamp: new Date().toISOString(),
  });
}

module.exports = {
  getKPIById,
  getKPIs,
  getKPISummary,
  getDashboardKPIs,
  getKPITrends,
  searchKPIs,
  healthCheck,
  kpiIdValidation,
  getKPIsValidation,
  dashboardValidation,
};
