import { Controller, Post, Body, Req } from '@nestjs/common';
import { EmailOrchestrationService } from '../../../application/services/email-orchestration.service';
import { WebhookEventDto } from '../../../application/dtos/webhook-event.dto';

@Controller('api/v1/webhooks')
export class WebhookController {
  constructor(private readonly service: EmailOrchestrationService) {}

  @Post('sendgrid')
  async handleSendGridWebhook(@Body() events: WebhookEventDto[], @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    await this.service.handleWebhook(events, tenantId);
    return { received: true };
  }

  @Post('ses')
  async handleSesWebhook(@Body() body: any, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    const events: WebhookEventDto[] = (body.EventType ? [body] : Array.isArray(body) ? body : []).map((e: any) => ({
      event: e.event ?? e.EventType ?? e.notificationType,
      email: e.email ?? e.mail?.destination?.[0],
      timestamp: e.timestamp ?? e.mail?.timestamp,
      messageId: e.sgMessageId ?? e.mail?.messageId,
      bounceType: e.bounceType ?? e.bounce?.bounceType,
      bounceSubType: e.bounceSubType ?? e.bounce?.bounceSubType,
      diagnosticCode: e.diagnosticCode ?? e.bounce?.diagnosticCode,
    }));
    await this.service.handleWebhook(events, tenantId);
    return { received: true };
  }
}
