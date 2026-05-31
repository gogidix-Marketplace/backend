import { ClientsModule, Transport } from '@nestjs/microservices';
import { ConfigService } from '@nestjs/config';

export const kafkaProviders = [
  ClientsModule.registerAsync([
    {
      name: 'KAFKA_SERVICE',
      useFactory: (configService: ConfigService) => ({
        transport: Transport.KAFKA,
        options: {
          client: {
            clientId: configService.get<string>('KAFKA_CLIENT_ID', 'payment-automation-service'),
            brokers: configService.get<string>('KAFKA_BROKERS', 'localhost:9092').split(','),
            ssl: configService.get<boolean>('KAFKA_SSL_ENABLED', false),
            sasl: configService.get<string>('KAFKA_SASL_MECHANISM')
              ? {
                  mechanism: configService.get<string>('KAFKA_SASL_MECHANISM') as any,
                  username: configService.get<string>('KAFKA_SASL_USERNAME'),
                  password: configService.get<string>('KAFKA_SASL_PASSWORD'),
                }
              : undefined,
          },
          consumer: {
            groupId: configService.get<string>('KAFKA_CONSUMER_GROUP', 'payment-automation-group'),
            allowAutoTopicCreation: true,
          },
        },
      }),
      inject: [ConfigService],
    },
  ]),
];
