import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { IEventPublisher } from '@domain/ports/output';
import { DomainEvent } from '@domain/events';
import { ConfigService } from '@nestjs/config';
import { Kafka, Producer, Admin } from 'kafkajs';

@Injectable()
export class KafkaEventPublisher implements IEventPublisher, OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  private kafka: Kafka;
  private producer: Producer;
  private admin: Admin;

  constructor(private configService: ConfigService) {
    const brokers = this.configService.get<string>('config.kafka.brokers') || 'localhost:9092';
    this.kafka = new Kafka({
      brokers: typeof brokers === 'string' ? brokers.split(',') : brokers,
      clientId: this.configService.get<string>('config.kafka.clientId') || 'chatbot-service',
    });
    this.producer = this.kafka.producer();
    this.admin = this.kafka.admin();
  }

  async onModuleInit() {
    try {
      await this.admin.connect();
      await this.producer.connect();
      this.logger.log('Kafka producer connected');
    } catch (error) {
      this.logger.warn('Kafka connection failed, events will be logged only: ' + error);
    }
  }

  async onModuleDestroy() {
    try {
      await this.producer.disconnect();
      await this.admin.disconnect();
    } catch {}
  }

  async publish(event: DomainEvent): Promise<void> {
    try {
      await this.producer.send({
        topic: `chatbot.${event.eventType.toLowerCase()}`,
        messages: [
          {
            key: event.aggregateId,
            value: JSON.stringify(event),
          },
        ],
      });
      this.logger.debug(`Published event ${event.eventType} for aggregate ${event.aggregateId}`);
    } catch (error) {
      this.logger.warn(`Failed to publish event ${event.eventType}: ${error}`);
    }
  }

  async publishMany(events: DomainEvent[]): Promise<void> {
    for (const event of events) {
      await this.publish(event);
    }
  }
}
