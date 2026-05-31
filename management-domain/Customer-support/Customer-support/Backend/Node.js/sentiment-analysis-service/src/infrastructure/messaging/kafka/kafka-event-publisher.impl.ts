import { Injectable, Logger } from '@nestjs/common';
import { EventPublisher } from '@domain/ports/output';
import { DomainEvent } from '@domain/events';

@Injectable()
export class KafkaEventPublisher implements EventPublisher {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  async publish(event: DomainEvent): Promise<void> {
    this.logger.log(`Publishing event: ${event.eventType}`);
  }
  async publishAll(events: DomainEvent[]): Promise<void> {
    for (const event of events) await this.publish(event);
  }
}
