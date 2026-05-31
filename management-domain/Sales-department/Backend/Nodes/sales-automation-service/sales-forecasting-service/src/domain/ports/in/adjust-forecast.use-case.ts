import { Forecast } from '../../entities/forecast.entity';
import { RequestContextData } from '../../../shared/request-context';

export interface AdjustForecastInput {
  forecastId: string;
  adjustmentFactor: number;
  reason: string;
  adjustmentType?: 'MANUAL' | 'AUTOMATIC';
}

export interface AdjustForecastResult {
  forecast: Forecast;
  previousTotal: number;
  newTotal: number;
  previousWeighted: number;
  newWeighted: number;
}

export interface AdjustForecastUseCase {
  execute(
    context: RequestContextData,
    input: AdjustForecastInput,
  ): Promise<AdjustForecastResult>;
}

