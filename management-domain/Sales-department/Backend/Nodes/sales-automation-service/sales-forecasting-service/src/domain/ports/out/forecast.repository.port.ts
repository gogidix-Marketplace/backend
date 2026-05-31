import { Forecast } from '../../entities/forecast.entity';

export interface ForecastFilter {
  tenantId: string;
  status?: string;
  model?: string;
  period?: string;
  granularity?: string;
  granularityId?: string;
  startDate?: Date;
  endDate?: Date;
}

export interface ForecastRepositoryPort {
  save(forecast: Forecast): Promise<Forecast>;
  findById(id: string, tenantId: string): Promise<Forecast | null>;
  findByTenantAndFilters(
    filters: ForecastFilter,
    page: number,
    limit: number,
    sortBy?: string,
    sortOrder?: 'ASC' | 'DESC',
  ): Promise<{ forecasts: Forecast[]; total: number }>;
  findActiveByTenant(tenantId: string): Promise<Forecast[]>;
  findLatestByGranularity(
    tenantId: string,
    granularity: string,
    granularityId?: string,
  ): Promise<Forecast | null>;
  update(forecast: Forecast): Promise<Forecast>;
  delete(id: string, tenantId: string): Promise<void>;
  existsByName(name: string, tenantId: string, excludeId?: string): Promise<boolean>;
}
