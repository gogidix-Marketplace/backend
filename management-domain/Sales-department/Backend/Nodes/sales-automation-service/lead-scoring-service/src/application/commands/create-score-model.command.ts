import { v4 as uuidv4 } from 'uuid';
import { ScoreModelType } from '../../domain/entities/enums/score-type.enum';
import { GradeConfig } from '../../domain/entities/enums/lead-grade.enum';

export interface ScoringConfigurationInput {
  enableScoreDecay?: boolean;
  decayRate?: number;
  decayPeriodDays?: number;
  minimumScoreThreshold?: number;
  autoReScoreEnabled?: boolean;
  reScoreIntervalDays?: number;
  gradeConfigs?: GradeConfig[];
}

export class CreateScoreModelCommand {
  readonly commandId: string;
  readonly commandType = 'CreateScoreModel';

  constructor(
    readonly tenantId: string,
    readonly name: string,
    readonly description: string,
    readonly modelType: ScoreModelType,
    readonly createdBy: string,
    readonly scoringConfig?: ScoringConfigurationInput,
    readonly ruleIds?: string[],
    readonly attributeIds?: string[],
    readonly isDefault?: boolean
  ) {
    this.commandId = uuidv4();
  }
}
