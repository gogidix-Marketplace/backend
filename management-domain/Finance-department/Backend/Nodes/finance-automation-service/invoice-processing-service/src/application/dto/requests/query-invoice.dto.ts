import { IsString, IsOptional, IsEnum, IsNumber, Min, Max, IsDateString, IsArray } from 'class-validator';
import { Type } from 'class-transformer';
import { InvoiceStatus } from '../../../domain/models/invoice.entity';

/**
 * DTO for querying invoices with pagination and filters
 */
export class QueryInvoiceDto {
  @IsString()
  @IsOptional()
  vendorId?: string;

  @IsEnum(InvoiceStatus)
  @IsOptional()
  status?: InvoiceStatus;

  @IsDateString()
  @IsOptional()
  startDate?: string;

  @IsDateString()
  @IsOptional()
  endDate?: string;

  @IsString()
  @IsOptional()
  dateField?: 'invoiceDate' | 'dueDate' | 'receivedDate' | 'processedDate';

  @IsString()
  @IsOptional()
  searchTerm?: string;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  searchFields?: ('invoiceNumber' | 'vendorName' | 'notes' | 'poNumber')[];

  @IsString()
  @IsOptional()
  category?: string;

  @IsString()
  @IsOptional()
  costCenter?: string;

  @IsString()
  @IsOptional()
  glAccountCode?: string;

  @IsNumber()
  @Min(0)
  @IsOptional()
  @Type(() => Number)
  minAmount?: number;

  @IsNumber()
  @Min(0)
  @IsOptional()
  @Type(() => Number)
  maxAmount?: number;

  @IsNumber()
  @Min(1)
  @IsOptional()
  @Type(() => Number)
  page?: number = 1;

  @IsNumber()
  @Min(1)
  @Max(100)
  @IsOptional()
  @Type(() => Number)
  pageSize?: number = 20;

  @IsString()
  @IsOptional()
  sortBy?: string = 'invoiceDate';

  @IsString()
  @IsOptional()
  sortOrder?: 'ASC' | 'DESC' = 'DESC';

  @IsString()
  @IsOptional()
  includeItems?: string;

  @IsString()
  @IsOptional()
  includeValidation?: string;
}
