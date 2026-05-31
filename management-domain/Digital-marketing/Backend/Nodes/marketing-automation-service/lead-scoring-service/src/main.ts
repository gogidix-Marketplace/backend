import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { AuthMiddleware } from './interfaces/rest/middleware/auth.middleware';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(AuthMiddleware);
  app.enableCors();
  const port = process.env.PORT ?? 3003;
  await app.listen(port);
  console.log(`Lead Scoring Service running on port ${port}`);
}
bootstrap();
