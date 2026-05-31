import { Injectable, Logger } from '@nestjs/common';
import { Invoice } from '../../domain/models/invoice.entity';

/**
 * OCR processing result
 */
export interface OcrResult {
  success: boolean;
  confidence?: number;
  extractedData?: ExtractedInvoiceData;
  errors?: Array<{ field: string; message: string }>;
}

/**
 * Extracted invoice data from OCR
 */
export interface ExtractedInvoiceData {
  invoiceNumber?: string;
  vendorName?: string;
  vendorTaxId?: string;
  invoiceDate?: Date;
  dueDate?: Date;
  purchaseOrderNumber?: string;
  totalAmount?: number;
  taxAmount?: number;
  currency?: string;
  lineItems?: Array<{
    description: string;
    quantity: number;
    unitPrice: number;
    taxRate: number;
  }>;
}

/**
 * Stub service for OCR processing
 * In a real implementation, this would integrate with an OCR service
 * like Google Cloud Vision, Azure Form Recognizer, AWS Textract, or similar
 */
@Injectable()
export class OcrService {
  private readonly logger = new Logger(OcrService.name);

  /**
   * Process an invoice using OCR
   * This is a stub implementation that simulates OCR processing
   */
  async processInvoice(invoice: Invoice): Promise<OcrResult> {
    this.logger.log(`Processing OCR for invoice ${invoice.getInvoiceNumber()}`);

    // Check if invoice has attachments to process
    const attachments = invoice.getAttachments();
    const imageAttachments = attachments.filter(a =>
      a.type.startsWith('image/') || a.type === 'application/pdf',
    );

    if (imageAttachments.length === 0) {
      return {
        success: false,
        errors: [
          { field: 'attachments', message: 'No image or PDF attachments found for OCR processing' },
        ],
      };
    }

    try {
      // Simulate OCR processing delay
      await this.simulateProcessingDelay();

      // In a real implementation, you would:
      // 1. Download the attachment from the URL
      // 2. Send it to the OCR service
      // 3. Parse the response
      // 4. Extract structured data

      // For now, return a stub result with simulated data
      return {
        success: true,
        confidence: 0.92,
        extractedData: this.generateStubExtractedData(invoice),
      };
    } catch (error) {
      this.logger.error(`OCR processing error: ${error.message}`, error.stack);
      return {
        success: false,
        errors: [
          { field: 'ocr', message: `OCR processing failed: ${error.message}` },
        ],
      };
    }
  }

  /**
   * Process an invoice from a URL
   */
  async processFromUrl(
    invoiceId: string,
    imageUrl: string,
    mimeType: string,
  ): Promise<OcrResult> {
    this.logger.log(`Processing OCR from URL for invoice ${invoiceId}: ${imageUrl}`);

    if (!mimeType.startsWith('image/') && mimeType !== 'application/pdf') {
      return {
        success: false,
        errors: [
          { field: 'imageUrl', message: `Unsupported MIME type: ${mimeType}` },
        ],
      };
    }

    try {
      await this.simulateProcessingDelay();

      return {
        success: true,
        confidence: 0.88,
        extractedData: {
          invoiceNumber: 'INV-' + Math.floor(Math.random() * 10000),
          vendorName: 'Extracted Vendor Name',
          invoiceDate: new Date(),
          dueDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000),
          totalAmount: Math.floor(Math.random() * 10000) + 100,
          currency: 'USD',
        },
      };
    } catch (error) {
      return {
        success: false,
        errors: [
          { field: 'ocr', message: `OCR processing failed: ${error.message}` },
        ],
      };
    }
  }

  /**
   * Process an invoice from a base64 encoded image
   */
  async processFromBase64(
    invoiceId: string,
    base64Data: string,
    mimeType: string,
  ): Promise<OcrResult> {
    this.logger.log(`Processing OCR from base64 for invoice ${invoiceId}`);

    if (!mimeType.startsWith('image/') && mimeType !== 'application/pdf') {
      return {
        success: false,
        errors: [
          { field: 'base64Data', message: `Unsupported MIME type: ${mimeType}` },
        ],
      };
    }

    try {
      await this.simulateProcessingDelay();

      return {
        success: true,
        confidence: 0.90,
        extractedData: {
          invoiceNumber: 'INV-' + Math.floor(Math.random() * 10000),
          vendorName: 'Extracted Vendor Name',
          invoiceDate: new Date(),
          dueDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000),
          totalAmount: Math.floor(Math.random() * 10000) + 100,
          currency: 'USD',
        },
      };
    } catch (error) {
      return {
        success: false,
        errors: [
          { field: 'ocr', message: `OCR processing failed: ${error.message}` },
        ],
      };
    }
  }

  /**
   * Validate OCR results against existing invoice data
   */
  validateOcrResults(
    invoice: Invoice,
    extractedData: ExtractedInvoiceData,
  ): Array<{ field: string; message: string; severity: 'ERROR' | 'WARNING' | 'INFO' }> {
    const issues: Array<{ field: string; message: string; severity: 'ERROR' | 'WARNING' | 'INFO' }> = [];

    // Check invoice number if both exist
    if (extractedData.invoiceNumber && invoice.getInvoiceNumber()) {
      if (extractedData.invoiceNumber !== invoice.getInvoiceNumber()) {
        issues.push({
          field: 'invoiceNumber',
          message: `OCR invoice number (${extractedData.invoiceNumber}) differs from entered (${invoice.getInvoiceNumber()})`,
          severity: 'WARNING',
        });
      }
    }

    // Check vendor name similarity
    if (extractedData.vendorName && invoice.getVendorName()) {
      const similarity = this.calculateSimilarity(
        extractedData.vendorName.toLowerCase(),
        invoice.getVendorName().toLowerCase(),
      );
      if (similarity < 0.7) {
        issues.push({
          field: 'vendorName',
          message: `OCR vendor name (${extractedData.vendorName}) differs significantly from entered (${invoice.getVendorName()})`,
          severity: 'WARNING',
        });
      }
    }

    // Check total amount
    if (extractedData.totalAmount && invoice.getTotalAmount() > 0) {
      const difference = Math.abs(extractedData.totalAmount - invoice.getTotalAmount());
      const percentageDiff = (difference / invoice.getTotalAmount()) * 100;

      if (percentageDiff > 5) {
        issues.push({
          field: 'totalAmount',
          message: `OCR total amount (${extractedData.totalAmount}) differs by ${percentageDiff.toFixed(1)}% from entered (${invoice.getTotalAmount()})`,
          severity: 'ERROR',
        });
      }
    }

    return issues;
  }

  private simulateProcessingDelay(): Promise<void> {
    // Simulate network/processing delay
    return new Promise(resolve => {
      setTimeout(resolve, 500 + Math.random() * 1000);
    });
  }

  private generateStubExtractedData(invoice: Invoice): ExtractedInvoiceData {
    // Generate stub data that's similar to the actual invoice
    return {
      invoiceNumber: invoice.getInvoiceNumber(),
      vendorName: invoice.getVendorName(),
      vendorTaxId: invoice.getVendorTaxId(),
      invoiceDate: invoice.getInvoiceDate(),
      dueDate: invoice.getDueDate(),
      purchaseOrderNumber: invoice.getPurchaseOrderNumber(),
      totalAmount: invoice.getTotalAmount(),
      taxAmount: invoice.getTaxAmount(),
      currency: invoice.getCurrency(),
      lineItems: invoice.getItems().map(item => ({
        description: item.getDescription(),
        quantity: item.getQuantity(),
        unitPrice: item.getUnitPrice(),
        taxRate: item.getTaxRate(),
      })),
    };
  }

  /**
   * Calculate string similarity using simple Levenshtein distance
   */
  private calculateSimilarity(str1: string, str2: string): number {
    const longer = str1.length > str2.length ? str1 : str2;
    const shorter = str1.length > str2.length ? str2 : str1;

    if (longer.length === 0) {
      return 1.0;
    }

    const editDistance = this.levenshteinDistance(longer, shorter);
    return (longer.length - editDistance) / longer.length;
  }

  private levenshteinDistance(str1: string, str2: string): number {
    const matrix = [];

    for (let i = 0; i <= str2.length; i++) {
      matrix[i] = [i];
    }

    for (let j = 0; j <= str1.length; j++) {
      matrix[0][j] = j;
    }

    for (let i = 1; i <= str2.length; i++) {
      for (let j = 1; j <= str1.length; j++) {
        if (str2.charAt(i - 1) === str1.charAt(j - 1)) {
          matrix[i][j] = matrix[i - 1][j - 1];
        } else {
          matrix[i][j] = Math.min(
            matrix[i - 1][j - 1] + 1, // substitution
            matrix[i][j - 1] + 1,     // insertion
            matrix[i - 1][j] + 1,     // deletion
          );
        }
      }
    }

    return matrix[str2.length][str1.length];
  }
}
