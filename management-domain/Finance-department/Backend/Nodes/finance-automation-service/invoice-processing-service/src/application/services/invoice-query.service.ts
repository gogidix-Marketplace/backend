import {Injectable, Logger, NotFoundException, Inject} from '@nestjs/common';
import { IInvoiceQuery, GetByIdQuery, GetByInvoiceNumberQuery, FindAllQuery, FindByVendorQuery, FindByStatusQuery, FindByDateRangeQuery, SearchInvoicesQuery, GetStatisticsQuery, GetDashboardQuery, InvoiceDto, PaginatedResult, InvoiceStatistics, DashboardData } from '../../domain/ports/input/invoice.query';
import { IInvoiceRepository } from '../../domain/ports/output/invoice-repository.interface';
import { Invoice } from '../../domain/models/invoice.entity';

/**
 * Query service for invoice operations
 * Implements the use cases for invoice query operations
 */
@Injectable()
export class InvoiceQueryService implements IInvoiceQuery {
  private readonly logger = new Logger(InvoiceQueryService.name);

  constructor(
    @Inject('IInvoiceRepository')
    private readonly invoiceRepository: IInvoiceRepository,
  ) {}

  async findById(query: GetByIdQuery): Promise<InvoiceDto | null> {
    this.logger.log(`Finding invoice by ID: ${query.invoiceId}`);

    const invoice = await this.invoiceRepository.findById(query.invoiceId);

    if (!invoice) {
      return null;
    }

    // Verify tenant access
    if (invoice.getTenantId() !== query.tenantId) {
      this.logger.warn(`Access denied: Invoice ${query.invoiceId} belongs to different tenant`);
      return null;
    }

    return this.mapToDto(invoice, query.includeItems);
  }

  async findByInvoiceNumber(query: GetByInvoiceNumberQuery): Promise<InvoiceDto | null> {
    this.logger.log(`Finding invoice by number: ${query.invoiceNumber}`);

    const invoice = await this.invoiceRepository.findByInvoiceNumber(
      query.invoiceNumber,
      query.tenantId,
    );

    if (!invoice) {
      return null;
    }

    return this.mapToDto(invoice, query.includeItems);
  }

  async findAll(query: FindAllQuery): Promise<PaginatedResult<InvoiceDto>> {
    this.logger.log(`Finding all invoices for tenant ${query.tenantId}, page ${query.page}`);

    const invoices = await this.invoiceRepository.findByTenantId(query.tenantId, {
      page: query.page,
      pageSize: query.pageSize,
      sortBy: query.sortBy,
      sortOrder: query.sortOrder,
      includeItems: query.includeItems,
    });

    const totalCount = await this.invoiceRepository.count({
      tenantId: query.tenantId,
    });

    const totalPages = Math.ceil(totalCount / (query.pageSize || 20));
    const currentPage = query.page || 1;

    return {
      data: invoices.map(invoice => this.mapToDto(invoice, query.includeItems)),
      total: totalCount,
      page: currentPage,
      pageSize: query.pageSize || 20,
      totalPages,
      hasNext: currentPage < totalPages,
      hasPrevious: currentPage > 1,
    };
  }

  async findByVendor(query: FindByVendorQuery): Promise<PaginatedResult<InvoiceDto>> {
    this.logger.log(`Finding invoices for vendor ${query.vendorId}`);

    const invoices = await this.invoiceRepository.findByVendorId(
      query.vendorId,
      query.tenantId,
      {
        page: query.page,
        pageSize: query.pageSize,
        sortBy: query.sortBy,
        sortOrder: query.sortOrder,
        includeItems: query.includeItems,
      },
    );

    const totalCount = await this.invoiceRepository.count({
      tenantId: query.tenantId,
      vendorId: query.vendorId,
    });

    const totalPages = Math.ceil(totalCount / (query.pageSize || 20));
    const currentPage = query.page || 1;

    return {
      data: invoices.map(invoice => this.mapToDto(invoice, query.includeItems)),
      total: totalCount,
      page: currentPage,
      pageSize: query.pageSize || 20,
      totalPages,
      hasNext: currentPage < totalPages,
      hasPrevious: currentPage > 1,
    };
  }

  async findByStatus(query: FindByStatusQuery): Promise<PaginatedResult<InvoiceDto>> {
    this.logger.log(`Finding invoices with status ${query.status}`);

    const invoices = await this.invoiceRepository.findByStatus(
      query.status,
      query.tenantId,
      {
        page: query.page,
        pageSize: query.pageSize,
        sortBy: query.sortBy,
        sortOrder: query.sortOrder,
        includeItems: query.includeItems,
      },
    );

    const totalCount = await this.invoiceRepository.count({
      tenantId: query.tenantId,
      status: query.status,
    });

    const totalPages = Math.ceil(totalCount / (query.pageSize || 20));
    const currentPage = query.page || 1;

    return {
      data: invoices.map(invoice => this.mapToDto(invoice, query.includeItems)),
      total: totalCount,
      page: currentPage,
      pageSize: query.pageSize || 20,
      totalPages,
      hasNext: currentPage < totalPages,
      hasPrevious: currentPage > 1,
    };
  }

  async findByDateRange(query: FindByDateRangeQuery): Promise<PaginatedResult<InvoiceDto>> {
    this.logger.log(`Finding invoices between ${query.startDate} and ${query.endDate}`);

    // Filter out processedDate if specified (not supported by repository)
    const dateField = query.dateField === 'processedDate' ? 'receivedDate' : query.dateField;

    const invoices = await this.invoiceRepository.findByDateRange(
      query.startDate,
      query.endDate,
      query.tenantId,
      dateField,
      {
        page: query.page,
        pageSize: query.pageSize,
        sortBy: query.sortBy,
        sortOrder: query.sortOrder,
        includeItems: query.includeItems,
      },
    );

    const totalCount = await this.invoiceRepository.count({
      tenantId: query.tenantId,
      startDate: query.startDate,
      endDate: query.endDate,
    });

    const totalPages = Math.ceil(totalCount / (query.pageSize || 20));
    const currentPage = query.page || 1;

    return {
      data: invoices.map(invoice => this.mapToDto(invoice, query.includeItems)),
      total: totalCount,
      page: currentPage,
      pageSize: query.pageSize || 20,
      totalPages,
      hasNext: currentPage < totalPages,
      hasPrevious: currentPage > 1,
    };
  }

  async searchInvoices(query: SearchInvoicesQuery): Promise<PaginatedResult<InvoiceDto>> {
    this.logger.log(`Searching invoices for term: ${query.searchTerm}`);

    const invoices = await this.invoiceRepository.search(
      {
        tenantId: query.tenantId,
        organizationId: query.organizationId,
        searchTerm: query.searchTerm,
      },
      {
        page: query.page,
        pageSize: query.pageSize,
        sortBy: query.sortBy,
        sortOrder: query.sortOrder,
        includeItems: query.includeItems,
      },
    );

    // For search, we'll estimate total count as the length of results
    // In a real implementation, you'd want a separate count method

    const totalCount = invoices.length;
    const totalPages = Math.ceil(totalCount / (query.pageSize || 20));
    const currentPage = query.page || 1;

    return {
      data: invoices.map(invoice => this.mapToDto(invoice, query.includeItems)),
      total: totalCount,
      page: currentPage,
      pageSize: query.pageSize || 20,
      totalPages,
      hasNext: currentPage < totalPages,
      hasPrevious: currentPage > 1,
    };
  }

  async getInvoiceStatistics(query: GetStatisticsQuery): Promise<InvoiceStatistics> {
    this.logger.log(`Getting invoice statistics for tenant ${query.tenantId}`);

    const stats = await this.invoiceRepository.getStatistics(
      query.tenantId,
      query.organizationId,
    );

    return {
      totalInvoices: stats.totalInvoices,
      totalAmount: stats.totalAmount,
      paidAmount: stats.totalAmount - stats.outstandingAmount,
      outstandingAmount: stats.outstandingAmount,
      overdueAmount: stats.overdueAmount,
      averageProcessingTime: 0, // Would be calculated from historical data
      statusBreakdown: stats.statusCounts as Record<string, number>,
      vendorBreakdown: stats.vendorCounts.map(v => ({
        vendorId: v.vendorId,
        vendorName: v.vendorId, // Would need to lookup actual name from vendor service
        count: v.count,
        totalAmount: v.totalAmount,
      })),
      monthlyTrend: [], // Would be calculated from historical data
    };
  }

  async getDashboardData(query: GetDashboardQuery): Promise<DashboardData> {
    this.logger.log(`Getting dashboard data for tenant ${query.tenantId}`);

    const stats = await this.invoiceRepository.getStatistics(
      query.tenantId,
      query.organizationId,
    );

    // Get pending invoices (RECEIVED, VALIDATING, VALIDATED)
    const pendingInvoices = await this.invoiceRepository.search({
      tenantId: query.tenantId,
      organizationId: query.organizationId,
      status: 'RECEIVED,VALIDATING,VALIDATED',
    });

    // Get overdue invoices
    const today = new Date();
    const overdueInvoices = await this.invoiceRepository.findByDateRange(
      new Date(0),
      today,
      query.tenantId,
      'dueDate',
      { pageSize: 100 },
    );

    // Get invoices due this week
    const nextWeek = new Date(today);
    nextWeek.setDate(nextWeek.getDate() + 7);
    const dueThisWeekInvoices = await this.invoiceRepository.findByDateRange(
      today,
      nextWeek,
      query.tenantId,
      'dueDate',
      { pageSize: 100 },
    );

    // Get recent invoices
    const recentInvoices = await this.invoiceRepository.findByTenantId(query.tenantId, {
      page: 1,
      pageSize: 5,
      sortBy: 'receivedDate',
      sortOrder: 'DESC',
    });

    return {
      pendingInvoices: pendingInvoices.length,
      pendingAmount: pendingInvoices.reduce((sum, inv) => sum + inv.getTotalAmount(), 0),
      overdueInvoices: overdueInvoices.filter(inv => inv.getStatus() !== 'PROCESSED' && inv.getDueDate() < today).length,
      overdueAmount: overdueInvoices
        .filter(inv => inv.getStatus() !== 'PROCESSED' && inv.getDueDate() < today)
        .reduce((sum, inv) => sum + inv.getOutstandingAmount(), 0),
      dueThisWeek: dueThisWeekInvoices.filter(inv => inv.getStatus() !== 'PROCESSED').length,
      dueThisWeekAmount: dueThisWeekInvoices
        .filter(inv => inv.getStatus() !== 'PROCESSED')
        .reduce((sum, inv) => sum + inv.getOutstandingAmount(), 0),
      recentInvoices: recentInvoices.slice(0, 5).map(inv => this.mapToDto(inv, false)),
      topVendors: stats.vendorCounts.slice(0, 10).map(v => ({
        vendorId: v.vendorId,
        vendorName: v.vendorId, // Would need to lookup actual name from vendor service
        count: v.count,
        amount: v.totalAmount,
      })),
      statusSummary: stats.statusCounts as Record<string, number>,
    };
  }

  private mapToDto(invoice: Invoice, includeItems: boolean = false): InvoiceDto {
    const dto: InvoiceDto = {
      id: invoice.id?.toString() || '',
      invoiceNumber: invoice.getInvoiceNumber(),
      purchaseOrderNumber: invoice.getPurchaseOrderNumber(),
      vendorId: invoice.getVendorId(),
      vendorName: invoice.getVendorName(),
      vendorTaxId: invoice.getVendorTaxId(),
      invoiceDate: invoice.getInvoiceDate(),
      dueDate: invoice.getDueDate(),
      paymentTerms: invoice.getPaymentTerms(),
      currency: invoice.getCurrency(),
      subtotalAmount: invoice.getSubtotalAmount(),
      taxAmount: invoice.getTaxAmount(),
      discountAmount: invoice.getDiscountAmount(),
      totalAmount: invoice.getTotalAmount(),
      amountPaid: invoice.getAmountPaid(),
      outstandingAmount: invoice.getOutstandingAmount(),
      status: invoice.getStatus(),
      notes: invoice.getNotes(),
      internalNotes: invoice.getInternalNotes(),
      receivedDate: invoice.getReceivedDate(),
      processedDate: invoice.getProcessedDate(),
      category: invoice.getCategory(),
      glAccountCode: invoice.getGlAccountCode(),
      costCenter: invoice.getCostCenter(),
      attachments: invoice.getAttachments(),
      approvedBy: invoice.getApprovedBy(),
      approvedAt: invoice.getApprovedAt(),
      rejectedBy: invoice.getRejectedBy(),
      rejectedAt: invoice.getRejectedAt(),
      rejectionReason: invoice.getRejectionReason(),
      ocrProcessed: invoice.getOcrProcessed(),
      ocrConfidence: invoice.getOcrConfidence(),
      createdAt: invoice.createdAtDate,
      updatedAt: invoice.updatedAtDate,
      version: invoice.getVersion,
    };

    if (includeItems) {
      dto.items = invoice.getItems().map(item => ({
        lineNumber: item.getLineNumber(),
        description: item.getDescription(),
        quantity: item.getQuantity(),
        unitPrice: item.getUnitPrice(),
        taxRate: item.getTaxRate(),
        taxAmount: item.getTaxAmount(),
        discountAmount: item.getDiscountAmount(),
        totalAmount: item.getTotalAmount(),
        subtotal: item.getSubtotalAmount(),
        sku: item.getSku(),
        unitOfMeasure: item.getUnitOfMeasure(),
      }));
    }

    return dto;
  }
}
