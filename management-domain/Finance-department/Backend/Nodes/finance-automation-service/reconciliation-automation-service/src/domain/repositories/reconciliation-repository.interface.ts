import { UUID } from 'crypto';
import { Reconciliation, ReconciliationProps } from '../models/reconciliation.entity';
import { ReconciliationRule, ReconciliationRuleProps } from '../models/reconciliation-rule.entity';
import { ReconciliationMatch, ReconciliationMatchProps } from '../models/reconciliation-match.entity';
import { TransactionDifference, TransactionDifferenceProps } from '../models/transaction-difference.entity';
import { ReconciliationStatus } from '../enums/reconciliation-status.enum';
import { MatchType } from '../enums/match-type.enum';
import { DifferenceStatus } from '../../domain/enums/difference-status.enum';
import { DifferenceType } from '../models/transaction-difference.entity';

export interface IReconciliationRepository {
  save(reconciliation: Reconciliation): Promise<Reconciliation>;
  findById(id: UUID, tenantId: string): Promise<Reconciliation | null>;
  findByTenant(tenantId: string, options?: FindOptions): Promise<Reconciliation[]>;
  findByStatus(status: ReconciliationStatus, tenantId: string): Promise<Reconciliation[]>;
  findScheduledReconciliations(): Promise<Reconciliation[]>;
  delete(id: UUID, tenantId: string): Promise<void>;
  update(reconciliation: Reconciliation): Promise<Reconciliation>;
  countByTenant(tenantId: string): Promise<number>;
  countByStatus(tenantId: string, status: ReconciliationStatus): Promise<number>;
  search(tenantId: string, filters: SearchFilters): Promise<Reconciliation[]>;
  getStats(tenantId: string, period?: StatsPeriod): Promise<ReconciliationStats>;
}

export interface IReconciliationRuleRepository {
  save(rule: ReconciliationRule): Promise<ReconciliationRule>;
  findById(id: UUID, tenantId: string): Promise<ReconciliationRule | null>;
  findByTenant(tenantId: string, options?: RuleFindOptions): Promise<ReconciliationRule[]>;
  findActive(tenantId: string): Promise<ReconciliationRule[]>;
  findByMatchType(matchType: MatchType, tenantId: string): Promise<ReconciliationRule[]>;
  delete(id: UUID, tenantId: string): Promise<void>;
  update(rule: ReconciliationRule): Promise<ReconciliationRule>;
  enable(id: UUID, tenantId: string): Promise<void>;
  disable(id: UUID, tenantId: string): Promise<void>;
}

export interface IReconciliationMatchRepository {
  save(match: ReconciliationMatch): Promise<ReconciliationMatch>;
  findById(id: UUID, tenantId: string): Promise<ReconciliationMatch | null>;
  findByReconciliation(reconciliationId: UUID, tenantId: string, options?: MatchFindOptions): Promise<ReconciliationMatch[]>;
  findByTransactions(bankTransactionId: string, internalTransactionId: string, tenantId: string): Promise<ReconciliationMatch[]>;
  delete(id: UUID, tenantId: string): Promise<void>;
  update(match: ReconciliationMatch): Promise<ReconciliationMatch>;
  countByReconciliation(reconciliationId: UUID): Promise<number>;
}

export interface ITransactionDifferenceRepository {
  save(difference: TransactionDifference): Promise<TransactionDifference>;
  findById(id: UUID, tenantId: string): Promise<TransactionDifference | null>;
  findByReconciliation(reconciliationId: UUID, tenantId: string, options?: DifferenceFindOptions): Promise<TransactionDifference[]>;
  findByStatus(status: DifferenceStatus, tenantId: string): Promise<TransactionDifference[]>;
  findUnresolved(tenantId: string, options?: UnresolvedOptions): Promise<TransactionDifference[]>;
  findOverdue(tenantId: string): Promise<TransactionDifference[]>;
  delete(id: UUID, tenantId: string): Promise<void>;
  update(difference: TransactionDifference): Promise<TransactionDifference>;
  countByReconciliation(reconciliationId: UUID): Promise<number>;
  countByStatus(tenantId: string, status: DifferenceStatus): Promise<number>;
}

export interface FindOptions {
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

export interface RuleFindOptions extends FindOptions {
  enabledOnly?: boolean;
  matchType?: MatchType;
}

export interface MatchFindOptions extends FindOptions {
  matchType?: MatchType;
  minConfidence?: number;
  verified?: boolean;
}

export interface DifferenceFindOptions extends FindOptions {
  status?: DifferenceStatus;
  differenceType?: DifferenceType;
  severity?: string;
}

export interface UnresolvedOptions extends FindOptions {
  severity?: string;
  overdueOnly?: boolean;
}

export interface SearchFilters {
  searchTerm?: string;
  status?: ReconciliationStatus;
  dataSourceType?: string;
  startDate?: Date;
  endDate?: Date;
  createdBy?: string;
}

export interface StatsPeriod {
  type: 'day' | 'week' | 'month' | 'year' | 'all';
  startDate?: Date;
  endDate?: Date;
}

export interface ReconciliationStats {
  total: number;
  byStatus: Record<ReconciliationStatus, number>;
  totalMatches: number;
  totalDifferences: number;
  autoResolvedCount: number;
  manualReviewCount: number;
  averageProcessingTime: number;
  byDay: Array<{
    date: string;
    count: number;
    matches: number;
    differences: number;
  }>;
}

export interface AuditLog {
  id: UUID;
  reconciliationId: UUID;
  action: string;
  userId: string;
  timestamp: Date;
  details?: Record<string, unknown>;
  ipAddress?: string;
}

export interface IAuditLogRepository {
  save(log: Omit<AuditLog, 'id'>): Promise<AuditLog>;
  findByReconciliation(reconciliationId: UUID, options?: FindOptions): Promise<AuditLog[]>;
  findByUser(userId: string, tenantId: string, options?: FindOptions): Promise<AuditLog[]>;
  findByAction(action: string, tenantId: string, options?: FindOptions): Promise<AuditLog[]>;
}
