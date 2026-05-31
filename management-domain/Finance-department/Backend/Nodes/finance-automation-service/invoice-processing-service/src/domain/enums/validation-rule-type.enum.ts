/**
 * Validation rule types for invoice processing
 */
export enum ValidationRuleType {
  REQUIRED_FIELD = 'REQUIRED_FIELD',
  AMOUNT_RANGE = 'AMOUNT_RANGE',
  TAX_CALCULATION = 'TAX_CALCULATION',
  DUPLICATE_DETECTION = 'DUPLICATE_DETECTION',
  VENDOR_VALIDATION = 'VENDOR_VALIDATION',
  LINE_ITEM_VALIDATION = 'LINE_ITEM_VALIDATION',
  INVOICE_NUMBER_FORMAT = 'INVOICE_NUMBER_FORMAT',
  DATE_VALIDATION = 'DATE_VALIDATION',
  CURRENCY_VALIDATION = 'CURRENCY_VALIDATION',
  PO_MATCHING = 'PO_MATCHING',
}

/**
 * Validation severity levels
 */
export enum ValidationSeverity {
  ERROR = 'ERROR',
  WARNING = 'WARNING',
  INFO = 'INFO',
}

/**
 * Validation result status
 */
export enum ValidationResult {
  PASSED = 'PASSED',
  FAILED = 'FAILED',
  WARNING = 'WARNING',
  PENDING = 'PENDING',
}
