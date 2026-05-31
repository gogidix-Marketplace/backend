import { IsString, IsNotEmpty, IsOptional, IsBoolean, IsArray, IsObject, ValidateNested, IsNumber } from 'class-validator';
import { Type } from 'class-transformer';
import { ScoreModelType } from '../../../domain/entities/enums/score-type.enum';

export class CreateScoreModelRequestDto {
  @IsString()
  @IsNotEmpty()
  tenantId!: string;

  @IsString()
  @IsNotEmpty()
  name!: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsString()
  @IsOptional()
  modelType?: ScoreModelType;

  @IsObject()
  @IsOptional()
  scoringConfig?: Record<string, any>;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  ruleIds?: string[];

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  attributeIds?: string[];

  @IsBoolean()
  @IsOptional()
  isDefault?: boolean;

  @IsString()
  @IsNotEmpty()
  createdBy!: string;
}

export class UpdateScoreModelRequestDto {
  @IsString()
  @IsOptional()
  name?: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsObject()
  @IsOptional()
  scoringConfig?: Record<string, any>;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  ruleIds?: string[];

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  attributeIds?: string[];
}

export class ActivateScoreModelRequestDto {
  @IsString()
  @IsNotEmpty()
  userId!: string;
}

export class AddVariantRequestDto {
  @IsString()
  @IsNotEmpty()
  name!: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsNumber()
  @IsNotEmpty()
  percentage!: number;
}

export class EnableABTestingRequestDto {
  @IsString()
  @IsNotEmpty()
  startDate!: string;

  @IsString()
  @IsNotEmpty()
  endDate!: string;
}
