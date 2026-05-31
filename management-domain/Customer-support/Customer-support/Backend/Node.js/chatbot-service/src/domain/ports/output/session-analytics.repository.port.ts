import { SessionAnalyticsProps } from '../../models';

export interface ISessionAnalyticsRepository {
  findOne(filter: any): Promise<SessionAnalyticsProps | null>;
  find(filter: any): Promise<any[]>;
  findByDateRange(startDate: Date, endDate: Date): Promise<any[]>;
  findByResolutionStatus(status: 'resolved' | 'escalated' | 'abandoned'): Promise<any[]>;
  getAverageSatisfaction(): Promise<{ averageSatisfaction: number; totalRatings: number }>;
  getResolutionStats(): Promise<any[]>;
  countDocuments(filter: any): Promise<number>;
  save(data: any): Promise<any>;
}
