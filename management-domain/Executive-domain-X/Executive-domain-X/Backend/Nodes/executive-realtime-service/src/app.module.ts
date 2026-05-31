import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { ConnectionRepositoryImpl } from './infrastructure/persistence/memory/connection.repository.impl';
import { RedisPubSubService } from './infrastructure/messaging/redis/redis-pubsub.service';
import { KafkaService } from './infrastructure/messaging/kafka/kafka.service';
import { ConnectionManagerService } from './application/services/connection-manager.service';
import { StreamHandlerService } from './application/services/stream-handler.service';
import { SubscriptionService } from './application/services/subscription.service';
import { StreamController } from './interfaces/http/stream.controller';
import { LoggerModule } from './infrastructure/config/logger.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, envFilePath: ['.env.local', '.env'] }),
    LoggerModule,
  ],
  controllers: [StreamController],
  providers: [
    ConnectionManagerService,
    StreamHandlerService,
    SubscriptionService,
    KafkaService,
    { provide: 'IConnectionRepository', useClass: ConnectionRepositoryImpl },
    { provide: 'IPubSubService', useClass: RedisPubSubService },
    ConnectionRepositoryImpl,
    RedisPubSubService,
  ],
  exports: [ConnectionManagerService, StreamHandlerService, KafkaService],
})
export class AppModule {}
