/**
 * Domain event emitted when a commission is clawed back
 */
export class CommissionClawbackEvent {
  readonly eventType = 'CommissionClawback';
  readonly occurredAt: Date;
  readonly commissionId: string;
  readonly salesRepId: string;
  readonly salesRepName: string;
  readonly clawbackAmount: number;
  readonly currency: string;
  readonly clawbackReason: string;
  readonly tenantId: string;
  readonly organizationId: string;
  readonly correlationId: string;

  constructor(
    commissionId: string,
    salesRepId: string,
    salesRepName: string,
    clawbackAmount: number,
    currency: string,
    clawbackReason: string,
    tenantId: string,
    organizationId: string,
    correlationId: string,
  ) {
    this.occurredAt = new Date();
    this.commissionId = commissionId;
    this.salesRepId = salesRepId;
    this.salesRepName = salesRepName;
    this.clawbackAmount = clawbackAmount;
    this.currency = currency;
    this.clawbackReason = clawbackReason;
    this.tenantId = tenantId;
    this.organizationId = organizationId;
    this.correlationId = correlationId;
  }

  toObject(): Record<string, unknown> {
    return {
      eventType: this.eventType,
      occurredAt: this.occurredAt,
      commissionId: this.commissionId,
      salesRepId: this.salesRepId,
      salesRepName: this.salesRepName,
      clawbackAmount: this.clawbackAmount,
      currency: this.currency,
      clawbackReason: this.clawbackReason,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      correlationId: this.correlationId,
    };
  }
}
