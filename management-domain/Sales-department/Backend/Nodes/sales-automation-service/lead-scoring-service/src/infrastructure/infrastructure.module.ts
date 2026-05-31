import { Module, Global } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { KafkaEventPublisher } from './messaging/kafka/event-publisher.impl';
import { LeadDataProviderImpl } from './messaging/kafka/lead-data-provider.impl';
import { LeadScoreRepositoryImpl } from './persistence/repositories/lead-score.repository.impl';
import { ScoreModelRepositoryImpl } from './persistence/repositories/score-model.repository.impl';
import { ScoreRuleRepositoryImpl } from './persistence/repositories/score-rule.repository.impl';
import { ScoreAttributeRepositoryImpl } from './persistence/repositories/score-attribute.repository.impl';
import { RequestContext } from './security/request-context.service';
import { TenantGuard } from './security/guards/tenant.guard';
import { TenantInterceptor } from './security/interceptors/tenant.interceptor';
import { ScoreDecayScheduler } from './scheduling/score-decay.scheduler';

@Global()
@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true }),
    MongooseModule.forRootAsync({
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
    ScoreDecayScheduler,
  ],
  exports: [
    KafkaEventPublisher,
    LeadDataProviderImpl,
    LeadScoreRepositoryImpl,
    ScoreModelRepositoryImpl,
    ScoreRuleRepositoryImpl,
    ScoreAttributeRepositoryImpl,
    RequestContext,
    TenantGuard,
    TenantInterceptor,
  ],
})
export class InfrastructureModule {}
