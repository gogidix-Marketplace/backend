import { InvoiceStatus } from '../../models/invoice-validation.entity';
import { PaymentTerms } from '../../models/invoice.entity';

/**
 * Query interface for invoice queries
 * Defines the contract for invoice query operations
 */
export interface IInvoiceQuery {
  findById(query: GetByIdQuery): Promise<InvoiceDto | null>;
  findByInvoiceNumber(query: GetByInvoiceNumberQuery): Promise<InvoiceDto | null>;
  findAll(query: FindAllQuery): Promise<PaginatedResult<InvoiceDto>>;
  findByVendor(query: FindByVendorQuery): Promise<PaginatedResult<InvoiceDto>>;
  findByStatus(query: FindByStatusQuery): Promise<PaginatedResult<InvoiceDto>>;
  findByDateRange(query: FindByDateRangeQuery): Promise<PaginatedResult<InvoiceDto>>;
  searchInvoices(query: SearchInvoicesQuery): Promise<PaginatedResult<InvoiceDto>>;
  getInvoiceStatistics(query: GetStatisticsQuery): Promise<InvoiceStatistics>;
  getDashboardData(query: GetDashboardQuery): Promise<DashboardData>;
}

/**
 * Query by ID
 */
export interface GetByIdQuery {
  tenantId: string;
  organizationId: string;
  userId: string;
  invoiceId: string;
  includeItems?: boolean;
  includeValidation?: boolean;
}

/**
 * Query by invoice number
 */
export interface GetByInvoiceNumberQuery {
  tenantId: string;
  organizationId: string;
  userId: string;
  invoiceNumber: string;
  includeItems?: boolean;
  includeValidation?: boolean;
}

/**
 * Find all invoices
 */
export interface FindAllQuery {
  tenantId: string;
  organizationId: string;
  userId: string;
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'ASC' | 'DESC';
  includeItems?: boolean;
}

/**
 * Find by vendor
 */
export interface FindByVendorQuery extends FindAllQuery {
  vendorId: string;
}

/**
 * Find by status
 */
export interface FindByStatusQuery extends FindAllQuery {
  status: InvoiceStatus;
}

/**
 * Find by date range
 */
export interface FindByDateRangeQuery extends FindAllQuery {
  startDate: Date;
  endDate: Date;
  dateField?: 'invoiceDate' | 'dueDate' | 'receivedDate' | 'processedDate';
}

/**
 * Search invoices
 */
export interface SearchInvoicesQuery extends FindAllQuery {
  searchTerm: string;
  searchFields?: Array<'invoiceNumber' | 'vendorName' | 'notes' | 'poNumber'>;
}

/**
 * Get statistics query
 */
export interface GetStatisticsQuery {
  tenantId: string;
  organizationId: string;
  userId: string;
  startDate?: Date;
  endDate?: Date;
}

/**
 * Get dashboard data query
 */
export interface GetDashboardQuery {
  tenantId: string;
  organizationId: string;
  userId: string;
}

/**
 * Invoice DTO
 */
export interface InvoiceDto {
  id: string;
  invoiceNumber: string;
  purchaseOrderNumber?: string;
  vendorId: string;
  vendorName: string;
  vendorTaxId?: string;
  invoiceDate: Date;
  dueDate: Date;
  paymentTerms: PaymentTerms;
  currency: string;
  subtotalAmount: number;
  taxAmount: number;
  discountAmount: number;
  totalAmount: number;
  amountPaid: number;
  outstandingAmount: number;
  status: InvoiceStatus;
  items?: InvoiceItemDto[];
  notes?: string;
  internalNotes?: string;
  receivedDate: Date;
  processedDate?: Date;
  category?: string;
  glAccountCode?: string;
  costCenter?: string;
  attachments: Array<{ name: string; url: string; type: string; size: number }>;
  approvedBy?: string;
  approvedAt?: Date;
  rejectedBy?: string;
  rejectedAt?: Date;
  rejectionReason?: string;
  ocrProcessed: boolean;
  ocrConfidence?: number;
  createdAt: Date;
  updatedAt: Date;
  version: number;
}

/**
 * Invoice item DTO
 */
export interface InvoiceItemDto {
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
 * Paginated result
 */
export interface PaginatedResult<T> {
  data: T[];
  total: number;
  page: number;
  pageSize: number;
  totalPages: number;
  hasNext: boolean;
  hasPrevious: boolean;
}

/**
 * Invoice statistics
 */
export interface InvoiceStatistics {
  totalInvoices: number;
  totalAmount: number;
  paidAmount: number;
  outstandingAmount: number;
  overdueAmount: number;
  averageProcessingTime: number;
  statusBreakdown: Record<InvoiceStatus, number>;
  vendorBreakdown: Array<{ vendorId: string; vendorName: string; count: number; totalAmount: number }>;
  monthlyTrend: Array<{ month: string; count: number; amount: number }>;
}

/**
 * Dashboard data
 */
export interface DashboardData {
  pendingInvoices: number;
  pendingAmount: number;
  overdueInvoices: number;
  overdueAmount: number;
  dueThisWeek: number;
  dueThisWeekAmount: number;
  recentInvoices: InvoiceDto[];
  topVendors: Array<{ vendorId: string; vendorName: string; count: number; amount: number }>;
  statusSummary: Record<InvoiceStatus, number>;
}
