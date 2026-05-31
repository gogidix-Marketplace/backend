import { Module } from '@nestjs/common';
import { EmailController } from './controllers/email.controller';
import { WebhookController } from './controllers/webhook.controller';
import { HealthController } from './controllers/health.controller';

@Module({
  controllers: [EmailController, WebhookController, HealthController],
})
export class RestModule {}
