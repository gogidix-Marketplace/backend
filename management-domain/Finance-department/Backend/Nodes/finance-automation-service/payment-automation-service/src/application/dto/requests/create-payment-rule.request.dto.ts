import { IsString, IsArray, IsOptional, IsDate, IsNumber, validateOrReject } from 'class-validator';
import { Transform } from 'class-transformer';

export interface RuleConditionDto {
  field: string;
  operator: 'eq' | 'ne' | 'gt' | 'gte' | 'lt' | 'lte' | 'in' | 'contains' | 'regex';
  value: any;
  logicalOperator?: 'AND' | 'OR';
}

export interface RuleActionDto {
  type: 'APPROVE' | 'REJECT' | 'ESCALATE' | 'SCHEDULE' | 'SET_GATEWAY' | 'ADD_TAG';
  parameters?: Record<string, any>;
}

export class CreatePaymentRuleRequestDto {
  @IsString()
  name!: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsString()
  ruleType!: string;

  @IsArray()
  conditions!: RuleConditionDto[];

  @IsArray()
  actions!: RuleActionDto[];

  @IsOptional()
  @IsNumber()
  priority?: number;

  @IsOptional()
  @IsDate()
  @Transform(({ value }) => value ? new Date(value) : undefined)
  effectiveFrom?: Date;

  @IsOptional()
  @IsDate()
  @Transform(({ value }) => value ? new Date(value) : undefined)
  effectiveTo?: Date;

  async validate(): Promise<void> {
    await validateOrReject(this);
  }
}

export class UpdatePaymentRuleRequestDto {
  @IsString()
  ruleId!: string;

  @IsOptional()
  @IsString()
  name?: string;

  @IsOptional()
  @IsString()
  description?: string;

  @IsOptional()
  @IsArray()
  conditions?: RuleConditionDto[];

  @IsOptional()
  @IsArray()
  actions?: RuleActionDto[];

  @IsOptional()
  @IsNumber()
  priority?: number;

  @IsOptional()
  isActive?: boolean;

  async validate(): Promise<void> {
    await validateOrReject(this);
  }
}
