import { Injectable, Logger } from '@nestjs/common';
import { EventPublisher } from '@domain/ports/output';
@Injectable()
export class KafkaEventPublisher implements EventPublisher {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  async publish(event: import('@domain/events').DomainEvent): Promise<void> { this.logger.log(`Event: ${event.eventType}`); }
  async publishAll(events: import('@domain/events').DomainEvent[]): Promise<void> { for (const e of events) await this.publish(e); }
}
