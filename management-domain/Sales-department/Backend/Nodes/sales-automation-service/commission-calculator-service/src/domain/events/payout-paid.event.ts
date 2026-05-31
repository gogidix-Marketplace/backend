/**
 * Domain event emitted when a commission payout is paid
 */
export class PayoutPaidEvent {
  readonly eventType = 'PayoutPaid';
  readonly occurredAt: Date;
  readonly commissionId: string;
  readonly salesRepId: string;
  readonly salesRepName: string;
  readonly payoutAmount: number;
  readonly currency: string;
  readonly paymentMethod: string;
  readonly paymentReference?: string;
  readonly tenantId: string;
  readonly organizationId: string;
  readonly correlationId: string;

  constructor(
    commissionId: string,
    salesRepId: string,
    salesRepName: string,
    payoutAmount: number,
    currency: string,
    paymentMethod: string,
    paymentReference: string | undefined,
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
    this.paymentMethod = paymentMethod;
    this.paymentReference = paymentReference;
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
      paymentMethod: this.paymentMethod,
      paymentReference: this.paymentReference,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      correlationId: this.correlationId,
    };
  }
}
