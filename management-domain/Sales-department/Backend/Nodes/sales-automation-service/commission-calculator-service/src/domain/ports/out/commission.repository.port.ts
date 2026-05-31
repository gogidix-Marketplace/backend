import { Commission } from '../../models/commission.entity';
import { CommissionStatus } from '../../enums/commission-status.enum';

/**
 * Output port for commission repository
 * Defines the contract for persisting commissions
 */
export interface CommissionRepositoryPort {
  /**
   * Save a commission (create or update)
   */
  save(commission: Commission): Promise<Commission>;

  /**
   * Find commission by ID
   */
  findById(commissionId: string): Promise<Commission | null>;

  /**
   * Find commissions by sales rep ID
   */
  findBySalesRepId(salesRepId: string, options?: FindOptions): Promise<Commission[]>;

  /**
   * Find commissions by period ID
   */
  findByPeriodId(periodId: string): Promise<Commission[]>;

  /**
   * Find commissions by status
   */
  findByStatus(status: CommissionStatus, tenantId: string): Promise<Commission[]>;

  /**
   * Find commissions by multiple filters
   */
  find(filters: CommissionFilters): Promise<Commission[]>;

  /**
   * Delete commission
   */
  delete(commissionId: string): Promise<void>;

  /**
   * Count commissions by filters
   */
  count(filters: CommissionFilters): Promise<number>;

  /**
   * Get commission summary
   */
  getSummary(filters: CommissionFilters): Promise<CommissionSummary>;

  /**
   * Find pending commissions for approval
   */
  findPendingApprovals(tenantId: string, limit?: number): Promise<Commission[]>;

  /**
   * Find overdue unpaid commissions
   */
  findOverdue(tenantId: string): Promise<Commission[]>;

  /**
   * Batch save commissions
   */
  batchSave(commissions: Commission[]): Promise<Commission[]>;
}

/**
 * Options for finding commissions
 */
export interface FindOptions {
  status?: CommissionStatus;
  periodId?: string;
  startDate?: Date;
  endDate?: Date;
  limit?: number;
  offset?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

/**
 * Filters for commission queries
 */
export interface CommissionFilters {
  tenantId: string;
  organizationId?: string;
  salesRepId?: string;
  status?: CommissionStatus;
  periodId?: string;
  startDate?: Date;
  endDate?: Date;
  minAmount?: number;
  maxAmount?: number;
  currency?: string;
}

/**
 * Commission summary result
 */
export interface CommissionSummary {
  totalCommissions: number;
  totalAmount: number;
  pendingAmount: number;
  approvedAmount: number;
  paidAmount: number;
  averageCommission: number;
  currency: string;
}
