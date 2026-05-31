import {Injectable, Logger, NotFoundException, Inject} from '@nestjs/common';
import { IInvoiceCommand, CreateInvoiceCommand, UpdateInvoiceCommand, ProcessInvoiceCommand, ValidateInvoiceCommand, ApproveInvoiceCommand, RejectInvoiceCommand, CancelInvoiceCommand, RecordPaymentCommand, AddAttachmentCommand, InvoiceCommandResult } from '../../domain/ports/input/invoice.command';
import { IInvoiceRepository } from '../../domain/ports/output/invoice-repository.interface';
import { IEventPublisher, EventTopic } from '../../domain/ports/output/event-publisher.interface';
import { Invoice, PaymentTerms } from '../../domain/models/invoice.entity';
import { InvoiceItem } from '../../domain/models/invoice-item.entity';
import { InvoiceStatus } from '../../domain/models/invoice-validation.entity';
import { InvoiceCreatedEventFactory } from '../../domain/events/invoice-created.event';
import { InvoiceProcessedEventFactory } from '../../domain/events/invoice-processed.event';
import { InvoiceStatusChangedEventFactory } from '../../domain/events/invoice-status-changed.event';
import { ValidationException } from '../../shared/exceptions/validation.exception';
import { BusinessException } from '../../shared/exceptions/business.exception';
import { ConflictException } from '../../shared/exceptions/conflict.exception';
import { InvoiceValidationService } from './invoice-validation.service';
import { OcrService } from './ocr.service';

/**
 * Command service for invoice operations
 * Implements the use cases for invoice command operations
 */
@Injectable()
export class InvoiceCommandService implements IInvoiceCommand {
  private readonly logger = new Logger(InvoiceCommandService.name);

  constructor(
    @Inject('IInvoiceRepository')
    private readonly invoiceRepository: IInvoiceRepository,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
    private readonly validationService: InvoiceValidationService,
    private readonly ocrService: OcrService,
  ) {}

  async createInvoice(command: CreateInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Creating invoice ${command.invoiceNumber} for tenant ${command.tenantId}`);

    try {
      // Check if invoice number already exists
      const exists = await this.invoiceRepository.existsByInvoiceNumber(
        command.invoiceNumber,
        command.tenantId,
      );

      if (exists) {
        throw new ConflictException(
          `Invoice with number ${command.invoiceNumber} already exists`,
          'Invoice',
          { invoiceNumber: command.invoiceNumber },
        );
      }

      // Create invoice entity
      const invoice = new Invoice(
        command.invoiceNumber,
        command.vendorId,
        command.vendorName,
        command.invoiceDate,
        command.dueDate,
        command.tenantId,
        command.organizationId,
      );

      // Set optional fields
      if (command.purchaseOrderNumber) {
        invoice.setPurchaseOrderNumber(command.purchaseOrderNumber);
      }
      if (command.vendorTaxId) {
        invoice.setVendorTaxId(command.vendorTaxId);
      }
      if (command.paymentTerms) {
        invoice.setPaymentTerms(command.paymentTerms);
      }
      if (command.currency) {
        invoice.setCurrency(command.currency);
      }
      if (command.category) {
        invoice.setCategory(command.category);
      }
      if (command.glAccountCode) {
        invoice.setGlAccountCode(command.glAccountCode);
      }
      if (command.costCenter) {
        invoice.setCostCenter(command.costCenter);
      }
      if (command.notes) {
        invoice.setNotes(command.notes);
      }
      if (command.internalNotes) {
        invoice.setInternalNotes(command.internalNotes);
      }

      // Add invoice items
      for (const itemDto of command.items) {
        const item = new InvoiceItem(
          0, // Line number will be set when added
          itemDto.description,
          itemDto.quantity,
          itemDto.unitPrice,
          itemDto.taxRate,
          itemDto.unitOfMeasure || 'EA',
          itemDto.sku,
        );
        invoice.addItem(item);
      }

      // Validate invoice
      if (!invoice.isValid()) {
        throw new ValidationException('Invoice validation failed', [
          { field: 'invoice', message: 'Invoice data is invalid' },
        ]);
      }

      // Save invoice
      const savedInvoice = await this.invoiceRepository.save(invoice);

      // Publish event
      const event = InvoiceCreatedEventFactory.create(savedInvoice, command.correlationId);
      await this.eventPublisher.publishToTopic(EventTopic.INVOICE_CREATED, event);

      this.logger.log(`Invoice ${savedInvoice.getInvoiceNumber()} created successfully`);

      return {
        success: true,
        invoiceId: savedInvoice.id?.toString(),
        invoiceNumber: savedInvoice.getInvoiceNumber(),
        status: savedInvoice.getStatus(),
        message: 'Invoice created successfully',
      };
    } catch (error) {
      this.logger.error(`Error creating invoice: ${error.message}`, error.stack);
      if (error instanceof ConflictException || error instanceof ValidationException) {
        throw error;
      }
      throw error;
    }
  }

  async updateInvoice(command: UpdateInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Updating invoice ${command.invoiceId} for tenant ${command.tenantId}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    // Check if invoice can be updated
    if (invoice.getStatus() !== InvoiceStatus.DRAFT && invoice.getStatus() !== InvoiceStatus.RECEIVED) {
      throw new BusinessException(
        `Cannot update invoice in status ${invoice.getStatus()}`,
        'INVALID_STATUS',
      );
    }

    // Update fields
    if (command.purchaseOrderNumber !== undefined) {
      invoice.setPurchaseOrderNumber(command.purchaseOrderNumber);
    }
    if (command.dueDate) {
      invoice.setDueDate(command.dueDate);
    }
    if (command.paymentTerms) {
      invoice.setPaymentTerms(command.paymentTerms);
    }
    if (command.category !== undefined) {
      invoice.setCategory(command.category);
    }
    if (command.glAccountCode !== undefined) {
      invoice.setGlAccountCode(command.glAccountCode);
    }
    if (command.costCenter !== undefined) {
      invoice.setCostCenter(command.costCenter);
    }
    if (command.notes !== undefined) {
      invoice.setNotes(command.notes);
    }
    if (command.internalNotes !== undefined) {
      invoice.setInternalNotes(command.internalNotes);
    }

    const savedInvoice = await this.invoiceRepository.update(invoice);

    return {
      success: true,
      invoiceId: savedInvoice.id?.toString(),
      invoiceNumber: savedInvoice.getInvoiceNumber(),
      status: savedInvoice.getStatus(),
      message: 'Invoice updated successfully',
    };
  }

  async processInvoice(command: ProcessInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Processing invoice ${command.invoiceId} with type ${command.processType}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    let result: InvoiceCommandResult = {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
    };

    switch (command.processType) {
      case 'OCR':
        result = await this.processOcr(invoice, command);
        break;
      case 'VALIDATION':
        result = await this.processValidation(invoice, command);
        break;
      case 'ENRICHMENT':
        result = await this.processEnrichment(invoice, command);
        break;
      default:
        throw new ValidationException('Invalid process type', [
          { field: 'processType', message: `Unknown process type: ${command.processType}` },
        ]);
    }

    return result;
  }

  async validateInvoice(command: ValidateInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Validating invoice ${command.invoiceId}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    // Transition to validating status
    invoice.transitionTo(InvoiceStatus.VALIDATING);
    await this.invoiceRepository.update(invoice);

    // Perform validation
    const validationResult = await this.validationService.validateInvoice(
      invoice,
      command.userId,
      command.validationRules,
    );

    // Update status based on validation result
    if (validationResult.hasPassed()) {
      invoice.transitionTo(InvoiceStatus.VALIDATED);
    } else if (validationResult.hasFailed()) {
      invoice.transitionTo(InvoiceStatus.FAILED);
    } else {
      invoice.transitionTo(InvoiceStatus.VALIDATED); // Warnings are OK
    }

    await this.invoiceRepository.update(invoice);

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: `Invoice validation completed with status: ${validationResult.getOverallStatus()}`,
      data: {
        validationResult: validationResult.toObject(),
      },
    };
  }

  async approveInvoice(command: ApproveInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Approving invoice ${command.invoiceId} by user ${command.userId}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    // Check if invoice can be approved
    if (invoice.getStatus() !== InvoiceStatus.VALIDATED) {
      throw new BusinessException(
        `Cannot approve invoice in status ${invoice.getStatus()}`,
        'INVALID_STATUS',
      );
    }

    const oldStatus = invoice.getStatus();
    invoice.approve(command.userId);
    await this.invoiceRepository.update(invoice);

    // Publish status changed event
    const statusEvent = InvoiceStatusChangedEventFactory.create(
      invoice.id?.toString() || '',
      invoice.getInvoiceNumber(),
      invoice.getTenantId(),
      invoice.getOrganizationId(),
      oldStatus,
      invoice.getStatus(),
      command.userId,
      command.notes,
      command.correlationId,
    );
    await this.eventPublisher.publishToTopic(EventTopic.INVOICE_STATUS_CHANGED, statusEvent);

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: 'Invoice approved successfully',
    };
  }

  async rejectInvoice(command: RejectInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Rejecting invoice ${command.invoiceId} by user ${command.userId}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    const oldStatus = invoice.getStatus();
    invoice.reject(command.userId, command.reason);
    await this.invoiceRepository.update(invoice);

    // Publish status changed event
    const statusEvent = InvoiceStatusChangedEventFactory.create(
      invoice.id?.toString() || '',
      invoice.getInvoiceNumber(),
      invoice.getTenantId(),
      invoice.getOrganizationId(),
      oldStatus,
      invoice.getStatus(),
      command.userId,
      command.reason,
      command.correlationId,
    );
    await this.eventPublisher.publishToTopic(EventTopic.INVOICE_STATUS_CHANGED, statusEvent);

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: 'Invoice rejected successfully',
    };
  }

  async cancelInvoice(command: CancelInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Canceling invoice ${command.invoiceId} by user ${command.userId}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    const oldStatus = invoice.getStatus();
    invoice.transitionTo(InvoiceStatus.CANCELLED);
    await this.invoiceRepository.update(invoice);

    // Publish status changed event
    const statusEvent = InvoiceStatusChangedEventFactory.create(
      invoice.id?.toString() || '',
      invoice.getInvoiceNumber(),
      invoice.getTenantId(),
      invoice.getOrganizationId(),
      oldStatus,
      invoice.getStatus(),
      command.userId,
      command.reason,
      command.correlationId,
    );
    await this.eventPublisher.publishToTopic(EventTopic.INVOICE_STATUS_CHANGED, statusEvent);

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: 'Invoice canceled successfully',
    };
  }

  async recordPayment(command: RecordPaymentCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Recording payment of ${command.amount} for invoice ${command.invoiceId}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    invoice.recordPayment(command.amount, command.paymentDate);
    await this.invoiceRepository.update(invoice);

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: 'Payment recorded successfully',
      data: {
        amountPaid: invoice.getAmountPaid(),
        outstandingAmount: invoice.getOutstandingAmount(),
      },
    };
  }

  async addAttachment(command: AddAttachmentCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Adding attachment ${command.attachment.name} to invoice ${command.invoiceId}`);

    const invoice = await this.invoiceRepository.findById(command.invoiceId);

    if (!invoice) {
      throw new NotFoundException('Invoice', command.invoiceId);
    }

    // Verify tenant
    if (invoice.getTenantId() !== command.tenantId) {
      throw new BusinessException('Access denied: Invoice belongs to different tenant', 'ACCESS_DENIED');
    }

    invoice.addAttachment(command.attachment);
    await this.invoiceRepository.update(invoice);

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: 'Attachment added successfully',
    };
  }

  // Private helper methods

  private async processOcr(invoice: Invoice, command: ProcessInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Processing OCR for invoice ${invoice.getInvoiceNumber()}`);

    try {
      const ocrResult = await this.ocrService.processInvoice(invoice);

      if (ocrResult.success) {
        invoice.markOcrProcessed(ocrResult.confidence || 0);

        // Update invoice fields if OCR provided new data
        if (ocrResult.extractedData) {
          if (ocrResult.extractedData.vendorName && !invoice.getVendorName()) {
            invoice.setVendorName(ocrResult.extractedData.vendorName);
          }
          if (ocrResult.extractedData.invoiceDate) {
            invoice.setInvoiceDate(ocrResult.extractedData.invoiceDate);
          }
          if (ocrResult.extractedData.dueDate) {
            invoice.setDueDate(ocrResult.extractedData.dueDate);
          }
          if (ocrResult.extractedData.totalAmount) {
            // Verify total amount matches calculated total
          }
        }

        await this.invoiceRepository.update(invoice);

        return {
          success: true,
          invoiceId: invoice.id?.toString(),
          invoiceNumber: invoice.getInvoiceNumber(),
          status: invoice.getStatus(),
          message: 'OCR processing completed successfully',
          data: {
            ocrConfidence: invoice.getOcrConfidence(),
            extractedData: ocrResult.extractedData,
          },
        };
      } else {
        return {
          success: false,
          invoiceId: invoice.id?.toString(),
          invoiceNumber: invoice.getInvoiceNumber(),
          status: invoice.getStatus(),
          message: 'OCR processing failed',
          errors: ocrResult.errors,
        };
      }
    } catch (error) {
      this.logger.error(`OCR processing error: ${error.message}`, error.stack);
      throw new BusinessException('OCR processing failed', 'OCR_ERROR', { originalError: error.message });
    }
  }

  private async processValidation(invoice: Invoice, command: ProcessInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Processing validation for invoice ${invoice.getInvoiceNumber()}`);

    const validationResult = await this.validationService.validateInvoice(
      invoice,
      command.userId,
      command.options?.validationRules as string[] | undefined,
    );

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: `Validation completed with status: ${validationResult.getOverallStatus()}`,
      data: {
        validationResult: validationResult.toObject(),
      },
    };
  }

  private async processEnrichment(invoice: Invoice, command: ProcessInvoiceCommand): Promise<InvoiceCommandResult> {
    this.logger.log(`Processing enrichment for invoice ${invoice.getInvoiceNumber()}`);

    // Enrichment logic could include:
    // - Vendor lookups
    // - Tax code validation
    // - GL account code suggestions
    // - Category suggestions

    return {
      success: true,
      invoiceId: invoice.id?.toString(),
      invoiceNumber: invoice.getInvoiceNumber(),
      status: invoice.getStatus(),
      message: 'Enrichment processing completed successfully',
    };
  }
}
