import { Forecast, ForecastDataPoint, ForecastBreakdown } from '../../entities/forecast.entity';
import { RequestContextData } from '../../../shared/request-context';

export interface GenerateForecastInput {
  name: string;
  description: string;
  model: string;
  period: string;
  granularity: string;
  granularityId?: string;
  currency: string;
  startDate: Date;
  endDate: Date;
  horizonMonths?: number;
  confidenceLevel?: number;
}

export interface GenerateForecastResult {
  forecast: Forecast;
  accuracy?: {
    overallAccuracy: number;
    mape: number;
    mae: number;
    rmse: number;
  };
}

export interface GenerateForecastUseCase {
  execute(
    context: RequestContextData,
    input: GenerateForecastInput,
  ): Promise<GenerateForecastResult>;
}

