import { ScalingEventType } from '../enums/scaling-event-type.enum';

export class ScalingExecutedEvent {
  constructor(
    public readonly policyId: string,
    public readonly policyName: string,
    public readonly eventType: ScalingEventType,
    public readonly previousCapacity: number,
    public readonly newCapacity: number,
    public readonly triggeredBy: string,
    public readonly timestamp: Date = new Date(),
  ) {}
}
