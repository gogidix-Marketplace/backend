import { Invoice } from '../models/invoice.entity';
import { InvoiceStatus, InvoiceValidation } from '../models/invoice-validation.entity';
import { OcrResult } from '../models/ocr-result.entity';

/**
 * Repository interface for Invoice entity
 * Following the Repository pattern from hexagonal architecture
 */
export interface IInvoiceRepository {
  // CRUD operations
  save(invoice: Invoice): Promise<Invoice>;
  update(invoice: Invoice): Promise<Invoice>;
  delete(invoiceId: string): Promise<void>;
  findById(invoiceId: string): Promise<Invoice | null>;
  findAll(tenantId: string, organizationId: string, options?: FindOptions): Promise<Invoice[]>;

  // Query operations
  findByInvoiceNumber(invoiceNumber: string, tenantId: string, organizationId: string): Promise<Invoice | null>;
  findByVendor(vendorId: string, tenantId: string, organizationId: string, options?: FindOptions): Promise<Invoice[]>;
  findByStatus(status: InvoiceStatus, tenantId: string, organizationId: string, options?: FindOptions): Promise<Invoice[]>;
  findByDateRange(startDate: Date, endDate: Date, tenantId: string, organizationId: string, dateField?: 'invoiceDate' | 'dueDate' | 'receivedDate'): Promise<Invoice[]>;
  findByPoNumber(poNumber: string, tenantId: string, organizationId: string): Promise<Invoice[]>;

  // Duplicate detection
  findDuplicate(invoiceNumber: string, vendorId: string, tenantId: string, organizationId: string, withinDays?: number): Promise<Invoice | null>;

  // Statistics
  countByStatus(tenantId: string, organizationId: string): Promise<Map<InvoiceStatus, number>>;
  getTotalAmountByStatus(tenantId: string, organizationId: string, status: InvoiceStatus): Promise<number>;

  // Pagination
  findPaginated(tenantId: string, organizationId: string, options: PaginationOptions): Promise<PaginatedResult<Invoice>>;

  // Search
  search(tenantId: string, organizationId: string, searchTerm: string, options?: FindOptions): Promise<Invoice[]>;
}

/**
 * Repository interface for InvoiceValidation entity
 */
export interface IInvoiceValidationRepository {
  save(validation: InvoiceValidation): Promise<InvoiceValidation>;
  findByInvoiceId(invoiceId: string): Promise<InvoiceValidation | null>;
  findLatestByInvoiceId(invoiceId: string): Promise<InvoiceValidation | null>;
  findAllByInvoiceId(invoiceId: string): Promise<InvoiceValidation[]>;
  delete(validationId: string): Promise<void>;
}

/**
 * Repository interface for OcrResult entity
 */
export interface IOcrResultRepository {
  save(ocrResult: OcrResult): Promise<OcrResult>;
  findById(resultId: string): Promise<OcrResult | null>;
  findByInvoiceId(invoiceId: string): Promise<OcrResult | null>;
  findByFileId(fileId: string): Promise<OcrResult | null>;
  delete(resultId: string): Promise<void>;
}

/**
 * Find options for queries
 */
export interface FindOptions {
  sortBy?: string;
  sortOrder?: 'ASC' | 'DESC';
  limit?: number;
  offset?: number;
  includeItems?: boolean;
  includeValidation?: boolean;
}

/**
 * Pagination options
 */
export interface PaginationOptions {
  page: number;
  pageSize: number;
  sortBy?: string;
  sortOrder?: 'ASC' | 'DESC';
}

/**
 * Paginated result wrapper
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
