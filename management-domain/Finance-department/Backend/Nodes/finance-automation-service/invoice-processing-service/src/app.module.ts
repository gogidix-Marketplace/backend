import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { ClientsModule, Transport } from '@nestjs/microservices';
import { APP_FILTER, APP_INTERCEPTOR } from '@nestjs/core';

// Domain
import {
  InvoiceDocument,
  InvoiceSchema,
  InvoiceValidationDocument,
  InvoiceValidationSchema,
} from './infrastructure/persistence/mongodb/invoice.repository';

// Application
import { InvoiceCommandService } from './application/services/invoice-command.service';
import { InvoiceQueryService } from './application/services/invoice-query.service';
import { InvoiceValidationService } from './application/services/invoice-validation.service';
import { InvoiceOcrService } from './application/services/invoice-ocr.service';
import { OcrService } from './application/services/ocr.service';

// Infrastructure - Persistence
import {
  MongoInvoiceRepository,
  MongoInvoiceValidationRepository,
} from './infrastructure/persistence/mongodb/invoice.repository';

// Infrastructure - Messaging
import { KafkaEventPublisher } from './infrastructure/messaging/kafka/kafka-event-publisher';
import { createKafkaOptions, KAFKA_TOPICS } from './infrastructure/messaging/kafka/kafka.config';

// Infrastructure - OCR
import { TesseractOcrService } from './infrastructure/ocr/tesseract-ocr.service';
import { GoogleVisionOcrService } from './infrastructure/ocr/google-vision-ocr.service';
import { OcrServiceFactory } from './infrastructure/ocr/ocr.factory';

// Interfaces
import { InvoiceController } from './interfaces/http/invoice.controller';
import { UploadController } from './interfaces/http/upload.controller';
import { TenantIdInterceptor } from './interfaces/http/interceptors/tenant.interceptor';
import { GlobalExceptionFilter } from './interfaces/http/exceptions/global.exception.filter';

// Configuration
import mongodbConfig from './infrastructure/config/app.config';

const getMongoUri = () => {
  return process.env.MONGODB_URI || 'mongodb://localhost:27017/invoice-processing';
};

const getKafkaBrokers = () => {
  return (process.env.KAFKA_BROKERS || 'localhost:9092').split(',');
};

@Module({
  imports: [
    ConfigModule.forRoot({
      isGlobal: true,
      envFilePath: '.env',
      load: [mongodbConfig],
    }),
    MongooseModule.forRoot(getMongoUri()),
    MongooseModule.forFeature([
      { name: InvoiceDocument.name, schema: InvoiceSchema },
      { name: InvoiceValidationDocument.name, schema: InvoiceValidationSchema },
    ]),
    ClientsModule.register([
      {
        name: 'KAFKA_SERVICE',
        transport: Transport.KAFKA,
        options: {
          client: {
            clientId: process.env.KAFKA_CLIENT_ID || 'invoice-processing-service',
            brokers: getKafkaBrokers(),
          },
          consumer: {
            groupId: process.env.KAFKA_CONSUMER_GROUP || 'invoice-processing-group',
          },
        },
      },
    ]),
  ],
  controllers: [InvoiceController, UploadController],
  providers: [
    // Exception Filter
    {
      provide: APP_FILTER,
      useClass: GlobalExceptionFilter,
    },

    // Interceptors
    {
      provide: APP_INTERCEPTOR,
      useClass: TenantIdInterceptor,
    },

    // Repositories
    MongoInvoiceRepository,
    MongoInvoiceValidationRepository,

    // Infrastructure Services
    {
      provide: 'EventPublisher',
      useClass: KafkaEventPublisher,
    },

    // OCR Services
    TesseractOcrService,
    GoogleVisionOcrService,
    OcrServiceFactory,
    {
      provide: 'IOcrService',
      useFactory: (factory: OcrServiceFactory, config: ConfigService) => {
        const engine = config.get<string>('OCR_ENGINE', 'tesseract');
        return factory.getService(engine as any);
      },
      inject: [OcrServiceFactory, ConfigService],
    },

    // Application Services
    {
      provide: 'InvoiceCommandService',
      useFactory: (
        invoiceRepo: MongoInvoiceRepository,
        eventPublisher: KafkaEventPublisher,
        validationService: InvoiceValidationService,
        ocrService: OcrService,
      ) => {
        return new InvoiceCommandService(
          invoiceRepo as any,
          eventPublisher as any,
          validationService,
          ocrService,
        );
      },
      inject: [
        MongoInvoiceRepository,
        'EventPublisher',
        InvoiceValidationService,
        OcrService,
      ],
    },
    InvoiceQueryService,
    InvoiceValidationService,
    OcrService,
    InvoiceOcrService,
  ],
  exports: [
    InvoiceCommandService,
    InvoiceQueryService,
    InvoiceValidationService,
    InvoiceOcrService,
  ],
})
export class AppModule {}
