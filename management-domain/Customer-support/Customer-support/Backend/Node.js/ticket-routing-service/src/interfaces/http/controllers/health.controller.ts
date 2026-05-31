import { Controller, Get } from '@nestjs/common';

@Controller('api/v1/health')
export class HealthController {
  @Get()
  check() {
    return {
      status: 'ok',
      service: 'ticket-routing-service',
      timestamp: new Date().toISOString(),
      uptime: process.uptime(),
    };
  }
}
