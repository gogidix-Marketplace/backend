import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Query,
  UseGuards,
  UseInterceptors,
  HttpStatus,
  HttpCode,
} from '@nestjs/common';
import { FileInterceptor } from '@nestjs/platform-express';
import { InvoiceCommandService } from '../../application/services/invoice-command.service';
import { InvoiceQueryService } from '../../application/services/invoice-query.service';
import { InvoiceValidationService } from '../../application/services/invoice-validation.service';
import { CreateInvoiceDto, UpdateInvoiceDto, SearchInvoicesDto } from '../../application/dto/requests';
import {
  InvoiceResponseDto,
  InvoiceListResponseDto,
  InvoiceStatisticsResponseDto,
  DashboardResponseDto,
  CommandResponseDto,
} from '../../application/dto/responses';
import { RequestContext, RequestContextType, TenantIdInterceptor } from './interceptors/tenant.interceptor';
import { InvoiceOcrService } from '../../application/services/invoice-ocr.service';
import { OcrOptionsDto } from '../../application/dto/requests/upload-invoice.dto';

/**
 * Controller for invoice operations
 * Handles HTTP requests for invoice CRUD and processing
 */
@Controller('invoices')
@UseInterceptors(TenantIdInterceptor)
export class InvoiceController {
  constructor(
    private readonly commandService: InvoiceCommandService,
    private readonly queryService: InvoiceQueryService,
    private readonly validationService: InvoiceValidationService,
    private readonly ocrService: InvoiceOcrService,
  ) {}

  /**
   * Create a new invoice
   */
  @Post()
  @HttpCode(HttpStatus.CREATED)
  async createInvoice(
    @Body() createInvoiceDto: CreateInvoiceDto,
    @RequestContext() context: RequestContextType,
  ): Promise<CommandResponseDto & { data?: InvoiceResponseDto }> {
    const command = {
      ...createInvoiceDto,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
    };

    const result = await this.commandService.createInvoice(command);

    if (result.success && result.invoiceId) {
      const invoice = await this.queryService.findById({
        invoiceId: result.invoiceId,
        tenantId: context.tenantId,
        organizationId: context.organizationId,
        userId: context.userId,
        includeItems: true,
      });

      return {
        ...result,
        data: invoice as unknown as InvoiceResponseDto,
      };
    }

    return result as unknown as CommandResponseDto & { data?: InvoiceResponseDto };
  }

  /**
   * Get invoice by ID
   */
  @Get(':id')
  async getInvoice(
    @Param('id') id: string,
    @Query('includeItems') includeItems?: string,
    @RequestContext() context?: RequestContextType,
  ): Promise<InvoiceResponseDto> {
    const invoice = await this.queryService.findById({
      invoiceId: id,
      tenantId: context?.tenantId || 'default',
      organizationId: context?.organizationId || 'default',
      userId: context?.userId || 'system',
      includeItems: includeItems === 'true',
    });

    if (!invoice) {
      throw new Error('Invoice not found');
    }

    return invoice as any;
  }

  /**
   * Get all invoices with pagination and filtering
   */
  @Get()
  async getInvoices(
    @Query() query: SearchInvoicesDto,
    @RequestContext() context?: RequestContextType,
  ): Promise<InvoiceListResponseDto> {
    const result = await this.queryService.searchInvoices({
      tenantId: context?.tenantId || 'default',
      organizationId: context?.organizationId || 'default',
      userId: context?.userId || 'system',
      searchTerm: query.searchTerm || '',
      page: query.page || 1,
      pageSize: query.pageSize || 20,
      sortBy: query.sortBy,
      sortOrder: query.sortOrder,
      includeItems: false,
    });

    return result as any;
  }

  /**
   * Update an invoice
   */
  @Put(':id')
  async updateInvoice(
    @Param('id') id: string,
    @Body() updateInvoiceDto: UpdateInvoiceDto,
    @RequestContext() context: RequestContextType,
  ): Promise<CommandResponseDto> {
    const command = {
      ...updateInvoiceDto,
      invoiceId: id,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
    };

    return this.commandService.updateInvoice(command);
  }

  /**
   * Validate an invoice
   */
  @Post(':id/validate')
  async validateInvoice(
    @Param('id') id: string,
    @Body() body: { validationRules?: string[] },
    @RequestContext() context: RequestContextType,
  ) {
    return this.commandService.validateInvoice({
      invoiceId: id,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
      validationRules: body.validationRules,
    });
  }

  /**
   * Approve an invoice
   */
  @Post(':id/approve')
  async approveInvoice(
    @Param('id') id: string,
    @Body() body: { notes?: string },
    @RequestContext() context: RequestContextType,
  ) {
    return this.commandService.approveInvoice({
      invoiceId: id,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
      notes: body.notes,
    });
  }

  /**
   * Reject an invoice
   */
  @Post(':id/reject')
  async rejectInvoice(
    @Param('id') id: string,
    @Body() body: { reason: string },
    @RequestContext() context: RequestContextType,
  ) {
    return this.commandService.rejectInvoice({
      invoiceId: id,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
      reason: body.reason,
    });
  }

  /**
   * Cancel an invoice
   */
  @Post(':id/cancel')
  async cancelInvoice(
    @Param('id') id: string,
    @RequestContext() context: RequestContextType,
    @Body() body?: { reason?: string },
  ) {
    return this.commandService.cancelInvoice({
      invoiceId: id,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
      reason: body?.reason,
    });
  }

  /**
   * Process invoice (OCR, validation, enrichment)
   */
  @Post(':id/process')
  async processInvoice(
    @Param('id') id: string,
    @Body() body: { processType: 'OCR' | 'VALIDATION' | 'ENRICHMENT'; options?: Record<string, unknown> },
    @RequestContext() context: RequestContextType,
  ) {
    return this.commandService.processInvoice({
      invoiceId: id,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
      processType: body.processType,
      options: body.options,
    });
  }

  /**
   * Get invoice statistics
   */
  @Get('statistics/summary')
  async getStatistics(
    @Query('startDate') startDate?: string,
    @Query('endDate') endDate?: string,
    @RequestContext() context?: RequestContextType,
  ): Promise<InvoiceStatisticsResponseDto> {
    return this.queryService.getInvoiceStatistics({
      tenantId: context?.tenantId || 'default',
      organizationId: context?.organizationId || 'default',
      userId: context?.userId || 'system',
      startDate: startDate ? new Date(startDate) : undefined,
      endDate: endDate ? new Date(endDate) : undefined,
    }) as Promise<InvoiceStatisticsResponseDto>;
  }

  /**
   * Get dashboard data
   */
  @Get('statistics/dashboard')
  async getDashboard(
    @RequestContext() context?: RequestContextType,
  ): Promise<DashboardResponseDto> {
    const data = await this.queryService.getDashboardData({
      tenantId: context?.tenantId || 'default',
      organizationId: context?.organizationId || 'default',
      userId: context?.userId || 'system',
    });

    // Convert Date fields to strings for response
    return {
      ...data,
      recentInvoices: data.recentInvoices.map(inv => ({
        ...inv,
        invoiceDate: inv.invoiceDate.toISOString(),
        dueDate: inv.dueDate.toISOString(),
        receivedDate: inv.receivedDate.toISOString(),
        processedDate: inv.processedDate?.toISOString(),
        createdAt: inv.createdAt.toISOString(),
        updatedAt: inv.updatedAt.toISOString(),
        approvedAt: inv.approvedAt?.toISOString(),
        rejectedAt: inv.rejectedAt?.toISOString(),
      })),
      // Convert amount to totalAmount for response DTO compatibility
      topVendors: data.topVendors.map(v => ({
        vendorId: v.vendorId,
        vendorName: v.vendorName,
        count: v.count,
        totalAmount: v.amount,
      })),
    };
  }

  /**
   * Delete an invoice (soft delete)
   */
  @Delete(':id')
  @HttpCode(HttpStatus.NO_CONTENT)
  async deleteInvoice(
    @Param('id') id: string,
    @RequestContext() context: RequestContextType,
  ): Promise<void> {
    // Soft delete implementation would go here
    // For now, we'll mark as cancelled
    await this.commandService.cancelInvoice({
      invoiceId: id,
      tenantId: context.tenantId,
      organizationId: context.organizationId,
      userId: context.userId,
      correlationId: context.correlationId,
      reason: 'Deleted via API',
    });
  }
}
