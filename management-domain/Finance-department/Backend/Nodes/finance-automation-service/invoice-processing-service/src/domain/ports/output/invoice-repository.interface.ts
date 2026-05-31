import { Invoice } from '../../models/invoice.entity';
import { InvoiceValidation } from '../../models/invoice-validation.entity';

/**
 * Output port for invoice repository
 * Abstracts the data persistence mechanism from the domain
 */
export interface IInvoiceRepository {
  /**
   * Save a new invoice
   */
  save(invoice: Invoice): Promise<Invoice>;

  /**
   * Update an existing invoice
   */
  update(invoice: Invoice): Promise<Invoice>;

  /**
   * Find invoice by ID
   */
  findById(id: string | string): Promise<Invoice | null>;

  /**
   * Find invoice by invoice number
   */
  findByInvoiceNumber(invoiceNumber: string, tenantId: string): Promise<Invoice | null>;

  /**
   * Find invoices by tenant ID
   */
  findByTenantId(
    tenantId: string,
    options?: FindOptions,
  ): Promise<Invoice[]>;

  /**
   * Find invoices by vendor ID
   */
  findByVendorId(
    vendorId: string,
    tenantId: string,
    options?: FindOptions,
  ): Promise<Invoice[]>;

  /**
   * Find invoices by status
   */
  findByStatus(
    status: string,
    tenantId: string,
    options?: FindOptions,
  ): Promise<Invoice[]>;

  /**
   * Find invoices by date range
   */
  findByDateRange(
    startDate: Date,
    endDate: Date,
    tenantId: string,
    dateField?: 'invoiceDate' | 'dueDate' | 'receivedDate',
    options?: FindOptions,
  ): Promise<Invoice[]>;

  /**
   * Search invoices by criteria
   */
  search(
    criteria: SearchCriteria,
    options?: FindOptions,
  ): Promise<Invoice[]>;

  /**
   * Count invoices matching criteria
   */
  count(criteria: SearchCriteria): Promise<number>;

  /**
   * Delete an invoice (soft delete)
   */
  delete(id: string | string): Promise<void>;

  /**
   * Check if invoice number exists
   */
  existsByInvoiceNumber(invoiceNumber: string, tenantId: string): Promise<boolean>;

  /**
   * Get invoice statistics
   */
  getStatistics(tenantId: string, organizationId?: string): Promise<InvoiceStatistics>;
}

/**
 * Find options for pagination and sorting
 */
export interface FindOptions {
  page?: number;
  pageSize?: number;
  sortBy?: string;
  sortOrder?: 'ASC' | 'DESC';
  includeItems?: boolean;
  includeValidation?: boolean;
}

/**
 * Search criteria
 */
export interface SearchCriteria {
  tenantId: string;
  organizationId?: string;
  vendorId?: string;
  status?: string;
  startDate?: Date;
  endDate?: Date;
  searchTerm?: string;
  minAmount?: number;
  maxAmount?: number;
  category?: string;
  costCenter?: string;
  glAccountCode?: string;
}

/**
 * Invoice statistics
 */
export interface InvoiceStatistics {
  totalInvoices: number;
  totalAmount: number;
  outstandingAmount: number;
  overdueAmount: number;
  averageAmount: number;
  statusCounts: Record<string, number>;
  vendorCounts: Array<{ vendorId: string; count: number; totalAmount: number }>;
}

/**
 * Output port for invoice validation storage
 */
export interface IInvoiceValidationRepository {
  save(validation: InvoiceValidation): Promise<InvoiceValidation>;
  findByInvoiceId(invoiceId: string): Promise<InvoiceValidation | null>;
  findLatestByInvoiceId(invoiceId: string): Promise<InvoiceValidation | null>;
  findAllByInvoiceId(invoiceId: string): Promise<InvoiceValidation[]>;
}
