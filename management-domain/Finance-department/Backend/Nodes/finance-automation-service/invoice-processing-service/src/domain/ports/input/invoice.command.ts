import { InvoiceStatus } from '../../models/invoice-validation.entity';
import { PaymentTerms } from '../../models/invoice.entity';
import { InvoiceItem } from '../../models/invoice-item.entity';

/**
 * Command interface for invoice operations
 * Defines the contract for invoice command operations
 */
export interface IInvoiceCommand {
  createInvoice(command: CreateInvoiceCommand): Promise<InvoiceCommandResult>;
  updateInvoice(command: UpdateInvoiceCommand): Promise<InvoiceCommandResult>;
  processInvoice(command: ProcessInvoiceCommand): Promise<InvoiceCommandResult>;
  validateInvoice(command: ValidateInvoiceCommand): Promise<InvoiceCommandResult>;
  approveInvoice(command: ApproveInvoiceCommand): Promise<InvoiceCommandResult>;
  rejectInvoice(command: RejectInvoiceCommand): Promise<InvoiceCommandResult>;
  cancelInvoice(command: CancelInvoiceCommand): Promise<InvoiceCommandResult>;
  recordPayment(command: RecordPaymentCommand): Promise<InvoiceCommandResult>;
  addAttachment(command: AddAttachmentCommand): Promise<InvoiceCommandResult>;
}

/**
 * Command to create a new invoice
 */
export interface CreateInvoiceCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceNumber: string;
  purchaseOrderNumber?: string;
  vendorId: string;
  vendorName: string;
  vendorTaxId?: string;
  invoiceDate: Date;
  dueDate: Date;
  paymentTerms?: PaymentTerms;
  currency?: string;
  category?: string;
  glAccountCode?: string;
  costCenter?: string;
  items: Array<{
    description: string;
    quantity: number;
    unitPrice: number;
    taxRate: number;
    sku?: string;
    unitOfMeasure?: string;
  }>;
  notes?: string;
  internalNotes?: string;
}

/**
 * Command to update an existing invoice
 */
export interface UpdateInvoiceCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  purchaseOrderNumber?: string;
  dueDate?: Date;
  paymentTerms?: PaymentTerms;
  category?: string;
  glAccountCode?: string;
  costCenter?: string;
  notes?: string;
  internalNotes?: string;
}

/**
 * Command to process an invoice (OCR extraction, etc.)
 */
export interface ProcessInvoiceCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  processType: 'OCR' | 'VALIDATION' | 'ENRICHMENT';
  options?: Record<string, unknown>;
}

/**
 * Command to validate an invoice
 */
export interface ValidateInvoiceCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  validationRules?: string[];
}

/**
 * Command to approve an invoice
 */
export interface ApproveInvoiceCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  notes?: string;
}

/**
 * Command to reject an invoice
 */
export interface RejectInvoiceCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  reason: string;
}

/**
 * Command to cancel an invoice
 */
export interface CancelInvoiceCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  reason?: string;
}

/**
 * Command to record a payment
 */
export interface RecordPaymentCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  amount: number;
  paymentDate: Date;
  paymentMethod?: string;
  reference?: string;
}

/**
 * Command to add an attachment
 */
export interface AddAttachmentCommand {
  tenantId: string;
  organizationId: string;
  userId: string;
  correlationId?: string;
  invoiceId: string;
  attachment: {
    name: string;
    url: string;
    type: string;
    size: number;
  };
}

/**
 * Result of a command operation
 */
export interface InvoiceCommandResult {
  success: boolean;
  invoiceId?: string;
  invoiceNumber?: string;
  status?: InvoiceStatus;
  message?: string;
  errors?: Array<{ field: string; message: string }>;
  data?: Record<string, unknown>;
}
