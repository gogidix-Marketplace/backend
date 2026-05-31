import { Module } from '@nestjs/common';
import { DatabaseModule } from './persistence/database.module';
import { CacheModule } from './cache/cache.module';
import { EventsModule } from './events/events.module';
import { LeadScoreRepositoryImpl } from './persistence/mongoose/lead-score.repository.impl';
import { ScoringRuleRepositoryImpl } from './persistence/mongoose/scoring-rule.repository.impl';
import { SegmentRepositoryImpl } from './persistence/mongoose/segment.repository.impl';
import { LEAD_SCORE_REPOSITORY } from '../domain/ports/repositories/lead-score.repository';
import { SCORING_RULE_REPOSITORY } from '../domain/ports/repositories/scoring-rule.repository';
import { SEGMENT_REPOSITORY } from '../domain/ports/repositories/segment.repository';

@Module({
  imports: [DatabaseModule, CacheModule, EventsModule],
  providers: [
    { provide: 'LEAD_SCORE_REPOSITORY' useClass: LeadScoreRepositoryImpl },
    { provide: 'SCORING_RULE_REPOSITORY' useClass: ScoringRuleRepositoryImpl },
    { provide: 'SEGMENT_REPOSITORY' useClass: SegmentRepositoryImpl },
  ],
  exports: [LEAD_SCORE_REPOSITORY, SCORING_RULE_REPOSITORY, SEGMENT_REPOSITORY, CacheModule, EventsModule],
})
export class InfrastructureModule {}
