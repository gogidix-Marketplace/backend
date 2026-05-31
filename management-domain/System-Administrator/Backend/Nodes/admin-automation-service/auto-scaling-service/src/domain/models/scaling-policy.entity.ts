import { AggregateRoot } from '@shared/base/base.entity';
import { CloudProvider } from '../enums/cloud-provider.enum';
import { ScalingMetric } from '../enums/scaling-metric.enum';
import { ScalingOperator } from '../enums/scaling-operator.enum';
import { AdjustmentType } from '../enums/adjustment-type.enum';

export interface ScalingRule {
  metric: ScalingMetric;
  operator: ScalingOperator;
  threshold: number;
  evaluationPeriods: number;
  adjustment: number;
  adjustmentType: AdjustmentType;
}

export class ScalingPolicy extends AggregateRoot {
  constructor(
    public name: string,
    public description: string,
    public resourceId: string,
    public cloudProvider: CloudProvider,
    public enabled: boolean,
    public scaleOutRules: ScalingRule[],
    public scaleInRules: ScalingRule[],
    public cooldownPeriod: number,
    public minInstances: number,
    public maxInstances: number,
    public currentInstances: number,
    public targetInstances: number,
    public lastEvaluatedAt?: Date,
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }

  updateCapacity(newCapacity: number): void {
    this.targetInstances = Math.max(this.minInstances, Math.min(this.maxInstances, newCapacity));
  }

  applyCapacity(): void {
    this.currentInstances = this.targetInstances;
    this.lastEvaluatedAt = new Date();
  }
}
