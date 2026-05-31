import { ForecastModelEntity } from '../../entities/forecast-model.entity';

export interface ForecastModelRepositoryPort {
  save(model: ForecastModelEntity): Promise<ForecastModelEntity>;
  findById(id: string, tenantId: string): Promise<ForecastModelEntity | null>;
  findByType(modelType: string, tenantId: string): Promise<ForecastModelEntity[]>;
  findActiveByTenant(tenantId: string): Promise<ForecastModelEntity[]>;
  findDefaultByTenant(tenantId: string): Promise<ForecastModelEntity | null>;
  update(model: ForecastModelEntity): Promise<ForecastModelEntity>;
  delete(id: string, tenantId: string): Promise<void>;
}
