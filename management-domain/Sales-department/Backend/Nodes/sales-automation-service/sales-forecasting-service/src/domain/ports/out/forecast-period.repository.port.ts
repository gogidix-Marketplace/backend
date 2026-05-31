import { ForecastPeriod } from '../../entities/forecast-period.entity';

export interface ForecastPeriodRepositoryPort {
  save(period: ForecastPeriod): Promise<ForecastPeriod>;
  findById(id: string, tenantId: string): Promise<ForecastPeriod | null>;
  findByForecastId(forecastId: string, tenantId: string): Promise<ForecastPeriod[]>;
  findActiveByDateRange(
    tenantId: string,
    startDate: Date,
    endDate: Date,
  ): Promise<ForecastPeriod[]>;
  update(period: ForecastPeriod): Promise<ForecastPeriod>;
  delete(id: string, tenantId: string): Promise<void>;
  deleteByForecastId(forecastId: string, tenantId: string): Promise<void>;
}
