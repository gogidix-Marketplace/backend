import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { ClientKafka } from '@nestjs/microservices';
import { EventPublisher, MessageBroker } from '../../../../domain/ports/output';
import {
  AutomationTriggeredEvent,
  WorkflowExecutedEvent,
  WorkflowFailedEvent,
  RuleCreatedEvent,
  RuleUpdatedEvent,
  RuleDeletedEvent,
  LeadScoredEvent,
  DealStageChangedEvent,
} from '../../../../domain/events';
import { ConfigService } from '@nestjs/config';

@Injectable()
export class KafkaEventPublisher implements EventPublisher, MessageBroker, OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  private client: ClientKafka;
  private connected = false;

  constructor(private readonly configService: ConfigService) {
    const brokers = this.configService.get<string[]>('kafka.brokers');
    const clientId = this.configService.get<string>('kafka.clientId');

    this.client = ClientKafka.create({
      client: { clientId, brokers },
    });
  }

  async onModuleInit() {
    await this.connect();
  }

  async onModuleDestroy() {
    await this.disconnect();
  }

  async connect(): Promise<void> {
    if (this.connected) {
      return;
    }

    try {
      await this.client.connect();
      this.connected = true;
      this.logger.log('Connected to Kafka');
    } catch (error) {
      this.logger.error('Failed to connect to Kafka', error);
      throw error;
    }
  }

  async disconnect(): Promise<void> {
    if (!this.connected) {
      return;
    }

    try {
      await this.client.close();
      this.connected = false;
      this.logger.log('Disconnected from Kafka');
    } catch (error) {
      this.logger.error('Error disconnecting from Kafka', error);
    }
  }

  isConnected(): boolean {
    return this.connected;
  }

  async publish(
    event: AutomationTriggeredEvent | WorkflowExecutedEvent | WorkflowFailedEvent | RuleCreatedEvent | RuleUpdatedEvent | RuleDeletedEvent | LeadScoredEvent | DealStageChangedEvent,
  ): Promise<void> {
    const topic = this.getTopicForEvent(event);

    this.logger.debug(`Publishing event ${event.eventType} to topic ${topic}`);

    try {
      await this.client.emit(topic, {
        key: event.aggregateId,
        value: event.toJSON(),
      }).toPromise();
    } catch (error) {
      this.logger.error(`Failed to publish event to topic ${topic}`, error);
      throw error;
    }
  }

  async publishBatch(
    events: Array<AutomationTriggeredEvent | WorkflowExecutedEvent | WorkflowFailedEvent | RuleCreatedEvent | RuleUpdatedEvent | RuleDeletedEvent | LeadScoredEvent | DealStageChangedEvent>,
  ): Promise<void> {
    const promises = events.map(event => this.publish(event));
    await Promise.all(promises);
  }

  async publish(topic: string, message: any): Promise<void> {
    this.logger.debug(`Publishing message to topic ${topic}`);

    try {
      await this.client.emit(topic, {
        key: message.key || 'default',
        value: message.value || message,
      }).toPromise();
    } catch (error) {
      this.logger.error(`Failed to publish message to topic ${topic}`, error);
      throw error;
    }
  }

  async subscribe(topic: string, handler: (message: any) => void): Promise<void> {
    this.logger.debug(`Subscribing to topic ${topic}`);
    // This would typically be handled by NestJS @EventPattern decorators
    // This is a placeholder for the interface contract
  }

  private getTopicForEvent(
    event: AutomationTriggeredEvent | WorkflowExecutedEvent | WorkflowFailedEvent | RuleCreatedEvent | RuleUpdatedEvent | RuleDeletedEvent | LeadScoredEvent | DealStageChangedEvent,
  ): string {
    const topics = this.configService.get<Record<string, string>>('kafka.topics');

    switch (event.eventType) {
      case 'AutomationTriggered':
        return topics.automationTriggered;
      case 'WorkflowExecuted':
        return topics.workflowExecuted;
      case 'WorkflowFailed':
        return topics.workflowFailed;
      case 'RuleCreated':
        return topics.ruleCreated;
      case 'RuleUpdated':
        return topics.ruleUpdated;
      case 'RuleDeleted':
        return topics.ruleDeleted;
      case 'LeadScored':
        return topics.leadScored;
      case 'DealStageChanged':
        return topics.dealStageChanged;
      default:
        return 'sales.automation.default';
    }
  }
}
