import { ForecastAccuracy } from '../../entities/forecast-accuracy.entity';

export interface ForecastAccuracyRepositoryPort {
  save(accuracy: ForecastAccuracy): Promise<ForecastAccuracy>;
  findById(id: string, tenantId: string): Promise<ForecastAccuracy | null>;
  findByForecastId(forecastId: string, tenantId: string): Promise<ForecastAccuracy[]>;
  findLatestByForecastId(forecastId: string, tenantId: string): Promise<ForecastAccuracy | null>;
  findByForecastAndPeriod(
    forecastId: string,
    periodId: string,
    tenantId: string,
  ): Promise<ForecastAccuracy | null>;
  findBelowThreshold(tenantId: string, threshold: number): Promise<ForecastAccuracy[]>;
  update(accuracy: ForecastAccuracy): Promise<ForecastAccuracy>;
  delete(id: string, tenantId: string): Promise<void>;
}
