import { IsString, IsOptional, IsEnum, IsDateString, IsNumber, IsArray } from 'class-validator';
import { CommissionStatus } from '../../domain/enums/commission-status.enum';
import { CommissionPeriodType } from '../../domain/enums/commission-period-type.enum';

/**
 * DTO for commission query filters
 */
export class CommissionQueryDto {
  @IsString()
  @IsOptional()
  salesRepId?: string;

  @IsEnum(CommissionStatus)
  @IsOptional()
  status?: CommissionStatus;

  @IsString()
  @IsOptional()
  periodId?: string;

  @IsDateString()
  @IsOptional()
  startDate?: string;

  @IsDateString()
  @IsOptional()
  endDate?: string;

  @IsNumber()
  @IsOptional()
  minAmount?: number;

  @IsNumber()
  @IsOptional()
  maxAmount?: number;

  @IsString()
  @IsOptional()
  currency?: string;

  @IsString()
  @IsOptional()
  sortBy?: string = 'createdAt';

  @IsEnum(['asc', 'desc'])
  @IsOptional()
  sortOrder?: 'asc' | 'desc' = 'desc';

  @IsNumber()
  @IsOptional()
  limit?: number = 50;

  @IsNumber()
  @IsOptional()
  offset?: number = 0;
}

/**
 * DTO for commission period query
 */
export class CommissionPeriodQueryDto {
  @IsEnum(CommissionPeriodType)
  @IsOptional()
  type?: CommissionPeriodType;

  @IsEnum(['OPEN', 'CALCULATING', 'CLOSED', 'LOCKED'])
  @IsOptional()
  status?: 'OPEN' | 'CALCULATING' | 'CLOSED' | 'LOCKED';

  @IsString()
  @IsOptional()
  sortBy?: string = 'startDate';

  @IsEnum(['asc', 'desc'])
  @IsOptional()
  sortOrder?: 'asc' | 'desc' = 'desc';

  @IsNumber()
  @IsOptional()
  limit?: number = 50;

  @IsNumber()
  @IsOptional()
  offset?: number = 0;
}

/**
 * DTO for commission calculation request
 */
export class CalculateCommissionDto {
  @IsString()
  salesRepId!: string;

  @IsString()
  salesRepName!: string;

  @IsString()
  ruleId!: string;

  @IsNumber()
  salesAmount!: number;

  @IsString()
  @IsOptional()
  productId?: string;

  @IsString()
  @IsOptional()
  customerId?: string;

  @IsNumber()
  @IsOptional()
  quotaAttained?: number;

  @IsNumber()
  @IsOptional()
  quotaTarget?: number;

  @IsString()
  periodId!: string;

  @IsDateString()
  @IsOptional()
  transactionDate?: string;

  @IsString()
  @IsOptional()
  currency?: string;
}

/**
 * DTO for bulk calculation request
 */
export class BulkCalculateCommissionDto {
  @IsArray()
  calculations!: CalculateCommissionDto[];
}
