import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { APP_INTERCEPTOR } from '@nestjs/core';
import configuration from '@infrastructure/config/configuration';
import { PersistenceModule } from '@infrastructure/persistence/mongodb';
import { MessagingModule } from '@infrastructure/messaging/kafka';
import { LoggerModule } from '@shared/logging';
import { LoggingInterceptor, TransformInterceptor } from '@shared/interceptors';
import { AgentController, TicketController, QueueController, RoutingController, HealthController } from '@interfaces/http/controllers';
import { RoutingService, AgentService } from '@application/services';
import { QueueService } from '@infrastructure/persistence/mongodb/queue.service';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, load: [configuration] }),
    LoggerModule,
    PersistenceModule,
    MessagingModule,
  ],
  controllers: [AgentController, TicketController, QueueController, RoutingController, HealthController],
  providers: [
    RoutingService, AgentService, QueueService,
    { provide: APP_INTERCEPTOR, useClass: LoggingInterceptor },
    { provide: APP_INTERCEPTOR, useClass: TransformInterceptor },
  ],
})
export class AppModule {}
