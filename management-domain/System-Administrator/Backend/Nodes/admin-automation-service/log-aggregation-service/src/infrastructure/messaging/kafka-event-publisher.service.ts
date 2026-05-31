import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Kafka, Producer } from 'kafkajs';
import { IEventPublisher } from '@domain/ports/output/event-publisher.interface';

@Injectable()
export class KafkaEventPublisher implements IEventPublisher, OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  private producer: Producer;

  constructor(private readonly configService: ConfigService) {
    const kafka = new Kafka({
      clientId: 'log-aggregation-service',
      brokers: (this.configService.get<string>('KAFKA_BROKERS') || 'localhost:9092').split(','),
    });
    this.producer = kafka.producer();
  }

  async onModuleInit() { await this.producer.connect(); }
  async onModuleDestroy() { await this.producer.disconnect(); }

  async publish(event: any): Promise<void> {
    const topic = 'log-events';
    await this.producer.send({
      topic,
      messages: [{ key: event.sourceName || 'default', value: JSON.stringify(event), timestamp: Date.now().toString() }],
    });
  }

  async publishAll(events: any[]): Promise<void> {
    for (const event of events) await this.publish(event);
  }
}
