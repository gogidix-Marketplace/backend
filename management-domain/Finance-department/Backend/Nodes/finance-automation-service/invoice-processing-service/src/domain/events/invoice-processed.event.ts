import { InvoiceStatus } from '../models/invoice-validation.entity';
import { Invoice, PaymentTerms } from '../models/invoice.entity';

/**
 * Domain event emitted when an invoice is processed
 */
export interface InvoiceProcessedEvent {
  eventType: 'InvoiceProcessed';
  eventId: string;
  occurredAt: Date;
  tenantId: string;
  organizationId: string;
  correlationId?: string;
  payload: {
    invoiceId: string;
    invoiceNumber: string;
    vendorId: string;
    vendorName: string;
    totalAmount: number;
    currency: string;
    status: InvoiceStatus;
    processedAt: Date;
    processedBy?: string;
    approvedBy?: string;
    category?: string;
    glAccountCode?: string;
    costCenter?: string;
    paymentTerms: PaymentTerms;
    dueDate: Date;
    outstandingAmount: number;
  };
}

export class InvoiceProcessedEventFactory {
  static create(
    invoice: Invoice,
    processedBy?: string,
    correlationId?: string,
  ): InvoiceProcessedEvent {
    return {
      eventType: 'InvoiceProcessed',
      eventId: this.generateEventId(),
      occurredAt: new Date(),
      tenantId: invoice.getTenantId(),
      organizationId: invoice.getOrganizationId(),
      correlationId,
      payload: {
        invoiceId: invoice.id?.toString() || '',
        invoiceNumber: invoice.getInvoiceNumber(),
        vendorId: invoice.getVendorId(),
        vendorName: invoice.getVendorName(),
        totalAmount: invoice.getTotalAmount(),
        currency: invoice.getCurrency(),
        status: invoice.getStatus(),
        processedAt: invoice.getProcessedDate() || new Date(),
        processedBy,
        approvedBy: invoice.getApprovedBy(),
        category: invoice.getCategory(),
        glAccountCode: invoice.getGlAccountCode(),
        costCenter: invoice.getCostCenter(),
        paymentTerms: invoice.getPaymentTerms(),
        dueDate: invoice.getDueDate(),
        outstandingAmount: invoice.getOutstandingAmount(),
      },
    };
  }

  private static generateEventId(): string {
    return `evt-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
