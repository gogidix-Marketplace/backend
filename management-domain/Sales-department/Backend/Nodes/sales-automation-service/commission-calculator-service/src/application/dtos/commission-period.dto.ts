import { IsString, IsOptional, IsEnum, IsDateString, IsArray } from 'class-validator';
import { CommissionPeriodType } from '../../domain/enums/commission-period-type.enum';

/**
 * DTO for creating commission period
 */
export class CreateCommissionPeriodDto {
  @IsString()
  name!: string;

  @IsEnum(CommissionPeriodType)
  periodType!: CommissionPeriodType;

  @IsDateString()
  startDate!: string;

  @IsDateString()
  endDate!: string;
}

/**
 * DTO for updating commission period
 */
export class UpdateCommissionPeriodDto {
  @IsString()
  @IsOptional()
  name?: string;

  @IsDateString()
  @IsOptional()
  startDate?: string;

  @IsDateString()
  @IsOptional()
  endDate?: string;
}

/**
 * DTO for period calculation request
 */
export class CalculatePeriodCommissionsDto {
  @IsString()
  periodId!: string;

  @IsArray()
  @IsOptional()
  salesRepIds?: string[];

  @IsString()
  @IsOptional()
  organizationId?: string;
}
