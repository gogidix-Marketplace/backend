import { EmailJob } from '../models/email-job';
export const EMAIL_JOB_REPOSITORY = Symbol('EMAIL_JOB_REPOSITORY');
export interface IEmailJobRepository {
  save(job: EmailJob): Promise<EmailJob>;
  findById(id: string): Promise<EmailJob | null>;
  findByTenantId(tenantId: string, options?: { status?: string; page?: number; limit?: number }): Promise<EmailJob[]>;
  findByStatus(status: string): Promise<EmailJob[]>;
  findScheduledReady(): Promise<EmailJob[]>;
  update(job: EmailJob): Promise<EmailJob>;
  delete(id: string): Promise<boolean>;
  countByTenant(tenantId: string, status?: string): Promise<number>;
}
