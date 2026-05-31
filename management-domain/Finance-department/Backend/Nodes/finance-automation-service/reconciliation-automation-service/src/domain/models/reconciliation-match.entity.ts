import { UUID } from 'crypto';
import { MatchType } from '../enums/match-type.enum';

export interface ReconciliationMatchProps {
  id?: UUID;
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
  bankTransaction?: TransactionReference;
  internalTransaction?: TransactionReference;
  metadata?: Record<string, unknown>;
  createdAt: Date;
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

export class ReconciliationMatch {
  readonly props: ReconciliationMatchProps;

  constructor(props: ReconciliationMatchProps) {
    if (props.confidence < 0 || props.confidence > 1) {
      throw new Error('Confidence must be between 0 and 1');
    }
    this.props = {
      ...props,
      id: props.id || crypto.randomUUID(),
      createdAt: props.createdAt || new Date(),
      matchDate: props.matchDate || new Date(),
    };
  }

  get id(): UUID {
    return this.props.id!;
  }

  get reconciliationId(): UUID {
    return this.props.reconciliationId;
  }

  get confidence(): number {
    return this.props.confidence;
  }

  verify(userId: string): void {
    this.props.verified = true;
    this.props.verifiedBy = userId;
    this.props.verifiedAt = new Date();
  }

  unverify(): void {
    this.props.verified = false;
    this.props.verifiedBy = undefined;
    this.props.verifiedAt = undefined;
  }

  addNotes(notes: string): void {
    this.props.notes = notes;
  }

  isHighConfidence(): boolean {
    return this.props.confidence >= 0.9;
  }

  isMediumConfidence(): boolean {
    return this.props.confidence >= 0.7 && this.props.confidence < 0.9;
  }

  isLowConfidence(): boolean {
    return this.props.confidence < 0.7;
  }

  toJSON() {
    return {
      id: this.props.id,
      reconciliationId: this.props.reconciliationId,
      tenantId: this.props.tenantId,
      bankTransactionId: this.props.bankTransactionId,
      internalTransactionId: this.props.internalTransactionId,
      matchType: this.props.matchType,
      confidence: this.props.confidence,
      matchDate: this.props.matchDate,
      matchedBy: this.props.matchedBy,
      verified: this.props.verified,
      verifiedBy: this.props.verifiedBy,
      verifiedAt: this.props.verifiedAt,
      notes: this.props.notes,
      bankTransaction: this.props.bankTransaction,
      internalTransaction: this.props.internalTransaction,
      metadata: this.props.metadata,
      createdAt: this.props.createdAt,
    };
  }
}
