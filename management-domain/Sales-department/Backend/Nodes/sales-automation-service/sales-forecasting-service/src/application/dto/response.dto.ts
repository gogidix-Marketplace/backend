import { ForecastDataPoint } from '../../domain/entities/forecast.entity';
import { ForecastModel, ForecastPeriod, ForecastStatus, ForecastGranularity } from '../../shared/constants';

export class ForecastDataPointResponseDto {
  period: string;
  amount: number;
  confidence: number;
  weightedAmount: number;
  bestCase: number;
  worstCase: number;
  dealsCount: number;
}

export class ForecastBreakdownResponseDto {
  byStage: Record<string, number>;
  byProduct: Record<string, number>;
  byRep: Record<string, number>;
  byTerritory: Record<string, number>;
}

export class ForecastResponseDto {
  id: string;
  tenantId: string;
  name: string;
  description: string;
  model: ForecastModel;
  period: ForecastPeriod;
  status: ForecastStatus;
  granularity: ForecastGranularity;
  granularityId?: string;
  dataPoints: ForecastDataPointResponseDto[];
  breakdown: ForecastBreakdownResponseDto;
  totalForecast: number;
  totalWeightedForecast: number;
  currency: string;
  startDate: Date;
  endDate: Date;
  generatedAt: Date;
  generatedBy: string;
  lastUpdated: Date;
  lastUpdatedBy: string;
  version: number;
  previousForecastId?: string;
  confidenceLevel: number;
  metadata: Record<string, any>;
}

export class GenerateForecastResponseDto {
  forecast: ForecastResponseDto;
  accuracy?: {
    overallAccuracy: number;
    mape: number;
    mae: number;
    rmse: number;
  };
}

export class AdjustForecastResponseDto {
  forecast: ForecastResponseDto;
  previousTotal: number;
  newTotal: number;
  previousWeighted: number;
  newWeighted: number;
}

export class ListForecastsResponseDto {
  forecasts: ForecastResponseDto[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}

export class ForecastAccuracyResponseDto {
  id: string;
  forecastId: string;
  modelUsed: string;
  metrics: Array<{
    metric: string;
    value: number;
    description?: string;
  }>;
  overallAccuracy: number;
  sampleSize: number;
  comparisonStartDate: Date;
  comparisonEndDate: Date;
  calculatedAt: Date;
  trend: 'IMPROVING' | 'DECLINING' | 'STABLE';
  previousAccuracy?: number;
  notes?: string;
}

export class CalculateAccuracyResponseDto {
  accuracy: ForecastAccuracyResponseDto;
  alertThreshold?: {
    threshold: number;
    currentAccuracy: number;
    requiresAttention: boolean;
  };
}

