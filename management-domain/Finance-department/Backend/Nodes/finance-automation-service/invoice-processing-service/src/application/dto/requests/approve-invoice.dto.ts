import { IsString, IsNotEmpty, IsOptional } from 'class-validator';

/**
 * DTO for approving an invoice
 */
export class ApproveInvoiceDto {
  @IsString()
  @IsNotEmpty()
  invoiceId!: string;

  @IsString()
  @IsOptional()
  notes?: string;
}

/**
 * DTO for rejecting an invoice
 */
export class RejectInvoiceDto {
  @IsString()
  @IsNotEmpty()
  invoiceId!: string;

  @IsString()
  @IsNotEmpty()
  reason!: string;
}

/**
 * DTO for canceling an invoice
 */
export class CancelInvoiceDto {
  @IsString()
  @IsNotEmpty()
  invoiceId!: string;

  @IsString()
  @IsOptional()
  reason?: string;
}
