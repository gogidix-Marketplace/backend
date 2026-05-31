import { Module } from '@nestjs/common';
import { KafkaEventPublisher } from './event-publisher.adapter';

@Module({
  providers: [KafkaEventPublisher],
  exports: [KafkaEventPublisher],
})
export class KafkaModule {}
