import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigService } from '@nestjs/config';
import { InvoiceDocument, InvoiceSchema } from '../persistence/mongodb/invoice.repository';
import { InvoiceValidationDocument, InvoiceValidationSchema } from '../persistence/mongodb/invoice.repository';

/**
 * MongoDB configuration module
 * Configures MongoDB connection and models
 */
@Module({
  imports: [
    MongooseModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => {
        const mongodbUri = configService.get<string>(
          'MONGODB_URI',
          'mongodb://localhost:27017/invoice-processing',
        );

        const options: Record<string, unknown> = {
          // Connection pool settings
          maxPoolSize: configService.get<number>('MONGODB_MAX_POOL_SIZE', 10),
          minPoolSize: configService.get<number>('MONGODB_MIN_POOL_SIZE', 2),
          maxIdleTimeMS: configService.get<number>('MONGODB_MAX_IDLE_TIME_MS', 60000),
          waitQueueTimeoutMS: configService.get<number>('MONGODB_WAIT_QUEUE_TIMEOUT_MS', 5000),

          // Retry settings
          retryWrites: configService.get<boolean>('MONGODB_RETRY_WRITES', true),
          retryReads: configService.get<boolean>('MONGODB_RETRY_READS', true),
        };

        // Add authentication if configured
        const username = configService.get<string>('MONGODB_USERNAME');
        const password = configService.get<string>('MONGODB_PASSWORD');
        const authDb = configService.get<string>('MONGODB_AUTH_DB', 'admin');

        if (username && password) {
          return {
            uri: mongodbUri,
            ...options,
            auth: {
              username,
              password,
              authSource: authDb,
            },
          };
        }

        return {
          uri: mongodbUri,
          ...options,
        };
      },
    }),
    MongooseModule.forFeature([
      { name: InvoiceDocument.name, schema: InvoiceSchema },
      { name: InvoiceValidationDocument.name, schema: InvoiceValidationSchema },
    ]),
  ],
  exports: [MongooseModule],
})
export class MongoConfigModule {}
