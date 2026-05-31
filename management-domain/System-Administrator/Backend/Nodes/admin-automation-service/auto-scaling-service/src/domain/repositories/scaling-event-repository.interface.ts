import { ScalingEvent } from '../models/scaling-event.entity';

export interface IScalingEventRepository {
  save(event: ScalingEvent): Promise<ScalingEvent>;
  findByPolicyId(policyId: string, limit?: number): Promise<ScalingEvent[]>;
  findById(id: string): Promise<ScalingEvent | null>;
}
