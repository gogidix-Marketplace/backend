import { Injectable, Logger } from '@nestjs/common';
import { AutomationRule } from '../../domain/models/automation-rule.entity';
import { AutomationRuleRepository } from '../../domain/ports/output';
import { AutomationRuleNotFoundException } from '@shared/exceptions';

@Injectable()
export class AutomationRuleQueryService {
  private readonly logger = new Logger(AutomationRuleQueryService.name);

  constructor(
    private readonly ruleRepository: AutomationRuleRepository,
  ) {}

  async findById(ruleId: string): Promise<AutomationRule> {
    this.logger.debug(`Finding automation rule by ID: ${ruleId}`);

    const rule = await this.ruleRepository.findById(ruleId);
    if (!rule) {
      throw new AutomationRuleNotFoundException(ruleId);
    }

    return rule;
  }

  async findByTenantId(tenantId: string, filters?: any): Promise<AutomationRule[]> {
    this.logger.debug(`Finding automation rules for tenant: ${tenantId}`);

    return this.ruleRepository.findByTenantId(tenantId, filters);
  }

  async findActiveByTenantId(tenantId: string): Promise<AutomationRule[]> {
    this.logger.debug(`Finding active automation rules for tenant: ${tenantId}`);

    return this.ruleRepository.findActiveByTenantId(tenantId);
  }

  async findByCategory(tenantId: string, category: string): Promise<AutomationRule[]> {
    this.logger.debug(`Finding automation rules by category: ${category} for tenant: ${tenantId}`);

    return this.ruleRepository.findByCategory(tenantId, category);
  }

  async findByTag(tenantId: string, tag: string): Promise<AutomationRule[]> {
    this.logger.debug(`Finding automation rules by tag: ${tag} for tenant: ${tenantId}`);

    return this.ruleRepository.findByTag(tenantId, tag);
  }

  async search(filters: any): Promise<AutomationRule[]> {
    this.logger.debug(`Searching automation rules with filters: ${JSON.stringify(filters)}`);

    return this.ruleRepository.findAll(filters, filters.tenantId);
  }

  async getStatistics(tenantId: string): Promise<{
    total: number;
    active: number;
    paused: number;
    draft: number;
    archived: number;
    byCategory: Record<string, number>;
  }> {
    this.logger.debug(`Getting automation rule statistics for tenant: ${tenantId}`);

    const allRules = await this.ruleRepository.findByTenantId(tenantId);

    const stats = {
      total: allRules.length,
      active: 0,
      paused: 0,
      draft: 0,
      archived: 0,
      byCategory: {} as Record<string, number>,
    };

    for (const rule of allRules) {
      switch (rule.status) {
        case 'ACTIVE':
          stats.active++;
          break;
        case 'PAUSED':
          stats.paused++;
          break;
        case 'DRAFT':
          stats.draft++;
          break;
        case 'ARCHIVED':
          stats.archived++;
          break;
      }

      if (rule.category) {
        stats.byCategory[rule.category] = (stats.byCategory[rule.category] || 0) + 1;
      }
    }

    return stats;
  }
}
