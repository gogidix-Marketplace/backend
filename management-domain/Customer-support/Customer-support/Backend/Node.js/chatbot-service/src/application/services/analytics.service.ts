import { Injectable, Inject, Logger } from '@nestjs/common';
import { IAnalyticsCommandPort, IAnalyticsQueryPort } from '@domain/ports/input';
import { IChatSessionRepository, ISessionAnalyticsRepository, IDailyAnalyticsRepository } from '@domain/ports/output';
import { SessionAnalyticsProps, DailyAnalyticsProps } from '@domain/models';

@Injectable()
export class AnalyticsApplicationService implements IAnalyticsCommandPort, IAnalyticsQueryPort {
  private readonly logger = new Logger(AnalyticsApplicationService.name);

  constructor(
    @Inject('IChatSessionRepository')
    private readonly sessionRepo: IChatSessionRepository,
    @Inject('ISessionAnalyticsRepository')
    private readonly sessionAnalyticsRepo: ISessionAnalyticsRepository,
    @Inject('IDailyAnalyticsRepository')
    private readonly dailyAnalyticsRepo: IDailyAnalyticsRepository,
  ) {}

  async createSessionAnalytics(sessionId: string): Promise<SessionAnalyticsProps> {
    const session = await this.sessionRepo.findOne(sessionId);
    if (!session) throw new Error(`Session ${sessionId} not found`);

    const duration = session.endedAt
      ? new Date(session.endedAt).getTime() - new Date(session.startedAt).getTime()
      : Date.now() - new Date(session.startedAt).getTime();

    const analytics = await this.sessionAnalyticsRepo.save({
      sessionId,
      userId: session.userId,
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

    this.logger.log(`Created analytics for session ${sessionId}`);
    return analytics;
  }

  async generateDailyAnalytics(date: Date = new Date()): Promise<DailyAnalyticsProps> {
    const startOfDay = new Date(date);
    startOfDay.setHours(0, 0, 0, 0);
    const endOfDay = new Date(date);
    endOfDay.setHours(23, 59, 59, 999);

    const stats = await this.sessionRepo.getSessionStats(startOfDay, endOfDay);

    let dailyAnalytics = await this.dailyAnalyticsRepo.getTodayAnalytics();

    const data: DailyAnalyticsProps = {
      date: startOfDay,
      totalSessions: stats.totalSessions || 0,
      activeSessions: stats.activeSessions || 0,
      resolvedByBot: stats.resolvedByBot || 0,
      escalatedToAgent: stats.escalatedSessions || 0,
      averageResolutionTime: 0,
      averageSessionDuration: 0,
      topIntents: [],
      sentimentDistribution: { positive: 0, neutral: 0, negative: 0 },
      languageDistribution: {},
    };

    if (dailyAnalytics) {
      Object.assign(dailyAnalytics, data);
      await this.dailyAnalyticsRepo.save(dailyAnalytics);
    } else {
      dailyAnalytics = await this.dailyAnalyticsRepo.save(data);
    }

    return dailyAnalytics;
  }

  async getSessionAnalytics(sessionId: string): Promise<SessionAnalyticsProps | null> {
    try {
      return await this.sessionAnalyticsRepo.findOne({ sessionId });
    } catch {
      return null;
    }
  }

  async getCustomerAnalytics(customerId: string, page: number = 1, limit: number = 20) {
    const skip = (page - 1) * limit;
    const [data, total] = await Promise.all([
      this.sessionAnalyticsRepo.find({ customerId }),
      this.sessionAnalyticsRepo.countDocuments({ customerId }),
    ]);
    return { data, total, page, pageSize: limit, totalPages: Math.ceil(total / limit) };
  }

  async getAggregatedAnalytics(startDate: Date, endDate: Date) {
    const stats = await this.sessionRepo.getSessionStats(startDate, endDate);
    return {
      totalSessions: stats.totalSessions || 0,
      resolvedSessions: stats.resolvedByBot || 0,
      escalatedSessions: stats.escalatedSessions || 0,
      abandonedSessions: (stats.totalSessions - stats.resolvedByBot - stats.escalatedSessions) || 0,
      avgSessionDuration: 0,
      avgTurnsPerSession: stats.avgTurnsPerSession || 0,
      avgMessagesPerSession: stats.avgMessagesPerSession || 0,
      resolutionRate: stats.totalSessions > 0 ? ((stats.resolvedByBot / stats.totalSessions) * 100) : 0,
      escalationRate: stats.totalSessions > 0 ? ((stats.escalatedSessions / stats.totalSessions) * 100) : 0,
    };
  }

  async getAnalyticsByDateRange(startDate: Date, endDate: Date): Promise<any[]> {
    try {
      return await this.dailyAnalyticsRepo.findByDateRange(startDate, endDate);
    } catch {
      return [];
    }
  }

  async getIntentStats(startDate: Date, endDate: Date, limit: number = 20) {
    const sessions = await this.sessionRepo.find({ startedAt: { $gte: startDate, $lte: endDate } } as any);
    const intentMap = new Map<string, { count: number; category: string }>();
    sessions.forEach(session => {
      session.tags.forEach(tag => {
        const existing = intentMap.get(tag);
        if (existing) existing.count++;
        else intentMap.set(tag, { count: 1, category: tag });
      });
    });
    return Array.from(intentMap.entries())
      .map(([intent, data]) => ({ intent, count: data.count, category: data.category }))
      .sort((a, b) => b.count - a.count)
      .slice(0, limit);
  }

  async getSatisfactionMetrics(startDate: Date, endDate: Date) {
    const analytics = await this.sessionAnalyticsRepo.findByDateRange(startDate, endDate);
    const ratings = analytics.filter((a: any) => a.customerSatisfaction !== undefined);
    if (ratings.length === 0) return { averageRating: 0, totalRatings: 0, ratingDistribution: {}, resolvedByRating: {} };

    const totalRating = ratings.reduce((sum: number, a: any) => sum + (a.customerSatisfaction || 0), 0);
    const averageRating = totalRating / ratings.length;
    const ratingDistribution: Record<number, number> = {};
    ratings.forEach((a: any) => {
      const rating = a.customerSatisfaction || 0;
      ratingDistribution[rating] = (ratingDistribution[rating] || 0) + 1;
    });

    return { averageRating: Math.round(averageRating * 10) / 10, totalRatings: ratings.length, ratingDistribution, resolvedByRating: {} };
  }

  async getHandoffStats(startDate: Date, endDate: Date) {
    const sessions = await this.sessionRepo.find({
      status: { $in: ['with_agent', 'closed'] },
      startedAt: { $gte: startDate, $lte: endDate },
      handoffRequest: { $exists: true },
    } as any);

    const totalHandoffs = sessions.length;
    const acceptedHandoffs = sessions.filter((s: any) => s.status === 'with_agent' || s.assignedAgentId).length;
    const handoffsByPriority: Record<string, number> = { low: 0, medium: 0, high: 0, urgent: 0 };

    sessions.forEach((session: any) => {
      if (session.handoffRequest) {
        handoffsByPriority[session.handoffRequest.priority] = (handoffsByPriority[session.handoffRequest.priority] || 0) + 1;
      }
    });

    return { totalHandoffs, acceptedHandoffs, rejectedHandoffs: totalHandoffs - acceptedHandoffs, averageWaitTime: 0, handoffsByPriority, handoffsByReason: [] };
  }

  async exportAnalytics(startDate: Date, endDate: Date, format: 'json' | 'csv' = 'json'): Promise<string> {
    const analytics = await this.sessionAnalyticsRepo.findByDateRange(startDate, endDate);
    if (format === 'csv') {
      const headers = ['sessionId', 'duration', 'messageCount', 'turnCount', 'resolutionStatus', 'handoffRequired', 'customerSatisfaction'].join(',');
      const rows = analytics.map((a: any) => [a.sessionId, a.duration, a.messageCount, a.turnCount, a.resolutionStatus, a.handoffRequired, a.customerSatisfaction || ''].join(','));
      return [headers, ...rows].join('\n');
    }
    return JSON.stringify(analytics, null, 2);
  }

  async getDashboardSummary() {
    const now = new Date();
    const startOfDay = new Date(now.setHours(0, 0, 0, 0));
    const startOfWeek = new Date(now.setDate(now.getDate() - 7));

    const [todayAnalytics, weekAnalytics] = await Promise.all([
      this.getAggregatedAnalytics(startOfDay, new Date()),
      this.getAggregatedAnalytics(startOfWeek, new Date()),
    ]);

    const recentIntents = await this.getIntentStats(startOfWeek, new Date(), 5);
    const satisfaction = await this.getSatisfactionMetrics(startOfWeek, new Date());

    return {
      today: todayAnalytics,
      week: weekAnalytics,
      topIntents: recentIntents,
      satisfaction,
    };
  }

  async getRealtimeStats() {
    const activeCount = await this.sessionRepo.countDocuments({ status: { $in: ['active', 'waiting_for_agent', 'with_agent'] } });
    const waitingForAgent = await this.sessionRepo.countDocuments({ status: 'waiting_for_agent' });
    const withAgent = await this.sessionRepo.countDocuments({ status: 'with_agent' });

    return { activeSessions: activeCount, waitingForAgent, withAgent, timestamp: new Date() };
  }

  private determineResolutionStatus(session: any): 'resolved' | 'escalated' | 'abandoned' {
    if (session.assignedAgentId) return 'escalated';
    if (session.status === 'timeout') return 'abandoned';
    return 'resolved';
  }

  private calculateAverageSentiment(session: any): number {
    return session.sentiment?.score || 50;
  }

  private calculateSentimentTrend(session: any): 'improving' | 'declining' | 'stable' {
    return 'stable';
  }
}
