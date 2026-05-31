import { ScoringRule } from '../../models/scoring-rule';
export const SCORING_RULE_REPOSITORY = Symbol('SCORING_RULE_REPOSITORY');
export interface IScoringRuleRepository {
  save(rule: ScoringRule): Promise<ScoringRule>;
  findById(id: string): Promise<ScoringRule | null>;
  findByTenantId(tenantId: string, options?: { category?: string; isActive?: boolean }): Promise<ScoringRule[]>;
  update(rule: ScoringRule): Promise<ScoringRule>;
  delete(id: string): Promise<boolean>;
}
