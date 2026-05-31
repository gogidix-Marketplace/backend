import { Module } from '@nestjs/common';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { ConfigService } from '@nestjs/config';

/**
 * Factory function to create Kafka options
 */
export const createKafkaOptions = (configService: ConfigService) => {
  const brokers = configService
    .get<string>('KAFKA_BROKERS', 'localhost:9092')
    .split(',');

  return {
    transport: Transport.KAFKA,
    options: {
      client: {
        clientId: configService.get<string>('KAFKA_CLIENT_ID', 'invoice-processing-service'),
        brokers,
        ssl: configService.get<boolean>('KAFKA_SSL', false),
        ...(configService.get<string>('KAFKA_USERNAME') && {
          sasl: {
            mechanism: configService.get<'plain' | 'scram-sha-256' | 'scram-sha-512'>(
              'KAFKA_AUTH_MECHANISM',
              'plain',
            ) as any,
            username: configService.get<string>('KAFKA_USERNAME')!,
            password: configService.get<string>('KAFKA_PASSWORD')!,
          },
        }),
      },
      consumer: {
        groupId: configService.get<string>('KAFKA_CONSUMER_GROUP', 'invoice-processing-group'),
        allowAutoTopicCreation: true,
      },
      producer: {
        maxInFlightRequests: 1,
        acks: -1, // Wait for all replicas
      },
      run: {
        autoCommit: true,
        autoCommitInterval: 5000,
      },
    },
  };
};

/**
 * Kafka topic names configuration
 */
export const KAFKA_TOPICS = {
  INVOICE_RECEIVED: 'invoice.received',
  INVOICE_VALIDATED: 'invoice.validated',
  INVOICE_PROCESSED: 'invoice.processed',
  INVOICE_APPROVED: 'invoice.approved',
  INVOICE_FAILED: 'invoice.failed',
  INVOICE_STATUS_CHANGED: 'invoice.status-changed',
  INVOICE_PAYMENT_RECORDED: 'invoice.payment-recorded',
} as const;

/**
 * Kafka consumer groups configuration
 */
export const KAFKA_CONSUMER_GROUPS = {
  INVOICE_PROCESSING: 'invoice-processing-group',
  INVOICE_VALIDATION: 'invoice-validation-group',
  INVOICE_NOTIFICATION: 'invoice-notification-group',
} as const;
