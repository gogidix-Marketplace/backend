import { ScoreModel } from '../../entities/score-model.entity';
import { PaginationOptions } from './lead-score.repository.port';

export { PaginationOptions };

export interface ScoreModelRepositoryPort {
  save(scoreModel: ScoreModel): Promise<ScoreModel>;
  findById(id: string): Promise<ScoreModel | null>;
  findByTenantId(tenantId: string, options?: PaginationOptions): Promise<ScoreModel[]>;
  findActiveByTenantId(tenantId: string): Promise<ScoreModel[]>;
  findDefaultByTenantId(tenantId: string): Promise<ScoreModel | null>;
  findByVersion(tenantId: string, modelName: string, version: number): Promise<ScoreModel | null>;
  findVersionsByName(tenantId: string, modelName: string): Promise<ScoreModel[]>;
  delete(id: string): Promise<void>;
  exists(id: string, tenantId: string): Promise<boolean>;
  count(tenantId: string): Promise<number>;
}
