import { NestFactory } from '@nestjs/core';
import { ValidationPipe } from '@nestjs/common';
import { SwaggerModule, DocumentBuilder } from '@nestjs/swagger';
import { AppModule } from './app.module';
import { LoggerService } from './shared/logging/logger.service';

async function bootstrap() {
  const app = await NestFactory.create(AppModule, { bufferLogs: true, cors: true });
  const logger = app.get(LoggerService);
  app.useLogger(logger);
  app.setGlobalPrefix('api/v1');
  app.useGlobalPipes(new ValidationPipe({ whitelist: true, forbidNonWhitelisted: true, transform: true, transformOptions: { enableImplicitConversion: true } }));

  const config = new DocumentBuilder()
    .setTitle('Executive Realtime Service')
    .setDescription('WebSocket and Kafka real-time streaming for executives')
    .setVersion('1.0')
    .addBearerAuth()
    .build();
  const document = SwaggerModule.createDocument(app, config);
  SwaggerModule.setup('api/docs', app, document);

  const port = process.env.PORT || 3006;
  await app.listen(port);
  logger.log(`Executive Realtime Service running on: http://localhost:${port}`);
}
bootstrap();
