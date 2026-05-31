import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { BullModule } from '@nestjs/bull';
import { ScheduleModule } from '@nestjs/schedule';
import { CqrsModule } from '@nestjs/cqrs';

import { PayrollSchema } from './infrastructure/persistence/mongodb/mongoose/payroll.schema';
import { EmployeePayrollSchema } from './infrastructure/persistence/mongodb/mongoose/employee-payroll.schema';

import { PayrollRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/payroll.repository.impl';
import { EmployeePayrollRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/employee-payroll.repository.impl';

import { KafkaEventPublisher } from './infrastructure/messaging/kafka/kafka-event-publisher.service';
import { KafkaConsumerService } from './infrastructure/messaging/kafka/kafka-consumer.service';
import { PayrollProcessorService } from './infrastructure/queue/payroll-processor.service';

import { PayrollCommandService } from './application/services/payroll-command.service';
import { PayrollQueryService } from './application/services/payroll-query.service';
import { PayrollCalculationService } from './application/services/payroll-calculation.service';

import { PayrollController } from './interfaces/http/payroll.controller';

import { LoggerModule } from './infrastructure/config/logger.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, envFilePath: ['.env.local', '.env'] }),
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (cs: ConfigService) => ({
        uri: cs.get<string>('MONGODB_URI') || 'mongodb://localhost:27017/payroll-automation',
      }),
      inject: [ConfigService],
    }),
    MongooseModule.forFeature([
      { name: 'Payroll', schema: PayrollSchema },
      { name: 'EmployeePayroll', schema: EmployeePayrollSchema },
    ]),
    BullModule.forRootAsync({
      imports: [ConfigModule],
      useFactory: (cs: ConfigService) => ({
        redis: {
          host: cs.get<string>('REDIS_HOST') || 'localhost',
          port: cs.get<number>('REDIS_PORT') || 6379,
          password: cs.get<string>('REDIS_PASSWORD') || undefined,
        },
      }),
      inject: [ConfigService],
    }),
    BullModule.registerQueue({ name: 'payroll-processing' }),
    ScheduleModule.forRoot(),
    CqrsModule,
    LoggerModule,
  ],
  controllers: [PayrollController],
  providers: [
    PayrollCommandService, PayrollQueryService, PayrollCalculationService,
    PayrollRepositoryImpl, EmployeePayrollRepositoryImpl,
    KafkaEventPublisher, KafkaConsumerService, PayrollProcessorService,
  ],
  exports: [
    PayrollCommandService, PayrollQueryService, PayrollCalculationService,
    PayrollRepositoryImpl, EmployeePayrollRepositoryImpl,
    KafkaEventPublisher,
  ],
})
export class AppModule {}
