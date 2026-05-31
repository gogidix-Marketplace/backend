import { ScoreAttribute } from '../../entities/score-attribute.entity';
import { PaginationOptions } from './lead-score.repository.port';

export { PaginationOptions };

export interface ScoreAttributeRepositoryPort {
  save(scoreAttribute: ScoreAttribute): Promise<ScoreAttribute>;
  findById(id: string): Promise<ScoreAttribute | null>;
  findByTenantId(tenantId: string, options?: PaginationOptions): Promise<ScoreAttribute[]>;
  findByType(tenantId: string, type: string): Promise<ScoreAttribute[]>;
  findActiveByTenantId(tenantId: string): Promise<ScoreAttribute[]>;
  delete(id: string): Promise<void>;
  exists(id: string, tenantId: string): Promise<boolean>;
  count(tenantId: string): Promise<number>;
}
