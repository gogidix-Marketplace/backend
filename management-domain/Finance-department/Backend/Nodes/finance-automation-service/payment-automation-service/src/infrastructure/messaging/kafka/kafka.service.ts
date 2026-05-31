import { Injectable, OnModuleInit, OnModuleDestroy, Logger } from '@nestjs/common';
import { ClientKafka, ClientProxyFactory, Transport } from '@nestjs/microservices';
import { ConfigService } from '@nestjs/config';
import { MessageBroker, EventPublisher } from '../../../domain/ports/output';
import {
  PaymentInitiatedEvent,
  PaymentCompletedEvent,
  PaymentFailedEvent,
  BatchProcessedEvent,
} from '../../../domain/events';

@Injectable()
export class KafkaService implements OnModuleInit, OnModuleDestroy, MessageBroker {
  private readonly logger = new Logger(KafkaService.name);
  private client: ClientKafka;
  private connected = false;

  constructor(private readonly configService: ConfigService) {
    const brokers = this.configService.get<string>('KAFKA_BROKERS', 'localhost:9092').split(',');
    const clientId = this.configService.get<string>('KAFKA_CLIENT_ID', 'payment-automation-service');

    this.client = ClientProxyFactory.create({
      transport: Transport.KAFKA,
      options: {
        client: {
          clientId,
          brokers,
          ssl: this.configService.get<boolean>('KAFKA_SSL_ENABLED', false),
          sasl: this.configService.get<string>('KAFKA_SASL_MECHANISM')
            ? {
                mechanism: this.configService.get<string>('KAFKA_SASL_MECHANISM') as any,
                username: this.configService.get<string>('KAFKA_SASL_USERNAME'),
                password: this.configService.get<string>('KAFKA_SASL_PASSWORD'),
              }
            : undefined,
        },
        consumer: {
          groupId: this.configService.get<string>('KAFKA_CONSUMER_GROUP', 'payment-automation-group'),
          allowAutoTopicCreation: true,
        },
        producer: {
          allowAutoTopicCreation: true,
        },
      },
    }) as ClientKafka;
  }

  async onModuleInit(): Promise<void> {
    await this.connect();
  }

  async onModuleDestroy(): Promise<void> {
    await this.disconnect();
  }

  async connect(): Promise<void> {
    try {
      await this.client.connect();
      this.connected = true;
      this.logger.log('Successfully connected to Kafka');
    } catch (error) {
      this.logger.error(`Failed to connect to Kafka: ${error.message}`);
      throw error;
    }
  }

  async disconnect(): Promise<void> {
    try {
      await this.client.close();
      this.connected = false;
      this.logger.log('Successfully disconnected from Kafka');
    } catch (error) {
      this.logger.error(`Error disconnecting from Kafka: ${error.message}`);
    }
  }

  isConnected(): boolean {
    return this.connected;
  }

  async publish(topic: string, message: any): Promise<void> {
    if (!this.connected) {
      this.logger.warn(`Kafka not connected. Skipping publish to topic: ${topic}`);
      return;
    }

    try {
      await this.client.emit(topic, message).toPromise();
      this.logger.debug(`Message published to topic: ${topic}`);
    } catch (error) {
      this.logger.error(`Failed to publish message to topic ${topic}: ${error.message}`);
      throw error;
    }
  }

  async subscribe(topic: string, handler: (message: any) => void): Promise<void> {
    this.logger.log(`Subscribing to topic: ${topic}`);
    // Note: In NestJS, subscription is typically handled via @EventPattern decorator
    // This is a placeholder for programmatic subscription if needed
  }

  getClient(): ClientKafka {
    return this.client;
  }
}

@Injectable()
export class KafkaEventPublisher implements EventPublisher {
  private readonly logger = new Logger(KafkaEventPublisher.name);

  constructor(private readonly kafkaService: KafkaService) {}

  async publish(
    event:
      | PaymentInitiatedEvent
      | PaymentCompletedEvent
      | PaymentFailedEvent
      | BatchProcessedEvent,
  ): Promise<void> {
    const topic = this.getTopicForEvent(event.eventType);
    const message = {
      ...event.toJSON(),
      publishedAt: new Date().toISOString(),
    };

    await this.kafkaService.publish(topic, message);
    this.logger.debug(`Published event ${event.eventType} to topic ${topic}`);
  }

  async publishBatch(
    events: Array<
      PaymentInitiatedEvent | PaymentCompletedEvent | PaymentFailedEvent | BatchProcessedEvent
    >,
  ): Promise<void> {
    const eventsByTopic: Record<string, any[]> = {};

    for (const event of events) {
      const topic = this.getTopicForEvent(event.eventType);
      if (!eventsByTopic[topic]) {
        eventsByTopic[topic] = [];
      }
      eventsByTopic[topic].push({
        ...event.toJSON(),
        publishedAt: new Date().toISOString(),
      });
    }

    for (const [topic, messages] of Object.entries(eventsByTopic)) {
      for (const message of messages) {
        await this.kafkaService.publish(topic, message);
      }
    }

    this.logger.debug(`Published ${events.length} events`);
  }

  private getTopicForEvent(eventType: string): string {
    const topicMap: Record<string, string> = {
      PaymentInitiated: 'payment.initiated',
      PaymentCompleted: 'payment.completed',
      PaymentFailed: 'payment.failed',
      BatchProcessed: 'payment.batch.processed',
    };

    return topicMap[eventType] || 'payment.events';
  }
}
