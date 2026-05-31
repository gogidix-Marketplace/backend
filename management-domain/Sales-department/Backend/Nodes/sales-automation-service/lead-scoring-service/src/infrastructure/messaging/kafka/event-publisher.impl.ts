import { Injectable, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Kafka, Producer, ProducerRecord } from 'kafkajs';
import { EventPublisherPort, DomainEvent } from '../../../domain/ports/out/event-publisher.port';

@Injectable()
export class KafkaEventPublisher implements EventPublisherPort {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  private readonly kafka: Kafka;
  private readonly producer: Producer;
  private readonly leadScoredTopic: string;
  private readonly scoreModelUpdatedTopic: string;
  private readonly leadQualifiedTopic: string;

  constructor(private readonly configService: ConfigService) {
    const brokers = this.configService.get<string>('KAFKA_BROKERS', 'localhost:9092').split(',');
    const clientId = this.configService.get<string>('KAFKA_CLIENT_ID', 'lead-scoring-service');

    this.kafka = new Kafka({
      clientId,
      brokers,
      retry: {
        initialRetryTime: 100,
        retries: 8,
      },
    });

    this.producer = this.kafka.producer();
    this.leadScoredTopic = this.configService.get<string>('KAFKA_LEAD_SCORED_TOPIC', 'lead.scored');
    this.scoreModelUpdatedTopic = this.configService.get<string>('KAFKA_SCORE_MODEL_UPDATED_TOPIC', 'scoring.model.updated');
    this.leadQualifiedTopic = this.configService.get<string>('KAFKA_LEAD_QUALIFIED_TOPIC', 'lead.qualified');
  }

  async onModuleInit(): Promise<void> {
    try {
      await this.producer.connect();
      this.logger.log('Kafka producer connected successfully');
    } catch (error) {
      this.logger.error('Failed to connect Kafka producer', error);
      throw error;
    }
  }

  async onModuleDestroy(): Promise<void> {
    try {
      await this.producer.disconnect();
      this.logger.log('Kafka producer disconnected');
    } catch (error) {
      this.logger.error('Error disconnecting Kafka producer', error);
    }
  }

  async publish(event: DomainEvent): Promise<void> {
    const topic = this.getTopicForEvent(event.eventType);

    if (!topic) {
      this.logger.warn(`No topic configured for event type: ${event.eventType}`);
      return;
    }

    const message: ProducerRecord = {
      topic,
      messages: [
        {
          key: this.extractKey(event),
          value: JSON.stringify(event.toPrimitives()),
          timestamp: Date.now().toString(),
        },
      ],
    };

    try {
      await this.producer.send(message);
      this.logger.debug(`Event published to ${topic}: ${event.eventType}`);
    } catch (error) {
      this.logger.error(`Failed to publish event to ${topic}`, error);
      throw error;
    }
  }

  async publishBatch(events: DomainEvent[]): Promise<void> {
    if (events.length === 0) {
      return;
    }

    // Group events by topic
    const eventsByTopic = new Map<string, DomainEvent[]>();

    for (const event of events) {
      const topic = this.getTopicForEvent(event.eventType);

      if (topic) {
        if (!eventsByTopic.has(topic)) {
          eventsByTopic.set(topic, []);
        }
        eventsByTopic.get(topic)!.push(event);
      }
    }

    // Publish to each topic
    for (const [topic, topicEvents] of eventsByTopic.entries()) {
      const message: ProducerRecord = {
        topic,
        messages: topicEvents.map(event => ({
          key: this.extractKey(event),
          value: JSON.stringify(event.toPrimitives()),
          timestamp: Date.now().toString(),
        })),
      };

      try {
        await this.producer.send(message);
        this.logger.debug(`Batch of ${topicEvents.length} events published to ${topic}`);
      } catch (error) {
        this.logger.error(`Failed to publish batch to ${topic}`, error);
        throw error;
      }
    }
  }

  private getTopicForEvent(eventType: string): string | null {
    switch (eventType) {
      case 'LeadScored':
        return this.leadScoredTopic;
      case 'ScoreDecayed':
        return this.leadScoredTopic;
      case 'ScoreModelUpdated':
        return this.scoreModelUpdatedTopic;
      case 'ScoreModelActivated':
        return this.scoreModelUpdatedTopic;
      case 'LeadQualified':
        return this.leadQualifiedTopic;
      default:
        return null;
    }
  }

  private extractKey(event: DomainEvent): string {
    const primitives = event.toPrimitives();

    // Use tenantId as the key for partitioning
    if (primitives.tenantId) {
      return primitives.tenantId;
    }

    if (primitives.leadId) {
      return primitives.leadId;
    }

    if (primitives.modelId) {
      return primitives.modelId;
    }

    return 'default';
  }

  async publishLeadScored(data: {
    scoreId: string;
    leadId: string;
    tenantId: string;
    previousScore: number;
    newScore: number;
    grade: string;
    scoreModelId: string;
    variantId?: string;
  }): Promise<void> {
    const message: ProducerRecord = {
      topic: this.leadScoredTopic,
      messages: [
        {
          key: data.tenantId,
          value: JSON.stringify({
            eventType: 'LeadScored',
            ...data,
            timestamp: new Date().toISOString(),
          }),
        },
      ],
    };

    try {
      await this.producer.send(message);
      this.logger.debug(`Lead scored event published for lead: ${data.leadId}`);
    } catch (error) {
      this.logger.error(`Failed to publish lead scored event`, error);
      throw error;
    }
  }

  async publishScoreModelUpdated(data: {
    modelId: string;
    tenantId: string;
    modelName: string;
    version: number;
    updatedBy: string;
  }): Promise<void> {
    const message: ProducerRecord = {
      topic: this.scoreModelUpdatedTopic,
      messages: [
        {
          key: data.tenantId,
          value: JSON.stringify({
            eventType: 'ScoreModelUpdated',
            ...data,
            timestamp: new Date().toISOString(),
          }),
        },
      ],
    };

    try {
      await this.producer.send(message);
      this.logger.debug(`Score model updated event published for model: ${data.modelId}`);
    } catch (error) {
      this.logger.error(`Failed to publish score model updated event`, error);
      throw error;
    }
  }
}

