import { IsString, IsOptional, IsNumber, IsEnum, Min, Max, IsDateString, IsArray } from 'class-validator';
import { Type } from 'class-transformer';
import { InvoiceStatus } from '../../../domain/models/invoice.entity';

/**
 * DTO for searching and filtering invoices
 */
export class SearchInvoicesDto {
  @IsString()
  @IsOptional()
  searchTerm?: string;

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
  sortBy?: string;

  @IsString()
  @IsOptional()
  sortOrder?: 'ASC' | 'DESC';

  @IsNumber()
  @Min(1)
  @IsOptional()
  @Type(() => Number)
  page?: number;

  @IsNumber()
  @Min(1)
  @Max(100)
  @IsOptional()
  @Type(() => Number)
  pageSize?: number;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  statuses?: InvoiceStatus[];
}

/**
 * DTO for getting invoice statistics
 */
export class GetStatisticsDto {
  @IsDateString()
  @IsOptional()
  startDate?: string;

  @IsDateString()
  @IsOptional()
  endDate?: string;
}
