import { UUID } from 'crypto';
import { ReconciliationStatus } from '../enums/reconciliation-status.enum';

export class ReconciliationCompletedEvent {
  readonly eventType = 'ReconciliationCompleted';
  readonly occurredAt: Date;
  readonly correlationId: string;

  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly name: string,
    public readonly status: ReconciliationStatus,
    public readonly totalTransactionsProcessed: number,
    public readonly totalMatches: number,
    public readonly totalDifferences: number,
    public readonly autoResolvedCount: number,
    public readonly manualReviewCount: number,
    public readonly duration: number,
    correlationId?: string,
  ) {
    this.occurredAt = new Date();
    this.correlationId = correlationId || crypto.randomUUID();
  }

  toJSON() {
    return {
      eventType: this.eventType,
      reconciliationId: this.reconciliationId,
      tenantId: this.tenantId,
      name: this.name,
      status: this.status,
      totalTransactionsProcessed: this.totalTransactionsProcessed,
      totalMatches: this.totalMatches,
      totalDifferences: this.totalDifferences,
      autoResolvedCount: this.autoResolvedCount,
      manualReviewCount: this.manualReviewCount,
      duration: this.duration,
      occurredAt: this.occurredAt,
      correlationId: this.correlationId,
    };
  }
}
