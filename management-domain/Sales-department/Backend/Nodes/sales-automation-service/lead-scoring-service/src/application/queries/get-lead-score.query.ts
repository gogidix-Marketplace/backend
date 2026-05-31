import { v4 as uuidv4 } from 'uuid';

export class GetLeadScoreQuery {
  readonly queryId: string;
  readonly queryType = 'GetLeadScore';

  constructor(
    readonly leadId: string,
    readonly tenantId: string
  ) {
    this.queryId = uuidv4();
  }
}

export interface PaginationOptions {
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

export class GetLeadScoresByTenantQuery {
  readonly queryId: string;
  readonly queryType = 'GetLeadScoresByTenant';

  constructor(
    readonly tenantId: string,
    readonly options?: PaginationOptions
  ) {
    this.queryId = uuidv4();
  }
}
