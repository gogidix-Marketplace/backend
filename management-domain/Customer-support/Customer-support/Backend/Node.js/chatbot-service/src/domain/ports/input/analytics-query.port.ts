import { SessionAnalyticsProps, DailyAnalyticsProps } from '../../models';

export interface IAnalyticsQueryPort {
  getSessionAnalytics(sessionId: string): Promise<SessionAnalyticsProps | null>;
  getCustomerAnalytics(customerId: string, page?: number, limit?: number): Promise<{ data: SessionAnalyticsProps[]; total: number; page: number; pageSize: number; totalPages: number }>;
  getAggregatedAnalytics(startDate: Date, endDate: Date): Promise<any>;
  getAnalyticsByDateRange(startDate: Date, endDate: Date): Promise<DailyAnalyticsProps[]>;
  getIntentStats(startDate: Date, endDate: Date, limit?: number): Promise<Array<{ intent: string; count: number; category: string }>>;
  getSatisfactionMetrics(startDate: Date, endDate: Date): Promise<any>;
  getHandoffStats(startDate: Date, endDate: Date): Promise<any>;
  exportAnalytics(startDate: Date, endDate: Date, format?: 'json' | 'csv'): Promise<string>;
  getDashboardSummary(): Promise<any>;
  getRealtimeStats(): Promise<{ activeSessions: number; waitingForAgent: number; withAgent: number; timestamp: Date }>;
}
