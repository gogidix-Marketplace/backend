import { IsString, IsNotEmpty, IsDate, IsArray, IsNumber, IsOptional, IsEnum, IsISO4217CurrencyCode, ValidateNested, Min, Max } from 'class-validator';
import { Type } from 'class-transformer';
import { PaymentTerms } from '../../../domain/models/invoice.entity';

/**
 * DTO for creating a new invoice
 */
export class CreateInvoiceItemDto {
  @IsString()
  @IsNotEmpty()
  description!: string;

  @IsNumber()
  @Min(0.01)
  quantity!: number;

  @IsNumber()
  @Min(0)
  unitPrice!: number;

  @IsNumber()
  @Min(0)
  @Max(100)
  taxRate!: number;

  @IsString()
  @IsOptional()
  sku?: string;

  @IsString()
  @IsOptional()
  unitOfMeasure?: string;
}

export class CreateInvoiceDto {
  @IsString()
  @IsNotEmpty()
  invoiceNumber!: string;

  @IsString()
  @IsOptional()
  purchaseOrderNumber?: string;

  @IsString()
  @IsNotEmpty()
  vendorId!: string;

  @IsString()
  @IsNotEmpty()
  vendorName!: string;

  @IsString()
  @IsOptional()
  vendorTaxId?: string;

  @IsDate()
  @Type(() => Date)
  invoiceDate!: Date;

  @IsDate()
  @Type(() => Date)
  dueDate!: Date;

  @IsEnum(PaymentTerms)
  @IsOptional()
  paymentTerms?: PaymentTerms;

  @IsISO4217CurrencyCode()
  @IsOptional()
  currency?: string;

  @IsString()
  @IsOptional()
  category?: string;

  @IsString()
  @IsOptional()
  glAccountCode?: string;

  @IsString()
  @IsOptional()
  costCenter?: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CreateInvoiceItemDto)
  items!: CreateInvoiceItemDto[];

  @IsString()
  @IsOptional()
  notes?: string;

  @IsString()
  @IsOptional()
  internalNotes?: string;
}
