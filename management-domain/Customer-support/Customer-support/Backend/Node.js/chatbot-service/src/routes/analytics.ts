/**
 * Analytics Routes
 */

import express from 'express';
import { analyticsController } from '../controllers/AnalyticsController';
import { authenticate, requireRole } from '../middleware/auth';
import { apiRateLimiter } from '../middleware/rateLimiter';
import { validate, analyticsQueryValidation } from '../utils/validators';

const router = express.Router();

// All analytics routes require authentication and admin/manager role
router.get(
  '/dashboard',
  authenticate,
  requireRole('admin', 'manager'),
  analyticsController.getDashboardSummary
);

router.get(
  '/realtime',
  authenticate,
  requireRole('admin', 'manager'),
  analyticsController.getRealtimeStats
);

router.get(
  '/sessions/:sessionId',
  authenticate,
  requireRole('admin', 'manager'),
  analyticsController.getSessionAnalytics
);

router.get(
  '/customers/:customerId',
  authenticate,
  requireRole('admin', 'manager'),
  validate(analyticsQueryValidation),
  analyticsController.getCustomerAnalytics
);

router.get(
  '/aggregated',
  authenticate,
  requireRole('admin', 'manager'),
  validate(analyticsQueryValidation),
  analyticsController.getAggregatedAnalytics
);

router.post(
  '/daily',
  authenticate,
  requireRole('admin'),
  analyticsController.generateDailyAnalytics
);

router.get(
  '/daily',
  authenticate,
  requireRole('admin', 'manager'),
  validate(analyticsQueryValidation),
  analyticsController.getAnalyticsByDateRange
);

router.get(
  '/intents',
  authenticate,
  requireRole('admin', 'manager'),
  validate(analyticsQueryValidation),
  analyticsController.getIntentStats
);

router.get(
  '/satisfaction',
  authenticate,
  requireRole('admin', 'manager'),
  validate(analyticsQueryValidation),
  analyticsController.getSatisfactionMetrics
);

router.get(
  '/handoffs',
  authenticate,
  requireRole('admin', 'manager'),
  validate(analyticsQueryValidation),
  analyticsController.getHandoffStats
);

router.get(
  '/export',
  authenticate,
  requireRole('admin'),
  validate(analyticsQueryValidation),
  analyticsController.exportAnalytics
);

export default router;
