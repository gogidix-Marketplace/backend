export interface PipelineDeal {
  id: string;
  amount: number;
  stage: string;
  probability: number;
  expectedCloseDate: Date;
  productId?: string;
  repId: string;
  teamId: string;
  territoryId: string;
  createdAt: Date;
}

export interface HistoricalSalesData {
  period: string;
  amount: number;
  dealsCount: number;
  winsCount: number;
}

export interface PipelineDataProviderPort {
  getOpenPipelineDeals(tenantId: string, filters?: {
    repId?: string;
    teamId?: string;
    territoryId?: string;
    stage?: string;
  }): Promise<PipelineDeal[]>;
  getHistoricalSales(
    tenantId: string,
    startDate: Date,
    endDate: Date,
    granularity?: 'rep' | 'team' | 'territory',
    granularityId?: string,
  ): Promise<HistoricalSalesData[]>;
  getStageWeights(tenantId: string): Promise<Map<string, number>>;
}
