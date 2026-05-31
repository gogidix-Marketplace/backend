import { InvoiceStatus } from '../models/invoice-validation.entity';
import { Invoice, PaymentTerms } from '../models/invoice.entity';

/**
 * Domain event emitted when an invoice processing fails
 */
export interface InvoiceFailedEvent {
  eventType: 'InvoiceFailed';
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
    failedAt: Date;
    failureReason: string;
    rejectedBy?: string;
    rejectionReason?: string;
    errorCategory: 'VALIDATION' | 'PROCESSING' | 'OCR' | 'DUPLICATE' | 'BUSINESS_RULE' | 'OTHER';
    errorDetails?: Record<string, unknown>;
  };
}

export class InvoiceFailedEventFactory {
  static create(
    invoice: Invoice,
    failureReason: string,
    errorCategory: 'VALIDATION' | 'PROCESSING' | 'OCR' | 'DUPLICATE' | 'BUSINESS_RULE' | 'OTHER',
    errorDetails?: Record<string, unknown>,
    correlationId?: string,
  ): InvoiceFailedEvent {
    return {
      eventType: 'InvoiceFailed',
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
        failedAt: new Date(),
        failureReason,
        rejectedBy: invoice.getRejectedBy(),
        rejectionReason: invoice.getRejectionReason(),
        errorCategory,
        errorDetails,
      },
    };
  }

  private static generateEventId(): string {
    return `evt-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
