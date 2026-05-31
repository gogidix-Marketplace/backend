import { Module, Global } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigModule } from '@nestjs/config';
import { ScheduleModule } from '@nestjs/schedule';
import { CacheModule } from '@nestjs/cache-manager';

// Domain
import { Forecast } from './domain/entities/forecast.entity';
import { ForecastPeriod } from './domain/entities/forecast-period.entity';
import { ForecastModelEntity } from './domain/entities/forecast-model.entity';
import { ForecastAccuracy } from './domain/entities/forecast-accuracy.entity';

// Application
import { GenerateForecastUseCaseImpl } from './application/use-cases/generate-forecast.use-case.impl';
import { AdjustForecastUseCaseImpl } from './application/use-cases/adjust-forecast.use-case.impl';
import { GetForecastUseCaseImpl } from './application/use-cases/get-forecast.use-case.impl';
import { ListForecastsUseCaseImpl } from './application/use-cases/list-forecasts.use-case.impl';
import { CalculateAccuracyUseCaseImpl } from './application/use-cases/calculate-accuracy.use-case.impl';
import { TriggerRollingForecastUseCaseImpl } from './application/use-cases/trigger-rolling-forecast.use-case.impl';
import { ForecastCalculatorService } from './application/services/forecast-calculator.service';

// Infrastructure
import { ForecastRepositoryImpl } from './infrastructure/persistence/mongo/repositories/forecast.repository.impl';
import { ForecastAccuracyRepositoryImpl } from './infrastructure/persistence/mongo/repositories/forecast-accuracy.repository.impl';
import { KafkaEventPublisherImpl } from './infrastructure/messaging/kafka/event-publisher.impl';
import { CacheManagerImpl } from './infrastructure/cache/cache-manager.impl';
import { PipelineDataProviderMock } from './infrastructure/persistence/mongo/repositories/pipeline-data-provider.mock';
import { KafkaConfigService } from './infrastructure/config/kafka.config';
import { RollingForecastScheduler } from './infrastructure/scheduler/rolling-forecast.scheduler';

// Schemas
import { ForecastSchema, ForecastDocument } from './infrastructure/persistence/mongo/schemas/forecast.schema';
import { ForecastAccuracySchema, ForecastAccuracyDocument } from './infrastructure/persistence/mongo/schemas/forecast-accuracy.schema';
import { ForecastPeriodSchema, ForecastPeriodDocument } from './infrastructure/persistence/mongo/schemas/forecast-period.schema';
import { ForecastModelSchema, ForecastModelDocument } from './infrastructure/persistence/mongo/schemas/forecast-model.schema';

// Interfaces
import { ForecastController } from './interfaces/rest/controllers/forecast.controller';
import { ForecastMapper } from './interfaces/rest/mappers/forecast.mapper';

const repositories = [
  {
    provide: 'ForecastRepositoryPort',
    useClass: ForecastRepositoryImpl,
  },
  {
    provide: 'ForecastAccuracyRepositoryPort',
    useClass: ForecastAccuracyRepositoryImpl,
  },
  {
    provide: 'PipelineDataProviderPort',
    useClass: PipelineDataProviderMock,
  },
  {
    provide: 'EventPublisherPort',
    useClass: KafkaEventPublisherImpl,
  },
  {
    provide: 'CacheManagerPort',
    useClass: CacheManagerImpl,
  },
];

const useCases = [
  {
    provide: 'GenerateForecastUseCase',
    useClass: GenerateForecastUseCaseImpl,
  },
  {
    provide: 'AdjustForecastUseCase',
    useClass: AdjustForecastUseCaseImpl,
  },
  {
    provide: 'GetForecastUseCase',
    useClass: GetForecastUseCaseImpl,
  },
  {
    provide: 'ListForecastsUseCase',
    useClass: ListForecastsUseCaseImpl,
  },
  {
    provide: 'CalculateAccuracyUseCase',
    useClass: CalculateAccuracyUseCaseImpl,
  },
  {
    provide: 'TriggerRollingForecastUseCase',
    useClass: TriggerRollingForecastUseCaseImpl,
  },
];

@Global()
@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),
    MongooseModule.forFeature([
      { name: ForecastDocument.name, schema: ForecastSchema },
      { name: ForecastAccuracyDocument.name, schema: ForecastAccuracySchema },
      { name: ForecastPeriodDocument.name, schema: ForecastPeriodSchema },
      { name: ForecastModelDocument.name, schema: ForecastModelSchema },
    ]),
    ScheduleModule.forRoot(),
    CacheModule.register({
      isGlobal: true,
      ttl: 3600,
    }),
  ],
  controllers: [ForecastController],
  providers: [
    ...repositories,
    ...useCases,
    ForecastCalculatorService,
    ForecastMapper,
    KafkaConfigService,
    RollingForecastScheduler,
  ],
  exports: [...repositories, ...useCases, ForecastCalculatorService],
})
export class ForecastModule {}
