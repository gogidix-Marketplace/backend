import { OcrResult, ExtractedField } from '../../models/ocr-result.entity';
import { OcrEngineType } from '../../enums';

/**
 * OCR processing request
 */
export interface OcrRequest {
  fileId: string;
  fileName: string;
  fileType: string;
  fileSize: number;
  fileUrl?: string;
  fileBuffer?: Buffer;
  invoiceId?: string;
  options?: OcrOptions;
}

/**
 * OCR processing options
 */
export interface OcrOptions {
  language?: string;
  pageCount?: number;
  extractFields?: string[];
  preprocessImage?: boolean;
  confidence?: number;
}

/**
 * OCR processing result
 */
export interface OcrProcessingResult {
  success: boolean;
  ocrResult: OcrResult;
  extractedFields: ExtractedField[];
  rawText?: string;
  confidence: number;
  processingDuration: number;
  errorMessage?: string;
}

/**
 * Output port for OCR service
 * Abstracts the OCR processing implementation
 */
export interface IOcrService {
  /**
   * Process a document with OCR
   */
  processDocument(request: OcrRequest): Promise<OcrProcessingResult>;

  /**
   * Process multiple documents in batch
   */
  processBatch(requests: OcrRequest[]): Promise<OcrProcessingResult[]>;

  /**
   * Extract specific fields from a document
   */
  extractFields(request: OcrRequest, fields: string[]): Promise<Map<string, ExtractedField>>;

  /**
   * Get raw text from a document
   */
  getRawText(request: OcrRequest): Promise<string>;

  /**
   * Check if OCR service is available
   */
  isAvailable(): boolean;

  /**
   * Get supported file types
   */
  getSupportedFileTypes(): string[];

  /**
   * Get the OCR engine type
   */
  getEngineType(): OcrEngineType;
}
