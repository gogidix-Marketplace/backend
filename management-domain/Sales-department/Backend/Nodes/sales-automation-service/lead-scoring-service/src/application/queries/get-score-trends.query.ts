import { v4 as uuidv4 } from 'uuid';

export class GetScoreTrendsQuery {
  readonly queryId: string;
  readonly queryType = 'GetScoreTrends';

  constructor(
    readonly tenantId: string,
    readonly leadId?: string,
    readonly days: number = 30,
    readonly granularity: 'day' | 'week' | 'month' = 'day'
  ) {
    this.queryId = uuidv4();
  }
}

export class GetScoreHistoryQuery {
  readonly queryId: string;
  readonly queryType = 'GetScoreHistory';

  constructor(
    readonly leadId: string,
    readonly tenantId: string,
    readonly limit: number = 50
  ) {
    this.queryId = uuidv4();
  }
}
