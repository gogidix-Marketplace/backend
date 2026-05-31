import { BounceRecord } from '../models/bounce-record';
export const BOUNCE_RECORD_REPOSITORY = Symbol('BOUNCE_RECORD_REPOSITORY');
export interface IBounceRecordRepository {
  save(record: BounceRecord): Promise<BounceRecord>;
  findByEmail(email: string): Promise<BounceRecord[]>;
  findByTenantId(tenantId: string, options?: { bounceType?: string }): Promise<BounceRecord[]>;
  countByTenant(tenantId: string): Promise<number>;
}
