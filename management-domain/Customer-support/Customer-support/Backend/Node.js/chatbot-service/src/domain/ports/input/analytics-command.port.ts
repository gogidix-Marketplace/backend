import { SessionAnalyticsProps, DailyAnalyticsProps } from '../../models';

export interface IAnalyticsCommandPort {
  createSessionAnalytics(sessionId: string): Promise<SessionAnalyticsProps>;
  generateDailyAnalytics(date?: Date): Promise<DailyAnalyticsProps>;
}
