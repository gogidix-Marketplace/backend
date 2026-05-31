import { Forecast } from '../../entities/forecast.entity';
import { RequestContextData } from '../../../shared/request-context';

export interface TriggerRollingForecastInput {
  granularity?: string;
  granularityId?: string;
  triggeredBy?: 'SCHEDULER' | 'MANUAL' | 'EVENT';
  reason?: string;
}

export interface TriggerRollingForecastResult {
  forecast: Forecast;
  previousForecastId?: string;
}

export interface TriggerRollingForecastUseCase {
  execute(
    context: RequestContextData,
    input: TriggerRollingForecastInput,
  ): Promise<TriggerRollingForecastResult>;
}

