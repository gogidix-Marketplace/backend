import { Injectable, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { ICloudProviderService } from '@domain/ports/output/cloud-provider.interface';
import { ScalingMetrics } from '@domain/models/scaling-event.entity';
import { CloudInstance } from '@domain/models/cloud-instance.entity';

@Injectable()
export class AzureAutoScalingService implements ICloudProviderService {
  private readonly logger = new Logger(AzureAutoScalingService.name);

  constructor(private readonly configService: ConfigService) {}

  async getMetrics(resourceId: string, periodMinutes = 5): Promise<ScalingMetrics> {
    this.logger.debug(`Getting Azure metrics for ${resourceId}`);
    return {
      cpuUtilization: 0, memoryUtilization: 0, diskUtilization: 0,
      networkInBytes: 0, networkOutBytes: 0, requestCount: 0, timestamp: new Date(),
    };
  }

  async setCapacity(resourceId: string, desiredCapacity: number): Promise<void> {
    this.logger.log(`Setting Azure VMSS ${resourceId} capacity to ${desiredCapacity}`);
  }

  async getInstances(resourceId?: string): Promise<CloudInstance[]> {
    return [];
  }
}
