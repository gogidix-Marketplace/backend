import { CommissionPeriod } from '../../models/commission-period.entity';
import { CommissionPeriodType, CommissionPeriodStatus } from '../../enums/commission-period-type.enum';

/**
 * Input port for commission period operations
 */
export interface CommissionPeriodServicePort {
  /**
   * Create a new commission period
   */
  createPeriod(request: CreatePeriodRequest): Promise<CommissionPeriod>;

  /**
   * Get period by ID
   */
  getPeriodById(periodId: string): Promise<CommissionPeriod | null>;

  /**
   * Get active periods
   */
  getActivePeriods(tenantId: string, organizationId?: string): Promise<CommissionPeriod[]>;

  /**
   * Get periods by type
   */
  getPeriodsByType(tenantId: string, type: CommissionPeriodType): Promise<CommissionPeriod[]>;

  /**
   * Get period for a specific date
   */
  getPeriodForDate(tenantId: string, date: Date): Promise<CommissionPeriod | null>;

  /**
   * Close period
   */
  closePeriod(periodId: string): Promise<CommissionPeriod>;

  /**
   * Lock period
   */
  lockPeriod(periodId: string): Promise<CommissionPeriod>;

  /**
   * Reopen period
   */
  reopenPeriod(periodId: string): Promise<CommissionPeriod>;

  /**
   * Start calculation for period
   */
  startCalculation(periodId: string): Promise<CommissionPeriod>;

  /**
   * Delete period
   */
  deletePeriod(periodId: string): Promise<void>;
}

/**
 * Request DTO for creating period
 */
export interface CreatePeriodRequest {
  name: string;
  periodType: CommissionPeriodType;
  startDate: Date;
  endDate: Date;
  tenantId: string;
  organizationId: string;
}

/**
 * Period summary
 */
export interface PeriodSummary {
  periodId: string;
  periodName: string;
  status: CommissionPeriodStatus;
  startDate: Date;
  endDate: Date;
  totalCommissions: number;
  totalAmount: number;
  pendingCommissions: number;
  approvedCommissions: number;
  paidCommissions: number;
}
