import { Injectable, Inject } from '@nestjs/common';
import { LeadScore } from '../../domain/models/lead-score';
import { ILeadScoreRepository, LEAD_SCORE_REPOSITORY } from '../../domain/ports/repositories/lead-score.repository';
import { IScoringRuleRepository, SCORING_RULE_REPOSITORY } from '../../domain/ports/repositories/scoring-rule.repository';
import { ICacheService, CACHE_SERVICE_PORT } from '../../domain/ports/services/cache-service.port';
import { CalculateScoreDto } from '../dtos/scoring.dto';

@Injectable()
export class CalculateScoreUseCase {
  constructor(
    @Inject(LEAD_SCORE_REPOSITORY) private readonly scoreRepo: ILeadScoreRepository,
    @Inject(SCORING_RULE_REPOSITORY) private readonly ruleRepo: IScoringRuleRepository,
    @Inject(CACHE_SERVICE_PORT) private readonly cache: ICacheService,
  ) {}

  async execute(dto: CalculateScoreDto, tenantId: string): Promise<LeadScore> {
    const cacheKey = `lead-score:${tenantId}:${dto.leadId}`;
    let leadScore = await this.scoreRepo.findByLeadId(dto.leadId, tenantId);

    if (!leadScore) {
      leadScore = new LeadScore({ tenantId, leadId: dto.leadId, score: 0, previousScore: 0 });
    }

    const rules = await this.ruleRepo.findByTenantId(tenantId, { isActive: true });
    let totalPoints = leadScore.score;

    for (const rule of rules) {
      if (rule.evaluate(dto.eventData)) {
        totalPoints += rule.points;
        rule.markApplied();
        await this.ruleRepo.update(rule);
      }
    }

    leadScore.updateScore(totalPoints, `Calculated from ${rules.length} active rules`);
    const saved = await this.scoreRepo.update(leadScore);
    await this.cache.set(cacheKey, saved.toPlainObject(), 300);
    return saved;
  }
}
