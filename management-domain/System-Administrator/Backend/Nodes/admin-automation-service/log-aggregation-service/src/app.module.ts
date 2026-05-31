import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ScheduleModule } from '@nestjs/schedule';
import { CqrsModule } from '@nestjs/cqrs';

import { LogSourceSchema } from './infrastructure/persistence/mongodb/mongoose/log-source.schema';
import { LogAlertRuleSchema } from './infrastructure/persistence/mongodb/mongoose/log-alert-rule.schema';

import { LogSourceRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/log-source.repository.impl';
import { LogAlertRuleRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/log-alert-rule.repository.impl';

import { ElasticsearchService } from './infrastructure/search/elasticsearch.service';
import { KafkaEventPublisher } from './infrastructure/messaging/kafka-event-publisher.service';
import { LogStreamGateway } from './infrastructure/websocket/log-stream.gateway';

import { LogSearchQueryService } from './application/services/log-search-query.service';
import { LogSourceCommandService } from './application/services/log-source-command.service';
import { LogSourceQueryService } from './application/services/log-source-query.service';
import { LogIngestionService } from './application/services/log-ingestion.service';
import { LogRetentionService } from './application/services/log-retention.service';

import { LogController } from './interfaces/http/log.controller';
import { LogSourceController } from './interfaces/http/log-source.controller';

import { LoggerModule } from './infrastructure/config/logger.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, envFilePath: ['.env.local', '.env'] }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (cs: ConfigService) => ({
        uri: cs.get<string>('MONGODB_URI') || 'mongodb://localhost:27017/log-aggregation-service',
      }),
      inject: [ConfigService],
    }),
    MongooseModule.forFeature([
      { name: 'LogSource', schema: LogSourceSchema },
      { name: 'LogAlertRule', schema: LogAlertRuleSchema },
    ]),
    ScheduleModule.forRoot(),
    CqrsModule,
    LoggerModule,
  ],
  controllers: [LogController, LogSourceController],
  providers: [
    LogSearchQueryService, LogSourceCommandService, LogSourceQueryService,
    LogIngestionService, LogRetentionService,
    LogSourceRepositoryImpl, LogAlertRuleRepositoryImpl,
    ElasticsearchService, KafkaEventPublisher, LogStreamGateway,
  ],
  exports: [
    LogSearchQueryService, LogSourceCommandService, LogSourceQueryService,
    LogIngestionService, LogSourceRepositoryImpl, ElasticsearchService,
    KafkaEventPublisher,
  ],
})
export class AppModule {}
