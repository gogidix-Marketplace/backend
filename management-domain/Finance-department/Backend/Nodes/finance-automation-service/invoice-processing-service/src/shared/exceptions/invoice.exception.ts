import { HttpStatus } from '@nestjs/common';
import { BaseException } from './base.exception';

/**
 * Exception thrown when invoice-related operations fail
 */
export class InvoiceException extends BaseException {
  constructor(message: string, errorCode: string = 'INVOICE_ERROR', context?: Record<string, unknown>) {
    super(
      message,
      HttpStatus.BAD_REQUEST,
      errorCode,
      context,
    );
  }
}

export class InvoiceNotFoundException extends BaseException {
  constructor(invoiceId: string, tenantId?: string) {
    super(
      `Invoice with ID '${invoiceId}' not found`,
      HttpStatus.NOT_FOUND,
      'INVOICE_NOT_FOUND',
      { invoiceId, tenantId },
    );
  }
}

export class DuplicateInvoiceException extends BaseException {
  constructor(invoiceNumber: string, vendorId?: string) {
    super(
      `Duplicate invoice detected. Invoice number '${invoiceNumber}' already exists`,
      HttpStatus.CONFLICT,
      'DUPLICATE_INVOICE',
      { invoiceNumber, vendorId },
    );
  }
}

export class InvoiceValidationException extends BaseException {
  constructor(message: string, violations: Array<{ field: string; message: string; value?: any }>) {
    super(
      message,
      HttpStatus.BAD_REQUEST,
      'INVOICE_VALIDATION_FAILED',
      { violations },
    );
  }
}

export class InvoiceProcessingException extends BaseException {
  constructor(message: string, invoiceId: string, context?: Record<string, unknown>) {
    super(
      message,
      HttpStatus.UNPROCESSABLE_ENTITY,
      'INVOICE_PROCESSING_FAILED',
      { invoiceId, ...context },
    );
  }
}

export class OcrProcessingException extends BaseException {
  constructor(message: string, fileName: string, context?: Record<string, unknown>) {
    super(
      `OCR processing failed for file '${fileName}': ${message}`,
      HttpStatus.UNPROCESSABLE_ENTITY,
      'OCR_PROCESSING_FAILED',
      { fileName, ...context },
    );
  }
}

export class InvalidInvoiceStatusException extends BaseException {
  constructor(invoiceId: string, currentStatus: string, expectedStatus: string[]) {
    super(
      `Invalid invoice status transition. Current: ${currentStatus}, Expected: ${expectedStatus.join(', ')}`,
      HttpStatus.BAD_REQUEST,
      'INVALID_INVOICE_STATUS',
      { invoiceId, currentStatus, expectedStatus },
    );
  }
}
