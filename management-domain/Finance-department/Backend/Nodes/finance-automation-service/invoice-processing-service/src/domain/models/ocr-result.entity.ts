import { BaseEntity } from '../../shared/base/base.entity';
import { OcrStatus, OcrEngineType } from '../enums';

/**
 * Extracted field from OCR processing with confidence score
 */
export interface ExtractedField {
  name: string;
  value: string;
  confidence: number;
  boundingBox?: {
    page: number;
    x: number;
    y: number;
    width: number;
    height: number;
  };
}

/**
 * OCR Result entity
 * Stores the results of OCR processing on invoice documents
 */
export class OcrResult extends BaseEntity {
  private invoiceId?: string;
  private fileId: string;
  private fileName: string;
  private fileType: string;
  private fileSize: number;
  private ocrEngine: OcrEngineType;
  private status: OcrStatus;
  private extractedFields: Map<string, ExtractedField>;
  private rawText?: string;
  private processedAt?: Date;
  private processingDuration?: number; // in milliseconds
  private confidence: number;
  private errorMessage?: string;
  private pageCount: number;
  private language: string;

  constructor(fileId: string, fileName: string, fileType: string, fileSize: number, ocrEngine: OcrEngineType) {
    super();
    this.fileId = fileId;
    this.fileName = fileName;
    this.fileType = fileType;
    this.fileSize = fileSize;
    this.ocrEngine = ocrEngine;
    this.status = OcrStatus.PENDING;
    this.extractedFields = new Map();
    this.confidence = 0;
    this.pageCount = 0;
    this.language = 'eng';
  }

  markAsProcessing(): void {
    this.status = OcrStatus.PROCESSING;
    this.markAsUpdated();
  }

  markAsCompleted(rawText?: string, processingDuration?: number): void {
    this.status = OcrStatus.COMPLETED;
    this.rawText = rawText;
    this.processedAt = new Date();
    this.processingDuration = processingDuration;
    this.calculateOverallConfidence();
    this.markAsUpdated();
  }

  markAsFailed(errorMessage: string): void {
    this.status = OcrStatus.FAILED;
    this.errorMessage = errorMessage;
    this.processedAt = new Date();
    this.markAsUpdated();
  }

  markAsPartial(rawText?: string): void {
    this.status = OcrStatus.PARTIAL;
    this.rawText = rawText;
    this.processedAt = new Date();
    this.calculateOverallConfidence();
    this.markAsUpdated();
  }

  addExtractedField(field: ExtractedField): void {
    this.extractedFields.set(field.name.toLowerCase(), field);
    this.calculateOverallConfidence();
    this.markAsUpdated();
  }

  addExtractedFields(fields: ExtractedField[]): void {
    fields.forEach(field => {
      this.extractedFields.set(field.name.toLowerCase(), field);
    });
    this.calculateOverallConfidence();
    this.markAsUpdated();
  }

  getExtractedField(fieldName: string): ExtractedField | undefined {
    return this.extractedFields.get(fieldName.toLowerCase());
  }

  hasField(fieldName: string): boolean {
    return this.extractedFields.has(fieldName.toLowerCase());
  }

  private calculateOverallConfidence(): void {
    if (this.extractedFields.size === 0) {
      this.confidence = 0;
      return;
    }

    const totalConfidence = Array.from(this.extractedFields.values())
      .reduce((sum, field) => sum + field.confidence, 0);

    this.confidence = totalConfidence / this.extractedFields.size;
  }

  // Getters
  getInvoiceId(): string | undefined {
    return this.invoiceId;
  }

  getFileId(): string {
    return this.fileId;
  }

  getFileName(): string {
    return this.fileName;
  }

  getFileType(): string {
    return this.fileType;
  }

  getFileSize(): number {
    return this.fileSize;
  }

  getOcrEngine(): OcrEngineType {
    return this.ocrEngine;
  }

  getStatus(): OcrStatus {
    return this.status;
  }

  getExtractedFields(): ExtractedField[] {
    return Array.from(this.extractedFields.values());
  }

  getRawText(): string | undefined {
    return this.rawText;
  }

  getProcessedAt(): Date | undefined {
    return this.processedAt;
  }

  getProcessingDuration(): number | undefined {
    return this.processingDuration;
  }

  getConfidence(): number {
    return this.confidence;
  }

  getErrorMessage(): string | undefined {
    return this.errorMessage;
  }

  getPageCount(): number {
    return this.pageCount;
  }

  getLanguage(): string {
    return this.language;
  }

  // Setters
  setInvoiceId(invoiceId: string): void {
    this.invoiceId = invoiceId;
    this.markAsUpdated();
  }

  setPageCount(pageCount: number): void {
    this.pageCount = pageCount;
    this.markAsUpdated();
  }

  setLanguage(language: string): void {
    this.language = language;
    this.markAsUpdated();
  }

  // Helper methods to get common invoice fields
  getInvoiceNumber(): string | undefined {
    return this.getExtractedField('invoice_number')?.value ||
           this.getExtractedField('invoice no')?.value ||
           this.getExtractedField('invoice number')?.value;
  }

  getVendorName(): string | undefined {
    return this.getExtractedField('vendor_name')?.value ||
           this.getExtractedField('vendor')?.value ||
           this.getExtractedField('supplier')?.value ||
           this.getExtractedField('from')?.value;
  }

  getInvoiceDate(): string | undefined {
    return this.getExtractedField('invoice_date')?.value ||
           this.getExtractedField('date')?.value;
  }

  getDueDate(): string | undefined {
    return this.getExtractedField('due_date')?.value ||
           this.getExtractedField('due date')?.value;
  }

  getTotalAmount(): string | undefined {
    return this.getExtractedField('total_amount')?.value ||
           this.getExtractedField('total')?.value ||
           this.getExtractedField('amount')?.value;
  }

  getTaxAmount(): string | undefined {
    return this.getExtractedField('tax_amount')?.value ||
           this.getExtractedField('tax')?.value;
  }

  toObject(): Record<string, unknown> {
    return {
      invoiceId: this.invoiceId,
      fileId: this.fileId,
      fileName: this.fileName,
      fileType: this.fileType,
      fileSize: this.fileSize,
      ocrEngine: this.ocrEngine,
      status: this.status,
      extractedFields: this.getExtractedFields(),
      rawText: this.rawText,
      processedAt: this.processedAt,
      processingDuration: this.processingDuration,
      confidence: this.confidence,
      errorMessage: this.errorMessage,
      pageCount: this.pageCount,
      language: this.language,
      createdAt: this.createdAtDate,
      updatedAt: this.updatedAtDate,
    };
  }

  static fromObject(obj: Record<string, unknown>): OcrResult {
    const result = new OcrResult(
      obj.fileId as string,
      obj.fileName as string,
      obj.fileType as string,
      obj.fileSize as number,
      obj.ocrEngine as OcrEngineType,
    );

    if (obj.id) result.id = obj.id as any;
    if (obj.invoiceId) result.setInvoiceId(obj.invoiceId as string);
    if (obj.pageCount) result.setPageCount(obj.pageCount as number);
    if (obj.language) result.setLanguage(obj.language as string);

    return result;
  }
}
