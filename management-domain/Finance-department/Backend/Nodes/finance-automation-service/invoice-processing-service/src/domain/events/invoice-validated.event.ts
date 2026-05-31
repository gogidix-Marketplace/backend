import { InvoiceValidation } from '../models/invoice-validation.entity';

/**
 * Domain event emitted when an invoice is validated
 */
export interface InvoiceValidatedEvent {
  eventType: 'InvoiceValidated';
  eventId: string;
  occurredAt: Date;
  tenantId: string;
  organizationId: string;
  correlationId?: string;
  payload: {
    invoiceId: string;
    validatedAt: Date;
    validatedBy: string;
    overallStatus: 'PASSED' | 'FAILED' | 'WARNING';
    validationResults: Array<{
      ruleName: string;
      passed: boolean;
      message: string;
      severity: 'ERROR' | 'WARNING' | 'INFO';
    }>;
    errorCount: number;
    warningCount: number;
    validatorVersion: string;
  };
}

export class InvoiceValidatedEventFactory {
  static create(
    invoiceId: string,
    tenantId: string,
    organizationId: string,
    validation: InvoiceValidation,
    correlationId?: string,
  ): InvoiceValidatedEvent {
    return {
      eventType: 'InvoiceValidated',
      eventId: this.generateEventId(),
      occurredAt: new Date(),
      tenantId,
      organizationId,
      correlationId,
      payload: {
        invoiceId,
        validatedAt: validation.getValidatedAt(),
        validatedBy: validation.getValidatedBy(),
        overallStatus: validation.getOverallStatus(),
        validationResults: validation.getValidationResults(),
        errorCount: validation.getErrors().length,
        warningCount: validation.getWarnings().length,
        validatorVersion: validation.getValidatorVersion(),
      },
    };
  }

  private static generateEventId(): string {
    return `evt-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
