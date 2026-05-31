/**
 * Commission calculation type enumeration
 */
export enum CommissionCalculationType {
  PERCENTAGE = 'PERCENTAGE',
  FIXED_AMOUNT = 'FIXED_AMOUNT',
  TIERED = 'TIERED',
  GRADUATED = 'GRADUATED',
}

/**
 * Commission tier type enumeration
 */
export enum CommissionTierType {
  REVENUE_BASED = 'REVENUE_BASED',
  QUOTA_BASED = 'QUOTA_BASED',
  MARGIN_BASED = 'MARGIN_BASED',
  PRODUCT_BASED = 'PRODUCT_BASED',
}

/**
 * Commission application scope enumeration
 */
export enum CommissionApplicationScope {
  ALL_SALES = 'ALL_SALES',
  SPECIFIC_PRODUCTS = 'SPECIFIC_PRODUCTS',
  SPECIFIC_CUSTOMERS = 'SPECIFIC_CUSTOMERS',
  SPECIFIC_REGIONS = 'SPECIFIC_REGIONS',
}

/**
 * Accelerator type enumeration for quota-based commissions
 */
export enum AcceleratorType {
  NONE = 'NONE',
  LINEAR = 'LINEAR',
  STEP_UP = 'STEP_UP',
  RETROACTIVE = 'RETROACTIVE',
}
