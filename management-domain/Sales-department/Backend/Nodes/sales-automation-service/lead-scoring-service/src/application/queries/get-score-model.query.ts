import { v4 as uuidv4 } from 'uuid';
import { PaginationOptions } from './get-lead-score.query';

export class GetScoreModelQuery {
  readonly queryId: string;
  readonly queryType = 'GetScoreModel';

  constructor(
    readonly modelId: string,
    readonly tenantId: string
  ) {
    this.queryId = uuidv4();
  }
}

export class GetScoreModelsByTenantQuery {
  readonly queryId: string;
  readonly queryType = 'GetScoreModelsByTenant';

  constructor(
    readonly tenantId: string,
    readonly status?: string,
    readonly options?: PaginationOptions
  ) {
    this.queryId = uuidv4();
  }
}
