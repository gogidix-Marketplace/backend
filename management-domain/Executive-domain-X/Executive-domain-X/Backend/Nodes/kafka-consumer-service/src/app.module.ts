import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { EventSchema } from './infrastructure/persistence/mongodb/mongoose/event.schema';
import { DeadLetterSchema } from './infrastructure/persistence/mongodb/mongoose/dead-letter.schema';
import { EventStoreRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/event-store.repository.impl';
import { DeadLetterQueueRepositoryImpl } from './infrastructure/persistence/mongodb/repositories/dead-letter-queue.repository.impl';
import { KafkaConsumerService } from './infrastructure/messaging/kafka/kafka-consumer.service';
import { EventProcessorService } from './application/services/event-processor.service';
import { RetryService } from './application/services/retry.service';
import { EventController } from './interfaces/http/event.controller';
import { LoggerModule } from './infrastructure/config/logger.module';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, envFilePath: ['.env.local', '.env'] }),
    MongooseModule.forRootAsync({ imports: [ConfigModule], useFactory: (cs: ConfigService) => ({ uri: cs.get('MONGODB_URI') || 'mongodb://localhost:27017/kafka-consumer' }), inject: [ConfigService] }),
    MongooseModule.forFeature([{ name: 'Event', schema: EventSchema }, { name: 'DeadLetter', schema: DeadLetterSchema }]),
    LoggerModule,
  ],
  controllers: [EventController],
  providers: [
    EventProcessorService, RetryService, KafkaConsumerService,
    { provide: 'IEventStoreRepository', useClass: EventStoreRepositoryImpl },
    { provide: 'IDeadLetterQueueRepository', useClass: DeadLetterQueueRepositoryImpl },
    EventStoreRepositoryImpl, DeadLetterQueueRepositoryImpl,
  ],
  exports: [EventProcessorService, RetryService],
})
export class AppModule {}
