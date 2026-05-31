import { Commission } from '../../models/commission.entity';
import { CommissionRule } from '../../models/commission-rule.entity';
import { CommissionPeriod } from '../../models/commission-period.entity';
import { CommissionStatus } from '../../enums/commission-status.enum';
import { CommissionCalculatedEvent } from '../../events/commission-calculated.event';
import { CommissionFilters, CommissionSummary } from '../out/commission.repository.port';

/**
 * Input port for commission operations
 * Defines the contract for commission-related use cases
 */
export interface CommissionServicePort {
  /**
   * Calculate commission for a sales transaction
   */
  calculateCommission(request: CalculateCommissionRequest): Promise<Commission>;

  /**
   * Create a new commission record
   */
  createCommission(request: CreateCommissionRequest): Promise<Commission>;

  /**
   * Get commission by ID
   */
  getCommissionById(commissionId: string): Promise<Commission | null>;

  /**
   * Get commissions for a sales rep
   */
  getCommissionsBySalesRep(salesRepId: string, filters?: CommissionFilters): Promise<Commission[]>;

  /**
   * Submit commission for approval
   */
  submitForApproval(commissionId: string): Promise<Commission>;

  /**
   * Approve commission
   */
  approveCommission(commissionId: string, approvedBy: string): Promise<Commission>;

  /**
   * Reject commission
   */
  rejectCommission(commissionId: string, reason: string): Promise<Commission>;

  /**
   * Apply adjustment to commission
   */
  applyAdjustment(commissionId: string, amount: number, reason: string): Promise<Commission>;

  /**
   * Process commission payment
   */
  processPayment(commissionId: string, paymentMethod: string, reference?: string): Promise<Commission>;

  /**
   * Clawback commission
   */
  clawbackCommission(commissionId: string, amount: number, reason: string): Promise<Commission>;

  /**
   * Calculate commissions for a period
   */
  calculatePeriodCommissions(periodId: string): Promise<CommissionCalculatedEvent[]>;

  /**
   * Get commission summary
   */
  getCommissionSummary(filters: CommissionFilters): Promise<CommissionSummary>;
}

/**
 * Request DTO for commission calculation
 */
export interface CalculateCommissionRequest {
  salesRepId: string;
  salesRepName: string;
  ruleId: string;
  salesAmount: number;
  productId?: string;
  customerId?: string;
  quotaAttained?: number;
  quotaTarget?: number;
  periodId: string;
  transactionDate: Date;
  tenantId: string;
  organizationId: string;
  currency?: string;
}

/**
 * Request DTO for creating commission
 */
export interface CreateCommissionRequest {
  salesRepId: string;
  salesRepName: string;
  periodId: string;
  ruleId: string;
  ruleName: string;
  salesAmount: number;
  commissionRate: number;
  calculatedAmount: number;
  currency?: string;
  transactionDate?: Date;
  tenantId: string;
  organizationId: string;
}
