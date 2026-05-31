import { Module } from '@nestjs/common';
import { InfrastructureModule } from '../infrastructure/infrastructure.module';
import { LeadScoringService } from './services/lead-scoring.service';
import { ScoreModelService } from './services/score-model.service';
import { ScoreRuleService } from './services/score-rule.service';
import { ScoreAttributeService } from './services/score-attribute.service';
import { ScoreLeadHandler } from './handlers/command-handlers/score-lead.handler';
import { BatchScoreLeadsHandler } from './handlers/command-handlers/batch-score-leads.handler';
import { CreateScoreModelHandler } from './handlers/command-handlers/create-score-model.handler';
import { UpdateScoreModelHandler } from './handlers/command-handlers/update-score-model.handler';
import { ActivateScoreModelHandler, DeactivateScoreModelHandler } from './handlers/command-handlers/activate-score-model.handler';
import { CreateScoreRuleHandler } from './handlers/command-handlers/create-score-rule.handler';
import { CreateScoreAttributeHandler } from './handlers/command-handlers/create-score-attribute.handler';
import { ApplyScoreDecayHandler, RescoreLeadHandler } from './handlers/command-handlers/apply-score-decay.handler';
import { GetLeadScoreHandler, GetLeadScoresByTenantHandler } from './handlers/query-handlers/get-lead-score.handler';
import { GetQualifiedLeadsHandler, GetScoreStatisticsHandler } from './handlers/query-handlers/get-qualified-leads.handler';
import { GetScoreModelHandler, GetScoreModelsByTenantHandler } from './handlers/query-handlers/get-score-model.handler';
import { GetScoreRulesHandler, GetScoreAttributesHandler } from './handlers/query-handlers/get-score-rules.handler';
import { GetScoreTrendsHandler, GetScoreHistoryHandler } from './handlers/query-handlers/get-score-trends.handler';

@Module({
  imports: [InfrastructureModule],
  providers: [
    // Services
    LeadScoringService,
    ScoreModelService,
    ScoreRuleService,
    ScoreAttributeService,

    // Command Handlers
    ScoreLeadHandler,
    BatchScoreLeadsHandler,
    CreateScoreModelHandler,
    UpdateScoreModelHandler,
    ActivateScoreModelHandler,
    DeactivateScoreModelHandler,
    CreateScoreRuleHandler,
    CreateScoreAttributeHandler,
    ApplyScoreDecayHandler,
    RescoreLeadHandler,

    // Query Handlers
    GetLeadScoreHandler,
    GetLeadScoresByTenantHandler,
    GetQualifiedLeadsHandler,
    GetScoreStatisticsHandler,
    GetScoreModelHandler,
    GetScoreModelsByTenantHandler,
    GetScoreRulesHandler,
    GetScoreAttributesHandler,
    GetScoreTrendsHandler,
    GetScoreHistoryHandler,
  ],
  exports: [
    LeadScoringService,
    ScoreModelService,
    ScoreRuleService,
    ScoreAttributeService,
    ScoreLeadHandler,
    BatchScoreLeadsHandler,
    CreateScoreModelHandler,
    UpdateScoreModelHandler,
    ActivateScoreModelHandler,
    DeactivateScoreModelHandler,
    CreateScoreRuleHandler,
    CreateScoreAttributeHandler,
    ApplyScoreDecayHandler,
    RescoreLeadHandler,
    GetLeadScoreHandler,
    GetLeadScoresByTenantHandler,
    GetQualifiedLeadsHandler,
    GetScoreStatisticsHandler,
    GetScoreModelHandler,
    GetScoreModelsByTenantHandler,
    GetScoreRulesHandler,
    GetScoreAttributesHandler,
    GetScoreTrendsHandler,
    GetScoreHistoryHandler,
  ],
})
export class ApplicationModule {}
