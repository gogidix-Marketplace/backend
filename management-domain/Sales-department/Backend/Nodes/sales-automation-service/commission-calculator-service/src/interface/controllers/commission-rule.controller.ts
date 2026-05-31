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
import { CommissionRuleService } from '../../application/services/commission-rule.service';
import { RequestContextHolder } from '../../shared/context/request-context';
import {
  CreateCommissionRuleDto,
  UpdateCommissionRuleDto,
  SetProductRateDto,
  SetCustomerRateDto,
  AddTierDto,
} from '../../application/dtos';
import { Roles } from '../guards/role.guard';
import { TenantGuard } from '../guards/tenant.guard';
import { LoggingInterceptor, TransformInterceptor } from '../interceptors';

/**
 * REST Controller for Commission Rule operations
 */
@Controller('commission-rules')
@UseGuards(TenantGuard)
@UseInterceptors(LoggingInterceptor, TransformInterceptor)
export class CommissionRuleController {
  constructor(private readonly commissionRuleService: CommissionRuleService) {}

  /**
   * Create a new commission rule
   */
  @Post()
  @Roles('admin', 'sales-manager')
  async createRule(@Body() dto: CreateCommissionRuleDto) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.createRule(dto, context);
    return rule.toObject();
  }

  /**
   * Get rule by ID
   */
  @Get(':id')
  async getRule(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.getRuleById(id, context);
    return rule.toObject();
  }

  /**
   * Get all rules for tenant
   */
  @Get()
  async getRules() {
    const context = RequestContextHolder.getContext()!;
    const rules = await this.commissionRuleService.getRulesByTenant(context);
    return rules.map(r => r.toObject());
  }

  /**
   * Get active rules
   */
  @Get('active/list')
  async getActiveRules() {
    const context = RequestContextHolder.getContext()!;
    const rules = await this.commissionRuleService.getActiveRules(context);
    return rules.map(r => r.toObject());
  }

  /**
   * Update rule
   */
  @Put(':id')
  @Roles('admin', 'sales-manager')
  async updateRule(
    @Param('id') id: string,
    @Body() dto: UpdateCommissionRuleDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.updateRule(id, dto, context);
    return rule.toObject();
  }

  /**
   * Delete rule (deactivate)
   */
  @Delete(':id')
  @Roles('admin')
  async deleteRule(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    await this.commissionRuleService.deleteRule(id, context);
    return { success: true, message: 'Rule deactivated successfully' };
  }

  /**
   * Add tier to rule
   */
  @Post(':id/tiers')
  @Roles('admin', 'sales-manager')
  async addTier(
    @Param('id') id: string,
    @Body() dto: AddTierDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.addTier(id, dto, context);
    return rule.toObject();
  }

  /**
   * Remove tier from rule
   */
  @Delete(':id/tiers/:tierId')
  @Roles('admin', 'sales-manager')
  async removeTier(
    @Param('id') id: string,
    @Param('tierId') tierId: string,
  ) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.removeTier(id, tierId, context);
    return rule.toObject();
  }

  /**
   * Set product-specific rate
   */
  @Post(':id/product-rates')
  @Roles('admin', 'sales-manager')
  async setProductRate(
    @Param('id') id: string,
    @Body() dto: SetProductRateDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.setProductRate(id, dto, context);
    return rule.toObject();
  }

  /**
   * Set customer-specific rate
   */
  @Post(':id/customer-rates')
  @Roles('admin', 'sales-manager')
  async setCustomerRate(
    @Param('id') id: string,
    @Body() dto: SetCustomerRateDto,
  ) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.setCustomerRate(id, dto, context);
    return rule.toObject();
  }

  /**
   * Activate rule
   */
  @Post(':id/activate')
  @Roles('admin', 'sales-manager')
  async activateRule(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.activateRule(id, context);
    return rule.toObject();
  }

  /**
   * Deactivate rule
   */
  @Post(':id/deactivate')
  @Roles('admin', 'sales-manager')
  async deactivateRule(@Param('id') id: string) {
    const context = RequestContextHolder.getContext()!;
    const rule = await this.commissionRuleService.deactivateRule(id, context);
    return rule.toObject();
  }
}
