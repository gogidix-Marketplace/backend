import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { Kafka, Consumer, EachMessagePayload } from 'kafkajs';
import { EventProcessorService } from '../../../application/services/event-processor.service';

@Injectable()
export class KafkaConsumerService implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaConsumerService.name);
  private consumer: Consumer;

  constructor(private readonly eventProcessor: EventProcessorService) {
    const brokers = process.env.KAFKA_BROKERS?.split(',') || ['localhost:9092'];
    this.consumer = new Kafka({ clientId: 'kafka-consumer-service', brokers }).consumer({ groupId: 'executive-events-group' });
  }

  async onModuleInit() {
    await this.consumer.connect();
    const topics = ['executive.events', 'kpi.events', 'strategy.events', 'approval.events'];
    for (const t of topics) await this.consumer.subscribe({ topic: t, fromBeginning: false });
    await this.consumer.run({ eachMessage: async ({ topic, message }: EachMessagePayload) => {
      const value = message.value?.toString(); if (!value) return;
      try { await this.eventProcessor.processEvent(JSON.parse(value)); } catch (e) { this.logger.error(`Error processing from ${topic}:`, e); }
    }});
    this.logger.log('Kafka consumer connected');
  }

  async onModuleDestroy() { await this.consumer.disconnect(); }
}
