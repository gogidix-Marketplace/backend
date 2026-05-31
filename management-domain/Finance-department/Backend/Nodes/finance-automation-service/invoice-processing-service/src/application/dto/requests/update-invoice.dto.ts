import { IsString, IsDate, IsOptional, IsEnum, IsArray, IsNumber, IsNotEmpty } from 'class-validator';
import { Type } from 'class-transformer';
import { PaymentTerms } from '../../../domain/models/invoice.entity';

/**
 * DTO for updating an existing invoice
 */
export class UpdateInvoiceDto {
  @IsString()
  @IsOptional()
  purchaseOrderNumber?: string;

  @IsDate()
  @IsOptional()
  @Type(() => Date)
  dueDate?: Date;

  @IsEnum(PaymentTerms)
  @IsOptional()
  paymentTerms?: PaymentTerms;

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
 * DTO for adding an item to an invoice
 */
export class AddInvoiceItemDto {
  @IsString()
  @IsNotEmpty()
  description!: string;

  @IsNumber()
  quantity!: number;

  @IsNumber()
  unitPrice!: number;

  @IsNumber()
  taxRate!: number;

  @IsString()
  @IsOptional()
  sku?: string;

  @IsString()
  @IsOptional()
  unitOfMeasure?: string;
}

/**
 * DTO for bulk updating invoice items
 */
export class UpdateInvoiceItemsDto {
  @IsArray()
  items!: Array<{
    lineNumber: number;
    description?: string;
    quantity?: number;
    unitPrice?: number;
    taxRate?: number;
    discountAmount?: number;
  }>;
}
