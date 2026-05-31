import { NestFactory } from '@nestjs/core';
import { AppModule } from './app.module';
import { AuthMiddleware } from './interfaces/rest/middleware/auth.middleware';

async function bootstrap() {
  const app = await NestFactory.create(AppModule);
  app.use(AuthMiddleware);
  app.enableCors();
  const port = process.env.PORT ?? 3002;
  await app.listen(port);
  console.log(`Social Automation Service running on port ${port}`);
}
bootstrap();
