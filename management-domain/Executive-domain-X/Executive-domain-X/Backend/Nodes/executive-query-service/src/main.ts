import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';
import { LoggerService } from './shared/logging/logger.service';

async function bootstrap() {
  const app = await NestFactory.create(AppModule, { bufferLogs: true });
  const logger = app.get(LoggerService);
  app.useLogger(logger);
  app.enableCors({ origin: process.env.ALLOWED_ORIGINS?.split(',') || '*', credentials: true });
  app.setGlobalPrefix('api/v1');
  app.useGlobalPipes(new ValidationPipe({ whitelist: true, forbidNonWhitelisted: true, transform: true, transformOptions: { enableImplicitConversion: true } }));

  const config = new DocumentBuilder()
    .setTitle('Executive Query Service')
    .setDescription('CQRS read operations for executive KPIs')
    .setVersion('1.0')
    .addTag('kpi')
    .addBearerAuth()
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api/docs', app, document);

  const port = process.env.PORT || 3005;
  await app.listen(port);
  logger.log(`Executive Query Service is running on: http://localhost:${port}`);
  logger.log(`API Documentation available at: http://localhost:${port}/api/docs`);
}
bootstrap();
