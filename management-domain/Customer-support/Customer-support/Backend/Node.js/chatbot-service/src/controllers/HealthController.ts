/**
 * Health Controller
 * Handles health check and system status requests
 */

import { Response } from 'express';
import { AuthenticatedRequest } from '../types';
import { healthCheck } from '../config/database';
import { knowledgeBaseService } from '../services/KnowledgeBaseService';
import { intentDetectionService } from '../services/IntentDetectionService';
import { logger } from '../utils/logger';
import { asyncHandler } from '../middleware/errorHandler';

export class HealthController {
  /**
   * Basic health check
   */
  healthCheck = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const dbHealth = await healthCheck();
    const kbHealthy = await knowledgeBaseService.healthCheck();

    const isHealthy =
      dbHealth.mongodb === 'connected' &&
      dbHealth.redis === 'connected';

    res.status(isHealthy ? 200 : 503).json({
      success: isHealthy,
      status: isHealthy ? 'healthy' : 'unhealthy',
      timestamp: new Date(),
      services: {
        mongodb: dbHealth.mongodb,
        redis: dbHealth.redis,
        knowledgeBase: kbHealthy ? 'connected' : 'disconnected',
      },
    });
  });

  /**
   * Detailed system status
   */
  systemStatus = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const dbHealth = await healthCheck();
    const kbHealthy = await knowledgeBaseService.healthCheck();

    // Get intent count
    const intents = intentDetectionService.getAllIntents();

    // Get system metrics
    const memoryUsage = process.memoryUsage();
    const uptime = process.uptime();

    res.json({
      success: true,
      timestamp: new Date(),
      system: {
        nodeVersion: process.version,
        platform: process.platform,
        uptime: Math.floor(uptime),
        uptimeFormatted: this.formatUptime(uptime),
        memory: {
          used: Math.round(memoryUsage.heapUsed / 1024 / 1024),
          total: Math.round(memoryUsage.heapTotal / 1024 / 1024),
          rss: Math.round(memoryUsage.rss / 1024 / 1024),
        },
      },
      services: {
        mongodb: {
          status: dbHealth.mongodb,
        },
        redis: {
          status: dbHealth.redis,
        },
        knowledgeBase: {
          status: kbHealthy ? 'connected' : 'disconnected',
        },
        intentDetection: {
          status: 'active',
          intentsLoaded: intents.length,
        },
      },
    });
  });

  /**
   * Readiness check (for Kubernetes)
   */
  readinessCheck = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const dbHealth = await healthCheck();

    const isReady =
      dbHealth.mongodb === 'connected' &&
      dbHealth.redis === 'connected';

    res.status(isReady ? 200 : 503).json({
      ready: isReady,
    });
  });

  /**
   * Liveness check (for Kubernetes)
   */
  livenessCheck = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    res.status(200).json({
      alive: true,
      timestamp: new Date(),
    });
  });

  /**
   * Format uptime
   */
  private formatUptime(seconds: number): string {
    const days = Math.floor(seconds / 86400);
    const hours = Math.floor((seconds % 86400) / 3600);
    const minutes = Math.floor((seconds % 3600) / 60);
    const secs = Math.floor(seconds % 60);

    const parts = [];
    if (days > 0) parts.push(`${days}d`);
    if (hours > 0) parts.push(`${hours}h`);
    if (minutes > 0) parts.push(`${minutes}m`);
    if (secs > 0 || parts.length === 0) parts.push(`${secs}s`);

    return parts.join(' ');
  }
}

export const healthController = new HealthController();
