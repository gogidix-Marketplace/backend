import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { Kafka, Producer, Consumer, EachMessagePayload } from 'kafkajs';

@Injectable()
export class KafkaService implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaService.name);
  private kafka: Kafka;
  private producer: Producer;
  private consumer: Consumer;

  constructor() {
    const brokers = process.env.KAFKA_BROKERS?.split(',') || ['localhost:9092'];
    this.kafka = new Kafka({ clientId: 'executive-realtime-service', brokers });
    this.producer = this.kafka.producer();
    this.consumer = this.kafka.consumer({ groupId: 'executive-realtime-group' });
  }

  async onModuleInit() {
    await this.producer.connect();
    await this.consumer.connect();
    this.logger.log('Kafka connected');
  }

  async onModuleDestroy() {
    await this.producer.disconnect();
    await this.consumer.disconnect();
  }

  async publish(topic: string, data: any) {
    await this.producer.send({ topic, messages: [{ key: data.correlationId || data.kpiId || 'unknown', value: JSON.stringify(data) }] });
  }

  async subscribe(topic: string, handler: (data: any) => Promise<void>) {
    await this.consumer.subscribe({ topic, fromBeginning: false });
    await this.consumer.run({ eachMessage: async ({ message }: EachMessagePayload) => {
      const value = message.value?.toString();
      if (value) await handler(JSON.parse(value));
    }});
  }
}
