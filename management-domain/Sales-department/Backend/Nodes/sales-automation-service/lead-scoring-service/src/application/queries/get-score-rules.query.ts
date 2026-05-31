import { v4 as uuidv4 } from 'uuid';

export class GetScoreRulesQuery {
  readonly queryId: string;
  readonly queryType = 'GetScoreRules';

  constructor(
    readonly scoreModelId: string,
    readonly tenantId: string,
    readonly isActive?: boolean
  ) {
    this.queryId = uuidv4();
  }
}

export class GetScoreAttributesQuery {
  readonly queryId: string;
  readonly queryType = 'GetScoreAttributes';

  constructor(
    readonly tenantId: string,
    readonly type?: string,
    readonly isActive?: boolean
  ) {
    this.queryId = uuidv4();
  }
}
