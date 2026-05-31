import { Injectable } from '@nestjs/common';
import {
  PipelineDeal,
  HistoricalSalesData,
  PipelineDataProviderPort,
} from '../../../../domain/ports/out/pipeline-data-provider.port';

/**
 * Mock implementation for PipelineDataProviderPort.
 * In production, this would make HTTP/gRPC calls to the Pipeline Service
 * or use a shared database to fetch pipeline and historical data.
 */
@Injectable()
export class PipelineDataProviderMock implements PipelineDataProviderPort {
  async getOpenPipelineDeals(
    tenantId: string,
    filters?: {
      repId?: string;
      teamId?: string;
      territoryId?: string;
      stage?: string;
    },
  ): Promise<PipelineDeal[]> {
    // Mock data - in production, fetch from Pipeline Service
    const mockDeals: PipelineDeal[] = [
      {
        id: 'deal-1',
        amount: 50000,
        stage: 'qualification',
        probability: 20,
        expectedCloseDate: new Date(Date.now() + 30 * 24 * 60 * 60 * 1000),
        productId: 'product-1',
        repId: filters?.repId || 'rep-1',
        teamId: filters?.teamId || 'team-1',
        territoryId: filters?.territoryId || 'territory-1',
        createdAt: new Date(),
      },
      {
        id: 'deal-2',
        amount: 100000,
        stage: 'proposal',
        probability: 50,
        expectedCloseDate: new Date(Date.now() + 60 * 24 * 60 * 60 * 1000),
        productId: 'product-2',
        repId: filters?.repId || 'rep-1',
        teamId: filters?.teamId || 'team-1',
        territoryId: filters?.territoryId || 'territory-1',
        createdAt: new Date(),
      },
      {
        id: 'deal-3',
        amount: 75000,
        stage: 'negotiation',
        probability: 75,
        expectedCloseDate: new Date(Date.now() + 45 * 24 * 60 * 60 * 1000),
        productId: 'product-1',
        repId: filters?.repId || 'rep-2',
        teamId: filters?.teamId || 'team-1',
        territoryId: filters?.territoryId || 'territory-1',
        createdAt: new Date(),
      },
      {
        id: 'deal-4',
        amount: 150000,
        stage: 'closing',
        probability: 90,
        expectedCloseDate: new Date(Date.now() + 15 * 24 * 60 * 60 * 1000),
        productId: 'product-3',
        repId: filters?.repId || 'rep-2',
        teamId: filters?.teamId || 'team-2',
        territoryId: filters?.territoryId || 'territory-2',
        createdAt: new Date(),
      },
    ];

    let filteredDeals = mockDeals;

    if (filters?.repId) {
      filteredDeals = filteredDeals.filter(d => d.repId === filters.repId);
    }
    if (filters?.teamId) {
      filteredDeals = filteredDeals.filter(d => d.teamId === filters.teamId);
    }
    if (filters?.territoryId) {
      filteredDeals = filteredDeals.filter(d => d.territoryId === filters.territoryId);
    }
    if (filters?.stage) {
      filteredDeals = filteredDeals.filter(d => d.stage === filters.stage);
    }

    return filteredDeals;
  }

  async getHistoricalSales(
    tenantId: string,
    startDate: Date,
    endDate: Date,
    granularity?: 'rep' | 'team' | 'territory',
    granularityId?: string,
  ): Promise<HistoricalSalesData[]> {
    // Mock historical data - in production, fetch from Sales Data Service
    const mockData: HistoricalSalesData[] = [
      {
        period: new Date(Date.now() - 90 * 24 * 60 * 60 * 1000).toISOString().slice(0, 7),
        amount: 150000,
        dealsCount: 25,
        winsCount: 8,
      },
      {
        period: new Date(Date.now() - 60 * 24 * 60 * 60 * 1000).toISOString().slice(0, 7),
        amount: 180000,
        dealsCount: 30,
        winsCount: 10,
      },
      {
        period: new Date(Date.now() - 30 * 24 * 60 * 60 * 1000).toISOString().slice(0, 7),
        amount: 200000,
        dealsCount: 28,
        winsCount: 12,
      },
      {
        period: new Date().toISOString().slice(0, 7),
        amount: 175000,
        dealsCount: 26,
        winsCount: 9,
      },
    ];

    return mockData.filter(
      d => d.period >= startDate.toISOString().slice(0, 7) &&
            d.period <= endDate.toISOString().slice(0, 7),
    );
  }

  async getStageWeights(tenantId: string): Promise<Map<string, number>> {
    // Mock stage weights - typically stored in configuration or tenant settings
    const weights = new Map<string, number>([
      ['lead', 0.05],
      ['qualification', 0.20],
      ['proposal', 0.50],
      ['negotiation', 0.75],
      ['closing', 0.90],
    ]);

    return weights;
  }
}

