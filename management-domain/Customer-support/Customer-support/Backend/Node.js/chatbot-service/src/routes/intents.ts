/**
 * Intent Routes
 */

import express from 'express';
import { intentController } from '../controllers/IntentController';
import { authenticate, requireRole } from '../middleware/auth';
import { apiRateLimiter } from '../middleware/rateLimiter';
import { validate } from '../utils/validators';
import {
  intentValidation,
  intentUpdateValidation,
  sessionParamValidation,
} from '../utils/validators';

const router = express.Router();

// Public intent detection
router.post(
  '/detect',
  authenticate,
  apiRateLimiter,
  validate([
    body('message').trim().notEmpty().withMessage('Message is required'),
    body('language').optional().isIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar']),
  ]),
  intentController.detectIntent
);

// Admin-only routes for intent management
router.get(
  '/',
  authenticate,
  requireRole('admin', 'manager'),
  intentController.getIntents
);

router.get(
  '/categories',
  authenticate,
  intentController.getCategories
);

router.post(
  '/',
  authenticate,
  requireRole('admin'),
  apiRateLimiter,
  validate(intentValidation),
  intentController.createIntent
);

router.post(
  '/batch-detect',
  authenticate,
  requireRole('admin'),
  apiRateLimiter,
  validate([
    body('messages').isArray({ min: 1, max: 50 }).withMessage('Messages array is required'),
    body('language').optional().isIn(['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar']),
  ]),
  intentController.batchDetectIntents
);

router.get(
  '/:intentId',
  authenticate,
  requireRole('admin', 'manager'),
  intentController.getIntentById
);

router.put(
  '/:intentId',
  authenticate,
  requireRole('admin'),
  validate(intentUpdateValidation),
  intentController.updateIntent
);

router.delete(
  '/:intentId',
  authenticate,
  requireRole('admin'),
  intentController.getIntentById,
  intentController.deleteIntent
);

router.post(
  '/:intentId/training-phrases',
  authenticate,
  requireRole('admin'),
  validate([
    ...intentUpdateValidation,
    body('phrase').trim().notEmpty().withMessage('Phrase is required'),
  ]),
  intentController.addTrainingPhrase
);

router.post(
  '/:intentId/responses',
  authenticate,
  requireRole('admin'),
  validate([
    ...intentUpdateValidation,
    body('response').trim().notEmpty().withMessage('Response is required'),
  ]),
  intentController.addResponse
);

router.patch(
  '/:intentId/toggle',
  authenticate,
  requireRole('admin'),
  validate(intentUpdateValidation),
  intentController.toggleIntent
);

export default router;
