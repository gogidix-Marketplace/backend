/**
 * Domain event emitted when a commission period is closed
 */
export class CommissionPeriodClosedEvent {
  readonly eventType = 'CommissionPeriodClosed';
  readonly occurredAt: Date;
  readonly periodId: string;
  readonly periodName: string;
  readonly startDate: Date;
  readonly endDate: Date;
  readonly totalCommissions: number;
  readonly totalAmount: number;
  readonly currency: string;
  readonly tenantId: string;
  readonly organizationId: string;
  readonly correlationId: string;

  constructor(
    periodId: string,
    periodName: string,
    startDate: Date,
    endDate: Date,
    totalCommissions: number,
    totalAmount: number,
    currency: string,
    tenantId: string,
    organizationId: string,
    correlationId: string,
  ) {
    this.occurredAt = new Date();
    this.periodId = periodId;
    this.periodName = periodName;
    this.startDate = startDate;
    this.endDate = endDate;
    this.totalCommissions = totalCommissions;
    this.totalAmount = totalAmount;
    this.currency = currency;
    this.tenantId = tenantId;
    this.organizationId = organizationId;
    this.correlationId = correlationId;
  }

  toObject(): Record<string, unknown> {
    return {
      eventType: this.eventType,
      occurredAt: this.occurredAt,
      periodId: this.periodId,
      periodName: this.periodName,
      startDate: this.startDate,
      endDate: this.endDate,
      totalCommissions: this.totalCommissions,
      totalAmount: this.totalAmount,
      currency: this.currency,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      correlationId: this.correlationId,
    };
  }
}
