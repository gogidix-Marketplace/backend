/**
 * Commission status enumeration
 * Represents the lifecycle of a commission
 */
export enum CommissionStatus {
  DRAFT = 'DRAFT',
  PENDING = 'PENDING',
  CALCULATED = 'CALCULATED',
  APPROVED = 'APPROVED',
  PAID = 'PAID',
  CLAWED_BACK = 'CLAWED_BACK',
  CANCELLED = 'CANCELLED',
}

/**
 * Valid status transitions for commissions
 */
export const CommissionStatusTransitions: Record<CommissionStatus, CommissionStatus[]> = {
  [CommissionStatus.DRAFT]: [CommissionStatus.PENDING, CommissionStatus.CANCELLED],
  [CommissionStatus.PENDING]: [CommissionStatus.CALCULATED, CommissionStatus.CANCELLED],
  [CommissionStatus.CALCULATED]: [CommissionStatus.APPROVED, CommissionStatus.PENDING, CommissionStatus.CANCELLED],
  [CommissionStatus.APPROVED]: [CommissionStatus.PAID, CommissionStatus.CALCULATED, CommissionStatus.CANCELLED],
  [CommissionStatus.PAID]: [CommissionStatus.CLAWED_BACK], // Only clawback after payment
  [CommissionStatus.CLAWED_BACK]: [],
  [CommissionStatus.CANCELLED]: [],
};
