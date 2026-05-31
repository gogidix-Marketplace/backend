import { ComplaintRecord } from '../models/complaint-record';
export const COMPLAINT_RECORD_REPOSITORY = Symbol('COMPLAINT_RECORD_REPOSITORY');
export interface IComplaintRecordRepository {
  save(record: ComplaintRecord): Promise<ComplaintRecord>;
  findByEmail(email: string): Promise<ComplaintRecord[]>;
  findByTenantId(tenantId: string): Promise<ComplaintRecord[]>;
  countByTenant(tenantId: string): Promise<number>;
}
