import { DailyAnalyticsProps } from '../../models';

export interface IDailyAnalyticsRepository {
  findByDateRange(startDate: Date, endDate: Date): Promise<any[]>;
  getTodayAnalytics(): Promise<DailyAnalyticsProps | null>;
  save(data: any): Promise<any>;
}
