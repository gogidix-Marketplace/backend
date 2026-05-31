/**
 * Commission period type enumeration
 */
export enum CommissionPeriodType {
  MONTHLY = 'MONTHLY',
  QUARTERLY = 'QUARTERLY',
  ANNUAL = 'ANNUAL',
  CUSTOM = 'CUSTOM',
}

/**
 * Commission period status enumeration
 */
export enum CommissionPeriodStatus {
  OPEN = 'OPEN',
  CALCULATING = 'CALCULATING',
  CLOSED = 'CLOSED',
  LOCKED = 'LOCKED',
}
