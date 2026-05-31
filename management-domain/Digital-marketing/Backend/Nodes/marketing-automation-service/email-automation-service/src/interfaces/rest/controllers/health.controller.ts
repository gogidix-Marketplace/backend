import { Controller, Get } from '@nestjs/common';

@Controller('health')
export class HealthController {
  @Get()
  health() { return { status: 'ok', service: 'email-automation-service', timestamp: new Date().toISOString() }; }
  @Get('ready')
  ready() { return { status: 'ready' }; }
  @Get('live')
  live() { return { status: 'alive' }; }
  @Get('detailed')
  detailed() { return { status: 'ok', service: 'email-automation-service', version: '2.0.0', timestamp: new Date().toISOString(), uptime: process.uptime(), memory: process.memoryUsage() }; }
}
