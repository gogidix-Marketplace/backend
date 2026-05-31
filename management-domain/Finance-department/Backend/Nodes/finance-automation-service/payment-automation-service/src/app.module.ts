import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { APP_FILTER, APP_INTERCEPTOR } from '@nestjs/core';

// Domain
import { Payment } from './domain/models/payment.entity';
import { PaymentBatch } from './domain/models/payment-batch.entity';
import { PaymentRule } from './domain/models/payment-rule.entity';
import { VendorPayment } from './domain/models/vendor-payment.entity';

// Application
import { PaymentCommandService } from './application/services/payment-command.service';
import { PaymentQueryService } from './application/services/payment-query.service';
import { PaymentBatchService } from './application/services/payment-batch.service';
import { PaymentAutomationService } from './application/services/payment-automation.service';

// Infrastructure - Persistence
import {
  PaymentDocument,
  PaymentBatchDocument,
  PaymentRuleDocument,
  VendorPaymentDocument,
  PaymentSchema,
  PaymentBatchSchema,
  PaymentRuleSchema,
  VendorPaymentSchema,
} from './infrastructure/persistence/mongodb/payment.schema';
import {
  MongoPaymentRepository,
  MongoPaymentBatchRepository,
  MongoPaymentRuleRepository,
  MongoVendorPaymentRepository,
} from './infrastructure/persistence/mongodb/payment.repository.impl';

// Infrastructure - Messaging
import { KafkaService, KafkaEventPublisher } from './infrastructure/messaging/kafka/kafka.service';

// Infrastructure - Gateway
import { StripeGateway } from './infrastructure/gateway/stripe.gateway';
import { TransferWiseGateway } from './infrastructure/gateway/transferwise.gateway';
import { PaymentGatewayFactory } from './infrastructure/gateway/payment-gateway.factory';

// Interfaces
import { PaymentController } from './interfaces/http/payment.controller';
import { BatchController } from './interfaces/http/batch.controller';
import { TenantIdInterceptor } from './interfaces/http/interceptors/tenant-id.interceptor';
import { HttpExceptionFilter } from './interfaces/http/exceptions/http.exception.filter';

const getMongoUri = () => {
  return process.env.MONGODB_URI || 'mongodb://localhost:27017/payment-automation';
};

const getKafkaBrokers = () => {
  return (process.env.KAFKA_BROKERS || 'localhost:9092').split(',');
};

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
    }),
    MongooseModule.forRoot(getMongoUri()),
    MongooseModule.forFeature([
      { name: PaymentDocument.name, schema: PaymentSchema },
      { name: PaymentBatchDocument.name, schema: PaymentBatchSchema },
      { name: PaymentRuleDocument.name, schema: PaymentRuleSchema },
      { name: VendorPaymentDocument.name, schema: VendorPaymentSchema },
    ]),
    ClientsModule.register([
      {
        name: 'KAFKA_SERVICE',
        transport: Transport.KAFKA,
        options: {
          client: {
            clientId: process.env.KAFKA_CLIENT_ID || 'payment-automation-service',
            brokers: getKafkaBrokers(),
          },
          consumer: {
            groupId: process.env.KAFKA_CONSUMER_GROUP || 'payment-automation-group',
          },
        },
      },
    ]),
  ],
  controllers: [PaymentController, BatchController],
  providers: [
    // Exception Filter
    {
      provide: APP_FILTER,
      useClass: HttpExceptionFilter,
    },

    // Interceptors
    {
      provide: APP_INTERCEPTOR,
      useClass: TenantIdInterceptor,
    },

    // Repositories
    MongoPaymentRepository,
    MongoPaymentBatchRepository,
    MongoPaymentRuleRepository,
    MongoVendorPaymentRepository,

    // Infrastructure Services
    KafkaService,
    {
      provide: 'EventPublisher',
      useClass: KafkaEventPublisher,
    },

    // Gateway
    PaymentGatewayFactory,
    {
      provide: 'StripeGateway',
      useFactory: (config: ConfigService) => {
        return new StripeGateway(config.get<string>('STRIPE_API_KEY', 'sk_test_...'));
      },
      inject: [ConfigService],
    },
    {
      provide: 'TransferWiseGateway',
      useFactory: (config: ConfigService) => {
        return new TransferWiseGateway({
          apiKey: config.get<string>('TRANSFERWISE_API_KEY', ''),
          baseUrl: config.get<string>('TRANSFERWISE_BASE_URL', 'https://api.transferwise.com'),
          profileId: config.get<string>('TRANSFERWISE_PROFILE_ID', ''),
        });
      },
      inject: [ConfigService],
    },

    // Application Services
    {
      provide: 'PaymentGateway',
      useFactory: (factory: PaymentGatewayFactory) => {
        return factory.createGateway('stripe');
      },
      inject: [PaymentGatewayFactory],
    },
    PaymentCommandService,
    PaymentQueryService,
    PaymentBatchService,
    PaymentAutomationService,
  ],
  exports: [
    PaymentCommandService,
    PaymentQueryService,
    PaymentBatchService,
    PaymentAutomationService,
    KafkaService,
    PaymentGatewayFactory,
  ],
})
export class AppModule {}
