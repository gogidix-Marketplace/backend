import { CommissionPeriod } from '../../models/commission-period.entity';
import { CommissionPeriodType, CommissionPeriodStatus } from '../../enums/commission-period-type.enum';

/**
 * Output port for commission period repository
 */
export interface CommissionPeriodRepositoryPort {
  /**
   * Save a period (create or update)
   */
  save(period: CommissionPeriod): Promise<CommissionPeriod>;

  /**
   * Find period by ID
   */
  findById(periodId: string): Promise<CommissionPeriod | null>;

  /**
   * Find periods by tenant
   */
  findByTenantId(tenantId: string, organizationId?: string): Promise<CommissionPeriod[]>;

  /**
   * Find periods by type
   */
  findByType(tenantId: string, type: CommissionPeriodType): Promise<CommissionPeriod[]>;

  /**
   * Find periods by status
   */
  findByStatus(tenantId: string, status: CommissionPeriodStatus): Promise<CommissionPeriod[]>;

  /**
   * Find period containing a specific date
   */
  findForDate(tenantId: string, date: Date): Promise<CommissionPeriod | null>;

  /**
   * Find active periods
   */
  findActive(tenantId: string, organizationId?: string): Promise<CommissionPeriod[]>;

  /**
   * Delete period
   */
  delete(periodId: string): Promise<void>;

  /**
   * Check if period exists
   */
  exists(periodId: string): Promise<boolean>;
}
