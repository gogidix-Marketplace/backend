import 'reflect-metadata';
import { NestFactory } from '@nestjs/core';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { ValidationPipe, Logger } from '@nestjs/common';
import { AppModule } from './app.module';
import { CacheAdapter } from './infrastructure/persistence/redis/cache.adapter';

async function bootstrap() {
  const logger = new Logger('Bootstrap');
  const app = await NestFactory.create(AppModule);

  app.enableCors({
    origin: process.env.CORS_ORIGIN || 'http://localhost:3000',
    credentials: true,
  });

  app.useGlobalPipes(
    new ValidationPipe({
      whitelist: true,
      forbidNonWhitelisted: true,
      transform: true,
    }),
  );

  const config = new DocumentBuilder()
    .setTitle('Chatbot Service')
    .setDescription('AI-powered customer support chatbot service with NLP capabilities')
    .setVersion('1.0.0')
    .addBearerAuth()
    .build();

  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api/docs', app, document);

  try {
    const cacheAdapter = app.get(CacheAdapter);
    await cacheAdapter.connect();
  } catch {}

  const port = process.env.PORT || 8112;
  const host = process.env.HOST || 'localhost';

  await app.listen(port as number, host);
  logger.log(`Chatbot Service listening on http://${host}:${port}`);
  logger.log(`Swagger docs available at http://${host}:${port}/api/docs`);
  logger.log(`Environment: ${process.env.NODE_ENV || 'development'}`);
}

bootstrap();
