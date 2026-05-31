/**
 * Routes Configuration
 */

const express = require('express');
const router = express.Router();

const KPIController = require('../controllers/KPIController');
const { setupAuth } = require('../middleware');
const { notFoundHandler } = require('../middleware/errorHandler');

/**
 * Setup all routes
 */
async function setupRoutes(app) {
  // Apply authentication to API routes
  await setupAuth(app);

  // KPI Routes
  app.post('/api/kpi',
    KPIController.createKPIValidation,
    KPIController.createKPI
  );

  app.put('/api/kpi/:id',
    KPIController.updateKPIValidation,
    KPIController.updateKPI
  );

  app.delete('/api/kpi/:id',
    KPIController.kpiIdValidation,
    KPIController.deleteKPI
  );

  app.post('/api/kpi/batch',
    KPIController.batchCreateKPIs
  );

  app.post('/api/kpi/:id/recalculate',
    KPIController.kpiIdValidation,
    KPIController.recalculateKPI
  );

  // Metrics routes (similar pattern)
  // TODO: Add metrics controller routes

  // 404 handler
  app.use(notFoundHandler);
}

module.exports = { setupRoutes };
