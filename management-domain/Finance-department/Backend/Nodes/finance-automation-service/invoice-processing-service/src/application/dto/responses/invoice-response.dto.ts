import { InvoiceStatus, PaymentTerms } from '../../../domain/models/invoice.entity';

/**
 * Response DTO for a single invoice
 */
export interface InvoiceResponseDto {
  id: string;
  invoiceNumber: string;
  purchaseOrderNumber?: string;
  vendorId: string;
  vendorName: string;
  vendorTaxId?: string;
  invoiceDate: string;
  dueDate: string;
  paymentTerms: PaymentTerms;
  currency: string;
  subtotalAmount: number;
  taxAmount: number;
  discountAmount: number;
  totalAmount: number;
  amountPaid: number;
  outstandingAmount: number;
  status: InvoiceStatus;
  items?: InvoiceItemResponseDto[];
  notes?: string;
  internalNotes?: string;
  receivedDate: string;
  processedDate?: string;
  category?: string;
  glAccountCode?: string;
  costCenter?: string;
  attachments: AttachmentResponseDto[];
  approvedBy?: string;
  approvedAt?: string;
  rejectedBy?: string;
  rejectedAt?: string;
  rejectionReason?: string;
  ocrProcessed: boolean;
  ocrConfidence?: number;
  createdAt: string;
  updatedAt: string;
  version: number;
}

/**
 * Response DTO for invoice item
 */
export interface InvoiceItemResponseDto {
  lineNumber: number;
  description: string;
  quantity: number;
  unitPrice: number;
  taxRate: number;
  taxAmount: number;
  discountAmount: number;
  totalAmount: number;
  subtotal: number;
  sku?: string;
  unitOfMeasure: string;
}

/**
 * Response DTO for attachment
 */
export interface AttachmentResponseDto {
  name: string;
  url: string;
  type: string;
  size: number;
}

/**
 * Paginated response DTO
 */
export interface PaginatedResponseDto<T> {
  data: T[];
  pagination: {
    total: number;
    page: number;
    pageSize: number;
    totalPages: number;
    hasNext: boolean;
    hasPrevious: boolean;
  };
}

/**
 * Invoice list response DTO
 */
export type InvoiceListResponseDto = PaginatedResponseDto<InvoiceResponseDto>;

/**
 * Invoice statistics response DTO
 */
export interface InvoiceStatisticsResponseDto {
  totalInvoices: number;
  totalAmount: number;
  paidAmount: number;
  outstandingAmount: number;
  overdueAmount: number;
  averageProcessingTime: number;
  statusBreakdown: Record<InvoiceStatus, number>;
  vendorBreakdown: VendorBreakdownResponseDto[];
  monthlyTrend: MonthlyTrendResponseDto[];
}

/**
 * Vendor breakdown response DTO
 */
export interface VendorBreakdownResponseDto {
  vendorId: string;
  vendorName: string;
  count: number;
  totalAmount: number;
}

/**
 * Monthly trend response DTO
 */
export interface MonthlyTrendResponseDto {
  month: string;
  count: number;
  amount: number;
}

/**
 * Dashboard data response DTO
 */
export interface DashboardResponseDto {
  pendingInvoices: number;
  pendingAmount: number;
  overdueInvoices: number;
  overdueAmount: number;
  dueThisWeek: number;
  dueThisWeekAmount: number;
  recentInvoices: InvoiceResponseDto[];
  topVendors: VendorBreakdownResponseDto[];
  statusSummary: Record<InvoiceStatus, number>;
}
