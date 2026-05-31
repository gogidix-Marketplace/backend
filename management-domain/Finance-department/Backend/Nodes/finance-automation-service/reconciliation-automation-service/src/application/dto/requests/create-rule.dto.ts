import { IsString, IsOptional, IsEnum, IsNumber, IsArray, IsObject, Min, Max } from 'class-validator';
import { ApiProperty, ApiPropertyOptional } from '@nestjs/swagger';

export class RuleConditionDto {
  @ApiProperty({ description: 'Field to evaluate' })
  @IsString()
  field!: string;

  @ApiProperty({ description: 'Operator', enum: ['equals', 'contains', 'startsWith', 'endsWith', 'greaterThan', 'lessThan', 'between'] })
  @IsEnum(['equals', 'contains', 'startsWith', 'endsWith', 'greaterThan', 'lessThan', 'between'])
  operator: 'equals' | 'contains' | 'startsWith' | 'endsWith' | 'greaterThan' | 'lessThan' | 'between';

  @ApiProperty({ description: 'Value to compare against' })
  value: unknown;

  @ApiPropertyOptional({ description: 'Condition weight' })
  @IsNumber()
  @IsOptional()
  weight?: number;
}

export class RuleActionDto {
  @ApiProperty({ description: 'Action type', enum: ['AUTO_MATCH', 'FLAG_FOR_REVIEW', 'AUTO_RESOLVE', 'SEND_NOTIFICATION'] })
  @IsEnum(['AUTO_MATCH', 'FLAG_FOR_REVIEW', 'AUTO_RESOLVE', 'SEND_NOTIFICATION'])
  type: 'AUTO_MATCH' | 'FLAG_FOR_REVIEW' | 'AUTO_RESOLVE' | 'SEND_NOTIFICATION';

  @ApiPropertyOptional({ description: 'Action parameters' })
  @IsObject()
  @IsOptional()
  params?: Record<string, unknown>;
}

export class CreateRuleDto {
  @ApiProperty({ description: 'Rule name' })
  @IsString()
  name!: string;

  @ApiPropertyOptional({ description: 'Rule description' })
  @IsString()
  @IsOptional()
  description?: string;

  @ApiProperty({ description: 'Match type', enum: ['EXACT', 'FUZZY', 'AI_BASED', 'MANUAL'] })
  @IsEnum(['EXACT', 'FUZZY', 'AI_BASED', 'MANUAL'])
  matchType: 'EXACT' | 'FUZZY' | 'AI_BASED' | 'MANUAL';

  @ApiProperty({ description: 'Rule priority (1-100)' })
  @IsNumber()
  @Min(1)
  @Max(100)
  priority!: number;

  @ApiProperty({ description: 'Rule conditions', type: [RuleConditionDto] })
  @IsArray()
  @IsObject({ each: true })
  conditions: RuleConditionDto[];

  @ApiProperty({ description: 'Rule actions', type: [RuleActionDto] })
  @IsArray()
  @IsObject({ each: true })
  actions: RuleActionDto[];

  @ApiPropertyOptional({ description: 'Confidence threshold (0-1)' })
  @IsNumber()
  @Min(0)
  @Max(1)
  @IsOptional()
  confidenceThreshold?: number;
}

export class UpdateRuleDto {
  @ApiPropertyOptional({ description: 'Rule name' })
  @IsString()
  @IsOptional()
  name?: string;

  @ApiPropertyOptional({ description: 'Rule description' })
  @IsString()
  @IsOptional()
  description?: string;

  @ApiPropertyOptional({ description: 'Match type', enum: ['EXACT', 'FUZZY', 'AI_BASED', 'MANUAL'] })
  @IsEnum(['EXACT', 'FUZZY', 'AI_BASED', 'MANUAL'])
  @IsOptional()
  matchType?: 'EXACT' | 'FUZZY' | 'AI_BASED' | 'MANUAL';

  @ApiPropertyOptional({ description: 'Rule priority (1-100)' })
  @IsNumber()
  @Min(1)
  @Max(100)
  @IsOptional()
  priority?: number;

  @ApiPropertyOptional({ description: 'Rule conditions', type: [RuleConditionDto] })
  @IsArray()
  @IsObject({ each: true })
  @IsOptional()
  conditions?: RuleConditionDto[];

  @ApiPropertyOptional({ description: 'Rule actions', type: [RuleActionDto] })
  @IsArray()
  @IsObject({ each: true })
  @IsOptional()
  actions?: RuleActionDto[];

  @ApiPropertyOptional({ description: 'Confidence threshold (0-1)' })
  @IsNumber()
  @Min(0)
  @Max(1)
  @IsOptional()
  confidenceThreshold?: number;
}
