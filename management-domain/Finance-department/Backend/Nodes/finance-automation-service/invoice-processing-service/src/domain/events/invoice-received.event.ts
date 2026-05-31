import { InvoiceStatus } from '../models/invoice-validation.entity';
import { Invoice, PaymentTerms } from '../models/invoice.entity';

/**
 * Domain event emitted when an invoice is received
 */
export interface InvoiceReceivedEvent {
  eventType: 'InvoiceReceived';
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
    invoiceDate: Date;
    dueDate: Date;
    totalAmount: number;
    currency: string;
    status: InvoiceStatus;
    receivedDate: Date;
    itemCount: number;
    hasAttachments: boolean;
    paymentTerms: PaymentTerms;
    purchaseOrderNumber?: string;
  };
}

export class InvoiceReceivedEventFactory {
  static create(
    invoice: Invoice,
    correlationId?: string,
  ): InvoiceReceivedEvent {
    return {
      eventType: 'InvoiceReceived',
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
        invoiceDate: invoice.getInvoiceDate(),
        dueDate: invoice.getDueDate(),
        totalAmount: invoice.getTotalAmount(),
        currency: invoice.getCurrency(),
        status: invoice.getStatus(),
        receivedDate: invoice.getReceivedDate(),
        itemCount: invoice.getItems().length,
        hasAttachments: invoice.getAttachments().length > 0,
        paymentTerms: invoice.getPaymentTerms(),
        purchaseOrderNumber: invoice.getPurchaseOrderNumber(),
      },
    };
  }

  private static generateEventId(): string {
    return `evt-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
