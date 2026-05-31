import { NestFactory } from '@nestjs/core';
import { Logger } from '@nestjs/common';
import { AppModule } from './app.module';

async function bootstrap() {
  const logger = new Logger('Bootstrap');
  const app = await NestFactory.create(AppModule);
  app.enableCors();
  const port = process.env.PORT || 3002;
  await app.listen(port);
  logger.log(`Chatbot Service listening on port ${port}`);
}

bootstrap();
