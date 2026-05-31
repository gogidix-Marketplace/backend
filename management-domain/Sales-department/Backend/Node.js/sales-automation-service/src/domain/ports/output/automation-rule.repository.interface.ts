import { AutomationRule } from '../../models/automation-rule.entity';

export interface AutomationRuleRepository {
  save(rule: AutomationRule): Promise<AutomationRule>;
  findById(id: string): Promise<AutomationRule | null>;
  findByTenantId(tenantId: string, filters?: any): Promise<AutomationRule[]>;
  findActiveByTenantId(tenantId: string): Promise<AutomationRule[]>;
  findByCategory(tenantId: string, category: string): Promise<AutomationRule[]>;
  findByTag(tenantId: string, tag: string): Promise<AutomationRule[]>;
  findAll(filters?: any, tenantId?: string): Promise<AutomationRule[]>;
  delete(id: string): Promise<void>;
  exists(id: string): Promise<boolean>;
}
