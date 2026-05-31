import { ScalingPolicy } from '../models/scaling-policy.entity';

export interface IScalingPolicyRepository {
  save(policy: ScalingPolicy): Promise<ScalingPolicy>;
  findById(id: string): Promise<ScalingPolicy | null>;
  findEnabled(): Promise<ScalingPolicy[]>;
  findByFilters(filters: any, limit: number, skip: number): Promise<{ data: ScalingPolicy[]; total: number }>;
  findByIdAndUpdate(id: string, update: any): Promise<ScalingPolicy | null>;
  findByIdAndDelete(id: string): Promise<ScalingPolicy | null>;
}
