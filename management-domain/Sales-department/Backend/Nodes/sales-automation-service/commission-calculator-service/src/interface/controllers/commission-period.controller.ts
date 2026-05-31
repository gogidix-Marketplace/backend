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
import { CommissionPeriodService } from '../../application/services/commission-period.service';
import { RequestContextHolder } from '../../shared/context/request-context';
import { CommissionPeriodType } from '../../domain/enums/commission-period-type.enum';
import {
  CreateCommissionPeriodDto,
  UpdateCommissionPeriodDto,
} from '../../application/dtos';
import { Roles } from '../guards/role.guard';
import { TenantGuard } from '../guards/tenant.guard';
import { LoggingInterceptor, TransformInterceptor } from '../interceptors';

/**
 * REST Controller for Commission Period operations
 */
@Controller('commission-periods')
@UseGuards(TenantGuard)
@UseInterceptors(LoggingInterceptor, TransformInterceptor)
export class CommissionPeriodController {
  constructor(private readonly commissionPeriodService: CommissionPeriodService) {}

  /**
   * Create a new commission period
   */
  @Post()
  @Roles('admin', 'sales-manager')
  async createPeriod(@Body() dto: CreateCommissionPeriodDto) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.createPeriod(dto, context);
    return period.toObject();
  }

  /**
   * Get period by ID
   */
  @Get(':id')
  async getPeriod(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.getPeriodById(id, context);
    return period.toObject();
  }

  /**
   * Get active periods
   */
  @Get('active/list')
  async getActivePeriods() {
    const context = RequestContextHolder.getContext()!;
    const periods = await this.commissionPeriodService.getActivePeriods(context);
    return periods.map(p => p.toObject());
  }

  /**
   * Get periods by type
   */
  @Get('type/:type')
  async getPeriodsByType(@Param('type') type: CommissionPeriodType) {
    const context = RequestContextHolder.getContext()!;
    const periods = await this.commissionPeriodService.getPeriodsByType(type, context);
    return periods.map(p => p.toObject());
  }

  /**
   * Get period for a specific date
   */
  @Get('for-date')
  async getPeriodForDate(@Query('date') dateStr: string) {
    const context = RequestContextHolder.getContext()!;
    const date = dateStr ? new Date(dateStr) : new Date();
    const period = await this.commissionPeriodService.getPeriodForDate(date, context);
    return period ? period.toObject() : null;
  }

  /**
   * Update period
   */
  @Put(':id')
  @Roles('admin', 'sales-manager')
  async updatePeriod(
    @Param('id') id: string,
    @Body() dto: UpdateCommissionPeriodDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.updatePeriod(id, dto, context);
    return period.toObject();
  }

  /**
   * Close period
   */
  @Post(':id/close')
  @Roles('admin', 'sales-manager')
  async closePeriod(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.closePeriod(id, context);
    return period.toObject();
  }

  /**
   * Lock period
   */
  @Post(':id/lock')
  @Roles('admin')
  async lockPeriod(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.lockPeriod(id, context);
    return period.toObject();
  }

  /**
   * Reopen period
   */
  @Post(':id/reopen')
  @Roles('admin')
  async reopenPeriod(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.reopenPeriod(id, context);
    return period.toObject();
  }

  /**
   * Start calculation for period
   */
  @Post(':id/calculate')
  @Roles('admin', 'sales-manager')
  async startCalculation(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.startCalculation(id, context);
    return period.toObject();
  }

  /**
   * Delete period
   */
  @Delete(':id')
  @Roles('admin')
  async deletePeriod(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    await this.commissionPeriodService.deletePeriod(id, context);
    return { success: true, message: 'Period deleted successfully' };
  }

  /**
   * Create monthly period
   */
  @Post('create/monthly')
  @Roles('admin', 'sales-manager')
  async createMonthlyPeriod(
    @Body() body: { year: number; month: number },
  ) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.createMonthlyPeriod(
      body.year,
      body.month,
      context,
    );
    return period.toObject();
  }

  /**
   * Create quarterly period
   */
  @Post('create/quarterly')
  @Roles('admin', 'sales-manager')
  async createQuarterlyPeriod(
    @Body() body: { year: number; quarter: number },
  ) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.createQuarterlyPeriod(
      body.year,
      body.quarter,
      context,
    );
    return period.toObject();
  }

  /**
   * Create annual period
   */
  @Post('create/annual')
  @Roles('admin', 'sales-manager')
  async createAnnualPeriod(@Body() body: { year: number }) {
    const context = RequestContextHolder.getContext()!;
    const period = await this.commissionPeriodService.createAnnualPeriod(
      body.year,
      context,
    );
    return period.toObject();
  }
}
