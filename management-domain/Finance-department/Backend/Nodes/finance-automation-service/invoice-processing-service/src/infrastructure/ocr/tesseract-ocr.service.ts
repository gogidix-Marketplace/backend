import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { createWorker } from 'tesseract.js';
import { IOcrService, OcrRequest, OcrProcessingResult } from '../../domain/ports/output/ocr-service.interface';
import { OcrResult, ExtractedField } from '../../domain/models/ocr-result.entity';
import { OcrStatus, OcrEngineType } from '../../domain/enums/ocr-engine.enum';

/**
 * Tesseract OCR implementation
 * Local OCR processing using Tesseract.js
 */
@Injectable()
export class TesseractOcrService implements IOcrService, OnModuleInit {
  private readonly logger = new Logger(TesseractOcrService.name);
  private worker?: Tesseract.Worker;

  constructor(private readonly configService: ConfigService) {}

  async onModuleInit(): Promise<void> {
    this.logger.log('Initializing Tesseract OCR service');
    // Worker will be created on-demand
  }

  async processDocument(request: OcrRequest): Promise<OcrProcessingResult> {
    this.logger.log(`Processing document with Tesseract: ${request.fileName}`);
    const startTime = Date.now();

    try {
      // Create worker for this request
      const worker = await createWorker(
        request.options?.language || 'eng',
        1,
        {
          logger: (m) => this.logger.debug(m.status + ' ' + (m.progress ? (m.progress * 100).toFixed(0) + '%' : '')),
        },
      );

      // Determine if we have a URL or buffer
      let image: string;

      if (request.fileBuffer) {
        // Convert buffer to base64
        image = `data:${request.fileType};base64,${request.fileBuffer.toString('base64')}`;
      } else if (request.fileUrl) {
        image = request.fileUrl;
      } else {
        throw new Error('Either fileBuffer or fileUrl must be provided');
      }

      // Perform OCR
      const { data } = await worker.recognize(image);

      // Terminate worker
      await worker.terminate();

      const processingDuration = Date.now() - startTime;

      // Create OCR result
      const ocrResult = new OcrResult(
        request.fileId,
        request.fileName,
        request.fileType,
        request.fileSize,
        OcrEngineType.TESSERACT,
      );

      if (request.invoiceId) {
        ocrResult.setInvoiceId(request.invoiceId);
      }

      ocrResult.setPageCount(1);
      ocrResult.setLanguage(request.options?.language || 'eng');

      // Extract fields from text
      const extractedFields = this.extractInvoiceFields(data.text);

      ocrResult.addExtractedFields(extractedFields);
      ocrResult.markAsCompleted(data.text, processingDuration);

      this.logger.log(`Tesseract OCR completed in ${processingDuration}ms with confidence ${ocrResult.getConfidence()}`);

      return {
        success: true,
        ocrResult,
        extractedFields,
        rawText: data.text,
        confidence: ocrResult.getConfidence(),
        processingDuration,
      };
    } catch (error) {
      this.logger.error(`Tesseract OCR error: ${error.message}`, error.stack);

      const ocrResult = new OcrResult(
        request.fileId,
        request.fileName,
        request.fileType,
        request.fileSize,
        OcrEngineType.TESSERACT,
      );

      ocrResult.markAsFailed(error.message);

      return {
        success: false,
        ocrResult,
        extractedFields: [],
        confidence: 0,
        processingDuration: Date.now() - startTime,
        errorMessage: error.message,
      };
    }
  }

  async processBatch(requests: OcrRequest[]): Promise<OcrProcessingResult[]> {
    this.logger.log(`Processing batch of ${requests.length} documents with Tesseract`);

    // Process sequentially (Tesseract.js can have issues with parallel workers)
    const results: OcrProcessingResult[] = [];

    for (const request of requests) {
      const result = await this.processDocument(request);
      results.push(result);
    }

    return results;
  }

  async extractFields(request: OcrRequest, fields: string[]): Promise<Map<string, ExtractedField>> {
    const result = await this.processDocument(request);
    const fieldMap = new Map<string, ExtractedField>();

    for (const field of result.extractedFields) {
      if (fields.includes(field.name) || fields.some(f => field.name.toLowerCase().includes(f.toLowerCase()))) {
        fieldMap.set(field.name, field);
      }
    }

    return fieldMap;
  }

  async getRawText(request: OcrRequest): Promise<string> {
    const result = await this.processDocument(request);
    return result.rawText || '';
  }

  isAvailable(): boolean {
    return true; // Tesseract.js is always available (self-contained)
  }

  getSupportedFileTypes(): string[] {
    return ['png', 'jpg', 'jpeg', 'tiff', 'bmp', 'webp'];
  }

  getEngineType(): OcrEngineType {
    return OcrEngineType.TESSERACT;
  }

  /**
   * Extract invoice fields from raw OCR text
   * Uses pattern matching to find common invoice fields
   */
  private extractInvoiceFields(text: string): ExtractedField[] {
    const fields: ExtractedField[] = [];
    const lines = text.split('\n').map(l => l.trim()).filter(l => l.length > 0);

    // Pattern matching for invoice fields
    const patterns = {
      invoiceNumber: /(?:invoice\s*(?:no|number|#)?[:#]?\s*)([A-Za-z0-9-]+)/i,
      invoiceDate: /(?:invoice\s*date|date|bill\s*date)[:#]?\s*([A-Za-z0-9\/\-,\s]+)/i,
      dueDate: /(?:due\s*date|payment\s*due|due)[:#]?\s*([A-Za-z0-9\/\-,\s]+)/i,
      totalAmount: /(?:total|amount|balance|grand\s*total)[:#]?\s*[\$]?\s*([0-9,]+\.?\d*)/i,
      taxAmount: /(?:tax|vat|gst|sales\s*tax)[:#]?\s*[\$]?\s*([0-9,]+\.?\d*)/i,
      vendorName: /(?:from|vendor|supplier)[:#]?\s*([A-Za-z\s]+(?:Inc|LLC|Ltd|Corp)?)/i,
      poNumber: /(?:po|purchase\s*order)(?:\s*no|number|#)?[:#]?\s*([A-Za-z0-9-]+)/i,
    };

    // Search through text for patterns
    for (const line of lines) {
      // Invoice number
      const invoiceMatch = line.match(patterns.invoiceNumber);
      if (invoiceMatch && !fields.find(f => f.name === 'invoice_number')) {
        fields.push({
          name: 'invoice_number',
          value: invoiceMatch[1].trim(),
          confidence: 0.8,
        });
      }

      // Invoice date
      const dateMatch = line.match(patterns.invoiceDate);
      if (dateMatch && !fields.find(f => f.name === 'invoice_date')) {
        fields.push({
          name: 'invoice_date',
          value: dateMatch[1].trim(),
          confidence: 0.75,
        });
      }

      // Due date
      const dueMatch = line.match(patterns.dueDate);
      if (dueMatch && !fields.find(f => f.name === 'due_date')) {
        fields.push({
          name: 'due_date',
          value: dueMatch[1].trim(),
          confidence: 0.75,
        });
      }

      // Total amount
      const totalMatch = line.match(patterns.totalAmount);
      if (totalMatch && !fields.find(f => f.name === 'total_amount')) {
        fields.push({
          name: 'total_amount',
          value: totalMatch[1].trim(),
          confidence: 0.85,
        });
      }

      // Tax amount
      const taxMatch = line.match(patterns.taxAmount);
      if (taxMatch && !fields.find(f => f.name === 'tax_amount')) {
        fields.push({
          name: 'tax_amount',
          value: taxMatch[1].trim(),
          confidence: 0.8,
        });
      }

      // Vendor name
      const vendorMatch = line.match(patterns.vendorName);
      if (vendorMatch && !fields.find(f => f.name === 'vendor_name')) {
        const vendor = vendorMatch[1].trim();
        if (vendor.length > 2) {
          fields.push({
            name: 'vendor_name',
            value: vendor,
            confidence: 0.7,
          });
        }
      }

      // PO number
      const poMatch = line.match(patterns.poNumber);
      if (poMatch && !fields.find(f => f.name === 'po_number')) {
        fields.push({
          name: 'po_number',
          value: poMatch[1].trim(),
          confidence: 0.8,
        });
      }
    }

    return fields;
  }
}
