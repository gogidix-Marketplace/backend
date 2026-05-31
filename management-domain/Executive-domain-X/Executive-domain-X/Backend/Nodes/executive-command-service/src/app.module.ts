import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';

import { KpiSchema } from './infrastructure/persistence/mongodb/mongoose/kpi.schema';

import { KpiRepository } from './infrastructure/persistence/mongodb/repositories/kpi.repository.impl';
import { RedisEventPublisherService } from './infrastructure/messaging/redis/redis-event-publisher.service';
import { RedisCacheService } from './infrastructure/messaging/redis/redis-cache.service';

import { KpiCommandService } from './application/services/kpi-command.service';

import { KpiController } from './interfaces/http/kpi.controller';

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
        uri: configService.get<string>('MONGODB_URI') || 'mongodb://localhost:27017/executive-command',
      }),
      inject: [ConfigService],
    }),
    MongooseModule.forFeature([
      { name: 'Kpi', schema: KpiSchema },
    ]),
    LoggerModule,
  ],
  controllers: [KpiController],
  providers: [
    KpiCommandService,
    { provide: 'IKpiRepository', useClass: KpiRepository },
    { provide: 'IEventPublisher', useClass: RedisEventPublisherService },
    { provide: 'ICacheService', useClass: RedisCacheService },
    KpiRepository,
    RedisEventPublisherService,
    RedisCacheService,
  ],
  exports: [
    KpiCommandService,
    KpiRepository,
    RedisEventPublisherService,
    RedisCacheService,
  ],
})
export class AppModule {}
