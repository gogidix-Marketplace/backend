import { v4 as uuidv4 } from 'uuid';
import { ScoreRuleType, ScoreRuleOperator } from '../../domain/entities/enums/score-type.enum';

export interface RuleConditionInput {
  field: string;
  operator: ScoreRuleOperator;
  value: any;
  weight?: number;
}

export interface RuleFormulaInput {
  expression: string;
  variables: string[];
}

export class CreateScoreRuleCommand {
  readonly commandId: string;
  readonly commandType = 'CreateScoreRule';

  constructor(
    readonly tenantId: string,
    readonly scoreModelId: string,
    readonly name: string,
    readonly description: string,
    readonly ruleType: ScoreRuleType,
    readonly conditions: RuleConditionInput[],
    readonly baseScore: number,
    readonly maxScore: number,
    readonly createdBy: string,
    readonly formula?: RuleFormulaInput,
    readonly priority?: number,
    readonly category?: string,
    readonly tags?: string[]
  ) {
    this.commandId = uuidv4();
  }
}
