import { IsString, IsNotEmpty, IsEnum, IsOptional, IsObject, IsArray, IsNumber } from 'class-validator';

/**
 * Process type enumeration
 */
export enum ProcessType {
  OCR = 'OCR',
  VALIDATION = 'VALIDATION',
  ENRICHMENT = 'ENRICHMENT',
}

/**
 * DTO for processing an invoice
 */
export class ProcessInvoiceDto {
  @IsString()
  @IsNotEmpty()
  invoiceId!: string;

  @IsEnum(ProcessType)
  processType!: ProcessType;

  @IsObject()
  @IsOptional()
  options?: Record<string, unknown>;
}

/**
 * DTO for recording a payment
 */
export class RecordPaymentDto {
  @IsString()
  @IsNotEmpty()
  invoiceId!: string;

  @IsNumber()
  amount!: number;

  @IsString()
  @IsNotEmpty()
  paymentDate!: string;

  @IsString()
  @IsOptional()
  paymentMethod?: string;

  @IsString()
  @IsOptional()
  reference?: string;
}

/**
 * DTO for adding an attachment
 */
export class AddAttachmentDto {
  @IsString()
  @IsNotEmpty()
  invoiceId!: string;

  @IsString()
  @IsNotEmpty()
  name!: string;

  @IsString()
  @IsNotEmpty()
  url!: string;

  @IsString()
  @IsNotEmpty()
  type!: string;

  @IsNumber()
  size!: number;
}
