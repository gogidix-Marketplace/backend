import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ScheduleModule } from '@nestjs/schedule';
import { CqrsModule } from '@nestjs/cqrs';

import { ScalingPolicySchema } from './infrastructure/persistence/mongodb/mongoose/scaling-policy.schema';
import { ScalingEventSchema } from './infrastructure/persistence/mongodb/mongoose/scaling-event.schema';
import { MetricsHistorySchema } from './infrastructure/persistence/mongodb/mongoose/metrics-history.schema';

import { ScalingPolicyRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/scaling-policy.repository.impl';
import { ScalingEventRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/scaling-event.repository.impl';
import { MetricsHistoryRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/metrics-history.repository.impl';

import { AWSAutoScalingService } from './infrastructure/cloud/aws-auto-scaling.service';
import { AzureAutoScalingService } from './infrastructure/cloud/azure-auto-scaling.service';
import { RedisCacheService } from './infrastructure/cache/redis-cache.service';
import { KafkaEventPublisher } from './infrastructure/messaging/kafka-event-publisher.service';
import { NotificationService } from './infrastructure/notification/notification.service';

import { ScalingPolicyCommandService } from './application/services/scaling-policy-command.service';
import { ScalingPolicyQueryService } from './application/services/scaling-policy-query.service';
import { ScalingEngineService } from './application/services/scaling-engine.service';

import { ScalingPolicyController } from './interfaces/http/scaling-policy.controller';
import { ScalingEventController } from './interfaces/http/scaling-event.controller';
import { MetricsController } from './interfaces/http/metrics.controller';

import { LoggerModule } from './infrastructure/config/logger.module';

const cloudProviders = {
  provide: 'CLOUD_PROVIDERS',
  useFactory: (aws: AWSAutoScalingService, azure: AzureAutoScalingService) => {
    const map = new Map<string, any>();
    map.set('aws', aws);
    map.set('azure', azure);
    return map;
  },
  inject: [AWSAutoScalingService, AzureAutoScalingService],
};

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, envFilePath: ['.env.local', '.env'] }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (cs: ConfigService) => ({
        uri: cs.get<string>('MONGODB_URI') || 'mongodb://localhost:27017/auto-scaling-service',
      }),
      inject: [ConfigService],
    }),
    MongooseModule.forFeature([
      { name: 'ScalingPolicy', schema: ScalingPolicySchema },
      { name: 'ScalingEvent', schema: ScalingEventSchema },
      { name: 'MetricsHistory', schema: MetricsHistorySchema },
    ]),
    ScheduleModule.forRoot(),
    CqrsModule,
    LoggerModule,
  ],
  controllers: [ScalingPolicyController, ScalingEventController, MetricsController],
  providers: [
    ScalingPolicyCommandService, ScalingPolicyQueryService, ScalingEngineService,
    ScalingPolicyRepositoryImpl, ScalingEventRepositoryImpl, MetricsHistoryRepositoryImpl,
    AWSAutoScalingService, AzureAutoScalingService,
    RedisCacheService, KafkaEventPublisher, NotificationService,
    cloudProviders,
  ],
  exports: [
    ScalingPolicyCommandService, ScalingPolicyQueryService,
    ScalingPolicyRepositoryImpl, ScalingEventRepositoryImpl,
    AWSAutoScalingService, AzureAutoScalingService, RedisCacheService,
    KafkaEventPublisher,
  ],
})
export class AppModule {}
