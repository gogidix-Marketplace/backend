import { Injectable, Inject, Logger } from '@nestjs/common';
import { IHealthQueryPort } from '@domain/ports/input';
import { IKnowledgeBasePort } from '@domain/ports/output';
import mongoose from 'mongoose';

@Injectable()
export class HealthApplicationService implements IHealthQueryPort {
  private readonly logger = new Logger(HealthApplicationService.name);

  constructor(
    @Inject('IKnowledgeBasePort')
    private readonly knowledgeBasePort: IKnowledgeBasePort,
  ) {}

  async healthCheck() {
    const mongoState = mongoose.connection.readyState;
    const mongoStatus = mongoState === 1 ? 'connected' : mongoState === 2 ? 'connecting' : mongoState === 3 ? 'disconnecting' : 'disconnected';
    const kbHealthy = await this.knowledgeBasePort.healthCheck();

    return {
      mongodb: mongoStatus,
      redis: 'connected',
      knowledgeBase: kbHealthy ? 'connected' : 'disconnected',
    };
  }

  async systemStatus() {
    const health = await this.healthCheck();
    const memoryUsage = process.memoryUsage();
    const uptime = process.uptime();

    return {
      success: true,
      timestamp: new Date(),
      system: {
        nodeVersion: process.version,
        platform: process.platform,
        uptime: Math.floor(uptime),
        memory: {
          used: Math.round(memoryUsage.heapUsed / 1024 / 1024),
          total: Math.round(memoryUsage.heapTotal / 1024 / 1024),
          rss: Math.round(memoryUsage.rss / 1024 / 1024),
        },
      },
      services: {
        mongodb: { status: health.mongodb },
        redis: { status: health.redis },
        knowledgeBase: { status: health.knowledgeBase },
      },
    };
  }

  async readinessCheck(): Promise<boolean> {
    const health = await this.healthCheck();
    return health.mongodb === 'connected';
  }

  livenessCheck(): boolean {
    return true;
  }
}
