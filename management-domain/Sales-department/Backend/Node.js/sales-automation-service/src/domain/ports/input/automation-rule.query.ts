import { AutomationRule } from '../../models/automation-rule.entity';

export interface AutomationRuleQuery {
  findById(ruleId: string): Promise<AutomationRule | null>;
  findByTenantId(tenantId: string, filters?: any): Promise<AutomationRule[]>;
  findActiveByTenantId(tenantId: string): Promise<AutomationRule[]>;
  findByCategory(tenantId: string, category: string): Promise<AutomationRule[]>;
  findByTag(tenantId: string, tag: string): Promise<AutomationRule[]>;
  search(filters: any): Promise<AutomationRule[]>;
  getStatistics(tenantId: string): Promise<{
    total: number;
    active: number;
    paused: number;
    draft: number;
    archived: number;
    byCategory: Record<string, number>;
  }>;
}
