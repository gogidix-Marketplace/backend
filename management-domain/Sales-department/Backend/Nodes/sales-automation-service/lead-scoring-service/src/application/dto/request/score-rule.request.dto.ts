import { IsString, IsNotEmpty, IsOptional, IsArray, IsNumber, IsBoolean, ValidateNested, IsObject } from 'class-validator';
import { Type } from 'class-transformer';
import { ScoreRuleType } from '../../../domain/entities/enums/score-type.enum';

export interface RuleConditionDto {
  field: string;
  operator: string;
  value: any;
  weight?: number;
}

export interface RuleFormulaDto {
  expression: string;
  variables: string[];
}

export class CreateScoreRuleRequestDto {
  @IsString()
  @IsNotEmpty()
  tenantId!: string;

  @IsString()
  @IsNotEmpty()
  scoreModelId!: string;

  @IsString()
  @IsNotEmpty()
  name!: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsString()
  @IsNotEmpty()
  ruleType!: ScoreRuleType;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Object)
  conditions!: RuleConditionDto[];

  @IsObject()
  @IsOptional()
  formula?: RuleFormulaDto;

  @IsNumber()
  @IsNotEmpty()
  baseScore!: number;

  @IsNumber()
  @IsNotEmpty()
  maxScore!: number;

  @IsNumber()
  @IsOptional()
  priority?: number;

  @IsBoolean()
  @IsOptional()
  isActive?: boolean;

  @IsString()
  @IsOptional()
  category?: string;

  @IsArray()
  @IsString({ each: true })
  @IsOptional()
  tags?: string[];
}

export class UpdateScoreRuleRequestDto {
  @IsString()
  @IsOptional()
  name?: string;

  @IsString()
  @IsOptional()
  description?: string;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => Object)
  @IsOptional()
  conditions?: RuleConditionDto[];

  @IsNumber()
  @IsOptional()
  baseScore?: number;

  @IsNumber()
  @IsOptional()
  maxScore?: number;

  @IsNumber()
  @IsOptional()
  priority?: number;

  @IsBoolean()
  @IsOptional()
  isActive?: boolean;
}

export class UpdateRuleScoreRequestDto {
  @IsNumber()
  @IsNotEmpty()
  baseScore!: number;

  @IsNumber()
  @IsNotEmpty()
  maxScore!: number;
}
