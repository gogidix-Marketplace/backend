import { Injectable } from '@nestjs/common';
import { IQueryHandler } from './interfaces/query-handler.interface';
import { GetScoreRulesQuery, GetScoreAttributesQuery } from '../../queries/get-score-rules.query';
import { ScoreRule } from '../../../domain/entities/score-rule.entity';
import { ScoreAttribute } from '../../../domain/entities/score-attribute.entity';
import { ScoreRuleRepositoryPort } from '../../../domain/ports/in/score-rule.repository.port';
import { ScoreAttributeRepositoryPort } from '../../../domain/ports/in/score-attribute.repository.port';

@Injectable()
export class GetScoreRulesHandler implements IQueryHandler<GetScoreRulesQuery, ScoreRule[]> {
  constructor(
    private readonly scoreRuleRepository: ScoreRuleRepositoryPort,
  ) {}

  async execute(query: GetScoreRulesQuery): Promise<ScoreRule[]> {
    let rules = await this.scoreRuleRepository.findByScoreModelId(query.scoreModelId);

    if (query.isActive !== undefined) {
      rules = rules.filter(r => r.isActive === query.isActive);
    }

    return rules;
  }
}

@Injectable()
export class GetScoreAttributesHandler implements IQueryHandler<GetScoreAttributesQuery, ScoreAttribute[]> {
  constructor(
    private readonly scoreAttributeRepository: ScoreAttributeRepositoryPort,
  ) {}

  async execute(query: GetScoreAttributesQuery): Promise<ScoreAttribute[]> {
    let attributes = await this.scoreAttributeRepository.findByTenantId(query.tenantId);

    if (query.type) {
      attributes = attributes.filter(a => a.type === query.type);
    }

    if (query.isActive !== undefined) {
      attributes = attributes.filter(a => a.isActive === query.isActive);
    }

    return attributes;
  }
}
