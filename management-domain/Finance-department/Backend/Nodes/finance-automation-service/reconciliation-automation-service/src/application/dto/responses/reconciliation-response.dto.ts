import { ReconciliationStatus } from '../../../domain/enums/reconciliation-status.enum';
import { MatchType } from '../../../domain/enums/match-type.enum';
import { DifferenceStatus } from '../../../domain/enums/difference-status.enum';

export class ReconciliationResponseDto {
  id: string;
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
  metadata?: Record<string, unknown>;
  createdAt: Date;
  updatedAt: Date;
}

export class ReconciliationListResponseDto {
  data: ReconciliationResponseDto[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}

export class ReconciliationStatsResponseDto {
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

export class MatchResponseDto {
  id: string;
  reconciliationId: string;
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
    description?: string;
    counterparty?: string;
  };
  internalTransaction?: {
    id: string;
    amount: number;
    currency: string;
    date: Date;
    reference?: string;
    description?: string;
    counterparty?: string;
  };
}

export class DifferenceResponseDto {
  id: string;
  reconciliationId: string;
  tenantId: string;
  bankTransactionId?: string;
  internalTransactionId?: string;
  differenceType: string;
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
  suggestedActions?: string[];
}

export class RuleResponseDto {
  id: string;
  tenantId: string;
  name: string;
  description?: string;
  matchType: MatchType;
  priority: number;
  enabled: boolean;
  conditions: RuleConditionResponseDto[];
  actions: RuleActionResponseDto[];
  confidenceThreshold?: number;
  createdBy: string;
  createdAt: Date;
  updatedAt: Date;
}

export class RuleConditionResponseDto {
  field: string;
  operator: string;
  value: unknown;
  weight?: number;
}

export class RuleActionResponseDto {
  type: string;
  params?: Record<string, unknown>;
}

export class AuditLogResponseDto {
  id: string;
  reconciliationId: string;
  action: string;
  userId: string;
  timestamp: Date;
  details?: Record<string, unknown>;
  ipAddress?: string;
}
