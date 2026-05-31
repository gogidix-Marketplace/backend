import { IsString, IsOptional, IsArray, IsObject, IsNumber, ValidateNested, PartialType } from 'class-validator';
import { Type } from 'class-transformer';
import { CreateAutomationRuleDto, RuleActionDto, LeadScoringRuleDto, DealStageRuleDto } from './create-automation-rule.dto';

export class UpdateAutomationRuleDto extends PartialType(CreateAutomationRuleDto) {
  @IsOptional()
  @IsString()
  name?: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  tags?: string[];

  @IsOptional()
  @IsNumber()
  priority?: number;
}
