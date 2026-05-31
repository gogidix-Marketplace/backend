import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { KpiSchema } from './infrastructure/persistence/mongodb/mongoose/kpi.schema';
import { KpiRepository } from './infrastructure/persistence/mongodb/repositories/kpi.repository.impl';
import { RedisCacheService } from './infrastructure/messaging/redis/redis-cache.service';
import { KpiQueryService } from './application/services/kpi-query.service';
import { KpiController } from './interfaces/http/kpi.controller';
import { LoggerModule } from './infrastructure/config/logger.module';


@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, envFilePath: ['.env.local', '.env'] }),
    MongooseModule.forRootAsync({ imports: [ConfigModule], useFactory: async (cs: ConfigService) => ({ uri: cs.get<string>('MONGODB_URI') || 'mongodb://localhost:27017/executive-query' }), inject: [ConfigService] }),
    MongooseModule.forFeature([{ name: 'Kpi', schema: KpiSchema }]),
    LoggerModule,
  ],
  controllers: [KpiController],
  providers: [
    KpiQueryService,
    { provide: 'IKpiRepository', useClass: KpiRepository },
    { provide: 'ICacheService', useClass: RedisCacheService },
    KpiRepository,
    RedisCacheService,
  ],
  exports: [KpiQueryService, KpiRepository, RedisCacheService],
})
export class AppModule {}
