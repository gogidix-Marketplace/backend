import { Forecast } from '../../entities/forecast.entity';
import { RequestContextData } from '../../../shared/request-context';

export interface ListForecastsInput {
  status?: string;
  model?: string;
  period?: string;
  granularity?: string;
  granularityId?: string;
  startDate?: Date;
  endDate?: Date;
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'ASC' | 'DESC';
}

export interface ListForecastsResult {
  forecasts: Forecast[];
  total: number;
  page: number;
  limit: number;
  totalPages: number;
}

export interface ListForecastsUseCase {
  execute(
    context: RequestContextData,
    input: ListForecastsInput,
  ): Promise<ListForecastsResult>;
}

