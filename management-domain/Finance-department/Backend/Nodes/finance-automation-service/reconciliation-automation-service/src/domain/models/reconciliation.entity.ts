import { UUID } from 'crypto';
import { ReconciliationStatus } from '../enums/reconciliation-status.enum';
import { ReconciliationMatch } from './reconciliation-match.entity';
import { TransactionDifference } from './transaction-difference.entity';

export interface ReconciliationProps {
  id?: UUID;
  tenantId: string;
  name: string;
  description?: string;
  status: ReconciliationStatus;
  startDate: Date;
  endDate?: Date;
  dataSourceType: 'BANK' | 'INTERNAL' | 'BOTH';
  bankAccountId?: string;
  internalAccountId?: string;
  ruleIds: string[];
  matches: ReconciliationMatch[];
  differences: TransactionDifference[];
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

export class Reconciliation {
  readonly props: ReconciliationProps;

  constructor(props: ReconciliationProps) {
    this.props = {
      ...props,
      id: props.id || crypto.randomUUID(),
      createdAt: props.createdAt || new Date(),
      updatedAt: props.updatedAt || new Date(),
      matches: props.matches || [],
      differences: props.differences || [],
      totalTransactionsProcessed: props.totalTransactionsProcessed || 0,
      totalMatches: props.totalMatches || 0,
      totalDifferences: props.totalDifferences || 0,
      autoResolvedCount: props.autoResolvedCount || 0,
      manualReviewCount: props.manualReviewCount || 0,
    };
  }

  get id(): UUID {
    return this.props.id!;
  }

  get tenantId(): string {
    return this.props.tenantId;
  }

  get status(): ReconciliationStatus {
    return this.props.status;
  }

  get name(): string {
    return this.props.name;
  }

  start(): void {
    if (this.props.status !== ReconciliationStatus.PENDING) {
      throw new Error('Reconciliation can only be started from PENDING status');
    }
    this.props.status = ReconciliationStatus.RUNNING;
    this.props.startDate = new Date();
    this.props.updatedAt = new Date();
  }

  complete(): void {
    if (this.props.status !== ReconciliationStatus.RUNNING) {
      throw new Error('Reconciliation can only be completed from RUNNING status');
    }
    this.props.status = this.props.totalDifferences === 0
      ? ReconciliationStatus.COMPLETED
      : ReconciliationStatus.PARTIAL;
    this.props.endDate = new Date();
    this.props.completedAt = new Date();
    this.props.updatedAt = new Date();
  }

  fail(error: string): void {
    this.props.status = ReconciliationStatus.FAILED;
    this.props.errorDetails = error;
    this.props.endDate = new Date();
    this.props.updatedAt = new Date();
  }

  addMatch(match: ReconciliationMatch): void {
    this.props.matches.push(match);
    this.props.totalMatches++;
    this.props.updatedAt = new Date();
  }

  addDifference(difference: TransactionDifference): void {
    this.props.differences.push(difference);
    this.props.totalDifferences++;
    this.props.updatedAt = new Date();
  }

  incrementProcessedCount(count: number = 1): void {
    this.props.totalTransactionsProcessed += count;
    this.props.updatedAt = new Date();
  }

  updateSchedule(expression: string, nextRun: Date): void {
    this.props.scheduleExpression = expression;
    this.props.nextRunAt = nextRun;
    this.props.scheduled = true;
    this.props.updatedAt = new Date();
  }

  toJSON() {
    return {
      id: this.props.id,
      tenantId: this.props.tenantId,
      name: this.props.name,
      description: this.props.description,
      status: this.props.status,
      startDate: this.props.startDate,
      endDate: this.props.endDate,
      dataSourceType: this.props.dataSourceType,
      bankAccountId: this.props.bankAccountId,
      internalAccountId: this.props.internalAccountId,
      ruleIds: this.props.ruleIds,
      matches: this.props.matches,
      differences: this.props.differences,
      totalTransactionsProcessed: this.props.totalTransactionsProcessed,
      totalMatches: this.props.totalMatches,
      totalDifferences: this.props.totalDifferences,
      autoResolvedCount: this.props.autoResolvedCount,
      manualReviewCount: this.props.manualReviewCount,
      createdBy: this.props.createdBy,
      completedBy: this.props.completedBy,
      completedAt: this.props.completedAt,
      errorDetails: this.props.errorDetails,
      scheduled: this.props.scheduled,
      scheduleExpression: this.props.scheduleExpression,
      lastRunAt: this.props.lastRunAt,
      nextRunAt: this.props.nextRunAt,
      metadata: this.props.metadata,
      createdAt: this.props.createdAt,
      updatedAt: this.props.updatedAt,
    };
  }
}
