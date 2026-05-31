import { Module } from '@nestjs/common';
import { SocialController } from './controllers/social.controller';
import { WebhookController } from './controllers/webhook.controller';
import { HealthController } from './controllers/health.controller';
@Module({ controllers: [SocialController, WebhookController, HealthController] })
export class RestModule {}
