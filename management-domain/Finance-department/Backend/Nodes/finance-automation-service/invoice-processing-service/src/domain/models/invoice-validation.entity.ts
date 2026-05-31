import { BaseEntity } from '../../shared/base/base.entity';

/**
 * Invoice status enumeration
 */
export enum InvoiceStatus {
  DRAFT = 'DRAFT',
  RECEIVED = 'RECEIVED',
  VALIDATING = 'VALIDATING',
  VALIDATED = 'VALIDATED',
  PROCESSING = 'PROCESSING',
  PROCESSED = 'PROCESSED',
  FAILED = 'FAILED',
  CANCELLED = 'CANCELLED',
}

/**
 * Validation rule result
 */
export interface ValidationRuleResult {
  ruleName: string;
  passed: boolean;
  message: string;
  severity: 'ERROR' | 'WARNING' | 'INFO';
}

/**
 * Invoice validation entity
 * Contains validation results and business rule checks
 */
export class InvoiceValidation extends BaseEntity {
  private invoiceId: string;
  private validatedAt: Date;
  private validatedBy: string;
  private validationResults: ValidationRuleResult[];
  private overallStatus: 'PASSED' | 'FAILED' | 'WARNING';
  private validatorVersion: string;
  private validationContext: Record<string, unknown>;

  constructor(invoiceId: string, validatedBy: string) {
    super();
    this.invoiceId = invoiceId;
    this.validatedAt = new Date();
    this.validatedBy = validatedBy;
    this.validationResults = [];
    this.overallStatus = 'PASSED';
    this.validatorVersion = '1.0.0';
    this.validationContext = {};
  }

  addValidationResult(result: ValidationRuleResult): void {
    this.validationResults.push(result);
    this.updateOverallStatus();
    this.markAsUpdated();
  }

  addValidationResults(results: ValidationRuleResult[]): void {
    this.validationResults.push(...results);
    this.updateOverallStatus();
    this.markAsUpdated();
  }

  private updateOverallStatus(): void {
    const hasErrors = this.validationResults.some(r => r.severity === 'ERROR' && !r.passed);
    const hasWarnings = this.validationResults.some(r => r.severity === 'WARNING' && !r.passed);

    if (hasErrors) {
      this.overallStatus = 'FAILED';
    } else if (hasWarnings) {
      this.overallStatus = 'WARNING';
    } else {
      this.overallStatus = 'PASSED';
    }
  }

  hasPassed(): boolean {
    return this.overallStatus === 'PASSED';
  }

  hasWarnings(): boolean {
    return this.overallStatus === 'WARNING';
  }

  hasFailed(): boolean {
    return this.overallStatus === 'FAILED';
  }

  getErrors(): ValidationRuleResult[] {
    return this.validationResults.filter(r => r.severity === 'ERROR' && !r.passed);
  }

  getWarnings(): ValidationRuleResult[] {
    return this.validationResults.filter(r => r.severity === 'WARNING' && !r.passed);
  }

  // Getters
  getInvoiceId(): string {
    return this.invoiceId;
  }

  getValidatedAt(): Date {
    return this.validatedAt;
  }

  getValidatedBy(): string {
    return this.validatedBy;
  }

  getValidationResults(): ValidationRuleResult[] {
    return [...this.validationResults];
  }

  getOverallStatus(): 'PASSED' | 'FAILED' | 'WARNING' {
    return this.overallStatus;
  }

  getValidatorVersion(): string {
    return this.validatorVersion;
  }

  getValidationContext(): Record<string, unknown> {
    return { ...this.validationContext };
  }

  // Setters
  setValidationContext(context: Record<string, unknown>): void {
    this.validationContext = { ...this.validationContext, ...context };
    this.markAsUpdated();
  }

  setValidatorVersion(version: string): void {
    this.validatorVersion = version;
    this.markAsUpdated();
  }

  toObject(): Record<string, unknown> {
    return {
      invoiceId: this.invoiceId,
      validatedAt: this.validatedAt,
      validatedBy: this.validatedBy,
      validationResults: this.validationResults,
      overallStatus: this.overallStatus,
      validatorVersion: this.validatorVersion,
      validationContext: this.validationContext,
      errorCount: this.getErrors().length,
      warningCount: this.getWarnings().length,
    };
  }
}
