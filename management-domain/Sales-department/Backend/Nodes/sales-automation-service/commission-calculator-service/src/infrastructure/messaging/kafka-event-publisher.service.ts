import { Injectable, Logger } from '@nestjs/common';
import { ClientKafka } from '@nestjs/microservices';
import {
  CommissionCalculatedEvent,
  PayoutApprovedEvent,
  PayoutPaidEvent,
  CommissionClawbackEvent,
  CommissionPeriodClosedEvent,
} from '../../domain/events';
import { EventPublisherPort, DomainEvent } from '../../domain/ports/out/event-publisher.port';

/**
 * Kafka implementation of Event Publisher
 * Publishes domain events to Kafka topics
 */
@Injectable()
export class KafkaEventPublisher implements EventPublisherPort {
  private readonly logger = new Logger(KafkaEventPublisher.name);

  constructor(
    private readonly clientKafka: ClientKafka,
  ) {}

  async publishCommissionCalculated(event: CommissionCalculatedEvent): Promise<void> {
    await this.publish('commission.calculated', event.toObject());
  }

  async publishPayoutApproved(event: PayoutApprovedEvent): Promise<void> {
    await this.publish('payout.approved', event.toObject());
  }

  async publishPayoutPaid(event: PayoutPaidEvent): Promise<void> {
    await this.publish('payout.paid', event.toObject());
  }

  async publishCommissionClawback(event: CommissionClawbackEvent): Promise<void> {
    await this.publish('commission.clawback', event.toObject());
  }

  async publishCommissionPeriodClosed(event: CommissionPeriodClosedEvent): Promise<void> {
    await this.publish('commission.period.closed', event.toObject());
  }

  async publishBatch(events: DomainEvent[]): Promise<void> {
    const promises = events.map(event => {
      if (event instanceof CommissionCalculatedEvent) {
        return this.publishCommissionCalculated(event);
      } else if (event instanceof PayoutApprovedEvent) {
        return this.publishPayoutApproved(event);
      } else if (event instanceof PayoutPaidEvent) {
        return this.publishPayoutPaid(event);
      } else if (event instanceof CommissionClawbackEvent) {
        return this.publishCommissionClawback(event);
      } else if (event instanceof CommissionPeriodClosedEvent) {
        return this.publishCommissionPeriodClosed(event);
      }
      return Promise.resolve();
    });

    await Promise.allSettled(promises);
  }

  private async publish(topic: string, payload: Record<string, unknown>): Promise<void> {
    try {
      this.logger.debug(`Publishing event to topic ${topic}`, { payload });
      this.clientKafka.emit(topic, JSON.stringify(payload)).subscribe({
        next: () => this.logger.debug(`Event published successfully to ${topic}`),
        error: (err) => this.logger.error(`Failed to publish event to ${topic}`, err),
      });
    } catch (error) {
      this.logger.error(`Error publishing event to topic ${topic}`, error);
      throw error;
    }
  }
}
