import { IsString, IsNotEmpty, IsOptional, IsArray, IsNumber, IsBoolean } from 'class-validator';
import { ScoreType } from '../../../domain/entities/enums/score-type.enum';

export class CreateScoreAttributeRequestDto {
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
  @IsNotEmpty()
  type!: ScoreType;

  @IsString()
  @IsNotEmpty()
  dataType!: string;

  @IsNumber()
  @IsNotEmpty()
  weight!: number;

  @IsOptional()
  defaultValue?: any;

  @IsBoolean()
  @IsOptional()
  isRequired?: boolean;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  options?: string[];

  @IsString()
  @IsOptional()
  validationRule?: string;

  @IsString()
  @IsNotEmpty()
  sourceField!: string;

  @IsBoolean()
  @IsOptional()
  isActive?: boolean;

  @IsNumber()
  @IsOptional()
  displayOrder?: number;
}

export class UpdateScoreAttributeRequestDto {
  @IsString()
  @IsOptional()
  name?: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsNumber()
  @IsOptional()
  weight?: number;

  @IsOptional()
  defaultValue?: any;

  @IsBoolean()
  @IsOptional()
  isActive?: boolean;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  options?: string[];
}

export class UpdateAttributeWeightRequestDto {
  @IsNumber()
  @IsNotEmpty()
  weight!: number;
}
