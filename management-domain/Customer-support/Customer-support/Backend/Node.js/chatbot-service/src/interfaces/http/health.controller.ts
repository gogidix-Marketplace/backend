import { Controller, Get } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { HealthApplicationService } from '@application/services/health.service';

@ApiTags('Health')
@Controller('api/v1/health')
export class HealthController {
  constructor(private readonly healthService: HealthApplicationService) {}

  @Get()
  @Get('health')
  @ApiOperation({ summary: 'Health check' })
  async healthCheck() {
    const health = await this.healthService.healthCheck();
    const isHealthy = health.mongodb === 'connected';
    return {
      success: isHealthy,
      status: isHealthy ? 'healthy' : 'unhealthy',
      timestamp: new Date(),
      services: health,
    };
  }

  @Get('status')
  @ApiOperation({ summary: 'Detailed system status' })
  async systemStatus() {
    return this.healthService.systemStatus();
  }

  @Get('readiness')
  @ApiOperation({ summary: 'Readiness check' })
  async readinessCheck() {
    const isReady = await this.healthService.readinessCheck();
    return { ready: isReady };
  }

  @Get('liveness')
  @ApiOperation({ summary: 'Liveness check' })
  async livenessCheck() {
    return { alive: this.healthService.livenessCheck(), timestamp: new Date() };
  }
}
