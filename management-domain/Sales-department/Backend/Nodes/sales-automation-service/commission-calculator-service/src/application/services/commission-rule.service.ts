import { Injectable, NotFoundException, BadRequestException } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { CommissionRule } from '../../domain/models/commission-rule.entity';
import { CommissionTier } from '../../domain/models/commission-tier.entity';
import { CommissionCalculationType, CommissionApplicationScope, AcceleratorType, CommissionTierType } from '../../domain/enums/commission-rule-type.enum';
import { CommissionRuleRepositoryPort } from '../../domain/ports/out';
import { RequestContextData } from '../../shared/context/request-context';
import { InvalidCommissionTierException } from '../../shared/exceptions/commission.exception';
import {
  CreateCommissionRuleDto,
  UpdateCommissionRuleDto,
  SetProductRateDto,
  SetCustomerRateDto,
  AddTierDto,
} from '../dtos';

/**
 * Commission Rule use case service
 */
@Injectable()
export class CommissionRuleService {
  constructor(
    private readonly commissionRuleRepository: CommissionRuleRepositoryPort,
  ) {}

  /**
   * Create a new commission rule
   */
  async createRule(
    dto: CreateCommissionRuleDto,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = new CommissionRule(
      dto.name,
      dto.calculationType,
      dto.baseRate,
      context.tenantId,
      context.organizationId || '',
      dto.effectiveDate ? new Date(dto.effectiveDate) : new Date(),
    );

    if (dto.description) {
      rule.setDescription(dto.description);
    }

    if (dto.applicationScope) {
      rule.setApplicationScope(dto.applicationScope);
    }

    if (dto.scopeFilters) {
      rule.setScopeFilters(dto.scopeFilters);
    }

    if (dto.acceleratorType) {
      rule.setAccelerator(
        dto.acceleratorType,
        dto.acceleratorThreshold,
        dto.acceleratorMultiplier,
      );
    }

    if (dto.capType) {
      rule.setCap(dto.capType, dto.capValue);
    }

    if (dto.expirationDate) {
      rule.setExpirationDate(new Date(dto.expirationDate));
    }

    // Add tiers if provided
    if (dto.tiers && Array.isArray(dto.tiers)) {
      for (const tierDto of dto.tiers) {
        const tier = new CommissionTier(
          tierDto.id || uuidv4(),
          tierDto.name,
          tierDto.tierType as CommissionTierType,
          tierDto.minThreshold,
          tierDto.commissionRate,
          tierDto.maxThreshold,
        );

        if (tierDto.fixedAmount !== undefined) {
          tier.setFixedAmount(tierDto.fixedAmount);
        }

        if (tierDto.multiplier !== undefined) {
          tier.setMultiplier(tierDto.multiplier);
        }

        try {
          rule.addTier(tier);
        } catch (error) {
          throw new InvalidCommissionTierException(error.message);
        }
      }
    }

    return await this.commissionRuleRepository.save(rule);
  }

  /**
   * Update commission rule
   */
  async updateRule(
    ruleId: string,
    dto: UpdateCommissionRuleDto,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.getRuleById(ruleId, context);

    if (dto.name !== undefined) {
      rule.setName(dto.name);
    }

    if (dto.description !== undefined) {
      rule.setDescription(dto.description);
    }

    if (dto.baseRate !== undefined) {
      rule.setBaseRate(dto.baseRate);
    }

    if (dto.applicationScope !== undefined) {
      rule.setApplicationScope(dto.applicationScope);
    }

    if (dto.scopeFilters !== undefined) {
      rule.setScopeFilters(dto.scopeFilters);
    }

    if (dto.acceleratorType !== undefined) {
      rule.setAccelerator(
        dto.acceleratorType,
        dto.acceleratorThreshold,
        dto.acceleratorMultiplier,
      );
    }

    if (dto.capType !== undefined) {
      rule.setCap(dto.capType, dto.capValue);
    }

    if (dto.expirationDate !== undefined) {
      rule.setExpirationDate(new Date(dto.expirationDate));
    }

    return await this.commissionRuleRepository.save(rule);
  }

  /**
   * Get rule by ID
   */
  async getRuleById(
    ruleId: string,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.commissionRuleRepository.findById(ruleId);

    if (!rule) {
      throw new NotFoundException(`Commission rule ${ruleId} not found`);
    }

    // Verify tenant access
    if (rule.getTenantId() !== context.tenantId) {
      throw new NotFoundException(`Commission rule ${ruleId} not found`);
    }

    return rule;
  }

  /**
   * Get all rules for tenant
   */
  async getRulesByTenant(
    context: RequestContextData,
  ): Promise<CommissionRule[]> {
    return await this.commissionRuleRepository.findByTenantId(
      context.tenantId,
      context.organizationId,
    );
  }

  /**
   * Get active rules
   */
  async getActiveRules(
    context: RequestContextData,
  ): Promise<CommissionRule[]> {
    return await this.commissionRuleRepository.findActive(
      context.tenantId,
      context.organizationId,
    );
  }

  /**
   * Delete rule
   */
  async deleteRule(
    ruleId: string,
    context: RequestContextData,
  ): Promise<void> {
    const rule = await this.getRuleById(ruleId, context);
    rule.deactivate();
    await this.commissionRuleRepository.save(rule);
  }

  /**
   * Add tier to rule
   */
  async addTier(
    ruleId: string,
    dto: AddTierDto,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.getRuleById(ruleId, context);

    const tier = new CommissionTier(
      dto.id || uuidv4(),
      dto.name,
      dto.tierType as CommissionTierType,
      dto.minThreshold,
      dto.commissionRate,
      dto.maxThreshold,
    );

    if (dto.multiplier !== undefined) {
      tier.setMultiplier(dto.multiplier);
    }

    try {
      rule.addTier(tier);
    } catch (error) {
      throw new InvalidCommissionTierException(error.message);
    }

    return await this.commissionRuleRepository.save(rule);
  }

  /**
   * Remove tier from rule
   */
  async removeTier(
    ruleId: string,
    tierId: string,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.getRuleById(ruleId, context);
    rule.removeTier(tierId);
    return await this.commissionRuleRepository.save(rule);
  }

  /**
   * Set product-specific rate
   */
  async setProductRate(
    ruleId: string,
    dto: SetProductRateDto,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.getRuleById(ruleId, context);
    rule.setProductRate(dto.productId, dto.rate);
    return await this.commissionRuleRepository.save(rule);
  }

  /**
   * Set customer-specific rate
   */
  async setCustomerRate(
    ruleId: string,
    dto: SetCustomerRateDto,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.getRuleById(ruleId, context);
    rule.setCustomerRate(dto.customerId, dto.rate);
    return await this.commissionRuleRepository.save(rule);
  }

  /**
   * Activate rule
   */
  async activateRule(
    ruleId: string,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.getRuleById(ruleId, context);
    rule.activate();
    return await this.commissionRuleRepository.save(rule);
  }

  /**
   * Deactivate rule
   */
  async deactivateRule(
    ruleId: string,
    context: RequestContextData,
  ): Promise<CommissionRule> {
    const rule = await this.getRuleById(ruleId, context);
    rule.deactivate();
    return await this.commissionRuleRepository.save(rule);
  }
}
