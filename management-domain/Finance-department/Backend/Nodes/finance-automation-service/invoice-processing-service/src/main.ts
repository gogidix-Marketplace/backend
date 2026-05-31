import { NestFactory } from '@nestjs/core';
import { ValidationPipe, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Transport, MicroserviceOptions } from '@nestjs/microservices';
import { AppModule } from './app.module';
import { GlobalExceptionFilter } from './interfaces/http/exceptions/global.exception.filter';

async function bootstrap() {
  const logger = new Logger('Bootstrap');

  const app = await NestFactory.create(AppModule, {
    logger: ['error', 'warn', 'log', 'debug', 'verbose'],
  });

  const configService = app.get(ConfigService);

  // Global prefix
  app.setGlobalPrefix('api/v1');

  // Enable CORS
  app.enableCors({
    origin: configService.get<string>('CORS_ORIGIN', '*'),
    methods: ['GET', 'POST', 'PUT', 'PATCH', 'DELETE', 'OPTIONS'],
    credentials: true,
  });

  // Validation pipe
  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
      transformOptions: {
        enableImplicitConversion: true,
      },
    }),
  );

  // Exception filter
  app.useGlobalFilters(new GlobalExceptionFilter());

  // Microservice setup for Kafka
  const kafkaBrokers = configService
    .get<string>('KAFKA_BROKERS', 'localhost:9092')
    .split(',');

  app.connectMicroservice<MicroserviceOptions>({
    transport: Transport.KAFKA,
    options: {
      client: {
        clientId: configService.get<string>('KAFKA_CLIENT_ID', 'invoice-processing-service'),
        brokers: kafkaBrokers,
      },
      consumer: {
        groupId: configService.get<string>('KAFKA_CONSUMER_GROUP', 'invoice-processing-group'),
      },
    },
  });

  const port = configService.get<number>('PORT', 3003);

  await app.startAllMicroservices();
  await app.listen(port);

  logger.log(`Invoice Processing Service is running on: http://localhost:${port}`);
  logger.log(`API available at: http://localhost:${port}/api/v1`);
  logger.log(`Kafka brokers: ${kafkaBrokers.join(', ')}`);
  logger.log(`Environment: ${configService.get<string>('NODE_ENV', 'development')}`);
}

bootstrap();
