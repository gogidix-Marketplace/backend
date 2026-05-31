import { LeadScore } from '../../entities/lead-score.entity';

export interface LeadScoreRepositoryPort {
  save(leadScore: LeadScore): Promise<LeadScore>;
  findById(id: string): Promise<LeadScore | null>;
  findByLeadId(leadId: string): Promise<LeadScore | null>;
  findByTenantId(tenantId: string, options?: PaginationOptions): Promise<LeadScore[]>;
  findByLeadIdAndTenantId(leadId: string, tenantId: string): Promise<LeadScore | null>;
  findByScoreModelId(scoreModelId: string, options?: PaginationOptions): Promise<LeadScore[]>;
  findQualifiedLeads(tenantId: string, minScore: number, options?: PaginationOptions): Promise<LeadScore[]>;
  findLeadsNeedingRescoring(tenantId: string, thresholdDays: number): Promise<LeadScore[]>;
  findByGrade(tenantId: string, grade: string, options?: PaginationOptions): Promise<LeadScore[]>;
  findByVariant(tenantId: string, variantId: string, options?: PaginationOptions): Promise<LeadScore[]>;
  delete(id: string): Promise<void>;
  deleteByLeadId(leadId: string): Promise<void>;
  exists(leadId: string, tenantId: string): Promise<boolean>;
  count(tenantId: string): Promise<number>;
  countByGrade(tenantId: string, grade: string): Promise<number>;
  getScoreStatistics(tenantId: string): Promise<ScoreStatistics>;
  updateMany(leadScores: LeadScore[]): Promise<void>;
  getScoreTrends(tenantId: string, leadId?: string, days?: number, granularity?: string): Promise<any[]>;
  getScoreHistory(leadId: string, tenantId: string, limit?: number): Promise<any[]>;
}

export interface PaginationOptions {
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

export interface ScoreStatistics {
  totalLeads: number;
  averageScore: number;
  scoreDistribution: Record<string, number>;
  gradeDistribution: Record<string, number>;
  topPerformers: Array<{ leadId: string; score: number }>;
}
