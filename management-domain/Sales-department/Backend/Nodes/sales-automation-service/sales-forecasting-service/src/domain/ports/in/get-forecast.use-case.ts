import { Forecast } from '../../entities/forecast.entity';
import { RequestContextData } from '../../../shared/request-context';

export interface GetForecastInput {
  forecastId: string;
  includeAccuracy?: boolean;
  includePeriods?: boolean;
}

export interface GetForecastUseCase {
  execute(
    context: RequestContextData,
    input: GetForecastInput,
  ): Promise<Forecast>;
}

