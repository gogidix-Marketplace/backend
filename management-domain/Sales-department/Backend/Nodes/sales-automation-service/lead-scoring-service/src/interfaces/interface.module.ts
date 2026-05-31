import { Module } from '@nestjs/common';
import { LeadScoreController } from './rest/lead-score.controller';
import { ScoreAttributeController } from './rest/score-attribute.controller';
import { ScoreModelController } from './rest/score-model.controller';
import { ScoreRuleController } from './rest/score-rule.controller';

@Module({
  controllers: [
    LeadScoreController,
    ScoreAttributeController,
    ScoreModelController,
    ScoreRuleController,
  ],
})
export class InterfaceModule {}
