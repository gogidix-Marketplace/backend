import { IsString, IsOptional } from 'class-validator';

/**
 * DTO for validating an invoice
 */
export class ValidateInvoiceDto {
  @IsString()
  @IsOptional()
  invoiceId?: string;

  @IsString()
  @IsOptional()
  validationLevel?: 'BASIC' | 'FULL' | 'STRICT';
}
