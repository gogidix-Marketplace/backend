import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { ImageAnnotatorClient } from '@google-cloud/vision';
import { IOcrService, OcrRequest, OcrProcessingResult } from '../../domain/ports/output/ocr-service.interface';
import { OcrResult, ExtractedField } from '../../domain/models/ocr-result.entity';
import { OcrEngineType } from '../../domain/enums/ocr-engine.enum';

/**
 * Google Cloud Vision OCR implementation
 * Cloud-based OCR with high accuracy
 */
@Injectable()
export class GoogleVisionOcrService implements IOcrService, OnModuleInit {
  private readonly logger = new Logger(GoogleVisionOcrService.name);
  private client?: ImageAnnotatorClient;

  constructor(private readonly configService: ConfigService) {}

  async onModuleInit(): Promise<void> {
    try {
      const keyFilename = this.configService.get<string>('GOOGLE_APPLICATION_CREDENTIALS');

      if (keyFilename) {
        this.client = new ImageAnnotatorClient({ keyFilename });
      } else {
        // Use default credentials (ADC)
        this.client = new ImageAnnotatorClient();
      }

      this.logger.log('Google Cloud Vision OCR service initialized');
    } catch (error) {
      this.logger.error(`Failed to initialize Google Vision: ${error.message}`);
      // Don't throw - service might not be configured
    }
  }

  async processDocument(request: OcrRequest): Promise<OcrProcessingResult> {
    this.logger.log(`Processing document with Google Vision: ${request.fileName}`);
    const startTime = Date.now();

    if (!this.client) {
      throw new Error('Google Vision client not initialized. Please check credentials.');
    }

    try {
      let imageSource;

      if (request.fileBuffer) {
        imageSource = { content: request.fileBuffer };
      } else if (request.fileUrl) {
        imageSource = { source: { imageUri: request.fileUrl } };
      } else {
        throw new Error('Either fileBuffer or fileUrl must be provided');
      }

      // Perform text detection
      const [result] = await this.client.documentTextDetection(imageSource);

      const fullTextAnnotation = result.fullTextAnnotation;
      const rawText = fullTextAnnotation?.text || '';

      // Extract pages and blocks info
      const pages = fullTextAnnotation?.pages || [];
      const pageCount = pages.length;

      // Extract fields with bounding boxes
      const extractedFields: ExtractedField[] = [];

      if (fullTextAnnotation?.pages) {
        for (const page of fullTextAnnotation.pages) {
          for (const block of page.blocks || []) {
            for (const paragraph of block.paragraphs || []) {
              for (const word of paragraph.words || []) {
                const text = word.symbols?.map(s => s.text).join('') || '';
                if (text) {
                  const boundingBox = block.boundingBox;
                  if (boundingBox) {
                    extractedFields.push({
                      name: 'text',
                      value: text,
                      confidence: block.confidence || 0,
                      boundingBox: {
                        page: 0,
                        x: boundingBox.vertices?.[0]?.x || 0,
                        y: boundingBox.vertices?.[0]?.y || 0,
                        width: (boundingBox.vertices?.[1]?.x || 0) - (boundingBox.vertices?.[0]?.x || 0),
                        height: (boundingBox.vertices?.[2]?.y || 0) - (boundingBox.vertices?.[0]?.y || 0),
                      },
                    });
                  }
                }
              }
            }
          }
        }
      }

      const processingDuration = Date.now() - startTime;

      // Create OCR result
      const ocrResult = new OcrResult(
        request.fileId,
        request.fileName,
        request.fileType,
        request.fileSize,
        OcrEngineType.GOOGLE_VISION,
      );

      if (request.invoiceId) {
        ocrResult.setInvoiceId(request.invoiceId);
      }

      ocrResult.setPageCount(pageCount);
      ocrResult.setLanguage(request.options?.language || 'en');

      // Extract invoice-specific fields
      const invoiceFields = this.extractInvoiceFields(rawText);
      ocrResult.addExtractedFields(invoiceFields);
      ocrResult.markAsCompleted(rawText, processingDuration);

      this.logger.log(`Google Vision OCR completed in ${processingDuration}ms with confidence ${ocrResult.getConfidence()}`);

      return {
        success: true,
        ocrResult,
        extractedFields: [...invoiceFields, ...extractedFields],
        rawText,
        confidence: ocrResult.getConfidence(),
        processingDuration,
      };
    } catch (error) {
      this.logger.error(`Google Vision OCR error: ${error.message}`, error.stack);

      const ocrResult = new OcrResult(
        request.fileId,
        request.fileName,
        request.fileType,
        request.fileSize,
        OcrEngineType.GOOGLE_VISION,
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
    this.logger.log(`Processing batch of ${requests.length} documents with Google Vision`);

    // Google Vision has rate limits, so process sequentially with delays
    const results: OcrProcessingResult[] = [];

    for (let i = 0; i < requests.length; i++) {
      const result = await this.processDocument(requests[i]);
      results.push(result);

      // Add delay between requests to avoid rate limiting
      if (i < requests.length - 1) {
        await new Promise(resolve => setTimeout(resolve, 100));
      }
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
    return this.client !== undefined;
  }

  getSupportedFileTypes(): string[] {
    return ['png', 'jpg', 'jpeg', 'tiff', 'bmp', 'gif', 'webp', 'pdf'];
  }

  getEngineType(): OcrEngineType {
    return OcrEngineType.GOOGLE_VISION;
  }

  /**
   * Extract invoice fields from raw OCR text
   * Uses pattern matching similar to Tesseract but with Google-specific enhancements
   */
  private extractInvoiceFields(text: string): ExtractedField[] {
    const fields: ExtractedField[] = [];
    const lines = text.split('\n').map(l => l.trim()).filter(l => l.length > 0);

    // Pattern matching for invoice fields
    const patterns = {
      invoiceNumber: /(?:invoice\s*(?:no|number|#)?[:#]?\s*)([A-Za-z0-9\-\/]+)/i,
      invoiceDate: /(?:invoice\s*date|date|bill\s*date)[:#]?\s*([A-Za-z0-9\/\-,\s]+)/i,
      dueDate: /(?:due\s*date|payment\s*due|due)[:#]?\s*([A-Za-z0-9\/\-,\s]+)/i,
      totalAmount: /(?:total|amount|balance|grand\s*total|amount\s*due)[:#]?\s*[\$€£]?\s*([0-9,]+\.?\d*)/i,
      taxAmount: /(?:tax|vat|gst|sales\s*tax|hst)[:#]?\s*[\$€£]?\s*([0-9,]+\.?\d*)/i,
      vendorName: /(?:from|vendor|supplier|bill\s*from)[:#]?\s*([A-Za-z\s&\.]+(?:Inc|LLC|Ltd|Corp|Company)?)/i,
      poNumber: /(?:po|purchase\s*order)(?:\s*no|number|#)?[:#]?\s*([A-Za-z0-9\-\/]+)/i,
    };

    for (const line of lines) {
      // Invoice number
      const invoiceMatch = line.match(patterns.invoiceNumber);
      if (invoiceMatch && !fields.find(f => f.name === 'invoice_number')) {
        fields.push({
          name: 'invoice_number',
          value: invoiceMatch[1].trim(),
          confidence: 0.95, // Higher confidence for Google Vision
        });
      }

      // Invoice date
      const dateMatch = line.match(patterns.invoiceDate);
      if (dateMatch && !fields.find(f => f.name === 'invoice_date')) {
        fields.push({
          name: 'invoice_date',
          value: dateMatch[1].trim(),
          confidence: 0.9,
        });
      }

      // Due date
      const dueMatch = line.match(patterns.dueDate);
      if (dueMatch && !fields.find(f => f.name === 'due_date')) {
        fields.push({
          name: 'due_date',
          value: dueMatch[1].trim(),
          confidence: 0.9,
        });
      }

      // Total amount
      const totalMatch = line.match(patterns.totalAmount);
      if (totalMatch && !fields.find(f => f.name === 'total_amount')) {
        fields.push({
          name: 'total_amount',
          value: totalMatch[1].trim(),
          confidence: 0.95,
        });
      }

      // Tax amount
      const taxMatch = line.match(patterns.taxAmount);
      if (taxMatch && !fields.find(f => f.name === 'tax_amount')) {
        fields.push({
          name: 'tax_amount',
          value: taxMatch[1].trim(),
          confidence: 0.9,
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
            confidence: 0.85,
          });
        }
      }

      // PO number
      const poMatch = line.match(patterns.poNumber);
      if (poMatch && !fields.find(f => f.name === 'po_number')) {
        fields.push({
          name: 'po_number',
          value: poMatch[1].trim(),
          confidence: 0.9,
        });
      }
    }

    return fields;
  }
}
