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

export class ScoreRuleResponseDto {
  id!: string;
  tenantId!: string;
  scoreModelId!: string;
  name!: string;
  description!: string;
  ruleType!: ScoreRuleType;
  conditions!: RuleConditionDto[];
  formula!: RuleFormulaDto | null;
  baseScore!: number;
  maxScore!: number;
  priority!: number;
  isActive!: boolean;
  category!: string;
  tags!: string[];
  metadata!: Record<string, any>;
  createdAt!: Date;
  updatedAt!: Date;
}

export class ScoreRuleListItemDto {
  id!: string;
  scoreModelId!: string;
  name!: string;
  description!: string;
  ruleType!: ScoreRuleType;
  baseScore!: number;
  maxScore!: number;
  priority!: number;
  isActive!: boolean;
  category!: string;
  createdAt!: Date;
}
