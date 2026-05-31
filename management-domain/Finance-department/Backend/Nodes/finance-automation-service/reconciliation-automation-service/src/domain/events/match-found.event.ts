import { UUID } from 'crypto';
import { MatchType } from '../enums/match-type.enum';

export class MatchFoundEvent {
  readonly eventType = 'MatchFound';
  readonly occurredAt: Date;
  readonly correlationId: string;

  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly matchId: UUID,
    public readonly bankTransactionId: string,
    public readonly internalTransactionId: string,
    public readonly matchType: MatchType,
    public readonly confidence: number,
    public readonly bankTransaction: {
      id: string;
      amount: number;
      currency: string;
      date: Date;
      reference?: string;
    },
    public readonly internalTransaction: {
      id: string;
      amount: number;
      currency: string;
      date: Date;
      reference?: string;
    },
    correlationId?: string,
  ) {
    this.occurredAt = new Date();
    this.correlationId = correlationId || crypto.randomUUID();
  }

  isHighConfidence(): boolean {
    return this.confidence >= 0.9;
  }

  isAutoVerifiable(): boolean {
    return this.matchType === 'EXACT' || this.confidence >= 0.95;
  }

  toJSON() {
    return {
      eventType: this.eventType,
      reconciliationId: this.reconciliationId,
      tenantId: this.tenantId,
      matchId: this.matchId,
      bankTransactionId: this.bankTransactionId,
      internalTransactionId: this.internalTransactionId,
      matchType: this.matchType,
      confidence: this.confidence,
      bankTransaction: this.bankTransaction,
      internalTransaction: this.internalTransaction,
      occurredAt: this.occurredAt,
      correlationId: this.correlationId,
    };
  }
}
