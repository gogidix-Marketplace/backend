import { v4 as uuidv4 } from 'uuid';

export class ActivateScoreModelCommand {
  readonly commandId: string;
  readonly commandType = 'ActivateScoreModel';

  constructor(
    readonly modelId: string,
    readonly tenantId: string,
    readonly userId: string
  ) {
    this.commandId = uuidv4();
  }
}

export class DeactivateScoreModelCommand {
  readonly commandId: string;
  readonly commandType = 'DeactivateScoreModel';

  constructor(
    readonly modelId: string,
    readonly tenantId: string,
    readonly userId: string
  ) {
    this.commandId = uuidv4();
  }
}
