import {Injectable, Logger, Inject} from '@nestjs/common';
import { Invoice } from '../../domain/models/invoice.entity';
import { InvoiceValidation, ValidationRuleResult, InvoiceStatus } from '../../domain/models/invoice-validation.entity';
import { IInvoiceValidationRepository } from '../../domain/ports/output/invoice-repository.interface';
import { InvoiceValidatedEventFactory } from '../../domain/events/invoice-validated.event';
import { IEventPublisher, EventTopic } from '../../domain/ports/output/event-publisher.interface';

/**
 * Validation rule interface
 */
interface IValidationRule {
  name: string;
  validate(invoice: Invoice): ValidationRuleResult;
}

/**
 * Service for invoice validation
 * Implements business rules for invoice validation
 */
@Injectable()
export class InvoiceValidationService {
  private readonly logger = new Logger(InvoiceValidationService.name);

  private validationRules: Map<string, IValidationRule> = new Map();

  constructor(
    @Inject('IInvoiceValidationRepository')
    private readonly validationRepository: IInvoiceValidationRepository,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
  ) {
    this.registerDefaultRules();
  }

  /**
   * Validate an invoice against registered rules
   */
  async validateInvoice(
    invoice: Invoice,
    validatedBy: string,
    ruleNames?: string[],
  ): Promise<InvoiceValidation> {
    this.logger.log(`Validating invoice ${invoice.getInvoiceNumber()}`);

    const validation = new InvoiceValidation(
      invoice.id?.toString() || '',
      validatedBy,
    );

    // Determine which rules to apply
    const rulesToApply = ruleNames
      ? this.getRulesByName(ruleNames)
      : Array.from(this.validationRules.values());

    // Apply each rule
    const results: ValidationRuleResult[] = [];
    for (const rule of rulesToApply) {
      try {
        const result = rule.validate(invoice);
        results.push(result);
        this.logger.debug(
          `Rule ${rule.name}: ${result.passed ? 'PASSED' : 'FAILED'} - ${result.message}`,
        );
      } catch (error) {
        this.logger.error(`Error executing rule ${rule.name}: ${error.message}`, error.stack);
        results.push({
          ruleName: rule.name,
          passed: false,
          message: `Rule execution error: ${error.message}`,
          severity: 'ERROR',
        });
      }
    }

    validation.addValidationResults(results);

    // Save validation result
    await this.validationRepository.save(validation);

    // Publish event
    const event = InvoiceValidatedEventFactory.create(
      invoice.id?.toString() || '',
      invoice.getTenantId(),
      invoice.getOrganizationId(),
      validation,
    );
    await this.eventPublisher.publishToTopic(EventTopic.INVOICE_VALIDATED, event);

    return validation;
  }

  /**
   * Register a custom validation rule
   */
  registerRule(rule: IValidationRule): void {
    this.validationRules.set(rule.name, rule);
    this.logger.log(`Registered validation rule: ${rule.name}`);
  }

  /**
   * Get validation by invoice ID
   */
  async getValidationByInvoiceId(invoiceId: string): Promise<InvoiceValidation | null> {
    return this.validationRepository.findLatestByInvoiceId(invoiceId);
  }

  /**
   * Get all validations for an invoice
   */
  async getAllValidationsByInvoiceId(invoiceId: string): Promise<InvoiceValidation[]> {
    return this.validationRepository.findAllByInvoiceId(invoiceId);
  }

  private registerDefaultRules(): void {
    // Required fields rule
    this.registerRule({
      name: 'REQUIRED_FIELDS',
      validate: (invoice: Invoice) => {
        const errors: string[] = [];

        if (!invoice.getInvoiceNumber()) errors.push('Invoice number is required');
        if (!invoice.getVendorId()) errors.push('Vendor ID is required');
        if (!invoice.getVendorName()) errors.push('Vendor name is required');
        if (!invoice.getInvoiceDate()) errors.push('Invoice date is required');
        if (!invoice.getDueDate()) errors.push('Due date is required');

        if (invoice.getItems().length === 0) {
          errors.push('At least one line item is required');
        }

        return {
          ruleName: 'REQUIRED_FIELDS',
          passed: errors.length === 0,
          message: errors.length > 0 ? errors.join(', ') : 'All required fields present',
          severity: errors.length > 0 ? 'ERROR' : 'INFO',
        };
      },
    });

    // Invoice amount validation rule
    this.registerRule({
      name: 'INVOICE_AMOUNT',
      validate: (invoice: Invoice) => {
        const totalAmount = invoice.getTotalAmount();

        if (totalAmount < 0) {
          return {
            ruleName: 'INVOICE_AMOUNT',
            passed: false,
            message: 'Total amount cannot be negative',
            severity: 'ERROR',
          };
        }

        if (totalAmount === 0 && invoice.getItems().length > 0) {
          return {
            ruleName: 'INVOICE_AMOUNT',
            passed: false,
            message: 'Total amount cannot be zero for invoice with items',
            severity: 'ERROR',
          };
        }

        // Verify calculated total matches item totals
        const calculatedTotal = invoice.getItems().reduce(
          (sum, item) => sum + item.getTotalAmount(),
          0,
        );

        const tolerance = 0.01; // Small tolerance for floating point
        if (Math.abs(totalAmount - calculatedTotal) > tolerance) {
          return {
            ruleName: 'INVOICE_AMOUNT',
            passed: false,
            message: `Total amount (${totalAmount}) does not match sum of line items (${calculatedTotal})`,
            severity: 'ERROR',
          };
        }

        return {
          ruleName: 'INVOICE_AMOUNT',
          passed: true,
          message: 'Invoice amount is valid',
          severity: 'INFO',
        };
      },
    });

    // Date validation rule
    this.registerRule({
      name: 'DATE_VALIDATION',
      validate: (invoice: Invoice) => {
        const invoiceDate = invoice.getInvoiceDate();
        const dueDate = invoice.getDueDate();
        const receivedDate = invoice.getReceivedDate();

        if (dueDate < invoiceDate) {
          return {
            ruleName: 'DATE_VALIDATION',
            passed: false,
            message: 'Due date cannot be before invoice date',
            severity: 'ERROR',
          };
        }

        // Check if invoice is significantly delayed (warning only)
        const daysDiff = Math.floor(
          (receivedDate.getTime() - invoiceDate.getTime()) / (1000 * 60 * 60 * 24),
        );

        if (daysDiff > 30) {
          return {
            ruleName: 'DATE_VALIDATION',
            passed: true,
            message: `Invoice received ${daysDiff} days after invoice date (possible delay)`,
            severity: 'WARNING',
          };
        }

        return {
          ruleName: 'DATE_VALIDATION',
          passed: true,
          message: 'Dates are valid',
          severity: 'INFO',
        };
      },
    });

    // Vendor validation rule
    this.registerRule({
      name: 'VENDOR_VALIDATION',
      validate: (invoice: Invoice) => {
        const vendorId = invoice.getVendorId();
        const vendorName = invoice.getVendorName();
        const vendorTaxId = invoice.getVendorTaxId();

        // Check vendor ID format (basic check)
        if (!vendorId || vendorId.length < 3) {
          return {
            ruleName: 'VENDOR_VALIDATION',
            passed: false,
            message: 'Vendor ID is too short',
            severity: 'ERROR',
          };
        }

        // Warning if vendor name is too generic
        const genericNames = ['unknown', 'tbd', 'to be determined', 'n/a'];
        if (vendorName && genericNames.includes(vendorName.toLowerCase())) {
          return {
            ruleName: 'VENDOR_VALIDATION',
            passed: true,
            message: 'Vendor name appears to be a placeholder',
            severity: 'WARNING',
          };
        }

        // Warning if no tax ID for high-value invoices
        if (!vendorTaxId && invoice.getTotalAmount() > 10000) {
          return {
            ruleName: 'VENDOR_VALIDATION',
            passed: true,
            message: 'High-value invoice should include vendor tax ID',
            severity: 'WARNING',
          };
        }

        return {
          ruleName: 'VENDOR_VALIDATION',
          passed: true,
          message: 'Vendor information is valid',
          severity: 'INFO',
        };
      },
    });

    // Line items validation rule
    this.registerRule({
      name: 'LINE_ITEMS_VALIDATION',
      validate: (invoice: Invoice) => {
        const items = invoice.getItems();

        if (items.length === 0) {
          return {
            ruleName: 'LINE_ITEMS_VALIDATION',
            passed: false,
            message: 'Invoice must have at least one line item',
            severity: 'ERROR',
          };
        }

        const warnings: string[] = [];

        for (const item of items) {
          // Check for zero or negative quantities
          if (item.getQuantity() <= 0) {
            return {
              ruleName: 'LINE_ITEMS_VALIDATION',
              passed: false,
              message: `Line ${item.getLineNumber()} has invalid quantity`,
              severity: 'ERROR',
            };
          }

          // Check for negative unit price
          if (item.getUnitPrice() < 0) {
            return {
              ruleName: 'LINE_ITEMS_VALIDATION',
              passed: false,
              message: `Line ${item.getLineNumber()} has negative unit price`,
              severity: 'ERROR',
            };
          }

          // Warning for missing SKU
          if (!item.getSku()) {
            warnings.push(`Line ${item.getLineNumber()} missing SKU`);
          }

          // Warning for zero unit price
          if (item.getUnitPrice() === 0) {
            warnings.push(`Line ${item.getLineNumber()} has zero unit price`);
          }
        }

        if (warnings.length > 0) {
          return {
            ruleName: 'LINE_ITEMS_VALIDATION',
            passed: true,
            message: warnings.join('; '),
            severity: 'WARNING',
          };
        }

        return {
          ruleName: 'LINE_ITEMS_VALIDATION',
          passed: true,
          message: `All ${items.length} line items are valid`,
          severity: 'INFO',
        };
      },
    });

    // Tax calculation validation rule
    this.registerRule({
      name: 'TAX_CALCULATION',
      validate: (invoice: Invoice) => {
        const items = invoice.getItems();
        const totalTax = invoice.getTaxAmount();
        const calculatedTax = items.reduce((sum, item) => sum + item.getTaxAmount(), 0);

        const tolerance = 0.01;
        if (Math.abs(totalTax - calculatedTax) > tolerance) {
          return {
            ruleName: 'TAX_CALCULATION',
            passed: false,
            message: `Tax amount mismatch: total ${totalTax} vs calculated ${calculatedTax}`,
            severity: 'ERROR',
          };
        }

        // Check for unusually high tax rate
        const avgTaxRate = items.length > 0
          ? items.reduce((sum, item) => sum + item.getTaxRate(), 0) / items.length
          : 0;

        if (avgTaxRate > 25) {
          return {
            ruleName: 'TAX_CALCULATION',
            passed: true,
            message: `Unusually high average tax rate: ${avgTaxRate}%`,
            severity: 'WARNING',
          };
        }

        return {
          ruleName: 'TAX_CALCULATION',
          passed: true,
          message: 'Tax calculations are correct',
          severity: 'INFO',
        };
      },
    });

    // Duplicate invoice check (would integrate with repository in real implementation)
    this.registerRule({
      name: 'DUPLICATE_CHECK',
      validate: (invoice: Invoice) => {
        // In real implementation, would query repository for similar invoices
        // For now, just check if invoice number looks like a duplicate
        const invoiceNumber = invoice.getInvoiceNumber();

        if (invoiceNumber.toLowerCase().includes('copy') ||
            invoiceNumber.toLowerCase().includes('dup')) {
          return {
            ruleName: 'DUPLICATE_CHECK',
            passed: true,
            message: 'Invoice number may indicate a duplicate',
            severity: 'WARNING',
          };
        }

        return {
          ruleName: 'DUPLICATE_CHECK',
          passed: true,
          message: 'No obvious duplicate indicators',
          severity: 'INFO',
        };
      },
    });

    this.logger.log(`Registered ${this.validationRules.size} default validation rules`);
  }

  private getRulesByName(names: string[]): IValidationRule[] {
    return names
      .map(name => this.validationRules.get(name))
      .filter((rule): rule is IValidationRule => rule !== undefined);
  }

  /**
   * Get all registered rule names
   */
  getRegisteredRuleNames(): string[] {
    return Array.from(this.validationRules.keys());
  }
}
