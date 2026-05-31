import { Commission } from '../models/commission.entity';

/**
 * Domain event emitted when a commission is calculated
 */
export class CommissionCalculatedEvent {
  readonly eventType = 'CommissionCalculated';
  readonly occurredAt: Date;
  readonly commissionId: string;
  readonly salesRepId: string;
  readonly salesRepName: string;
  readonly periodId: string;
  readonly salesAmount: number;
  readonly calculatedAmount: number;
  readonly finalAmount: number;
  readonly currency: string;
  readonly tenantId: string;
  readonly organizationId: string;
  readonly correlationId: string;

  constructor(
    commission: Commission,
    correlationId: string,
  ) {
    this.occurredAt = new Date();
    this.commissionId = commission.getIdAsString();
    this.salesRepId = commission.getSalesRepId();
    this.salesRepName = commission.getSalesRepName();
    this.periodId = commission.getPeriodId();
    this.salesAmount = commission.getSalesAmount();
    this.calculatedAmount = commission.getCalculatedAmount();
    this.finalAmount = commission.getFinalAmount();
    this.currency = commission.getCurrency();
    this.tenantId = commission.getTenantId();
    this.organizationId = commission.getOrganizationId();
    this.correlationId = correlationId;
  }

  toObject(): Record<string, unknown> {
    return {
      eventType: this.eventType,
      occurredAt: this.occurredAt,
      commissionId: this.commissionId,
      salesRepId: this.salesRepId,
      salesRepName: this.salesRepName,
      periodId: this.periodId,
      salesAmount: this.salesAmount,
      calculatedAmount: this.calculatedAmount,
      finalAmount: this.finalAmount,
      currency: this.currency,
      tenantId: this.tenantId,
      organizationId: this.organizationId,
      correlationId: this.correlationId,
    };
  }
}
