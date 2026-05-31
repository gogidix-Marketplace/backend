import { BaseEntity } from '@shared/base/base.entity';
import { ScalingEventType } from '../enums/scaling-event-type.enum';
import { ScalingStatus } from '../enums/scaling-status.enum';

export interface ScalingMetrics {
  cpuUtilization: number;
  memoryUtilization: number;
  diskUtilization: number;
  networkInBytes: number;
  networkOutBytes: number;
  requestCount: number;
  timestamp: Date;
}

export class ScalingEvent extends BaseEntity {
  constructor(
    public policyId: string,
    public policyName: string,
    public eventType: ScalingEventType,
    public previousCapacity: number,
    public newCapacity: number,
    public triggeredBy: string,
    public metrics: ScalingMetrics,
    public status: ScalingStatus,
    public error?: string,
    public startedAt: Date = new Date(),
    public completedAt?: Date,
    public metadata: Record<string, unknown> = {},
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }

  complete(): void {
    this.status = ScalingStatus.COMPLETED;
    this.completedAt = new Date();
  }

  fail(error: string): void {
    this.status = ScalingStatus.FAILED;
    this.error = error;
  }
}
