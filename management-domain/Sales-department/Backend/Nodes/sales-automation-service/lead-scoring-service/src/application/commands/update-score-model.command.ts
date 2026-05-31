import { v4 as uuidv4 } from 'uuid';
import { ScoringConfigurationInput } from './create-score-model.command';

export class UpdateScoreModelCommand {
  readonly commandId: string;
  readonly commandType = 'UpdateScoreModel';

  constructor(
    readonly modelId: string,
    readonly tenantId: string,
    readonly updatedBy: string,
    readonly name?: string,
    readonly description?: string,
    readonly scoringConfig?: ScoringConfigurationInput
  ) {
    this.commandId = uuidv4();
  }
}
