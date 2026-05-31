import { CommissionStatus } from '../../domain/enums/commission-status.enum';

/**
 * DTO for commission response
 */
export class CommissionResponseDto {
  id!: string;
  salesRepId!: string;
  salesRepName!: string;
  periodId!: string;
  ruleId!: string;
  ruleName!: string;
  status!: CommissionStatus;
  salesAmount!: number;
  commissionRate!: number;
  calculatedAmount!: number;
  adjustedAmount!: number;
  finalAmount!: number;
  currency!: string;
  transactionDate!: Date;
  settlementDate?: Date;
  paymentDate?: Date;
  splits!: CommissionSplitResponse[];
  adjustmentReason?: string;
  clawbackAmount!: number;
  clawbackReason?: string;
  clawbackDate?: Date;
  quotaAttained?: number;
  quotaTarget?: number;
  quotaPercentage?: number;
  acceleratorApplied?: number;
  productSales!: ProductSaleResponse[];
  tenantId!: string;
  organizationId!: string;
  approvedBy?: string;
  approvedAt?: Date;
  paidVia?: string;
  paymentReference?: string;
  isOverdue!: boolean;
  hasSplit!: boolean;
  createdAt!: Date;
  updatedAt!: Date;
  version!: number;
}

/**
 * DTO for commission split response
 */
export interface CommissionSplitResponse {
  salesRepId: string;
  salesRepName: string;
  percentage: number;
  role: string;
}

/**
 * DTO for product sale response
 */
export interface ProductSaleResponse {
  productId: string;
  productName: string;
  amount: number;
  commission: number;
}

/**
 * DTO for commission summary response
 */
export class CommissionSummaryResponseDto {
  totalCommissions!: number;
  totalAmount!: number;
  pendingAmount!: number;
  approvedAmount!: number;
  paidAmount!: number;
  averageCommission!: number;
  currency!: string;
}

/**
 * DTO for commission rule response
 */
export class CommissionRuleResponseDto {
  id!: string;
  name!: string;
  description?: string;
  calculationType!: string;
  baseRate!: number;
  tiers!: any[];
  applicationScope!: string;
  scopeFilters!: Record<string, string[]>;
  acceleratorType!: string;
  acceleratorThreshold?: number;
  acceleratorMultiplier?: number;
  capType?: string;
  capValue?: number;
  isActive!: boolean;
  effectiveDate!: Date;
  expirationDate?: Date;
  tenantId!: string;
  organizationId!: string;
  productRates!: [string, number][];
  customerRates!: [string, number][];
  createdAt!: Date;
  updatedAt!: Date;
  version!: number;
}

/**
 * DTO for commission period response
 */
export class CommissionPeriodResponseDto {
  id!: string;
  name!: string;
  periodType!: string;
  startDate!: Date;
  endDate!: Date;
  status!: string;
  processingDate?: Date;
  tenantId!: string;
  organizationId!: string;
  fiscalYear!: number;
  fiscalQuarter?: number;
  fiscalMonth?: number;
  durationInDays!: number;
  daysRemaining!: number;
  completionPercentage!: number;
  createdAt!: Date;
  updatedAt!: Date;
  version!: number;
}

/**
 * DTO for commission payout response
 */
export class CommissionPayoutResponseDto {
  id!: string;
  salesRepId!: string;
  salesRepName!: string;
  periodId!: string;
  periodName!: string;
  commissionIds!: string[];
  status!: string;
  grossAmount!: number;
  adjustments!: number;
  taxWithholdings!: number;
  deductions!: number;
  netAmount!: number;
  currency!: string;
  scheduledDate!: Date;
  processedDate?: Date;
  completedDate?: Date;
  paymentMethod?: string;
  paymentReference?: string;
  failureReason?: string;
  approvedBy?: string;
  approvedAt?: Date;
  tenantId!: string;
  organizationId!: string;
  notes?: string;
  isOverdue!: boolean;
  commissionCount!: number;
  createdAt!: Date;
  updatedAt!: Date;
  version!: number;
}
