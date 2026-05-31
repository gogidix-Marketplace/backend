import { InvoiceValidation } from '../../../domain/models/invoice-validation.entity';

/**
 * Validation response DTO
 */
export interface InvoiceValidationDetailsDto {
  invoiceId: string;
  validatedAt: string;
  validatedBy: string;
  overallStatus: 'PASSED' | 'FAILED' | 'WARNING';
  validationResults: ValidationRuleResultDto[];
  errorCount: number;
  warningCount: number;
  validatorVersion: string;
}

/**
 * Validation rule response DTO
 */
export interface ValidationRuleResultDto {
  ruleName: string;
  passed: boolean;
  message: string;
  severity: 'ERROR' | 'WARNING' | 'INFO';
}

/**
 * Factory to create validation response DTO from domain entity
 */
export class ValidationResponseFactory {
  static fromEntity(validation: InvoiceValidation): InvoiceValidationDetailsDto {
    return {
      invoiceId: validation.getInvoiceId(),
      validatedAt: validation.getValidatedAt().toISOString(),
      validatedBy: validation.getValidatedBy(),
      overallStatus: validation.getOverallStatus(),
      validationResults: validation.getValidationResults(),
      errorCount: validation.getErrors().length,
      warningCount: validation.getWarnings().length,
      validatorVersion: validation.getValidatorVersion(),
    };
  }
}
