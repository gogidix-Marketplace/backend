import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { Kafka, Consumer, EachMessagePayload } from 'kafkajs';

@Injectable()
export class KafkaConsumerService implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaConsumerService.name);
  private readonly consumer: Consumer;
  private readonly kafka: Kafka;

  constructor() {
    const brokers = process.env.KAFKA_BROKERS?.split(',') || ['localhost:9092'];
    const clientId = process.env.KAFKA_CLIENT_ID || 'reconciliation-automation-service-consumer';
    const groupId = process.env.KAFKA_GROUP_ID || 'reconciliation-automation-group';

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

    this.consumer = this.kafka.consumer({ groupId });
  }

  async onModuleInit(): Promise<void> {
    await this.connect();
    await this.subscribeToTopics();
    await this.run();
  }

  async onModuleDestroy(): Promise<void> {
    await this.consumer.disconnect();
  }

  private async connect(): Promise<void> {
    try {
      await this.consumer.connect();
      this.logger.log('Kafka consumer connected successfully');
    } catch (error) {
      this.logger.error('Failed to connect Kafka consumer', error.stack);
    }
  }

  private async subscribeToTopics(): Promise<void> {
    const topics = [
      'transaction.bank.imported',
      'transaction.internal.imported',
      'account.balance.updated',
      'reconciliation.requested',
    ];

    for (const topic of topics) {
      try {
        await this.consumer.subscribe({ topic, fromBeginning: false });
        this.logger.debug(`Subscribed to topic: ${topic}`);
      } catch (error) {
        this.logger.warn(`Failed to subscribe to topic: ${topic}`, error.message);
      }
    }
  }

  private async run(): Promise<void> {
    await this.consumer.run({
      eachMessage: async ({ topic, partition, message }: EachMessagePayload) => {
        try {
          const value = message.value?.toString();
          if (!value) return;

          const event = JSON.parse(value);
          this.logger.debug(`Received message from topic: ${topic}`, { eventType: event.eventType });

          await this.handleMessage(topic, event);
        } catch (error) {
          this.logger.error(`Error processing message from topic: ${topic}`, error.stack);
        }
      },
    });
  }

  private async handleMessage(topic: string, event: any): Promise<void> {
    switch (topic) {
      case 'transaction.bank.imported':
        await this.handleBankTransactionImported(event);
        break;
      case 'transaction.internal.imported':
        await this.handleInternalTransactionImported(event);
        break;
      case 'account.balance.updated':
        await this.handleBalanceUpdated(event);
        break;
      case 'reconciliation.requested':
        await this.handleReconciliationRequested(event);
        break;
      default:
        this.logger.warn(`Unhandled topic: ${topic}`);
    }
  }

  private async handleBankTransactionImported(event: any): Promise<void> {
    this.logger.debug('Handling bank transaction imported event', { transactionId: event.transactionId });
  }

  private async handleInternalTransactionImported(event: any): Promise<void> {
    this.logger.debug('Handling internal transaction imported event', { transactionId: event.transactionId });
  }

  private async handleBalanceUpdated(event: any): Promise<void> {
    this.logger.debug('Handling balance updated event', { accountId: event.accountId });
  }

  private async handleReconciliationRequested(event: any): Promise<void> {
    this.logger.debug('Handling reconciliation requested event', { accountId: event.accountId });
  }
}
