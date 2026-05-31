import { IsString, IsNumber, IsOptional, IsDateString, IsEnum, IsArray, ValidateNested } from 'class-validator';
import { Type } from 'class-transformer';
import { CommissionStatus } from '../../domain/enums/commission-status.enum';

/**
 * DTO for creating a commission
 */
export class CreateCommissionDto {
  @IsString()
  salesRepId!: string;

  @IsString()
  salesRepName!: string;

  @IsString()
  periodId!: string;

  @IsString()
  ruleId!: string;

  @IsString()
  ruleName!: string;

  @IsNumber()
  salesAmount!: number;

  @IsNumber()
  commissionRate!: number;

  @IsNumber()
  @IsOptional()
  calculatedAmount?: number;

  @IsString()
  @IsOptional()
  currency?: string;

  @IsDateString()
  @IsOptional()
  transactionDate?: string;

  @IsNumber()
  @IsOptional()
  quotaAttained?: number;

  @IsNumber()
  @IsOptional()
  quotaTarget?: number;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CommissionSplitDto)
  @IsOptional()
  splits?: CommissionSplitDto[];
}

/**
 * DTO for commission split
 */
export class CommissionSplitDto {
  @IsString()
  salesRepId!: string;

  @IsString()
  salesRepName!: string;

  @IsNumber()
  percentage!: number;

  @IsEnum(['PRIMARY', 'SECONDARY', 'SUPPORT'])
  role!: 'PRIMARY' | 'SECONDARY' | 'SUPPORT';
}

/**
 * DTO for creating commission with splits
 */
export class CreateCommissionWithSplitsDto extends CreateCommissionDto {
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => CommissionSplitDto)
  @IsOptional()
  splits?: CommissionSplitDto[];
}
