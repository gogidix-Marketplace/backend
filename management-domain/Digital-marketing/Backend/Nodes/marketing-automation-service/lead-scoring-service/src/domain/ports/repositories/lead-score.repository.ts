import { LeadScore } from '../../models/lead-score';
export const LEAD_SCORE_REPOSITORY = Symbol('LEAD_SCORE_REPOSITORY');
export interface ILeadScoreRepository {
  save(score: LeadScore): Promise<LeadScore>;
  findById(id: string): Promise<LeadScore | null>;
  findByLeadId(leadId: string, tenantId: string): Promise<LeadScore | null>;
  findByTenantId(tenantId: string, options?: { minScore?: number; maxScore?: number; page?: number; limit?: number }): Promise<LeadScore[]>;
  findHotLeads(tenantId: string, threshold?: number): Promise<LeadScore[]>;
  update(score: LeadScore): Promise<LeadScore>;
  delete(id: string): Promise<boolean>;
  countByTenant(tenantId: string): Promise<number>;
}
