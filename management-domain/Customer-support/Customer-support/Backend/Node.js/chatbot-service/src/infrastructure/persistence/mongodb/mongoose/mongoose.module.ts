import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigService } from '@nestjs/config';
import { ChatSessionSchema } from './chat-session.schema';
import { IntentSchema } from './intent.schema';
import { SessionAnalyticsSchema } from './session-analytics.schema';
import { DailyAnalyticsSchema } from './daily-analytics.schema';

@Module({
  imports: [
    MongooseModule.forRootAsync({
      useFactory: (configService: ConfigService) => ({
        uri: configService.get<string>('config.mongoUri'),
        dbName: configService.get<string>('config.mongoDbName'),
      }),
      inject: [ConfigService],
    }),
    MongooseModule.forFeature([
      { name: 'ChatSession', schema: ChatSessionSchema },
      { name: 'Intent', schema: IntentSchema },
      { name: 'SessionAnalytics', schema: SessionAnalyticsSchema },
      { name: 'DailyAnalytics', schema: DailyAnalyticsSchema },
    ]),
  ],
  exports: [MongooseModule],
})
export class MongooseConfigModule {}
