import { BaseEntity } from '@shared/base/base.entity';
import { CloudProvider } from '../enums/cloud-provider.enum';
import { ScalingMetrics } from './scaling-event.entity';

export class MetricsHistory extends BaseEntity {
  constructor(
    public resourceId: string,
    public cloudProvider: CloudProvider,
    public metrics: ScalingMetrics[],
    public aggregatedMetrics: {
      avgCpuUtilization: number;
      avgMemoryUtilization: number;
      maxCpuUtilization: number;
      maxMemoryUtilization: number;
      minCpuUtilization: number;
      minMemoryUtilization: number;
    },
    public collectedAt: Date,
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }
}
