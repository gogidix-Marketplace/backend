import { v4 as uuidv4 } from 'uuid';

export class ScoreLeadCommand {
  readonly commandId: string;
  readonly commandType = 'ScoreLead';

  constructor(
    readonly leadId: string,
    readonly tenantId: string,
    readonly scoreModelId?: string,
    readonly leadData?: Record<string, any>,
    readonly variantId?: string,
    readonly requestedBy?: string
  ) {
    this.commandId = uuidv4();
  }
}
