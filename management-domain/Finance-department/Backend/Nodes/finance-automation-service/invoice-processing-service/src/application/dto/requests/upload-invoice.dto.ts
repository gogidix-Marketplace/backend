import { IsString, IsNotEmpty, IsOptional, IsEnum } from 'class-validator';

/**
 * DTO for uploading an invoice file for OCR processing
 */
export class UploadInvoiceDto {
  @IsString()
  @IsNotEmpty()
  vendorId!: string;

  @IsString()
  @IsNotEmpty()
  vendorName!: string;

  @IsString()
  @IsOptional()
  purchaseOrderNumber?: string;

  @IsString()
  @IsOptional()
  category?: string;

  @IsString()
  @IsOptional()
  glAccountCode?: string;

  @IsString()
  @IsOptional()
  costCenter?: string;

  @IsString()
  @IsOptional()
  notes?: string;

  @IsString()
  @IsOptional()
  internalNotes?: string;
}

/**
 * OcrEngine enumeration for upload
 */
export enum OcrEngineType {
  TESSERACT = 'tesseract',
  GOOGLE_VISION = 'google-vision',
}

/**
 * DTO for OCR processing options
 */
export class OcrOptionsDto {
  @IsEnum(OcrEngineType)
  @IsOptional()
  engine?: OcrEngineType;

  @IsString()
  @IsOptional()
  language?: string;

  @IsString()
  @IsOptional()
  extractFields?: string; // Comma-separated list of fields to extract
}
