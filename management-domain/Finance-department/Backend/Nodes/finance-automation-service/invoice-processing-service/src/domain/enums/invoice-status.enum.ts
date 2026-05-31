/**
 * Invoice status enumeration
 * Represents the lifecycle states of an invoice
 */
export enum InvoiceStatus {
  DRAFT = 'DRAFT',
  RECEIVED = 'RECEIVED',
  VALIDATING = 'VALIDATING',
  VALIDATED = 'VALIDATED',
  PROCESSING = 'PROCESSING',
  PROCESSED = 'PROCESSED',
  APPROVED = 'APPROVED',
  PAID = 'PAID',
  CANCELLED = 'CANCELLED',
  FAILED = 'FAILED',
}

/**
 * Valid status transitions for invoice state machine
 */
export const InvoiceStatusTransitions: Record<InvoiceStatus, InvoiceStatus[]> = {
  [InvoiceStatus.DRAFT]: [InvoiceStatus.RECEIVED, InvoiceStatus.CANCELLED],
  [InvoiceStatus.RECEIVED]: [InvoiceStatus.VALIDATING, InvoiceStatus.CANCELLED],
  [InvoiceStatus.VALIDATING]: [InvoiceStatus.VALIDATED, InvoiceStatus.FAILED, InvoiceStatus.CANCELLED],
  [InvoiceStatus.VALIDATED]: [InvoiceStatus.PROCESSING, InvoiceStatus.CANCELLED],
  [InvoiceStatus.PROCESSING]: [InvoiceStatus.PROCESSED, InvoiceStatus.FAILED, InvoiceStatus.CANCELLED],
  [InvoiceStatus.PROCESSED]: [InvoiceStatus.APPROVED, InvoiceStatus.CANCELLED],
  [InvoiceStatus.APPROVED]: [InvoiceStatus.PAID],
  [InvoiceStatus.PAID]: [],
  [InvoiceStatus.CANCELLED]: [],
  [InvoiceStatus.FAILED]: [InvoiceStatus.VALIDATING, InvoiceStatus.CANCELLED],
};

/**
 * Check if status transition is valid
 */
export function isValidStatusTransition(currentStatus: InvoiceStatus, newStatus: InvoiceStatus): boolean {
  const allowedTransitions = InvoiceStatusTransitions[currentStatus];
  return allowedTransitions.includes(newStatus);
}
