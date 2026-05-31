import { MongooseModule } from '@nestjs/mongoose';
import { Module } from '@nestjs/common';
import { SentimentAnalysisRepository, SentimentAlertRepository } from '@domain/ports/output';
import { MongoSentimentAnalysisRepository, MongoSentimentAlertRepository, SentimentAnalysisSchema, SentimentAlertSchema } from './sentiment.repository.impl';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: 'SentimentAnalysis', schema: SentimentAnalysisSchema },
      { name: 'SentimentAlert', schema: SentimentAlertSchema },
    ]),
  ],
  providers: [
    { provide: 'SentimentAnalysisRepository', useClass: MongoSentimentAnalysisRepository },
    { provide: 'SentimentAlertRepository', useClass: MongoSentimentAlertRepository },
  ],
  exports: ['SentimentAnalysisRepository', 'SentimentAlertRepository'],
})
export class PersistenceModule {}
