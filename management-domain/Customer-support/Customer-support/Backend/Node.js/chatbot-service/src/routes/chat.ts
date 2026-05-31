/**
 * Chat Routes
 */

import express from 'express';
import { chatController } from '../controllers/ChatController';
import { authenticateOptional, authenticate } from '../middleware/auth';
import { apiRateLimiter, chatRateLimiter } from '../middleware/rateLimiter';
import { validate } from '../utils/validators';
import {
  sendMessageValidation,
  createSessionValidation,
  handoffValidation,
  feedbackValidation,
  sessionParamValidation,
  translateValidation,
} from '../utils/validators';

const router = express.Router();

// Public routes (with optional authentication)
router.post(
  '/sessions',
  authenticateOptional,
  apiRateLimiter,
  validate(createSessionValidation),
  chatController.createSession
);

router.post(
  '/send',
  authenticateOptional,
  chatRateLimiter,
  validate(sendMessageValidation),
  chatController.sendMessage
);

// Authenticated routes
router.get(
  '/sessions/:sessionId',
  authenticate,
  validate(sessionParamValidation),
  chatController.getSession
);

router.get(
  '/sessions/:sessionId/history',
  authenticate,
  validate(sessionParamValidation),
  chatController.getSessionHistory
);

router.post(
  '/sessions/:sessionId/handoff',
  authenticate,
  validate([...sessionParamValidation, ...handoffValidation]),
  handoffRateLimiter,
  chatController.requestHandoff
);

router.get(
  '/sessions/:sessionId/handoff/status',
  authenticate,
  validate(sessionParamValidation),
  chatController.getHandoffStatus
);

router.delete(
  '/sessions/:sessionId/handoff',
  authenticate,
  validate(sessionParamValidation),
  chatController.cancelHandoff
);

router.post(
  '/sessions/:sessionId/close',
  authenticate,
  validate(sessionParamValidation),
  chatController.closeSession
);

router.post(
  '/feedback',
  authenticate,
  validate(feedbackValidation),
  chatController.submitFeedback
);

router.get(
  '/customers/:customerId/sessions',
  authenticate,
  chatController.getCustomerSessions
);

router.post(
  '/translate',
  authenticate,
  apiRateLimiter,
  validate(translateValidation),
  chatController.translateText
);

router.get(
  '/languages',
  authenticateOptional,
  chatController.getSupportedLanguages
);

export default router;
