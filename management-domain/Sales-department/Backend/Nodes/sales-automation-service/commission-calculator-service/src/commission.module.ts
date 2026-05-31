import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { ClientsModule } from '@nestjs/microservices';
import { CommissionController } from './interface/controllers/commission.controller';
import { CommissionRuleController } from './interface/controllers/commission-rule.controller';
import { CommissionPeriodController } from './interface/controllers/commission-period.controller';
import { HealthController } from './interface/controllers/health.controller';
import { CommissionService } from './application/services/commission.service';
import { CommissionRuleService } from './application/services/commission-rule.service';
import { CommissionPeriodService } from './application/services/commission-period.service';
import { MongoModule } from './infrastructure/mongodb/mongodb.module';
import { KafkaModule } from './infrastructure/messaging/kafka.module';
import { CommissionRepository } from './infrastructure/mongodb/repositories/commission.repository';
import { CommissionRuleRepository } from './infrastructure/mongodb/repositories/commission-rule.repository';
import { CommissionPeriodRepository } from './infrastructure/mongodb/repositories/commission-period.repository';
import { KafkaEventPublisher } from './infrastructure/messaging/kafka-event-publisher.service';

/**
 * Main Commission Calculator Module
 * Contains all the application's modules and providers
 */
@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),
    MongoModule,
    KafkaModule,
  ],
  controllers: [
    CommissionController,
    CommissionRuleController,
    CommissionPeriodController,
    HealthController,
  ],
  providers: [
    // Application Services
    CommissionService,
    CommissionRuleService,
    CommissionPeriodService,

    // Infrastructure
    KafkaEventPublisher,
  ],
  exports: [
    CommissionService,
    CommissionRuleService,
    CommissionPeriodService,
  ],
})
export class CommissionModule {}
