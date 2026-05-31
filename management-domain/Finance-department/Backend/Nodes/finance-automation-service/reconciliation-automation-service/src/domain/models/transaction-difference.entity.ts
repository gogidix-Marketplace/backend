import { UUID } from 'crypto';
import { DifferenceStatus } from '../enums/difference-status.enum';

export interface TransactionDifferenceProps {
  id?: UUID;
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
  autoResolution?: AutoResolution;
  severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL';
  assignee?: string;
  dueDate?: Date;
  bankTransaction?: TransactionReference;
  internalTransaction?: TransactionReference;
  metadata?: Record<string, unknown>;
  createdAt: Date;
}

export type DifferenceType =
  | 'AMOUNT_MISMATCH'
  | 'MISSING_BANK_TRANSACTION'
  | 'MISSING_INTERNAL_TRANSACTION'
  | 'DUPLICATE_TRANSACTION'
  | 'TIMING_DIFFERENCE'
  | 'CURRENCY_MISMATCH'
  | 'COUNTERPARTY_MISMATCH'
  | 'REFERENCE_MISMATCH';

export interface AutoResolution {
  applied: boolean;
  rule?: string;
  action?: string;
  timestamp?: Date;
}

export interface TransactionReference {
  id: string;
  amount: number;
  currency: string;
  date: Date;
  reference?: string;
  description?: string;
  counterparty?: string;
}

export class TransactionDifference {
  readonly props: TransactionDifferenceProps;

  constructor(props: TransactionDifferenceProps) {
    this.props = {
      ...props,
      id: props.id || crypto.randomUUID(),
      createdAt: props.createdAt || new Date(),
      detectedAt: props.detectedAt || new Date(),
    };
  }

  get id(): UUID {
    return this.props.id!;
  }

  get reconciliationId(): UUID {
    return this.props.reconciliationId;
  }

  get status(): DifferenceStatus {
    return this.props.status;
  }

  get differenceType(): DifferenceType {
    return this.props.differenceType;
  }

  get severity(): string {
    return this.props.severity;
  }

  autoResolve(rule: string, action: string, notes: string): void {
    this.props.status = DifferenceStatus.AUTO_RESOLVED;
    this.props.autoResolution = {
      applied: true,
      rule,
      action,
      timestamp: new Date(),
    };
    this.props.resolvedAt = new Date();
    this.props.resolutionNotes = notes;
  }

  manuallyResolve(userId: string, notes: string): void {
    this.props.status = DifferenceStatus.MANUALLY_RESOLVED;
    this.props.resolvedBy = userId;
    this.props.resolvedAt = new Date();
    this.props.resolutionNotes = notes;
  }

  escalate(): void {
    this.props.status = DifferenceStatus.ESCALATED;
  }

  ignore(): void {
    this.props.status = DifferenceStatus.IGNORED;
  }

  assignTo(assignee: string, dueDate?: Date): void {
    this.props.assignee = assignee;
    if (dueDate) {
      this.props.dueDate = dueDate;
    }
  }

  calculateSeverity(): void {
    if (this.props.amountDifference && Math.abs(this.props.amountDifference) > 10000) {
      this.props.severity = 'CRITICAL';
    } else if (
      this.props.amountDifference &&
      Math.abs(this.props.amountDifference) > 1000
    ) {
      this.props.severity = 'HIGH';
    } else if (
      this.props.differenceType === 'MISSING_BANK_TRANSACTION' ||
      this.props.differenceType === 'MISSING_INTERNAL_TRANSACTION'
    ) {
      this.props.severity = 'HIGH';
    } else if (
      this.props.differenceType === 'TIMING_DIFFERENCE' ||
      this.props.differenceType === 'DUPLICATE_TRANSACTION'
    ) {
      this.props.severity = 'MEDIUM';
    } else {
      this.props.severity = 'LOW';
    }
  }

  isOverdue(): boolean {
    return this.props.dueDate ? new Date() > this.props.dueDate : false;
  }

  toJSON() {
    return {
      id: this.props.id,
      reconciliationId: this.props.reconciliationId,
      tenantId: this.props.tenantId,
      bankTransactionId: this.props.bankTransactionId,
      internalTransactionId: this.props.internalTransactionId,
      differenceType: this.props.differenceType,
      status: this.props.status,
      bankAmount: this.props.bankAmount,
      internalAmount: this.props.internalAmount,
      amountDifference: this.props.amountDifference,
      currency: this.props.currency,
      bankDate: this.props.bankDate,
      internalDate: this.props.internalDate,
      description: this.props.description,
      detectedAt: this.props.detectedAt,
      resolvedAt: this.props.resolvedAt,
      resolvedBy: this.props.resolvedBy,
      resolutionNotes: this.props.resolutionNotes,
      autoResolution: this.props.autoResolution,
      severity: this.props.severity,
      assignee: this.props.assignee,
      dueDate: this.props.dueDate,
      bankTransaction: this.props.bankTransaction,
      internalTransaction: this.props.internalTransaction,
      metadata: this.props.metadata,
      createdAt: this.props.createdAt,
    };
  }
}
