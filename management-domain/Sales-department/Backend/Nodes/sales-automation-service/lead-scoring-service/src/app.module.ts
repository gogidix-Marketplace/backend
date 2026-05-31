import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { ScheduleModule } from '@nestjs/schedule';
import { InterfaceModule } from './interfaces/interface.module';
import mongoConfig from './infrastructure/config/mongoose.config';
import kafkaConfig from './infrastructure/config/kafka.config';
import { KafkaEventPublisher } from './infrastructure/messaging/kafka/event-publisher.impl';
import { LeadScoreRepositoryImpl } from './infrastructure/persistence/repositories/lead-score.repository.impl';
import { ScoreModelRepositoryImpl } from './infrastructure/persistence/repositories/score-model.repository.impl';
import { ScoreRuleRepositoryImpl } from './infrastructure/persistence/repositories/score-rule.repository.impl';
import { ScoreAttributeRepositoryImpl } from './infrastructure/persistence/repositories/score-attribute.repository.impl';
import { LeadDataProviderImpl } from './infrastructure/messaging/kafka/lead-data-provider.impl';
import { RequestContext } from './infrastructure/security/request-context.service';
import { TenantGuard } from './infrastructure/security/guards/tenant.guard';
import { TenantInterceptor } from './infrastructure/security/interceptors/tenant.interceptor';
import { LeadScoringService } from './application/services/lead-scoring.service';
import { ScoreModelService } from './application/services/score-model.service';
import { ScoreRuleService } from './application/services/score-rule.service';
import { ScoreAttributeService } from './application/services/score-attribute.service';
import { ScoreLeadHandler } from './application/handlers/command-handlers/score-lead.handler';
import { BatchScoreLeadsHandler } from './application/handlers/command-handlers/batch-score-leads.handler';
import { CreateScoreModelHandler } from './application/handlers/command-handlers/create-score-model.handler';
import { UpdateScoreModelHandler } from './application/handlers/command-handlers/update-score-model.handler';
import { ActivateScoreModelHandler, DeactivateScoreModelHandler } from './application/handlers/command-handlers/activate-score-model.handler';
import { CreateScoreRuleHandler } from './application/handlers/command-handlers/create-score-rule.handler';
import { CreateScoreAttributeHandler } from './application/handlers/command-handlers/create-score-attribute.handler';
import { ApplyScoreDecayHandler, RescoreLeadHandler } from './application/handlers/command-handlers/apply-score-decay.handler';
import { GetLeadScoreHandler, GetLeadScoresByTenantHandler } from './application/handlers/query-handlers/get-lead-score.handler';
import { GetQualifiedLeadsHandler, GetScoreStatisticsHandler } from './application/handlers/query-handlers/get-qualified-leads.handler';
import { GetScoreModelHandler, GetScoreModelsByTenantHandler } from './application/handlers/query-handlers/get-score-model.handler';
import { GetScoreRulesHandler, GetScoreAttributesHandler } from './application/handlers/query-handlers/get-score-rules.handler';
import { GetScoreTrendsHandler, GetScoreHistoryHandler } from './application/handlers/query-handlers/get-score-trends.handler';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: ['.env.local', '.env'],
      load: [mongoConfig, kafkaConfig],
    }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (configService: ConfigService) => ({
        uri: configService.get<string>('MONGODB_URI', 'mongodb://localhost:27017/lead_scoring_db'),
      }),
      inject: [ConfigService],
    }),
    ClientsModule.register([
      {
        name: 'KAFKA_SERVICE',
        transport: Transport.KAFKA,
        options: {
          client: {
            clientId: process.env.KAFKA_CLIENT_ID || 'lead-scoring-service',
            brokers: (process.env.KAFKA_BROKERS || 'localhost:9092').split(','),
          },
          consumer: {
            groupId: process.env.KAFKA_CONSUMER_GROUP_ID || 'lead-scoring-consumer',
          },
        },
      },
    ]),
    ScheduleModule.forRoot(),
    InterfaceModule,
  ],
  providers: [
    KafkaEventPublisher,
    LeadDataProviderImpl,
    LeadScoreRepositoryImpl,
    ScoreModelRepositoryImpl,
    ScoreRuleRepositoryImpl,
    ScoreAttributeRepositoryImpl,
    RequestContext,
    TenantGuard,
    TenantInterceptor,
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
export class AppModule {}
