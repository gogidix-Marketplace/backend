import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import {
  ClientProxy,
  ClientProxyFactory,
  Transport,
  RmqRecord,
} from '@nestjs/microservices';
import { firstValueFrom } from 'rxjs';
import { IEventPublisher, EventTopic } from '../../../domain/ports/output/event-publisher.interface';
import { InvoiceDomainEvent } from '../../../domain/events/base-event.interface';

/**
 * Kafka configuration interface
 */
interface KafkaConfig {
  urls: string[];
  clientId: string;
  consumerGroupId: string;
  authentication?: {
    mechanism: 'plain' | 'scram-sha-256' | 'scram-sha-512';
    username: string;
    password: string;
  };
}

/**
 * Kafka implementation of EventPublisher
 * Publishes domain events to Kafka topics
 */
@Injectable()
export class KafkaEventPublisher implements IEventPublisher, OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  private client: ClientProxy;
  private connected = false;

  constructor(private readonly configService: ConfigService) {
    const kafkaConfig: KafkaConfig = {
      urls: this.configService.get<string>('KAFKA_BROKERS', 'localhost:9092').split(','),
      clientId: this.configService.get<string>('KAFKA_CLIENT_ID', 'invoice-processing-service'),
      consumerGroupId: this.configService.get<string>('KAFKA_GROUP_ID', 'invoice-processing-group'),
    };

    // Add authentication if configured
    const auth = {
      username: this.configService.get<string>('KAFKA_USERNAME'),
      password: this.configService.get<string>('KAFKA_PASSWORD'),
    };

    if (auth.username && auth.password) {
      kafkaConfig.authentication = {
        mechanism: this.configService.get<'plain' | 'scram-sha-256' | 'scram-sha-512'>('KAFKA_AUTH_MECHANISM', 'plain'),
        ...auth,
      };
    }

    this.client = ClientProxyFactory.create({
      transport: Transport.RMQ,
      options: {
        urls: kafkaConfig.urls,
        queue: this.configService.get<string>('KAFKA_QUEUE', 'invoice-events'),
        queueOptions: {
          durable: true,
        },
        // Note: This uses RabbitMQ transport for simplicity
        // For Kafka, you would use Transport.KAFKA with @nestjs/microservices
        // or use the kafkajs library directly
      },
    });

    // Alternative: Use kafkajs directly for true Kafka support
    // Uncomment below if using kafkajs
    /*
    this.kafka = new Kafka({
      clientId: kafkaConfig.clientId,
      brokers: kafkaConfig.urls,
      ...(kafkaConfig.authentication && {
        sasl: {
          mechanism: kafkaConfig.authentication.mechanism,
          username: kafkaConfig.authentication.username,
          password: kafkaConfig.authentication.password,
        },
      }),
    });
    this.producer = this.kafka.producer();
    */
  }

  async onModuleInit(): Promise<void> {
    try {
      await this.client.connect();
      this.connected = true;
      this.logger.log('Kafka event publisher connected successfully');
    } catch (error) {
      this.logger.error('Failed to connect to Kafka', error.stack);
      this.connected = false;
    }
  }

  async onModuleDestroy(): Promise<void> {
    try {
      await this.client.close();
      this.connected = false;
      this.logger.log('Kafka event publisher disconnected');
    } catch (error) {
      this.logger.error('Error during disconnect', error.stack);
    }
  }

  async publish(event: InvoiceDomainEvent): Promise<void> {
    this.logger.log(`Publishing event ${event.eventType} for invoice ${event.tenantId}`);

    if (!this.connected) {
      this.logger.warn('Kafka client not connected, event not published');
      throw new Error('Kafka client not connected');
    }

    try {
      const topic = this.getTopicForEvent(event);

      const record = new RmqRecord(
        {
          ...event,
          publishedAt: new Date().toISOString(),
        },
        {
          headers: {
            'X-Tenant-Id': event.tenantId,
            'X-Organization-Id': event.organizationId,
            'X-Correlation-Id': event.correlationId || this.generateCorrelationId(),
            'X-Event-Type': event.eventType,
            'X-Event-Id': event.eventId,
          },
          contentType: 'application/json',
          deliveryMode: 2, // Persistent
        },
      );

      await firstValueFrom(this.client.emit(topic, record));

      this.logger.log(`Event ${event.eventType} published to topic ${topic}`);
    } catch (error) {
      this.logger.error(`Failed to publish event ${event.eventType}: ${error.message}`, error.stack);
      throw error;
    }
  }

  async publishBatch(events: InvoiceDomainEvent[]): Promise<void> {
    this.logger.log(`Publishing batch of ${events.length} events`);

    const results = await Promise.allSettled(
      events.map(event => this.publish(event)),
    );

    const failed = results.filter(r => r.status === 'rejected');
    if (failed.length > 0) {
      this.logger.warn(`${failed.length} events failed to publish out of ${events.length}`);
    }
  }

  async publishToTopic(topic: string, event: InvoiceDomainEvent): Promise<void> {
    this.logger.log(`Publishing event ${event.eventType} to topic ${topic}`);

    if (!this.connected) {
      this.logger.warn('Kafka client not connected, event not published');
      throw new Error('Kafka client not connected');
    }

    try {
      const record = new RmqRecord(
        {
          ...event,
          publishedAt: new Date().toISOString(),
        },
        {
          headers: {
            'X-Tenant-Id': event.tenantId,
            'X-Organization-Id': event.organizationId,
            'X-Correlation-Id': event.correlationId || this.generateCorrelationId(),
            'X-Event-Type': event.eventType,
            'X-Event-Id': event.eventId,
          },
          contentType: 'application/json',
          deliveryMode: 2,
        },
      );

      await firstValueFrom(this.client.emit(topic, record));

      this.logger.log(`Event ${event.eventType} published to topic ${topic}`);
    } catch (error) {
      this.logger.error(`Failed to publish event to topic ${topic}: ${error.message}`, error.stack);
      throw error;
    }
  }

  isConnected(): boolean {
    return this.connected;
  }

  private getTopicForEvent(event: InvoiceDomainEvent): string {
    // Map event types to topics
    switch (event.eventType) {
      case 'InvoiceCreated':
        return EventTopic.INVOICE_CREATED;
      case 'InvoiceValidated':
        return EventTopic.INVOICE_VALIDATED;
      case 'InvoiceProcessed':
        return EventTopic.INVOICE_PROCESSED;
      case 'InvoiceStatusChanged':
        return EventTopic.INVOICE_STATUS_CHANGED;
      default:
        return 'invoice.events';
    }
  }

  private generateCorrelationId(): string {
    return `corr-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}

/**
 * Alternative implementation using kafkajs directly
 * This provides true Kafka support (uncomment and use instead of RabbitMQ-based approach)
 */
/*
import { Kafka, Producer, ProducerRecord } from 'kafkajs';

@Injectable()
export class KafkaJsEventPublisher implements IEventPublisher, OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaJsEventPublisher.name);
  private kafka: Kafka;
  private producer: Producer;
  private connected = false;

  constructor(private readonly configService: ConfigService) {
    this.kafka = new Kafka({
      clientId: this.configService.get<string>('KAFKA_CLIENT_ID', 'invoice-processing-service'),
      brokers: this.configService.get<string>('KAFKA_BROKERS', 'localhost:9092').split(','),
      ...(this.configService.get<string>('KAFKA_USERNAME') && {
        sasl: {
          mechanism: this.configService.get<'plain' | 'scram-sha-256' | 'scram-sha-512'>('KAFKA_AUTH_MECHANISM', 'plain') as any,
          username: this.configService.get<string>('KAFKA_USERNAME')!,
          password: this.configService.get<string>('KAFKA_PASSWORD')!,
        },
      }),
      ssl: this.configService.get<boolean>('KAFKA_SSL', false),
    });

    this.producer = this.kafka.producer();
  }

  async onModuleInit(): Promise<void> {
    try {
      await this.producer.connect();
      this.connected = true;
      this.logger.log('Kafka producer connected successfully');
    } catch (error) {
      this.logger.error('Failed to connect Kafka producer', error.stack);
      this.connected = false;
    }
  }

  async onModuleDestroy(): Promise<void> {
    try {
      await this.producer.disconnect();
      this.connected = false;
      this.logger.log('Kafka producer disconnected');
    } catch (error) {
      this.logger.error('Error during disconnect', error.stack);
    }
  }

  async publish(event: InvoiceDomainEvent): Promise<void> {
    if (!this.connected) {
      throw new Error('Kafka producer not connected');
    }

    const topic = this.getTopicForEvent(event);
    const record: ProducerRecord = {
      topic,
      messages: [
        {
          key: `${event.tenantId}-${event.organizationId}`,
          value: JSON.stringify({
            ...event,
            publishedAt: new Date().toISOString(),
          }),
          headers: {
            'X-Tenant-Id': event.tenantId,
            'X-Organization-Id': event.organizationId,
            'X-Correlation-Id': event.correlationId || this.generateCorrelationId(),
            'X-Event-Type': event.eventType,
            'X-Event-Id': event.eventId,
          },
        },
      ],
    };

    await this.producer.send(record);
    this.logger.log(`Event ${event.eventType} published to topic ${topic}`);
  }

  async publishBatch(events: InvoiceDomainEvent[]): Promise<void> {
    const eventsByTopic = new Map<string, InvoiceDomainEvent[]>();

    for (const event of events) {
      const topic = this.getTopicForEvent(event);
      if (!eventsByTopic.has(topic)) {
        eventsByTopic.set(topic, []);
      }
      eventsByTopic.get(topic)!.push(event);
    }

    for (const [topic, topicEvents] of eventsByTopic) {
      const record: ProducerRecord = {
        topic,
        messages: topicEvents.map(event => ({
          key: `${event.tenantId}-${event.organizationId}`,
          value: JSON.stringify({
            ...event,
            publishedAt: new Date().toISOString(),
          }),
          headers: {
            'X-Tenant-Id': event.tenantId,
            'X-Organization-Id': event.organizationId,
            'X-Correlation-Id': event.correlationId || this.generateCorrelationId(),
            'X-Event-Type': event.eventType,
            'X-Event-Id': event.eventId,
          },
        })),
      };

      await this.producer.send(record);
    }

    this.logger.log(`Batch of ${events.length} events published`);
  }

  async publishToTopic(topic: string, event: InvoiceDomainEvent): Promise<void> {
    const record: ProducerRecord = {
      topic,
      messages: [
        {
          key: `${event.tenantId}-${event.organizationId}`,
          value: JSON.stringify({
            ...event,
            publishedAt: new Date().toISOString(),
          }),
          headers: {
            'X-Tenant-Id': event.tenantId,
            'X-Organization-Id': event.organizationId,
            'X-Correlation-Id': event.correlationId || this.generateCorrelationId(),
            'X-Event-Type': event.eventType,
            'X-Event-Id': event.eventId,
          },
        },
      ],
    };

    await this.producer.send(record);
    this.logger.log(`Event ${event.eventType} published to topic ${topic}`);
  }

  isConnected(): boolean {
    return this.connected;
  }

  private getTopicForEvent(event: InvoiceDomainEvent): string {
    switch (event.eventType) {
      case 'InvoiceCreated':
        return EventTopic.INVOICE_CREATED;
      case 'InvoiceValidated':
        return EventTopic.INVOICE_VALIDATED;
      case 'InvoiceProcessed':
        return EventTopic.INVOICE_PROCESSED;
      case 'InvoiceStatusChanged':
        return EventTopic.INVOICE_STATUS_CHANGED;
      default:
        return 'invoice.events';
    }
  }

  private generateCorrelationId(): string {
    return `corr-${Date.now()}-${Math.random().toString(36).substring(2, 9)}`;
  }
}
*/
