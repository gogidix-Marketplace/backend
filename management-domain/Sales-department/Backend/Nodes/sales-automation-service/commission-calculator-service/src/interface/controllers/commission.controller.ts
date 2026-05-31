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
} from '@nestjs/common';
import { CommissionService } from '../../application/services/commission.service';
import { RequestContextHolder } from '../../shared/context/request-context';
import {
  CreateCommissionDto,
  CreateCommissionWithSplitsDto,
  UpdateCommissionDto,
  ApplyAdjustmentDto,
  ClawbackDto,
  ProcessPaymentDto,
  CalculateCommissionDto,
  CommissionQueryDto,
} from '../../application/dtos';
import { Roles } from '../guards/role.guard';
import { TenantGuard } from '../guards/tenant.guard';
import { LoggingInterceptor, TransformInterceptor } from '../interceptors';

/**
 * REST Controller for Commission operations
 */
@Controller('commissions')
@UseGuards(TenantGuard)
@UseInterceptors(LoggingInterceptor, TransformInterceptor)
export class CommissionController {
  constructor(private readonly commissionService: CommissionService) {}

  /**
   * Calculate commission for a transaction
   */
  @Post('calculate')
  async calculateCommission(@Body() dto: CalculateCommissionDto) {
    const context = RequestContextHolder.getContext()!;
    const commission = await this.commissionService.calculateCommission(dto, context);
    return this.toResponseDto(commission);
  }

  /**
   * Create a new commission
   */
  @Post()
  @Roles('admin', 'sales-manager')
  async createCommission(@Body() dto: CreateCommissionWithSplitsDto) {
    const context = RequestContextHolder.getContext()!;
    const commission = await this.commissionService.createCommission(dto, context);
    return this.toResponseDto(commission);
  }

  /**
   * Get commission by ID
   */
  @Get(':id')
  async getCommission(@Param('id') id: string) {
    const commission = await this.commissionService.getCommissionById(id);
    return this.toResponseDto(commission);
  }

  /**
   * Get commissions for a sales rep
   */
  @Get('sales-rep/:salesRepId')
  async getCommissionsBySalesRep(
    @Param('salesRepId') salesRepId: string,
    @Query() query: CommissionQueryDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const commissions = await this.commissionService.getCommissionsBySalesRep(salesRepId, context, {
      status: query.status,
      periodId: query.periodId,
      startDate: query.startDate ? new Date(query.startDate) : undefined,
      endDate: query.endDate ? new Date(query.endDate) : undefined,
    });
    return commissions.map(c => this.toResponseDto(c));
  }

  /**
   * Query commissions with filters
   */
  @Get()
  async queryCommissions(@Query() query: CommissionQueryDto) {
    const context = RequestContextHolder.getContext()!;
    const summary = await this.commissionService.getCommissionSummary(context, {
      salesRepId: query.salesRepId,
      periodId: query.periodId,
      status: query.status,
      startDate: query.startDate ? new Date(query.startDate) : undefined,
      endDate: query.endDate ? new Date(query.endDate) : undefined,
    });
    return summary;
  }

  /**
   * Submit commission for approval
   */
  @Post(':id/submit')
  @Roles('sales-rep', 'sales-manager')
  async submitForApproval(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const commission = await this.commissionService.submitForApproval(id, context);
    return this.toResponseDto(commission);
  }

  /**
   * Approve commission
   */
  @Post(':id/approve')
  @Roles('admin', 'sales-manager', 'finance')
  async approveCommission(
    @Param('id') id: string,
    @Body() body: { approvedBy?: string },
  ) {
    const context = RequestContextHolder.getContext()!;
    const commission = await this.commissionService.approveCommission(
      id,
      body.approvedBy || context.userId,
      context,
    );
    return this.toResponseDto(commission);
  }

  /**
   * Apply adjustment to commission
   */
  @Post(':id/adjustment')
  @Roles('admin', 'finance')
  async applyAdjustment(
    @Param('id') id: string,
    @Body() dto: ApplyAdjustmentDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const commission = await this.commissionService.applyAdjustment(id, dto, context);
    return this.toResponseDto(commission);
  }

  /**
   * Process payment
   */
  @Post(':id/payment')
  @Roles('admin', 'finance')
  async processPayment(
    @Param('id') id: string,
    @Body() dto: ProcessPaymentDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const commission = await this.commissionService.processPayment(id, dto, context);
    return this.toResponseDto(commission);
  }

  /**
   * Clawback commission
   */
  @Post(':id/clawback')
  @Roles('admin', 'finance')
  async clawbackCommission(
    @Param('id') id: string,
    @Body() dto: ClawbackDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const commission = await this.commissionService.clawbackCommission(id, dto, context);
    return this.toResponseDto(commission);
  }

  /**
   * Get pending approvals
   */
  @Get('approvals/pending')
  @Roles('admin', 'sales-manager', 'finance')
  async getPendingApprovals(@Query('limit') limit?: string) {
    const context = RequestContextHolder.getContext()!;
    const commissions = await this.commissionService.getPendingApprovals(
      context,
      limit ? parseInt(limit, 10) : 50,
    );
    return commissions.map(c => this.toResponseDto(c));
  }

  /**
   * Get overdue commissions
   */
  @Get('overdue/list')
  @Roles('admin', 'sales-manager')
  async getOverdueCommissions() {
    const context = RequestContextHolder.getContext()!;
    const commissions = await this.commissionService.getOverdueCommissions(context);
    return commissions.map(c => this.toResponseDto(c));
  }

  /**
   * Calculate period commissions
   */
  @Post('periods/:periodId/calculate')
  @Roles('admin', 'sales-manager')
  async calculatePeriodCommissions(
    @Param('periodId') periodId: string,
  ) {
    const context = RequestContextHolder.getContext()!;
    const commissions = await this.commissionService.calculatePeriodCommissions(periodId, context);
    return commissions.map(c => this.toResponseDto(c));
  }

  /**
   * Get commission summary
   */
  @Get('summary/stats')
  async getCommissionSummary(@Query() query: CommissionQueryDto) {
    const context = RequestContextHolder.getContext()!;
    return await this.commissionService.getCommissionSummary(context, {
      salesRepId: query.salesRepId,
      periodId: query.periodId,
      status: query.status,
      startDate: query.startDate ? new Date(query.startDate) : undefined,
      endDate: query.endDate ? new Date(query.endDate) : undefined,
    });
  }

  private toResponseDto(commission: any) {
    return commission.toObject();
  }
}
