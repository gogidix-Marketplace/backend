/**
 * Domain event emitted when a commission payout is approved
 */
export class PayoutApprovedEvent {
  readonly eventType = 'PayoutApproved';
  readonly occurredAt: Date;
  readonly commissionId: string;
  readonly salesRepId: string;
  readonly salesRepName: string;
  readonly payoutAmount: number;
  readonly currency: string;
  readonly approvedBy: string;
  readonly approvedAt: Date;
  readonly tenantId: string;
  readonly organizationId: string;
  readonly correlationId: string;

  constructor(
    commissionId: string,
    salesRepId: string,
    salesRepName: string,
    payoutAmount: number,
    currency: string,
    approvedBy: string,
    tenantId: string,
    organizationId: string,
    correlationId: string,
  ) {
    this.occurredAt = new Date();
    this.commissionId = commissionId;
    this.salesRepId = salesRepId;
    this.salesRepName = salesRepName;
    this.payoutAmount = payoutAmount;
    this.currency = currency;
    this.approvedBy = approvedBy;
    this.approvedAt = new Date();
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
      payoutAmount: this.payoutAmount,
      currency: this.currency,
      approvedBy: this.approvedBy,
      approvedAt: this.approvedAt,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      correlationId: this.correlationId,
    };
  }
}
