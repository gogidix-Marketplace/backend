import { UUID } from 'crypto';

export class ReconciliationFailedEvent {
  readonly eventType = 'ReconciliationFailed';
  readonly occurredAt: Date;
  readonly correlationId: string;

  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly name: string,
    public readonly errorMessage: string,
    public readonly processedTransactions: number,
    public readonly errorDetails?: string,
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
      errorMessage: this.errorMessage,
      errorDetails: this.errorDetails,
      processedTransactions: this.processedTransactions,
      occurredAt: this.occurredAt,
      correlationId: this.correlationId,
    };
  }
}
