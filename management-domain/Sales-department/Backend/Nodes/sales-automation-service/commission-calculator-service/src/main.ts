import { NestFactory } from '@nestjs/core';
import { ValidationPipe, Logger } from '@nestjs/common';
import { NestExpressApplication } from '@nestjs/platform-express';
import { AppModule } from './app.module';
import { AllExceptionsFilter } from './interface/interceptors/exception-filter';
import { TransformInterceptor } from './interface/interceptors/transform.interceptor';
import { LoggingInterceptor } from './interface/interceptors/logging.interceptor';
import { ClientKafka, ClientProxy } from '@nestjs/microservices';
import { ConfigService } from '@nestjs/config';

/**
 * Bootstrap the application
 */
async function bootstrap() {
  const logger = new Logger('Bootstrap');

  const app = await NestFactory.create<NestExpressApplication>(AppModule, {
    logger: ['log', 'error', 'warn', 'debug', 'verbose'],
  });

  const configService = app.get(ConfigService);

  // Global prefix
  app.setGlobalPrefix('api/v1');

  // CORS
  app.enableCors({
    origin: configService.get<string>('CORS_ORIGIN') || '*',
    methods: 'GET,HEAD,PUT,PATCH,POST,DELETE,OPTIONS',
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

  // Global filters and interceptors are already applied in app.module.ts
  // but we can add additional ones here if needed

  // Get Kafka client for connecting to microservice
  const kafkaClient = app.get<ClientKafka>('COMMISSION_SERVICE_KAFKA');
  await kafkaClient.connect();

  const port = configService.get<number>('PORT') || 3002;
  const host = configService.get<string>('HOST') || '0.0.0.0';

  await app.listen(port, host);

  logger.log(`
    ============================================
    🚀 Commission Calculator Service Started
    ============================================
    📍 Environment: ${configService.get<string>('NODE_ENV') || 'development'}
    🌐 URL: http://${host}:${port}
    📡 API: http://${host}:${port}/api/v1
    💚 Health: http://${host}:${port}/health
    ============================================
  `);
}

bootstrap().catch(err => {
  console.error('Error starting application:', err);
  process.exit(1);
});
