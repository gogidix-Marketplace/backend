import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { LeadScoreDocument, LeadScoreSchema } from '../schemas/lead-score.schema';
import { ScoringRuleDocument, ScoringRuleSchema } from '../schemas/scoring-rule.schema';
import { SegmentDocument, SegmentSchema } from '../schemas/segment.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: LeadScoreDocument.name, schema: LeadScoreSchema },
      { name: ScoringRuleDocument.name, schema: ScoringRuleSchema },
      { name: SegmentDocument.name, schema: SegmentSchema },
    ]),
  ],
  exports: [MongooseModule],
})
export class DatabaseModule {}
