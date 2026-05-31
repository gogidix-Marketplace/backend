import { v4 as uuidv4 } from 'uuid';
import { PaginationOptions } from './get-lead-score.query';

export class GetQualifiedLeadsQuery {
  readonly queryId: string;
  readonly queryType = 'GetQualifiedLeads';

  constructor(
    readonly tenantId: string,
    readonly minScore: number = 50,
    readonly options?: PaginationOptions
  ) {
    this.queryId = uuidv4();
  }
}

export class GetScoreStatisticsQuery {
  readonly queryId: string;
  readonly queryType = 'GetScoreStatistics';

  constructor(
    readonly tenantId: string,
    readonly scoreModelId?: string
  ) {
    this.queryId = uuidv4();
  }
}
