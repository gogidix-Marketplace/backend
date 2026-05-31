import { Injectable, Logger } from '@nestjs/common';
import { Kafka, Producer, ProducerRecord } from 'kafkajs';
import {
  IEventPublisher,
  ReconciliationStartedEvent,
  ReconciliationCompletedEvent,
  ReconciliationFailedEvent,
  MatchFoundEvent,
  DifferenceDetectedEvent,
  DifferenceResolvedEvent,
} from '../../../domain/ports/output/event-publisher.interface';

@Injectable()
export class KafkaEventPublisher implements IEventPublisher {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  private readonly producer: Producer;
  private readonly kafka: Kafka;

  constructor() {
    const brokers = process.env.KAFKA_BROKERS?.split(',') || ['localhost:9092'];
    const clientId = process.env.KAFKA_CLIENT_ID || 'reconciliation-automation-service';

    this.kafka = new Kafka({
      clientId,
      brokers,
      ssl: process.env.KAFKA_SSL === 'true',
      sasl: process.env.KAFKA_SASL_MECHANISM
        ? {
            mechanism: process.env.KAFKA_SASL_MECHANISM as any,
            username: process.env.KAFKA_USERNAME,
            password: process.env.KAFKA_PASSWORD,
          }
        : undefined,
    });

    this.producer = this.kafka.producer();
    this.connect();
  }

  private async connect(): Promise<void> {
    try {
      await this.producer.connect();
      this.logger.log('Kafka producer connected successfully');
    } catch (error) {
      this.logger.error('Failed to connect Kafka producer', error.stack);
    }
  }

  async publishReconciliationStarted(event: ReconciliationStartedEvent): Promise<void> {
    await this.publish('reconciliation.started', event);
  }

  async publishReconciliationCompleted(event: ReconciliationCompletedEvent): Promise<void> {
    await this.publish('reconciliation.completed', event);
  }

  async publishReconciliationFailed(event: ReconciliationFailedEvent): Promise<void> {
    await this.publish('reconciliation.failed', event);
  }

  async publishMatchFound(event: MatchFoundEvent): Promise<void> {
    await this.publish('reconciliation.match.found', event);
  }

  async publishDifferenceDetected(event: DifferenceDetectedEvent): Promise<void> {
    await this.publish('reconciliation.difference.detected', event);
  }

  async publishDifferenceResolved(event: DifferenceResolvedEvent): Promise<void> {
    await this.publish('reconciliation.difference.resolved', event);
  }

  async publishBulkEvents(events: unknown[]): Promise<void> {
    const records: ProducerRecord[] = events.map((event) => {
      let topic: string;

      if ((event as any).eventType === 'ReconciliationStarted') {
        topic = 'reconciliation.started';
      } else if ((event as any).eventType === 'ReconciliationCompleted') {
        topic = 'reconciliation.completed';
      } else if ((event as any).eventType === 'ReconciliationFailed') {
        topic = 'reconciliation.failed';
      } else if ((event as any).eventType === 'MatchFound') {
        topic = 'reconciliation.match.found';
      } else if ((event as any).eventType === 'DifferenceDetected') {
        topic = 'reconciliation.difference.detected';
      } else if ((event as any).eventType === 'DifferenceResolved') {
        topic = 'reconciliation.difference.resolved';
      } else {
        topic = 'reconciliation.unknown';
      }

      return {
        topic,
        messages: [
          {
            key: (event as any).correlationId || (event as any).reconciliationId,
            value: JSON.stringify(event),
            timestamp: Date.now().toString(),
          },
        ],
      };
    });

    for (const record of records) {
      await this.producer.send(record);
    }
  }

  private async publish(topic: string, event: any): Promise<void> {
    try {
      const record: ProducerRecord = {
        topic,
        messages: [
          {
            key: event.correlationId || event.reconciliationId,
            value: JSON.stringify(event),
            timestamp: Date.now().toString(),
          },
        ],
      };

      await this.producer.send(record);
      this.logger.debug(`Event published to topic: ${topic}`, { eventType: event.eventType });
    } catch (error) {
      this.logger.error(`Failed to publish event to topic: ${topic}`, error.stack);
      throw error;
    }
  }

  async onModuleDestroy(): Promise<void> {
    await this.producer.disconnect();
  }
}
