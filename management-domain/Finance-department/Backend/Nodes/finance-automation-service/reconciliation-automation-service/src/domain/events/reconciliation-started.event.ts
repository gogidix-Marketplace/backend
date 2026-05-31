import { UUID } from 'crypto';

export class ReconciliationStartedEvent {
  readonly eventType = 'ReconciliationStarted';
  readonly occurredAt: Date;
  readonly correlationId: string;

  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly name: string,
    public readonly dataSourceType: string,
    public readonly bankAccountId?: string,
    public readonly internalAccountId?: string,
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
      dataSourceType: this.dataSourceType,
      bankAccountId: this.bankAccountId,
      internalAccountId: this.internalAccountId,
      occurredAt: this.occurredAt,
      correlationId: this.correlationId,
    };
  }
}
