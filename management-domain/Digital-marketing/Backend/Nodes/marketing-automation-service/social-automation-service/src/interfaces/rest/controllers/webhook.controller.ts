import { Controller, Post, Body, Param, Headers } from '@nestjs/common';

@Controller('api/v1/webhooks')
export class WebhookController {
  @Post('twitter')
  async handleTwitterWebhook(@Body() body: any) { return { received: true }; }
  @Post('facebook')
  async handleFacebookWebhook(@Body() body: any) { return { received: true }; }
  @Post('instagram')
  async handleInstagramWebhook(@Body() body: any) { return { received: true }; }
  @Post('linkedin')
  async handleLinkedinWebhook(@Body() body: any) { return { received: true }; }
}
