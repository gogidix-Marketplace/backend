import { IsString, IsOptional, IsEnum, IsArray, IsBoolean } from 'class-validator';
import { Type } from 'class-transformer';
import { AutomationRuleStatus } from '../../../../domain/enums/automation-rule-status.enum';

export class QueryAutomationRulesDto {
  @IsOptional()
  @IsString()
  category?: string;

  @IsOptional()
  @IsEnum(AutomationRuleStatus)
  status?: AutomationRuleStatus;

  @IsOptional()
  @IsArray()
  @IsString({ each: true })
  tags?: string[];

  @IsOptional()
  @IsBoolean()
  isEnabled?: boolean;

  @IsOptional()
  @IsString()
  search?: string;

  @IsOptional()
  @IsString()
  sortBy?: 'name' | 'createdAt' | 'updatedAt' | 'executionCount' | 'priority';

  @IsOptional()
  @IsString()
  sortOrder?: 'asc' | 'desc';

  @IsOptional()
  @IsString()
  page?: string;

  @IsOptional()
  @IsString()
  limit?: string;
}
