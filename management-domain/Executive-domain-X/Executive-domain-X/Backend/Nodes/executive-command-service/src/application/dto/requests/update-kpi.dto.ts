import { IsString, IsNumber, IsOptional, IsEnum, IsBoolean } from 'class-validator';
import { KpiCategory } from '@domain/enums/kpi-category.enum';
import { KpiStatus } from '@domain/enums/kpi-status.enum';
import { ExecutiveLevel } from '@domain/enums/executive-level.enum';

export class UpdateKpiDto {
  @IsOptional()
  @IsString()
  name?: string;

  @IsOptional()
  @IsEnum(KpiCategory)
  category?: KpiCategory;

  @IsOptional()
  @IsEnum(ExecutiveLevel)
  executiveLevel?: ExecutiveLevel;

  @IsOptional()
  @IsNumber()
  value?: number;

  @IsOptional()
  @IsString()
  unit?: string;

  @IsOptional()
  @IsString()
  period?: string;

  @IsOptional()
  @IsNumber()
  target?: number;

  @IsOptional()
  @IsNumber()
  previousValue?: number;

  @IsOptional()
  @IsEnum(KpiStatus)
  status?: KpiStatus;

  @IsOptional()
  @IsString()
  trend?: string;

  @IsOptional()
  @IsBoolean()
  visible?: boolean;
}
