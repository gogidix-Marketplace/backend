import { Controller, Get } from '@nestjs/common';

@Controller('health')
export class HealthController {
  @Get() health() { return { status: 'ok', service: 'social-automation-service', timestamp: new Date().toISOString() }; }
  @Get('ready') ready() { return { status: 'ready' }; }
  @Get('live') live() { return { status: 'alive' }; }
}
