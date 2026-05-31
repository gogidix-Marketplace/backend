import { Module, Global } from '@nestjs/common';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { ConfigService } from '@nestjs/config';

/**
 * Kafka Module for microservice communication
 */
@Global()
@Module({
  imports: [
    ClientsModule.registerAsync([
      {
        name: 'COMMISSION_SERVICE_KAFKA',
        useFactory: (configService: ConfigService) => ({
          transport: Transport.KAFKA,
          options: {
            client: {
              clientId: 'commission-calculator-service',
              brokers: [configService.get<string>('KAFKA_BROKERS') || 'localhost:9092'],
              ssl: configService.get<boolean>('KAFKA_SSL') || false,
              sasl: configService.get<string>('KAFKA_SASL_MECHANISM')
                ? {
                    mechanism: configService.get<string>('KAFKA_SASL_MECHANISM'),
                    username: configService.get<string>('KAFKA_USERNAME') || '',
                    password: configService.get<string>('KAFKA_PASSWORD') || '',
                  } as any
                : undefined,
            },
            consumer: {
              groupId: 'commission-calculator-consumer',
              allowAutoTopicCreation: true,
            },
            producer: {
              allowAutoTopicCreation: true,
            },
          },
        }),
        inject: [ConfigService],
      },
    ]),
  ],
  exports: [ClientsModule],
})
export class KafkaModule {}
