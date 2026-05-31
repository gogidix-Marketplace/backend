import { v4 as uuidv4 } from 'uuid';

export class BatchScoreLeadsCommand {
  readonly commandId: string;
  readonly commandType = 'BatchScoreLeads';

  constructor(
    readonly leadIds: string[],
    readonly tenantId: string,
    readonly scoreModelId?: string,
    readonly requestedBy?: string
  ) {
    this.commandId = uuidv4();
  }
}
