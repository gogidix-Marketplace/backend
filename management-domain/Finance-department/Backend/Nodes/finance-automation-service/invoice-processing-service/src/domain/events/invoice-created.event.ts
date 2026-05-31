import { Invoice, PaymentTerms } from '../models/invoice.entity';

/**
 * Domain event emitted when an invoice is created
 */
export interface InvoiceCreatedEvent {
  eventType: 'InvoiceCreated';
  eventId: string;
  occurredAt: Date;
  tenantId: string;
  organizationId: string;
  correlationId?: string;
  payload: {
    invoiceId: string;
    invoiceNumber: string;
    purchaseOrderNumber?: string;
    vendorId: string;
    vendorName: string;
    invoiceDate: Date;
    dueDate: Date;
    paymentTerms: PaymentTerms;
    currency: string;
    totalAmount: number;
    status: string;
    category?: string;
    glAccountCode?: string;
    costCenter?: string;
  };
}

export class InvoiceCreatedEventFactory {
  static create(
    invoice: Invoice,
    correlationId?: string,
  ): InvoiceCreatedEvent {
    return {
      eventType: 'InvoiceCreated',
      eventId: this.generateEventId(),
      occurredAt: new Date(),
      tenantId: invoice.getTenantId(),
      organizationId: invoice.getOrganizationId(),
      correlationId,
      payload: {
        invoiceId: invoice.id?.toString() || '',
        invoiceNumber: invoice.getInvoiceNumber(),
        purchaseOrderNumber: invoice.getPurchaseOrderNumber(),
        vendorId: invoice.getVendorId(),
        vendorName: invoice.getVendorName(),
        invoiceDate: invoice.getInvoiceDate(),
        dueDate: invoice.getDueDate(),
        paymentTerms: invoice.getPaymentTerms(),
        currency: invoice.getCurrency(),
        totalAmount: invoice.getTotalAmount(),
        status: invoice.getStatus(),
        category: invoice.getCategory(),
        glAccountCode: invoice.getGlAccountCode(),
        costCenter: invoice.getCostCenter(),
      },
    };
  }

  private static generateEventId(): string {
    return `evt-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
