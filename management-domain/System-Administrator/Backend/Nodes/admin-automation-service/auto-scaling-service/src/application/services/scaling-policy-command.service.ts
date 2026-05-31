import { Injectable, Logger } from '@nestjs/common';
import { IScalingPolicyCommand } from '@domain/ports/input/scaling-policy.command';
import { IScalingPolicyRepository } from '@domain/repositories/scaling-policy-repository.interface';
import { ScalingPolicy } from '@domain/models/scaling-policy.entity';
import { CloudProvider } from '@domain/enums/cloud-provider.enum';

@Injectable()
export class ScalingPolicyCommandService implements IScalingPolicyCommand {
  private readonly logger = new Logger(ScalingPolicyCommandService.name);

  constructor(private readonly policyRepository: IScalingPolicyRepository) {}

  async createPolicy(data: any): Promise<any> {
    const policy = new ScalingPolicy(
      data.name, data.description, data.resourceId,
      data.cloudProvider as CloudProvider, data.enabled ?? true,
      data.scaleOutRules || [], data.scaleInRules || [],
      data.cooldownPeriod || 300000,
      data.minInstances, data.maxInstances,
      data.currentInstances, data.targetInstances,
    );
    return this.policyRepository.save(policy);
  }

  async updatePolicy(id: string, data: any): Promise<any> {
    return this.policyRepository.findByIdAndUpdate(id, data);
  }

  async deletePolicy(id: string): Promise<void> {
    await this.policyRepository.findByIdAndDelete(id);
  }

  async enablePolicy(id: string): Promise<any> {
    return this.policyRepository.findByIdAndUpdate(id, { enabled: true });
  }

  async disablePolicy(id: string): Promise<any> {
    return this.policyRepository.findByIdAndUpdate(id, { enabled: false });
  }
}
