import { NestFactory } from '@nestjs/core';
import { ValidationPipe, Logger } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';
import { RequestContext as RequestContextService } from './infrastructure/security/request-context.service';
import { TenantInterceptor } from './infrastructure/security/interceptors/tenant.interceptor';

async function bootstrap() {
  const logger = new Logger('Bootstrap');

  const app = await NestFactory.create(AppModule, {
    logger: ['error', 'warn', 'log', 'debug', 'verbose'],
  });

  // Global prefix
  app.setGlobalPrefix('api/v1');

  // Enable CORS
  app.enableCors({
    origin: process.env.CORS_ORIGIN || '*',
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

  // Global interceptors
  const requestContext = app.get(RequestContextService);
  const tenantInterceptor = new TenantInterceptor(requestContext);
  app.useGlobalInterceptors(tenantInterceptor);

  // Swagger documentation
  if (process.env.NODE_ENV !== 'production') {
    const config = new DocumentBuilder()
      .setTitle('Lead Scoring Service API')
      .setDescription('Lead Scoring Microservice - Dynamic lead scoring models and rules')
      .setVersion('1.0')
      .addTag('Lead Scoring', 'Endpoints for scoring leads and managing scores')
      .addTag('Score Models', 'Endpoints for managing scoring models')
      .addTag('Score Rules', 'Endpoints for managing scoring rules')
      .addTag('Score Attributes', 'Endpoints for managing scoring attributes')
      .addBearerAuth()
      .addServer('/api/v1')
      .build();

    const document = SwaggerModule.createDocument(app, config);
    SwaggerModule.setup('api/docs', app, document);

    logger.log('Swagger documentation available at /api/docs');
  }

  // Kafka consumer setup (for incoming events)
  // This would be set up here if needed

  const port = process.env.PORT || 3001;
  await app.listen(port);

  logger.log(`Lead Scoring Service is running on: http://localhost:${port}`);
  logger.log(`API endpoint: http://localhost:${port}/api/v1`);
  logger.log(`Environment: ${process.env.NODE_ENV || 'development'}`);
}

bootstrap().catch((error) => {
  console.error('Error starting application:', error);
  process.exit(1);
});
