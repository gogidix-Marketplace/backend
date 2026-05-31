import { IsString, IsOptional, IsArray, IsObject, IsNumber, IsBoolean, ValidateNested, IsEnum } from 'class-validator';
import { Type } from 'class-transformer';

export class RuleConditionDto {
  @IsString()
  field: string;

  @IsString()
  @IsEnum(['eq', 'ne', 'gt', 'lt', 'gte', 'lte', 'in', 'nin', 'contains', 'startsWith', 'endsWith', 'empty', 'notEmpty'])
  operator: string;

  @IsOptional()
  value?: any;

  @IsOptional()
  @IsArray()
  values?: any[];
}

export class RuleTriggerDto {
  @IsString()
  type: string;

  @IsOptional()
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => RuleConditionDto)
  conditions?: RuleConditionDto[];

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  entityTypes?: string[];

  @IsOptional()
  @IsString()
  cronExpression?: string;

  @IsOptional()
  @IsNumber()
  debounceMs?: number;
}

export class RuleActionDto {
  @IsString()
  type: string;

  @IsString()
  name: string;

  @IsNumber()
  order: number;

  @IsObject()
  parameters: Record<string, any>;

  @IsOptional()
  @IsBoolean()
  continueOnError?: boolean;

  @IsOptional()
  @IsNumber()
  delayMs?: number;
}

export class LeadScoringRuleDto {
  @IsBoolean()
  enabled: boolean;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => RuleConditionDto)
  conditions: RuleConditionDto[];

  @IsNumber()
  score: number;

  @IsOptional()
  @IsNumber()
  maxScore?: number;

  @IsOptional()
  @IsString()
  @IsEnum(['demographic', 'behavioral', 'engagement'])
  category?: 'demographic' | 'behavioral' | 'engagement';
}

export class DealStageRuleDto {
  @IsBoolean()
  enabled: boolean;

  @IsOptional()
  @IsString()
  currentStage?: string;

  @IsOptional()
  @IsString()
  targetStage?: string;

  @IsOptional()
  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => RuleConditionDto)
  conditions?: RuleConditionDto[];

  @IsOptional()
  @IsBoolean()
  autoTransition?: boolean;

  @IsOptional()
  @IsBoolean()
  notifyAssignee?: boolean;
}

export class ScheduleDto {
  @IsString()
  @IsEnum(['once', 'daily', 'weekly', 'monthly', 'cron'])
  frequency: 'once' | 'daily' | 'weekly' | 'monthly' | 'cron';

  @IsOptional()
  @IsString()
  cronExpression?: string;

  @IsOptional()
  @IsString()
  timezone?: string;

  @IsOptional()
  startDate?: Date;

  @IsOptional()
  endDate?: Date;
}

export class CreateAutomationRuleDto {
  @IsString()
  name: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsObject()
  @ValidateNested()
  @Type(() => RuleTriggerDto)
  trigger: RuleTriggerDto;

  @IsArray()
  @ValidateNested({ each: true })
  @Type(() => RuleActionDto)
  actions: RuleActionDto[];

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  tags?: string[];

  @IsOptional()
  @IsNumber()
  priority?: number;

  @IsOptional()
  @IsString()
  category?: string;

  @IsOptional()
  @IsString()
  templateId?: string;

  @IsOptional()
  @ValidateNested()
  @Type(() => ScheduleDto)
  schedule?: ScheduleDto;

  @IsOptional()
  @ValidateNested()
  @Type(() => LeadScoringRuleDto)
  leadScoring?: LeadScoringRuleDto;

  @IsOptional()
  @ValidateNested()
  @Type(() => DealStageRuleDto)
  dealStageRule?: DealStageRuleDto;
}
