/**
 * Analytics Service
 * Handles session analytics and reporting
 */

import { ChatSessionModel } from '../models/ChatSession';
import { SessionAnalyticsModel, DailyAnalyticsModel } from '../models/Analytics';
import { ISessionAnalytics, IDailyAnalytics, PaginatedResult } from '../types';
import { logger } from '../utils/logger';
import { redisClient } from './KnowledgeBaseService';

export class AnalyticsService {
  /**
   * Create session analytics record
   */
  async createSessionAnalytics(sessionId: string): Promise<ISessionAnalytics> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      const duration = session.endedAt
        ? session.endedAt.getTime() - session.startedAt.getTime()
        : Date.now() - session.startedAt.getTime();

      const analytics = new SessionAnalyticsModel({
        sessionId,
        userId: session.userId,
        customerId: session.customerId,
        duration,
        messageCount: session.messages.length,
        turnCount: session.context.turnCount,
        resolutionStatus: this.determineResolutionStatus(session),
        intents: session.tags,
        sentiment: {
          average: this.calculateAverageSentiment(session),
          trend: this.calculateSentimentTrend(session),
        },
        handoffRequired: !!session.assignedAgentId,
        agentId: session.assignedAgentId,
      });

      await analytics.save();
      logger.info(`Created analytics for session ${sessionId}`);

      return analytics;
    } catch (error) {
      logger.error('Error creating session analytics:', error);
      throw error;
    }
  }

  /**
   * Determine resolution status
   */
  private determineResolutionStatus(session: any): 'resolved' | 'escalated' | 'abandoned' {
    if (session.assignedAgentId) {
      return 'escalated';
    }

    if (session.status === 'timeout') {
      return 'abandoned';
    }

    // Check if user seemed satisfied (sentiment improved or stayed positive)
    const avgSentiment = this.calculateAverageSentiment(session);
    if (avgSentiment >= 50) {
      return 'resolved';
    }

    return 'abandoned';
  }

  /**
   * Calculate average sentiment
   */
  private calculateAverageSentiment(session: any): number {
    const sentimentMessages = session.messages.filter((m: any) => m.sentiment !== undefined);
    if (sentimentMessages.length === 0) {
      return session.sentiment?.score || 50;
    }

    const sum = sentimentMessages.reduce((acc: number, m: any) => acc + (m.sentiment?.score || 50), 0);
    return Math.round(sum / sentimentMessages.length);
  }

  /**
   * Calculate sentiment trend
   */
  private calculateSentimentTrend(session: any): 'improving' | 'declining' | 'stable' {
    const messages = session.messages;
    if (messages.length < 2) return 'stable';

    const firstHalf = messages.slice(0, Math.floor(messages.length / 2));
    const secondHalf = messages.slice(Math.floor(messages.length / 2));

    const firstAvg = firstHalf.reduce((acc: number, m: any) => acc + (m.sentiment?.score || 50), 0) / firstHalf.length;
    const secondAvg = secondHalf.reduce((acc: number, m: any) => acc + (m.sentiment?.score || 50), 0) / secondHalf.length;

    const diff = secondAvg - firstAvg;

    if (diff > 10) return 'improving';
    if (diff < -10) return 'declining';
    return 'stable';
  }

  /**
   * Get session analytics
   */
  async getSessionAnalytics(sessionId: string): Promise<ISessionAnalytics | null> {
    try {
      return SessionAnalyticsModel.findOne({ sessionId });
    } catch (error) {
      logger.error('Error getting session analytics:', error);
      return null;
    }
  }

  /**
   * Get customer analytics
   */
  async getCustomerAnalytics(
    customerId: string,
    page: number = 1,
    limit: number = 20
  ): Promise<PaginatedResult<ISessionAnalytics>> {
    try {
      const skip = (page - 1) * limit;

      const [data, total] = await Promise.all([
        SessionAnalyticsModel.find({ customerId })
          .sort({ createdAt: -1 })
          .skip(skip)
          .limit(limit)
          .lean(),
        SessionAnalyticsModel.countDocuments({ customerId }),
      ]);

      return {
        data,
        total,
        page,
        pageSize: limit,
        totalPages: Math.ceil(total / limit),
      };
    } catch (error) {
      logger.error('Error getting customer analytics:', error);
      return {
        data: [],
        total: 0,
        page,
        pageSize: limit,
        totalPages: 0,
      };
    }
  }

  /**
   * Get aggregated analytics for date range
   */
  async getAggregatedAnalytics(
    startDate: Date,
    endDate: Date
  ): Promise<{
    totalSessions: number;
    resolvedSessions: number;
    escalatedSessions: number;
    abandonedSessions: number;
    avgSessionDuration: number;
    avgTurnsPerSession: number;
    avgMessagesPerSession: number;
    resolutionRate: number;
    escalationRate: number;
  }> {
    try {
      const stats = await ChatSessionModel.getSessionStats(startDate, endDate);

      return {
        totalSessions: stats.totalSessions || 0,
        resolvedSessions: stats.resolvedByBot || 0,
        escalatedSessions: stats.escalatedSessions || 0,
        abandonedSessions: stats.totalSessions - stats.resolvedByBot - stats.escalatedSessions || 0,
        avgSessionDuration: 0, // Calculate from analytics
        avgTurnsPerSession: stats.avgTurnsPerSession || 0,
        avgMessagesPerSession: stats.avgMessagesPerSession || 0,
        resolutionRate: stats.totalSessions > 0
          ? (stats.resolvedByBot / stats.totalSessions) * 100
          : 0,
        escalationRate: stats.totalSessions > 0
          ? (stats.escalatedSessions / stats.totalSessions) * 100
          : 0,
      };
    } catch (error) {
      logger.error('Error getting aggregated analytics:', error);
      throw error;
    }
  }

  /**
   * Generate daily analytics
   */
  async generateDailyAnalytics(date: Date = new Date()): Promise<IDailyAnalytics> {
    try {
      const startOfDay = new Date(date);
      startOfDay.setHours(0, 0, 0, 0);

      const endOfDay = new Date(date);
      endOfDay.setHours(23, 59, 59, 999);

      // Get sessions for the day
      const sessions = await ChatSessionModel.find({
        startedAt: { $gte: startOfDay, $lte: endOfDay },
      });

      // Calculate stats
      const totalSessions = sessions.length;
      const activeSessions = sessions.filter(
        s => ['active', 'with_agent', 'waiting_for_agent'].includes(s.status)
      ).length;

      const resolvedByBot = sessions.filter(
        s => !s.assignedAgentId && s.status === 'closed'
      ).length;

      const escalatedToAgent = sessions.filter(s => s.assignedAgentId).length;

      // Get all analytics for the day to calculate averages
      const analyticsData = await SessionAnalyticsModel.find({
        createdAt: { $gte: startOfDay, $lte: endOfDay },
      });

      const avgResolutionTime =
        analyticsData.length > 0
          ? analyticsData.reduce((sum, a) => sum + a.duration, 0) / analyticsData.length
          : 0;

      const avgSessionDuration =
        analyticsData.length > 0
          ? analyticsData.reduce((sum, a) => sum + a.duration, 0) / analyticsData.length
          : 0;

      // Get top intents
      const intentCounts = new Map<string, number>();
      sessions.forEach(session => {
        session.tags.forEach(tag => {
          intentCounts.set(tag, (intentCounts.get(tag) || 0) + 1);
        });
      });

      const topIntents = Array.from(intentCounts.entries())
        .map(([intent, count]) => ({ intent, count }))
        .sort((a, b) => b.count - a.count)
        .slice(0, 10);

      // Calculate sentiment distribution
      let positiveCount = 0;
      let neutralCount = 0;
      let negativeCount = 0;

      sessions.forEach(session => {
        if (session.sentiment?.label === 'positive') positiveCount++;
        else if (session.sentiment?.label === 'negative') negativeCount++;
        else neutralCount++;
      });

      // Language distribution
      const languageDistribution: Record<string, number> = {};
      sessions.forEach(session => {
        const lang = session.language || 'en';
        languageDistribution[lang] = (languageDistribution[lang] || 0) + 1;
      });

      // Check if daily analytics already exists
      let dailyAnalytics = await DailyAnalyticsModel.getTodayAnalytics();

      if (dailyAnalytics) {
        // Update existing
        dailyAnalytics.totalSessions = totalSessions;
        dailyAnalytics.activeSessions = activeSessions;
        dailyAnalytics.resolvedByBot = resolvedByBot;
        dailyAnalytics.escalatedToAgent = escalatedToAgent;
        dailyAnalytics.averageResolutionTime = avgResolutionTime;
        dailyAnalytics.averageSessionDuration = avgSessionDuration;
        dailyAnalytics.topIntents = topIntents;
        dailyAnalytics.sentimentDistribution = {
          positive: positiveCount,
          neutral: neutralCount,
          negative: negativeCount,
        };
        dailyAnalytics.languageDistribution = languageDistribution;

        await dailyAnalytics.save();
      } else {
        // Create new
        dailyAnalytics = new DailyAnalyticsModel({
          date: startOfDay,
          totalSessions,
          activeSessions,
          resolvedByBot,
          escalatedToAgent,
          averageResolutionTime: avgResolutionTime,
          averageSessionDuration: avgSessionDuration,
          topIntents,
          sentimentDistribution: {
            positive: positiveCount,
            neutral: neutralCount,
            negative: negativeCount,
          },
          languageDistribution,
        });

        await dailyAnalytics.save();
      }

      logger.info(`Generated daily analytics for ${startOfDay.toISOString()}`);

      return dailyAnalytics;
    } catch (error) {
      logger.error('Error generating daily analytics:', error);
      throw error;
    }
  }

  /**
   * Get analytics by date range
   */
  async getAnalyticsByDateRange(
    startDate: Date,
    endDate: Date
  ): Promise<IDailyAnalytics[]> {
    try {
      return DailyAnalyticsModel.findByDateRange(startDate, endDate);
    } catch (error) {
      logger.error('Error getting analytics by date range:', error);
      return [];
    }
  }

  /**
   * Get intent usage statistics
   */
  async getIntentStats(
    startDate: Date,
    endDate: Date,
    limit: number = 20
  ): Promise<Array<{ intent: string; count: number; category: string }>> {
    try {
      const sessions = await ChatSessionModel.find({
        startedAt: { $gte: startDate, $lte: endDate },
      });

      const intentMap = new Map<string, { count: number; category: string }>();

      sessions.forEach(session => {
        session.tags.forEach(tag => {
          const existing = intentMap.get(tag);
          if (existing) {
            existing.count++;
          } else {
            intentMap.set(tag, { count: 1, category: tag });
          }
        });
      });

      return Array.from(intentMap.entries())
        .map(([intent, data]) => ({ intent, count: data.count, category: data.category }))
        .sort((a, b) => b.count - a.count)
        .slice(0, limit);
    } catch (error) {
      logger.error('Error getting intent stats:', error);
      return [];
    }
  }

  /**
   * Get customer satisfaction metrics
   */
  async getSatisfactionMetrics(
    startDate: Date,
    endDate: Date
  ): Promise<{
    averageRating: number;
    totalRatings: number;
    ratingDistribution: Record<number, number>;
    resolvedByRating: Record<number, { resolved: number; total: number }>;
  }> {
    try {
      const analytics = await SessionAnalyticsModel.findByDateRange(startDate, endDate);

      const ratings = analytics.filter(a => a.customerSatisfaction !== undefined);

      if (ratings.length === 0) {
        return {
          averageRating: 0,
          totalRatings: 0,
          ratingDistribution: {},
          resolvedByRating: {},
        };
      }

      const totalRating = ratings.reduce((sum, a) => sum + (a.customerSatisfaction || 0), 0);
      const averageRating = totalRating / ratings.length;

      const ratingDistribution: Record<number, number> = {};
      ratings.forEach(a => {
        const rating = a.customerSatisfaction || 0;
        ratingDistribution[rating] = (ratingDistribution[rating] || 0) + 1;
      });

      const resolvedByRating: Record<number, { resolved: number; total: number }> = {};
      ratings.forEach(a => {
        const rating = a.customerSatisfaction || 0;
        if (!resolvedByRating[rating]) {
          resolvedByRating[rating] = { resolved: 0, total: 0 };
        }
        resolvedByRating[rating].total++;
        if (a.resolutionStatus === 'resolved') {
          resolvedByRating[rating].resolved++;
        }
      });

      return {
        averageRating: Math.round(averageRating * 10) / 10,
        totalRatings: ratings.length,
        ratingDistribution,
        resolvedByRating,
      };
    } catch (error) {
      logger.error('Error getting satisfaction metrics:', error);
      throw error;
    }
  }

  /**
   * Export analytics data
   */
  async exportAnalytics(startDate: Date, endDate: Date, format: 'json' | 'csv' = 'json'): Promise<string> {
    try {
      const analytics = await SessionAnalyticsModel.findByDateRange(startDate, endDate);

      if (format === 'csv') {
        const headers = [
          'sessionId',
          'customerId',
          'duration',
          'messageCount',
          'turnCount',
          'resolutionStatus',
          'handoffRequired',
          'sentimentAverage',
          'sentimentTrend',
          'customerSatisfaction',
          'createdAt',
        ].join(',');

        const rows = analytics.map(a => [
          a.sessionId,
          a.customerId || '',
          a.duration,
          a.messageCount,
          a.turnCount,
          a.resolutionStatus,
          a.handoffRequired,
          a.sentiment.average,
          a.sentiment.trend,
          a.customerSatisfaction || '',
          a.createdAt.toISOString(),
        ].join(','));

        return [headers, ...rows].join('\n');
      }

      return JSON.stringify(analytics, null, 2);
    } catch (error) {
      logger.error('Error exporting analytics:', error);
      throw error;
    }
  }
}

export const analyticsService = new AnalyticsService();
