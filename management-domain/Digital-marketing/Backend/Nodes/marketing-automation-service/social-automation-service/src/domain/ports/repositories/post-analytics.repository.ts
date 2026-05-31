import { PostAnalytics } from '../models/post-analytics';
export const POST_ANALYTICS_REPOSITORY = Symbol('POST_ANALYTICS_REPOSITORY');
export interface IPostAnalyticsRepository {
  save(analytics: PostAnalytics): Promise<PostAnalytics>;
  findByAccountAndDateRange(accountId: string, startDate: Date, endDate: Date): Promise<PostAnalytics[]>;
  findByPostId(postId: string): Promise<PostAnalytics[]>;
  findByTenantId(tenantId: string, options?: { platform?: string; startDate?: Date; endDate?: Date }): Promise<PostAnalytics[]>;
  update(analytics: PostAnalytics): Promise<PostAnalytics>;
}
