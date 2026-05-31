import { ScheduledPost } from '../models/scheduled-post';
export const SCHEDULED_POST_REPOSITORY = Symbol('SCHEDULED_POST_REPOSITORY');
export interface IScheduledPostRepository {
  save(post: ScheduledPost): Promise<ScheduledPost>;
  findById(id: string): Promise<ScheduledPost | null>;
  findByTenantId(tenantId: string, options?: { status?: string; platform?: string; page?: number; limit?: number }): Promise<ScheduledPost[]>;
  findReadyToPublish(): Promise<ScheduledPost[]>;
  findByAccountId(accountId: string): Promise<ScheduledPost[]>;
  update(post: ScheduledPost): Promise<ScheduledPost>;
  delete(id: string): Promise<boolean>;
  countByTenant(tenantId: string, status?: string): Promise<number>;
}
