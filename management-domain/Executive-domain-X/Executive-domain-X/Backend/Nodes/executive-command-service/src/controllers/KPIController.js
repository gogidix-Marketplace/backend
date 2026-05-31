/**
 * KPI Controller
 */

const { body, param, query, validationResult } = require('express-validator');
const KPICommandService = require('../services/KPICommandService');
const { asyncHandler } = require('../middleware/errorHandler');
const logger = require('../config/logger');

/**
 * Validation rules
 */
const createKPIValidation = [
  body('name').trim().notEmpty().withMessage('Name is required'),
  body('category').isIn(['FINANCIAL', 'OPERATIONAL', 'CUSTOMER', 'EMPLOYEE', 'ALL'])
    .withMessage('Invalid category'),
  body('executiveLevel').optional().isIn(['CEO', 'CFO', 'CTO', 'COO', 'ALL'])
    .withMessage('Invalid executive level'),
  body('value').isNumeric().withMessage('Value must be numeric'),
  body('unit').trim().notEmpty().withMessage('Unit is required'),
  body('period').trim().notEmpty().withMessage('Period is required'),
  body('target').optional().isNumeric().withMessage('Target must be numeric'),
  body('previousValue').optional().isNumeric().withMessage('Previous value must be numeric'),
];

const updateKPIValidation = [
  param('id').isMongoId().withMessage('Invalid KPI ID'),
  body('value').optional().isNumeric().withMessage('Value must be numeric'),
  body('target').optional().isNumeric().withMessage('Target must be numeric'),
  body('previousValue').optional().isNumeric().withMessage('Previous value must be numeric'),
  body('status').optional().isIn(['ON_TRACK', 'AT_RISK', 'BEHIND', 'AHEAD'])
    .withMessage('Invalid status'),
];

const kpiIdValidation = [
  param('id').isMongoId().withMessage('Invalid KPI ID'),
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
 * Create a new KPI
 */
async function createKPI(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const data = req.body;

  try {
    const kpi = await KPICommandService.createKPI(tenantId, data);

    res.status(201).json({
      success: true,
      data: kpi.toDocument(),
    });
  } catch (error) {
    res.status(400).json({
      error: error.message,
    });
  }
}

/**
 * Update an existing KPI
 */
async function updateKPI(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const kpiId = req.params.id;
  const data = req.body;

  try {
    const kpi = await KPICommandService.updateKPI(tenantId, kpiId, data);

    res.json({
      success: true,
      data: kpi.toDocument(),
    });
  } catch (error) {
    res.status(404).json({
      error: error.message,
    });
  }
}

/**
 * Delete a KPI
 */
async function deleteKPI(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const kpiId = req.params.id;

  try {
    await KPICommandService.deleteKPI(tenantId, kpiId);

    res.json({
      success: true,
      message: 'KPI deleted successfully',
    });
  } catch (error) {
    res.status(404).json({
      error: error.message,
    });
  }
}

/**
 * Batch create KPIs
 */
async function batchCreateKPIs(req, res) {
  const tenantId = req.user.tenantId;
  const { kpis } = req.body;

  if (!Array.isArray(kpis) || kpis.length === 0) {
    return res.status(400).json({
      error: 'kpis must be a non-empty array',
    });
  }

  const result = await KPICommandService.batchCreateKPIs(tenantId, kpis);

  res.status(201).json({
    success: true,
    ...result,
  });
}

/**
 * Recalculate KPI value
 */
async function recalculateKPI(req, res) {
  if (handleValidation(req, res)) return;

  const tenantId = req.user.tenantId;
  const kpiId = req.params.id;
  const { value, metadata } = req.body;

  if (value === undefined) {
    return res.status(400).json({
      error: 'value is required',
    });
  }

  try {
    const kpi = await KPICommandService.recalculateKPI(tenantId, kpiId, value, metadata);

    res.json({
      success: true,
      data: kpi.toDocument(),
    });
  } catch (error) {
    res.status(404).json({
      error: error.message,
    });
  }
}

module.exports = {
  createKPI,
  updateKPI,
  deleteKPI,
  batchCreateKPIs,
  recalculateKPI,
  createKPIValidation,
  updateKPIValidation,
  kpiIdValidation,
};
