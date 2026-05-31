import { IsString, IsEnum, IsDate, IsNumber, IsOptional, Min, Max } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';
import { ForecastModel, ForecastPeriod, ForecastGranularity } from '../../shared/constants';

export class GenerateForecastRequestDto {
  @ApiProperty({ description: 'Name of the forecast' })
  @IsString()
  name!: string;

  @ApiProperty({ description: 'Description of the forecast' })
  @IsString()
  description!: string;

  @ApiProperty({ enum: ForecastModel, description: 'Forecasting model to use' })
  @IsEnum(ForecastModel)
  model: ForecastModel;

  @ApiProperty({ enum: ForecastPeriod, description: 'Forecast period type' })
  @IsEnum(ForecastPeriod)
  period: ForecastPeriod;

  @ApiProperty({ enum: ForecastGranularity, description: 'Forecast granularity level' })
  @IsEnum(ForecastGranularity)
  granularity: ForecastGranularity;

  @ApiPropertyOptional({ description: 'ID of the rep/team/territory (if not global)' })
  @IsString()
  @IsOptional()
  granularityId?: string;

  @ApiProperty({ description: 'Currency code' })
  @IsString()
  currency!: string;

  @ApiProperty({ description: 'Forecast start date', type: Date })
  @IsDate()
  startDate!: Date;

  @ApiProperty({ description: 'Forecast end date', type: Date })
  @IsDate()
  endDate!: Date;

  @ApiPropertyOptional({ description: 'Forecast horizon in months', default: 12 })
  @IsNumber()
  @IsOptional()
  @Min(1)
  @Max(36)
  horizonMonths?: number;

  @ApiPropertyOptional({ description: 'Confidence level percentage', default: 85 })
  @IsNumber()
  @IsOptional()
  @Min(50)
  @Max(99)
  confidenceLevel?: number;
}

export class AdjustForecastRequestDto {
  @ApiProperty({ description: 'Adjustment factor (1.0 = no change, 1.1 = +10%, 0.9 = -10%)' })
  @IsNumber()
  @Min(0)
  @Max(10)
  adjustmentFactor!: number;

  @ApiProperty({ description: 'Reason for adjustment' })
  @IsString()
  reason!: string;

  @ApiPropertyOptional({ enum: ['MANUAL', 'AUTOMATIC'], description: 'Type of adjustment' })
  @IsEnum(['MANUAL', 'AUTOMATIC'])
  @IsOptional()
  adjustmentType?: 'MANUAL' | 'AUTOMATIC';
}

export class CalculateAccuracyRequestDto {
  @ApiProperty({ description: 'Actual amount achieved' })
  @IsNumber()
  @Min(0)
  actualAmount!: number;

  @ApiProperty({ description: 'Comparison start date', type: Date })
  @IsDate()
  comparisonStartDate!: Date;

  @ApiProperty({ description: 'Comparison end date', type: Date })
  @IsDate()
  comparisonEndDate!: Date;

  @ApiPropertyOptional({ description: 'Period ID to calculate accuracy for (optional)' })
  @IsString()
  @IsOptional()
  periodId?: string;
}

export class ListForecastsQueryDto {
  @ApiPropertyOptional({ enum: ForecastModel })
  @IsEnum(ForecastModel)
  @IsOptional()
  model?: ForecastModel;

  @ApiPropertyOptional({ enum: ForecastPeriod })
  @IsEnum(ForecastPeriod)
  @IsOptional()
  period?: ForecastPeriod;

  @ApiPropertyOptional({ enum: ForecastGranularity })
  @IsEnum(ForecastGranularity)
  @IsOptional()
  granularity?: ForecastGranularity;

  @ApiPropertyOptional()
  @IsString()
  @IsOptional()
  granularityId?: string;

  @ApiPropertyOptional({ type: Date })
  @IsDate()
  @IsOptional()
  startDate?: Date;

  @ApiPropertyOptional({ type: Date })
  @IsDate()
  @IsOptional()
  endDate?: Date;

  @ApiPropertyOptional({ description: 'Page number', default: 1, minimum: 1 })
  @IsNumber()
  @IsOptional()
  @Min(1)
  page?: number;

  @ApiPropertyOptional({ description: 'Items per page', default: 20, minimum: 1, maximum: 100 })
  @IsNumber()
  @IsOptional()
  @Min(1)
  @Max(100)
  limit?: number;

  @ApiPropertyOptional({ description: 'Sort by field', default: 'createdAt' })
  @IsString()
  @IsOptional()
  sortBy?: string;

  @ApiPropertyOptional({ enum: ['ASC', 'DESC'], default: 'DESC' })
  @IsEnum(['ASC', 'DESC'])
  @IsOptional()
  sortOrder?: 'ASC' | 'DESC';
}

