import { UUID } from 'crypto';
import { DifferenceType } from '../models/transaction-difference.entity';

export class DifferenceDetectedEvent {
  readonly eventType = 'DifferenceDetected';
  readonly occurredAt: Date;
  readonly correlationId: string;

  constructor(
    public readonly reconciliationId: UUID,
    public readonly tenantId: string,
    public readonly differenceId: UUID,
    public readonly differenceType: DifferenceType,
    public readonly severity: 'LOW' | 'MEDIUM' | 'HIGH' | 'CRITICAL',
    public readonly description: string,
    public readonly bankTransactionId?: string,
    public readonly internalTransactionId?: string,
    public readonly bankAmount?: number,
    public readonly internalAmount?: number,
    public readonly amountDifference?: number,
    public readonly currency?: string,
    correlationId?: string,
  ) {
    this.occurredAt = new Date();
    this.correlationId = correlationId || crypto.randomUUID();
  }

  isCritical(): boolean {
    return this.severity === 'CRITICAL';
  }

  requiresImmediateAttention(): boolean {
    return this.severity === 'CRITICAL' || this.severity === 'HIGH';
  }

  toJSON() {
    return {
      eventType: this.eventType,
      reconciliationId: this.reconciliationId,
      tenantId: this.tenantId,
      differenceId: this.differenceId,
      differenceType: this.differenceType,
      bankTransactionId: this.bankTransactionId,
      internalTransactionId: this.internalTransactionId,
      bankAmount: this.bankAmount,
      internalAmount: this.internalAmount,
      amountDifference: this.amountDifference,
      currency: this.currency,
      severity: this.severity,
      description: this.description,
      occurredAt: this.occurredAt,
      correlationId: this.correlationId,
    };
  }
}
