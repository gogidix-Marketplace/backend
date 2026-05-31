import {Injectable, Logger, Inject} from '@nestjs/common';
import { Invoice } from '../../domain/models/invoice.entity';
import { OcrResult, ExtractedField } from '../../domain/models/ocr-result.entity';
import { IOcrService, OcrRequest, OcrProcessingResult } from '../../domain/ports/output/ocr-service.interface';
import { IOcrResultRepository } from '../../domain/repositories/invoice-repository.interface';

/**
 * Interface for OCR extraction result
 */
export interface InvoiceOcrResult {
  success: boolean;
  confidence?: number;
  extractedData?: {
    invoiceNumber?: string;
    vendorName?: string;
    invoiceDate?: Date;
    dueDate?: Date;
    totalAmount?: number;
    taxAmount?: number;
    lineItems?: Array<{
      description: string;
      quantity: number;
      unitPrice: number;
      amount: number;
    }>;
  };
  rawText?: string;
  errors?: Array<{ field: string; message: string }>;
}

/**
 * Service for handling OCR operations on invoices
 * Coordinates OCR processing and invoice field extraction
 */
@Injectable()
export class InvoiceOcrService {
  private readonly logger = new Logger(InvoiceOcrService.name);

  constructor(
    @Inject('IOcrService')
    private readonly ocrService: IOcrService,
    @Inject('IOcrResultRepository')
    private readonly ocrResultRepository: IOcrResultRepository,
  ) {}

  /**
   * Process an invoice file with OCR
   */
  async processInvoiceFile(
    fileId: string,
    fileName: string,
    fileType: string,
    fileSize: number,
    fileUrl?: string,
    invoiceId?: string,
  ): Promise<InvoiceOcrResult> {
    this.logger.log(`Processing OCR for file: ${fileName}`);

    try {
      const request: OcrRequest = {
        fileId,
        fileName,
        fileType,
        fileSize,
        fileUrl,
        invoiceId,
        options: {
          language: 'eng',
          extractFields: [
            'invoice_number',
            'vendor_name',
            'invoice_date',
            'due_date',
            'total_amount',
            'tax_amount',
          ],
        },
      };

      const result = await this.ocrService.processDocument(request);

      if (!result.success) {
        return {
          success: false,
          errors: [{ field: 'ocr', message: result.errorMessage || 'OCR processing failed' }],
        };
      }

      // Save OCR result
      await this.ocrResultRepository.save(result.ocrResult);

      // Extract invoice fields from OCR result
      const extractedData = this.extractInvoiceFields(result.extractedFields, result.rawText);

      return {
        success: true,
        confidence: result.confidence,
        extractedData,
        rawText: result.rawText,
      };
    } catch (error) {
      this.logger.error(`Error processing invoice with OCR: ${error.message}`, error.stack);
      return {
        success: false,
        errors: [{ field: 'ocr', message: error.message }],
      };
    }
  }

  /**
   * Process OCR for an existing invoice
   */
  async processInvoice(invoice: Invoice): Promise<InvoiceOcrResult> {
    this.logger.log(`Processing OCR for invoice: ${invoice.getInvoiceNumber()}`);

    // Check if invoice has attachments
    const attachments = invoice.getAttachments();
    if (attachments.length === 0) {
      return {
        success: false,
        errors: [{ field: 'attachment', message: 'No attachments found for OCR processing' }],
      };
    }

    // Process the first attachment
    const attachment = attachments[0];
    return this.processInvoiceFile(
      attachment.url,
      attachment.name,
      attachment.type,
      attachment.size,
      attachment.url,
      invoice.id?.toString(),
    );
  }

  /**
   * Extract invoice fields from OCR results
   */
  private extractInvoiceFields(
    extractedFields: ExtractedField[],
    rawText?: string,
  ): InvoiceOcrResult['extractedData'] {
    const fieldsMap = new Map<string, ExtractedField>();
    extractedFields.forEach(field => {
      fieldsMap.set(field.name.toLowerCase(), field);
    });

    const result: InvoiceOcrResult['extractedData'] = {};

    // Extract invoice number
    const invoiceNumber = this.getFieldValue(fieldsMap, ['invoice_number', 'invoice no', 'invoice number']);
    if (invoiceNumber) {
      result.invoiceNumber = invoiceNumber;
    }

    // Extract vendor name
    const vendorName = this.getFieldValue(fieldsMap, ['vendor_name', 'vendor', 'supplier', 'from']);
    if (vendorName) {
      result.vendorName = vendorName;
    }

    // Extract dates
    const invoiceDate = this.getFieldValue(fieldsMap, ['invoice_date', 'date', 'bill date']);
    if (invoiceDate) {
      result.invoiceDate = this.parseDate(invoiceDate);
    }

    const dueDate = this.getFieldValue(fieldsMap, ['due_date', 'due', 'payment due']);
    if (dueDate) {
      result.dueDate = this.parseDate(dueDate);
    }

    // Extract amounts
    const totalAmount = this.getFieldValue(fieldsMap, ['total_amount', 'total', 'amount due', 'balance due']);
    if (totalAmount) {
      result.totalAmount = this.parseAmount(totalAmount);
    }

    const taxAmount = this.getFieldValue(fieldsMap, ['tax_amount', 'tax', 'vat', 'sales tax']);
    if (taxAmount) {
      result.taxAmount = this.parseAmount(taxAmount);
    }

    // Extract line items (would need more sophisticated parsing)
    result.lineItems = this.extractLineItems(rawText);

    return result;
  }

  /**
   * Get field value from map with multiple possible names
   */
  private getFieldValue(fieldsMap: Map<string, ExtractedField>, possibleNames: string[]): string | undefined {
    for (const name of possibleNames) {
      const field = fieldsMap.get(name);
      if (field && field.confidence > 0.7) {
        return field.value;
      }
    }
    return undefined;
  }

  /**
   * Parse date from string
   */
  private parseDate(dateString: string): Date | undefined {
    try {
      // Try common date formats
      const date = new Date(dateString);
      if (!isNaN(date.getTime())) {
        return date;
      }
    } catch {
      // Invalid date
    }
    return undefined;
  }

  /**
   * Parse amount from string
   */
  private parseAmount(amountString: string): number | undefined {
    try {
      // Remove currency symbols and commas, then parse
      const cleaned = amountString.replace(/[^0-9.-]/g, '');
      const amount = parseFloat(cleaned);
      if (!isNaN(amount)) {
        return amount;
      }
    } catch {
      // Invalid amount
    }
    return undefined;
  }

  /**
   * Extract line items from raw text
   * This is a simplified implementation - real OCR would need more sophisticated parsing
   */
  private extractLineItems(rawText?: string): Array<{
    description: string;
    quantity: number;
    unitPrice: number;
    amount: number;
  }> {
    if (!rawText) {
      return [];
    }

    const items: Array<{
      description: string;
      quantity: number;
      unitPrice: number;
      amount: number;
    }> = [];

    // Simple pattern matching for line items
    // In production, this would use more sophisticated NLP or ML
    const lines = rawText.split('\n');
    let inItemsSection = false;

    for (const line of lines) {
      const trimmedLine = line.trim();

      // Detect start of items section
      if (trimmedLine.toLowerCase().includes('description') ||
          trimmedLine.toLowerCase().includes('item') ||
          trimmedLine.toLowerCase().includes('qty')) {
        inItemsSection = true;
        continue;
      }

      // Detect end of items section
      if (inItemsSection && (trimmedLine.toLowerCase().includes('subtotal') ||
                             trimmedLine.toLowerCase().includes('total'))) {
        break;
      }

      if (inItemsSection && trimmedLine.length > 10) {
        // Try to parse as line item
        const parts = trimmedLine.split(/\s{2,}|\t/);
        if (parts.length >= 3) {
          const quantity = parseFloat(parts[parts.length - 3]);
          const unitPrice = parseFloat(parts[parts.length - 2]);
          const amount = parseFloat(parts[parts.length - 1]);

          if (!isNaN(quantity) && !isNaN(unitPrice) && !isNaN(amount)) {
            items.push({
              description: parts.slice(0, parts.length - 3).join(' ').trim(),
              quantity,
              unitPrice,
              amount,
            });
          }
        }
      }
    }

    return items;
  }

  /**
   * Get OCR result by invoice ID
   */
  async getOcrResultByInvoiceId(invoiceId: string): Promise<OcrResult | null> {
    return this.ocrResultRepository.findByInvoiceId(invoiceId);
  }

  /**
   * Check if OCR service is available
   */
  isOcrAvailable(): boolean {
    return this.ocrService.isAvailable();
  }

  /**
   * Get supported file types
   */
  getSupportedFileTypes(): string[] {
    return this.ocrService.getSupportedFileTypes();
  }
}
