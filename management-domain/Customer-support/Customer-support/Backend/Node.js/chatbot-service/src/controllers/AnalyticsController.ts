/**
 * Analytics Controller
 * Handles HTTP requests for analytics operations
 */

import { Response } from 'express';
import { AuthenticatedRequest } from '../types';
import { analyticsService } from '../services/AnalyticsService';
import { handoffService } from '../services/HandoffService';
import { knowledgeBaseService } from '../services/KnowledgeBaseService';
import { logger } from '../utils/logger';
import { asyncHandler } from '../middleware/errorHandler';

export class AnalyticsController {
  /**
   * Get session analytics
   */
  getSessionAnalytics = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId } = req.params;

    const analytics = await analyticsService.getSessionAnalytics(sessionId);

    if (!analytics) {
      res.status(404).json({
        success: false,
        error: 'Session analytics not found',
      });
      return;
    }

    res.json({
      success: true,
      data: analytics,
    });
  });

  /**
   * Get customer analytics
   */
  getCustomerAnalytics = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { customerId } = req.params;
    const { page = 1, limit = 20 } = req.query;

    const result = await analyticsService.getCustomerAnalytics(
      customerId,
      parseInt(page as string),
      parseInt(limit as string)
    );

    res.json({
      success: true,
      data: result,
    });
  });

  /**
   * Get aggregated analytics
   */
  getAggregatedAnalytics = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { startDate, endDate } = req.query;

    if (!startDate || !endDate) {
      res.status(400).json({
        success: false,
        error: 'Start date and end date are required',
      });
      return;
    }

    const start = new Date(startDate as string);
    const end = new Date(endDate as string);

    if (isNaN(start.getTime()) || isNaN(end.getTime())) {
      res.status(400).json({
        success: false,
        error: 'Invalid date format',
      });
      return;
    }

    const analytics = await analyticsService.getAggregatedAnalytics(start, end);

    res.json({
      success: true,
      data: analytics,
    });
  });

  /**
   * Generate daily analytics
   */
  generateDailyAnalytics = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { date } = req.body;

    const targetDate = date ? new Date(date) : new Date();

    const analytics = await analyticsService.generateDailyAnalytics(targetDate);

    res.json({
      success: true,
      data: analytics,
    });
  });

  /**
   * Get analytics by date range
   */
  getAnalyticsByDateRange = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { startDate, endDate } = req.query;

    if (!startDate || !endDate) {
      res.status(400).json({
        success: false,
        error: 'Start date and end date are required',
      });
      return;
    }

    const start = new Date(startDate as string);
    const end = new Date(endDate as string);

    const analytics = await analyticsService.getAnalyticsByDateRange(start, end);

    res.json({
      success: true,
      data: analytics,
    });
  });

  /**
   * Get intent statistics
   */
  getIntentStats = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { startDate, endDate, limit = 20 } = req.query;

    if (!startDate || !endDate) {
      res.status(400).json({
        success: false,
        error: 'Start date and end date are required',
      });
      return;
    }

    const start = new Date(startDate as string);
    const end = new Date(endDate as string);

    const stats = await analyticsService.getIntentStats(
      start,
      end,
      parseInt(limit as string)
    );

    res.json({
      success: true,
      data: stats,
    });
  });

  /**
   * Get satisfaction metrics
   */
  getSatisfactionMetrics = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { startDate, endDate } = req.query;

    if (!startDate || !endDate) {
      res.status(400).json({
        success: false,
        error: 'Start date and end date are required',
      });
      return;
    }

    const start = new Date(startDate as string);
    const end = new Date(endDate as string);

    const metrics = await analyticsService.getSatisfactionMetrics(start, end);

    res.json({
      success: true,
      data: metrics,
    });
  });

  /**
   * Get handoff statistics
   */
  getHandoffStats = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { startDate, endDate } = req.query;

    if (!startDate || !endDate) {
      res.status(400).json({
        success: false,
        error: 'Start date and end date are required',
      });
      return;
    }

    const start = new Date(startDate as string);
    const end = new Date(endDate as string);

    const stats = await handoffService.getHandoffStats(start, end);

    res.json({
      success: true,
      data: stats,
    });
  });

  /**
   * Export analytics
   */
  exportAnalytics = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { startDate, endDate, format = 'json' } = req.query;

    if (!startDate || !endDate) {
      res.status(400).json({
        success: false,
        error: 'Start date and end date are required',
      });
      return;
    }

    const start = new Date(startDate as string);
    const end = new Date(endDate as string);

    const data = await analyticsService.exportAnalytics(start, end, format as 'json' | 'csv');

    if (format === 'csv') {
      res.setHeader('Content-Type', 'text/csv');
      res.setHeader('Content-Disposition', `attachment; filename="analytics_${startDate}_${endDate}.csv"`);
    } else {
      res.setHeader('Content-Type', 'application/json');
      res.setHeader('Content-Disposition', `attachment; filename="analytics_${startDate}_${endDate}.json"`);
    }

    res.send(data);
  });

  /**
   * Get dashboard summary
   */
  getDashboardSummary = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const now = new Date();
    const startOfDay = new Date(now.setHours(0, 0, 0, 0));
    const startOfWeek = new Date(now.setDate(now.getDate() - 7));
    const startOfMonth = new Date(now.setDate(now.getDate() - 30));

    const [todayAnalytics, weekAnalytics, monthAnalytics] = await Promise.all([
      analyticsService.getAggregatedAnalytics(startOfDay, new Date()),
      analyticsService.getAggregatedAnalytics(startOfWeek, new Date()),
      analyticsService.getAggregatedAnalytics(startOfMonth, new Date()),
    ]);

    // Get recent intent stats
    const recentIntents = await analyticsService.getIntentStats(startOfWeek, new Date(), 5);

    // Get satisfaction metrics
    const satisfaction = await analyticsService.getSatisfactionMetrics(startOfWeek, new Date());

    // Get handoff stats
    const handoffStats = await handoffService.getHandoffStats(startOfWeek, new Date());

    // Check knowledge base health
    const kbHealthy = await knowledgeBaseService.healthCheck();

    res.json({
      success: true,
      data: {
        today: todayAnalytics,
        week: weekAnalytics,
        month: monthAnalytics,
        topIntents: recentIntents,
        satisfaction,
        handoffs: handoffStats,
        systemHealth: {
          knowledgeBase: kbHealthy ? 'healthy' : 'unhealthy',
        },
      },
    });
  });

  /**
   * Get real-time statistics
   */
  getRealtimeStats = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    // This would typically use Redis for real-time stats
    // For now, return current session counts

    const { ChatSessionModel } = await import('../models/ChatSession');

    const activeCount = await ChatSessionModel.countDocuments({
      status: { $in: ['active', 'waiting_for_agent', 'with_agent'] },
    });

    const waitingForAgent = await ChatSessionModel.countDocuments({
      status: 'waiting_for_agent',
    });

    const withAgent = await ChatSessionModel.countDocuments({
      status: 'with_agent',
    });

    res.json({
      success: true,
      data: {
        activeSessions: activeCount,
        waitingForAgent,
        withAgent,
        timestamp: new Date(),
      },
    });
  });
}

export const analyticsController = new AnalyticsController();
