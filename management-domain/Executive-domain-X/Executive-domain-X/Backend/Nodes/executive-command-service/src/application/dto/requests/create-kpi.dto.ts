import { IsString, IsNumber, IsOptional, IsEnum, IsBoolean, IsArray } from 'class-validator';
import { KpiCategory } from '@domain/enums/kpi-category.enum';
import { ExecutiveLevel } from '@domain/enums/executive-level.enum';

export class CreateKpiDto {
  @IsString()
  name!: string;

  @IsEnum(KpiCategory)
  category: KpiCategory;

  @IsOptional()
  @IsEnum(ExecutiveLevel)
  executiveLevel?: ExecutiveLevel;

  @IsNumber()
  value!: number;

  @IsString()
  unit!: string;

  @IsString()
  period!: string;

  @IsOptional()
  @IsNumber()
  target?: number;

  @IsOptional()
  @IsNumber()
  previousValue?: number;

  @IsOptional()
  @IsString()
  trend?: string;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  dataSources?: string[];

  @IsOptional()
  metadata?: Record<string, unknown>;

  @IsOptional()
  @IsBoolean()
  visible?: boolean;

  @IsOptional()
  @IsBoolean()
  isCalculated?: boolean;
}
