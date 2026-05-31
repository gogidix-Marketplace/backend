import { Module } from '@nestjs/common';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { ConfigService } from '@nestjs/config';

/**
 * Kafka/RabbitMQ configuration module
 * Configures message broker client for event publishing
 */
@Module({
  imports: [
    ClientsModule.registerAsync([
      {
        name: 'KAFKA_CLIENT',
        inject: [ConfigService],
        useFactory: (configService: ConfigService) => {
          const brokers = configService
            .get<string>('KAFKA_BROKERS', 'localhost:9092')
            .split(',');

          const queue = configService.get<string>('KAFKA_QUEUE', 'invoice-events');

          // Using RMQ transport for simplicity
          // For true Kafka support, use kafkajs library directly
          return {
            transport: Transport.RMQ,
            options: {
              urls: brokers,
              queue,
              queueOptions: {
                durable: true,
              },
            },
          };
        },
      },
    ]),
  ],
  exports: [ClientsModule],
})
export class KafkaConfigModule {}
