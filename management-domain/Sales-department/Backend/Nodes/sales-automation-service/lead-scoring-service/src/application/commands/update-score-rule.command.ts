import { v4 as uuidv4 } from 'uuid';
import { RuleConditionInput, RuleFormulaInput } from './create-score-rule.command';

export class UpdateScoreRuleCommand {
  readonly commandId: string;
  readonly commandType = 'UpdateScoreRule';

  constructor(
    readonly ruleId: string,
    readonly tenantId: string,
    readonly name?: string,
    readonly description?: string,
    readonly conditions?: RuleConditionInput[],
    readonly formula?: RuleFormulaInput,
    readonly baseScore?: number,
    readonly maxScore?: number,
    readonly priority?: number,
    readonly category?: string,
    readonly tags?: string[]
  ) {
    this.commandId = uuidv4();
  }
}
