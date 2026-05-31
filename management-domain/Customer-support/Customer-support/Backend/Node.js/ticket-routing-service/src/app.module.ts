import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { APP_INTERCEPTOR } from '@nestjs/core';
import configuration from '@infrastructure/config/configuration';
import { PersistenceModule } from '@infrastructure/persistence/mongodb';
import { MessagingModule } from '@infrastructure/messaging/kafka';
import { LoggerModule } from '@shared/logging';
import { LoggingInterceptor, TransformInterceptor } from '@shared/interceptors';
import { RoutingController } from '@interfaces/http/controllers/routing.controller';
import { AgentController } from '@interfaces/http/controllers/agent.controller';
import { QueueController } from '@interfaces/http/controllers/queue.controller';
import { HealthController } from '@interfaces/http/controllers/health.controller';
import { RoutingService } from '@application/services/routing.service';
import { AgentManagementService } from '@application/services/agent-management.service';
import { QueueManagementService } from '@application/services/queue-management.service';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, load: [configuration] }),
    MongooseModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (config: ConfigService) => ({
        uri: config.get<string>('config.mongoUri'),
        dbName: config.get<string>('config.mongoDbName'),
      }),
    }),
    LoggerModule,
    PersistenceModule,
    MessagingModule,
  ],
  controllers: [
    RoutingController,
    AgentController,
    QueueController,
    HealthController,
  ],
  providers: [
    RoutingService,
    AgentManagementService,
    QueueManagementService,
    { provide: APP_INTERCEPTOR, useClass: LoggingInterceptor },
    { provide: APP_INTERCEPTOR, useClass: TransformInterceptor },
  ],
})
export class AppModule {}
