import { UUID } from 'crypto';
import { DifferenceStatus } from '../enums/difference-status.enum';
import { DifferenceType } from '../models/transaction-difference.entity';

export class DifferenceResolvedEvent {
  readonly eventType = 'DifferenceResolved';
  readonly occurredAt: Date;
  readonly correlationId: string;

  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly differenceId: UUID,
    public readonly differenceType: DifferenceType,
    public readonly previousStatus: DifferenceStatus,
    public readonly newStatus: DifferenceStatus,
    public readonly resolvedBy?: string,
    public readonly resolutionNotes?: string,
    public readonly autoResolution?: boolean,
    correlationId?: string,
  ) {
    this.occurredAt = new Date();
    this.correlationId = correlationId || crypto.randomUUID();
  }

  isAutoResolved(): boolean {
    return this.autoResolution || this.newStatus === DifferenceStatus.AUTO_RESOLVED;
  }

  toJSON() {
    return {
      eventType: this.eventType,
      reconciliationId: this.reconciliationId,
      tenantId: this.tenantId,
      differenceId: this.differenceId,
      differenceType: this.differenceType,
      previousStatus: this.previousStatus,
      newStatus: this.newStatus,
      resolvedBy: this.resolvedBy,
      resolutionNotes: this.resolutionNotes,
      autoResolution: this.autoResolution,
      occurredAt: this.occurredAt,
      correlationId: this.correlationId,
    };
  }
}
