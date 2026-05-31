import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ScheduleModule } from '@nestjs/schedule';

import { ReconciliationSchema } from './infrastructure/persistence/mongodb/mongoose/reconciliation.schema';
import { ReconciliationRuleSchema } from './infrastructure/persistence/mongodb/mongoose/reconciliation-rule.schema';
import { ReconciliationMatchSchema } from './infrastructure/persistence/mongodb/mongoose/reconciliation-match.schema';
import { TransactionDifferenceSchema } from './infrastructure/persistence/mongodb/mongoose/transaction-difference.schema';
import { AuditLogSchema } from './infrastructure/persistence/mongodb/mongoose/audit-log.schema';

import { ReconciliationRepository } from './infrastructure/persistence/mongodb/repositories/reconciliation.repository.impl';
import { ReconciliationRuleRepository } from './infrastructure/persistence/mongodb/repositories/reconciliation-rule.repository.impl';
import { ReconciliationMatchRepository } from './infrastructure/persistence/mongodb/repositories/reconciliation-match.repository.impl';
import { TransactionDifferenceRepository } from './infrastructure/persistence/mongodb/repositories/transaction-difference.repository.impl';
import { AuditLogRepository } from './infrastructure/persistence/mongodb/repositories/audit-log.repository.impl';

import { KafkaEventPublisher } from './infrastructure/messaging/kafka/kafka-event-publisher.service';
import { KafkaConsumerService } from './infrastructure/messaging/kafka/kafka-consumer.service';

import { BankApiDataSource } from './infrastructure/datasources/bank-api.datasource';
import { InternalApiDataSource } from './infrastructure/datasources/internal-api.datasource';

import { ReconciliationCommandService } from './application/services/reconciliation-command.service';
import { ReconciliationQueryService } from './application/services/reconciliation-query.service';
import { AutoMatchService } from './application/services/auto-match.service';
import { DifferenceResolutionService } from './application/services/difference-resolution.service';

import { ReconciliationController } from './interfaces/http/reconciliation.controller';
import { MatchController } from './interfaces/http/match.controller';
import { RuleController } from './interfaces/http/rule.controller';
import { DifferencesController } from './interfaces/http/differences.controller';

import { SchedulerService } from './infrastructure/config/scheduler.service';
import { LoggerModule } from './infrastructure/config/logger.module';

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: ['.env.local', '.env'],
    }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: async (configService: ConfigService) => ({
        uri: configService.get<string>('MONGODB_URI') || 'mongodb://localhost:27017/reconciliation-automation',
      }),
      inject: [ConfigService],
    }),
    MongooseModule.forFeature([
      { name: 'Reconciliation', schema: ReconciliationSchema },
      { name: 'ReconciliationRule', schema: ReconciliationRuleSchema },
      { name: 'ReconciliationMatch', schema: ReconciliationMatchSchema },
      { name: 'TransactionDifference', schema: TransactionDifferenceSchema },
      { name: 'AuditLog', schema: AuditLogSchema },
    ]),
    ScheduleModule.forRoot(),
    LoggerModule,
  ],
  controllers: [ReconciliationController, MatchController, RuleController, DifferencesController],
  providers: [
    ReconciliationCommandService,
    ReconciliationQueryService,
    AutoMatchService,
    DifferenceResolutionService,
    SchedulerService,
    ReconciliationRepository,
    ReconciliationRuleRepository,
    ReconciliationMatchRepository,
    TransactionDifferenceRepository,
    AuditLogRepository,
    KafkaEventPublisher,
    KafkaConsumerService,
    BankApiDataSource,
    InternalApiDataSource,
  ],
  exports: [
    ReconciliationCommandService,
    ReconciliationQueryService,
    AutoMatchService,
    DifferenceResolutionService,
    ReconciliationRepository,
    ReconciliationRuleRepository,
    ReconciliationMatchRepository,
    TransactionDifferenceRepository,
    AuditLogRepository,
    KafkaEventPublisher,
    BankApiDataSource,
    InternalApiDataSource,
  ],
})
export class AppModule {}
