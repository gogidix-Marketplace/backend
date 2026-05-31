import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { AuthMiddleware } from './interfaces/rest/middleware/auth.middleware';
import { RateLimiterMiddleware } from './interfaces/rest/middleware/rate-limiter.middleware';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(AuthMiddleware);
  app.use(RateLimiterMiddleware);
  app.enableCors();
  const port = process.env.PORT ?? 3001;
  await app.listen(port);
  console.log(`Email Automation Service running on port ${port}`);
}
bootstrap();
