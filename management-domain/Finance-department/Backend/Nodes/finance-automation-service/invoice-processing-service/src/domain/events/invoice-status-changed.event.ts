import { InvoiceStatus } from '../models/invoice-validation.entity';

/**
 * Domain event emitted when an invoice status changes
 */
export interface InvoiceStatusChangedEvent {
  eventType: 'InvoiceStatusChanged';
  eventId: string;
  occurredAt: Date;
  tenantId: string;
  organizationId: string;
  correlationId?: string;
  payload: {
    invoiceId: string;
    invoiceNumber: string;
    oldStatus: InvoiceStatus;
    newStatus: InvoiceStatus;
    changedBy: string;
    changedAt: Date;
    reason?: string;
  };
}

export class InvoiceStatusChangedEventFactory {
  static create(
    invoiceId: string,
    invoiceNumber: string,
    tenantId: string,
    organizationId: string,
    oldStatus: InvoiceStatus,
    newStatus: InvoiceStatus,
    changedBy: string,
    reason?: string,
    correlationId?: string,
  ): InvoiceStatusChangedEvent {
    return {
      eventType: 'InvoiceStatusChanged',
      eventId: this.generateEventId(),
      occurredAt: new Date(),
      tenantId,
      organizationId,
      correlationId,
      payload: {
        invoiceId,
        invoiceNumber,
        oldStatus,
        newStatus,
        changedBy,
        changedAt: new Date(),
        reason,
      },
    };
  }

  private static generateEventId(): string {
    return `evt-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
