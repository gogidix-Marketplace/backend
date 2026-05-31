import { v4 as uuidv4 } from 'uuid';

export class ApplyScoreDecayCommand {
  readonly commandId: string;
  readonly commandType = 'ApplyScoreDecay';

  constructor(
    readonly tenantId: string,
    readonly decayRate?: number,
    readonly decayPeriodDays?: number
  ) {
    this.commandId = uuidv4();
  }
}

export class RescoreLeadCommand {
  readonly commandId: string;
  readonly commandType = 'RescoreLead';

  constructor(
    readonly leadId: string,
    readonly tenantId: string,
    readonly force: boolean = false,
    readonly requestedBy?: string
  ) {
    this.commandId = uuidv4();
  }
}
