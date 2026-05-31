import { ScoreRule } from '../../entities/score-rule.entity';
import { PaginationOptions } from './lead-score.repository.port';

export { PaginationOptions };

export interface ScoreRuleRepositoryPort {
  save(scoreRule: ScoreRule): Promise<ScoreRule>;
  findById(id: string): Promise<ScoreRule | null>;
  findByTenantId(tenantId: string, options?: PaginationOptions): Promise<ScoreRule[]>;
  findByScoreModelId(scoreModelId: string): Promise<ScoreRule[]>;
  findByCategory(tenantId: string, category: string): Promise<ScoreRule[]>;
  findActiveByScoreModelId(scoreModelId: string): Promise<ScoreRule[]>;
  delete(id: string): Promise<void>;
  deleteByScoreModelId(scoreModelId: string): Promise<void>;
  exists(id: string, tenantId: string): Promise<boolean>;
  count(tenantId: string): Promise<number>;
  countByScoreModelId(scoreModelId: string): Promise<number>;
}
