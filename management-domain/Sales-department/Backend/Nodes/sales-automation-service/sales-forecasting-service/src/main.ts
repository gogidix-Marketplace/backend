import { NestFactory } from '@nestjs/core';
import { ValidationPipe, VersioningType } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);

  // Global prefix
  const apiPrefix = process.env.API_PREFIX || 'api/v1';
  app.setGlobalPrefix(apiPrefix);

  // Enable versioning
  app.enableVersioning({
    type: VersioningType.URI,
  });

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

  // Swagger documentation
  if (process.env.SWAGGER_ENABLED === 'true') {
    const config = new DocumentBuilder()
      .setTitle('Sales Forecasting Service')
      .setDescription('Sales Forecasting Microservice - Part of Gogidix Ecosystem')
      .setVersion('1.0')
      .addTag('forecasts', 'Sales forecast management')
      .addBearerAuth()
      .build();

    const document = SwaggerModule.createDocument(app, config);
    SwaggerModule.setup('api/docs', app, document);
  }

  const port = process.env.PORT || 3004;
  await app.listen(port);

  console.log(`
  ┌─────────────────────────────────────────────┐
  │                                             │
  │   Sales Forecasting Service                 │
  │   Gogidix Ecosystem                         │
  │                                             │
  │   Server running on port ${port}               │
  │   API: http://localhost:${port}/${apiPrefix}          │
  │   Docs: http://localhost:${port}/api/docs          │
  │                                             │
  └─────────────────────────────────────────────┘
  `);
}

bootstrap();
