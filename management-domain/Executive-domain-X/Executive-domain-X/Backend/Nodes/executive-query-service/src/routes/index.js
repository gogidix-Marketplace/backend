/**
 * Routes Configuration
 */

const express = require('express');
const router = express.Router();

const KPIController = require('../controllers/KPIController');
const { setupAuth } = require('../middleware/auth');
const { notFoundHandler, errorHandler } = require('../middleware/errorHandler');

/**
 * Setup all routes
 */
async function setupRoutes(app) {
  // Apply authentication to API routes
  await setupAuth(app);

  // Health check
  app.get('/health', KPIController.healthCheck);

  // KPI Query Routes
  app.get('/api/kpi',
    KPIController.getKPIsValidation,
    KPIController.getKPIs
  );

  app.get('/api/kpi/summary',
    KPIController.getKPISummary
  );

  app.get('/api/kpi/search',
    KPIController.searchKPIs
  );

  app.get('/api/kpi/dashboard/:level',
    KPIController.dashboardValidation,
    KPIController.getDashboardKPIs
  );

  app.get('/api/kpi/:id',
    KPIController.kpiIdValidation,
    KPIController.getKPIById
  );

  app.get('/api/kpi/:id/trend',
    KPIController.kpiIdValidation,
    KPIController.getKPITrends
  );

  // 404 handler
  app.use(notFoundHandler);

  // Global error handler
  app.use(errorHandler);
}

module.exports = { setupRoutes };
