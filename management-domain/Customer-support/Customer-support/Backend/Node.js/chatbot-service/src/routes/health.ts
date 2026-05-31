/**
 * Health Routes
 */

import express from 'express';
import { healthController } from '../controllers/HealthController';

const router = express.Router();

// Health check endpoints (no authentication required)
router.get('/', healthController.healthCheck);
router.get('/health', healthController.healthCheck);
router.get('/status', healthController.systemStatus);
router.get('/readiness', healthController.readinessCheck);
router.get('/liveness', healthController.livenessCheck);

export default router;
