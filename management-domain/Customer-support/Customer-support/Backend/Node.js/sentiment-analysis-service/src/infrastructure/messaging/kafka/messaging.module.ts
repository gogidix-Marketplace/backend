import { Module } from '@nestjs/common';
import { EventPublisher } from '@domain/ports/output';
import { KafkaEventPublisher } from './kafka-event-publisher.impl';

@Module({
  providers: [{ provide: 'EventPublisher', useClass: KafkaEventPublisher }],
  exports: ['EventPublisher'],
})
export class MessagingModule {}
