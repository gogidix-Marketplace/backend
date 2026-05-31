import { UUID } from 'crypto';
import { ReconciliationStatus } from '../../enums/reconciliation-status.enum';
import { MatchType } from '../../enums/match-type.enum';
import { DifferenceStatus } from '../../enums/difference-status.enum';
import { DifferenceType } from '../../models/transaction-difference.entity';

export interface IReconciliationQueryHandler {
  getReconciliationById(query: GetReconciliationQuery): Promise<ReconciliationDto | null>;
  getReconciliationsByTenant(query: GetReconciliationsByTenantQuery): Promise<ReconciliationDto[]>;
  getReconciliationsByStatus(query: GetReconciliationsByStatusQuery): Promise<ReconciliationDto[]>;
  searchReconciliations(query: SearchReconciliationsQuery): Promise<PaginatedResult<ReconciliationDto>>;
  getReconciliationStats(query: GetReconciliationStatsQuery): Promise<ReconciliationStats>;
  getMatchesByReconciliation(query: GetMatchesByReconciliationQuery): Promise<MatchDto[]>;
  getMatchById(query: GetMatchByIdQuery): Promise<MatchDto | null>;
  getDifferencesByReconciliation(query: GetDifferencesByReconciliationQuery): Promise<DifferenceDto[]>;
  getDifferenceById(query: GetDifferenceByIdQuery): Promise<DifferenceDto | null>;
  getUnresolvedDifferences(query: GetUnresolvedDifferencesQuery): Promise<DifferenceDto[]>;
  getRuleById(query: GetRuleByIdQuery): Promise<RuleDto | null>;
  getRulesByTenant(query: GetRulesByTenantQuery): Promise<RuleDto[]>;
  getActiveRules(query: GetActiveRulesQuery): Promise<RuleDto[]>;
  getReconciliationAuditTrail(query: GetReconciliationAuditTrailQuery): Promise<AuditLogDto[]>;
}

export class GetReconciliationQuery {
  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
  ) {}
}

export class GetReconciliationsByTenantQuery {
  constructor(
    public readonly tenantId: string,
    public readonly page: number = 1,
    public readonly limit: number = 20,
    public readonly sortBy: string = 'createdAt',
    public readonly sortOrder: 'asc' | 'desc' = 'desc',
  ) {}
}

export class GetReconciliationsByStatusQuery {
  constructor(
    public readonly tenantId: string,
    public readonly status: ReconciliationStatus,
    public readonly page: number = 1,
    public readonly limit: number = 20,
  ) {}
}

export class SearchReconciliationsQuery {
  constructor(
    public readonly tenantId: string,
    public readonly searchTerm?: string,
    public readonly status?: ReconciliationStatus,
    public readonly dataSourceType?: string,
    public readonly startDate?: Date,
    public readonly endDate?: Date,
    public readonly page: number = 1,
    public readonly limit: number = 20,
  ) {}
}

export class GetReconciliationStatsQuery {
  constructor(
    public readonly tenantId: string,
    public readonly period?: 'day' | 'week' | 'month' | 'year' | 'all',
    public readonly startDate?: Date,
    public readonly endDate?: Date,
  ) {}
}

export class GetMatchesByReconciliationQuery {
  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly matchType?: MatchType,
    public readonly minConfidence?: number,
    public readonly page: number = 1,
    public readonly limit: number = 50,
  ) {}
}

export class GetMatchByIdQuery {
  constructor(
    public readonly matchId: UUID,
    public readonly tenantId: string,
  ) {}
}

export class GetDifferencesByReconciliationQuery {
  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly status?: DifferenceStatus,
    public readonly differenceType?: DifferenceType,
    public readonly severity?: string,
    public readonly page: number = 1,
    public readonly limit: number = 50,
  ) {}
}

export class GetDifferenceByIdQuery {
  constructor(
    public readonly differenceId: UUID,
    public readonly tenantId: string,
  ) {}
}

export class GetUnresolvedDifferencesQuery {
  constructor(
    public readonly tenantId: string,
    public readonly severity?: string,
    public readonly overdueOnly?: boolean,
    public readonly page: number = 1,
    public readonly limit: number = 50,
  ) {}
}

export class GetRuleByIdQuery {
  constructor(
    public readonly ruleId: UUID,
    public readonly tenantId: string,
  ) {}
}

export class GetRulesByTenantQuery {
  constructor(
    public readonly tenantId: string,
    public readonly enabledOnly?: boolean,
    public readonly matchType?: MatchType,
  ) {}
}

export class GetActiveRulesQuery {
  constructor(
    public readonly tenantId: string,
  ) {}
}

export class GetReconciliationAuditTrailQuery {
  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly page: number = 1,
    public readonly limit: number = 50,
  ) {}
}

export interface ReconciliationDto {
  id: UUID;
  tenantId: string;
  name: string;
  description?: string;
  status: ReconciliationStatus;
  startDate: Date;
  endDate?: Date;
  dataSourceType: string;
  bankAccountId?: string;
  internalAccountId?: string;
  ruleIds: string[];
  totalTransactionsProcessed: number;
  totalMatches: number;
  totalDifferences: number;
  autoResolvedCount: number;
  manualReviewCount: number;
  createdBy: string;
  completedBy?: string;
  completedAt?: Date;
  errorDetails?: string;
  scheduled?: boolean;
  scheduleExpression?: string;
  lastRunAt?: Date;
  nextRunAt?: Date;
  createdAt: Date;
  updatedAt: Date;
}

export interface MatchDto {
  id: UUID;
  reconciliationId: UUID;
  tenantId: string;
  bankTransactionId?: string;
  internalTransactionId?: string;
  matchType: MatchType;
  confidence: number;
  matchDate: Date;
  matchedBy: string;
  verified?: boolean;
  verifiedBy?: string;
  verifiedAt?: Date;
  notes?: string;
  bankTransaction?: {
    id: string;
    amount: number;
    currency: string;
    date: Date;
    reference?: string;
  };
  internalTransaction?: {
    id: string;
    amount: number;
    currency: string;
    date: Date;
    reference?: string;
  };
}

export interface DifferenceDto {
  id: UUID;
  reconciliationId: UUID;
  tenantId: string;
  bankTransactionId?: string;
  internalTransactionId?: string;
  differenceType: DifferenceType;
  status: DifferenceStatus;
  bankAmount?: number;
  internalAmount?: number;
  amountDifference?: number;
  currency?: string;
  bankDate?: Date;
  internalDate?: Date;
  description?: string;
  detectedAt: Date;
  resolvedAt?: Date;
  resolvedBy?: string;
  resolutionNotes?: string;
  autoResolution?: {
    applied: boolean;
    rule?: string;
    action?: string;
    timestamp?: Date;
  };
  severity: string;
  assignee?: string;
  dueDate?: Date;
  isOverdue: boolean;
}

export interface RuleDto {
  id: UUID;
  tenantId: string;
  name: string;
  description?: string;
  matchType: MatchType;
  priority: number;
  enabled: boolean;
  conditions: RuleConditionDto[];
  actions: RuleActionDto[];
  confidenceThreshold?: number;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

export interface RuleConditionDto {
  field: string;
  operator: string;
  value: unknown;
  weight?: number;
}

export interface RuleActionDto {
  type: string;
  params?: Record<string, unknown>;
}

export interface ReconciliationStats {
  totalReconciliations: number;
  completedReconciliations: number;
  failedReconciliations: number;
  pendingReconciliations: number;
  runningReconciliations: number;
  averageProcessingTime: number;
  totalMatches: number;
  totalDifferences: number;
  autoResolutionRate: number;
  byStatus: Record<ReconciliationStatus, number>;
  byDay: Array<{
    date: string;
    count: number;
    matches: number;
    differences: number;
  }>;
}

export interface AuditLogDto {
  id: UUID;
  reconciliationId: UUID;
  action: string;
  userId: string;
  timestamp: Date;
  details?: Record<string, unknown>;
  ipAddress?: string;
}

export interface PaginatedResult<T> {
  data: T[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}
