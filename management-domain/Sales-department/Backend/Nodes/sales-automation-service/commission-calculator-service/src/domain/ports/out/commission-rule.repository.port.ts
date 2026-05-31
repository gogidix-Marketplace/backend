import { CommissionRule } from '../../models/commission-rule.entity';

/**
 * Output port for commission rule repository
 */
export interface CommissionRuleRepositoryPort {
  /**
   * Save a rule (create or update)
   */
  save(rule: CommissionRule): Promise<CommissionRule>;

  /**
   * Find rule by ID
   */
  findById(ruleId: string): Promise<CommissionRule | null>;

  /**
   * Find all rules for tenant
   */
  findByTenantId(tenantId: string, organizationId?: string): Promise<CommissionRule[]>;

  /**
   * Find active rules for tenant
   */
  findActive(tenantId: string, organizationId?: string): Promise<CommissionRule[]>;

  /**
   * Find rules by name
   */
  findByName(name: string, tenantId: string): Promise<CommissionRule[]>;

  /**
   * Delete rule
   */
  delete(ruleId: string): Promise<void>;

  /**
   * Check if rule exists
   */
  exists(ruleId: string): Promise<boolean>;
}
