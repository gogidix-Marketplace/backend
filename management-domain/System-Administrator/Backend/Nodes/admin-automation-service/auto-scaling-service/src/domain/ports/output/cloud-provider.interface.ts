import { ScalingMetrics } from '../../models/scaling-event.entity';
import { CloudInstance } from '../../models/cloud-instance.entity';

export interface ICloudProviderService {
  getMetrics(resourceId: string, periodMinutes?: number): Promise<ScalingMetrics>;
  setCapacity(resourceId: string, desiredCapacity: number): Promise<void>;
  getInstances(resourceId?: string): Promise<CloudInstance[]>;
}
