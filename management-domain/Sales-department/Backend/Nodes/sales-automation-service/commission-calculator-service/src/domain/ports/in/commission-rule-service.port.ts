import { CommissionRule } from '../../models/commission-rule.entity';
import { CommissionCalculationType, CommissionApplicationScope, AcceleratorType } from '../../enums/commission-rule-type.enum';
import { CommissionTier } from '../../models/commission-tier.entity';

/**
 * Input port for commission rule operations
 */
export interface CommissionRuleServicePort {
  /**
   * Create a new commission rule
   */
  createRule(request: CreateRuleRequest): Promise<CommissionRule>;

  /**
   * Update commission rule
   */
  updateRule(ruleId: string, request: UpdateRuleRequest): Promise<CommissionRule>;

  /**
   * Get rule by ID
   */
  getRuleById(ruleId: string): Promise<CommissionRule | null>;

  /**
   * Get all rules for tenant
   */
  getRulesByTenant(tenantId: string, organizationId?: string): Promise<CommissionRule[]>;

  /**
   * Get active rules
   */
  getActiveRules(tenantId: string, organizationId?: string): Promise<CommissionRule[]>;

  /**
   * Delete rule
   */
  deleteRule(ruleId: string): Promise<void>;

  /**
   * Add tier to rule
   */
  addTier(ruleId: string, tier: Omit<CommissionTier, 'id'>): Promise<CommissionRule>;

  /**
   * Remove tier from rule
   */
  removeTier(ruleId: string, tierId: string): Promise<CommissionRule>;

  /**
   * Set product-specific rate
   */
  setProductRate(ruleId: string, productId: string, rate: number): Promise<CommissionRule>;

  /**
   * Set customer-specific rate
   */
  setCustomerRate(ruleId: string, customerId: string, rate: number): Promise<CommissionRule>;

  /**
   * Activate rule
   */
  activateRule(ruleId: string): Promise<CommissionRule>;

  /**
   * Deactivate rule
   */
  deactivateRule(ruleId: string): Promise<CommissionRule>;
}

/**
 * Request DTO for creating rule
 */
export interface CreateRuleRequest {
  name: string;
  description?: string;
  calculationType: CommissionCalculationType;
  baseRate: number;
  applicationScope?: CommissionApplicationScope;
  scopeFilters?: Record<string, string[]>;
  acceleratorType?: AcceleratorType;
  acceleratorThreshold?: number;
  acceleratorMultiplier?: number;
  capType?: 'NONE' | 'AMOUNT' | 'PERCENTAGE';
  capValue?: number;
  effectiveDate?: Date;
  expirationDate?: Date;
  tenantId: string;
  organizationId: string;
}

/**
 * Request DTO for updating rule
 */
export interface UpdateRuleRequest {
  name?: string;
  description?: string;
  baseRate?: number;
  applicationScope?: CommissionApplicationScope;
  scopeFilters?: Record<string, string[]>;
  acceleratorType?: AcceleratorType;
  acceleratorThreshold?: number;
  acceleratorMultiplier?: number;
  capType?: 'NONE' | 'AMOUNT' | 'PERCENTAGE';
  capValue?: number;
  expirationDate?: Date;
}
