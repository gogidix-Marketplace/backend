import {Injectable, Logger, Inject} from '@nestjs/common';
import { Cron } from '@nestjs/schedule';
import { IScalingPolicyRepository } from '@domain/repositories/scaling-policy-repository.interface';
import { IScalingEventRepository } from '@domain/repositories/scaling-event-repository.interface';
import { IMetricsHistoryRepository } from '@domain/repositories/metrics-history-repository.interface';
import { ICloudProviderService } from '@domain/ports/output/cloud-provider.interface';
import { ICacheService } from '@domain/ports/output/cache.interface';
import { IEventPublisher } from '@domain/ports/output/event-publisher.interface';
import { INotificationService } from '@domain/ports/output/notification.interface';
import { ScalingPolicy, ScalingRule } from '@domain/models/scaling-policy.entity';
import { ScalingEvent, ScalingMetrics } from '@domain/models/scaling-event.entity';
import { MetricsHistory } from '@domain/models/metrics-history.entity';
import { CloudProvider } from '@domain/enums/cloud-provider.enum';
import { ScalingEventType } from '@domain/enums/scaling-event-type.enum';
import { ScalingStatus } from '@domain/enums/scaling-status.enum';
import { ScalingMetric } from '@domain/enums/scaling-metric.enum';
import { ScalingExecutedEvent } from '@domain/events/scaling-executed.event';
import { ScalingFailedEvent } from '@domain/events/scaling-failed.event';

@Injectable()
export class ScalingEngineService {
  private readonly logger = new Logger(ScalingEngineService.name);

  constructor(
    @Inject('IScalingPolicyRepository')
    private readonly policyRepository: IScalingPolicyRepository,
    @Inject('IScalingEventRepository')
    private readonly eventRepository: IScalingEventRepository,
    @Inject('IMetricsHistoryRepository')
    private readonly metricsHistoryRepository: IMetricsHistoryRepository,
    private readonly cloudProviders: Map<string, ICloudProviderService>,
    @Inject('ICacheService')
    private readonly cacheService: ICacheService,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
    @Inject('INotificationService')
    private readonly notificationService: INotificationService,
  ) {}

  @Cron('*/1 * * * *')
  async collectMetrics(): Promise<void> {
    const policies = await this.policyRepository.findEnabled();
    for (const policy of policies) {
      try {
        const provider = this.getCloudProvider(policy.cloudProvider);
        const metrics = await provider.getMetrics(policy.resourceId);
        await this.cacheService.set(`metrics:${policy.resourceId}`, JSON.stringify(metrics), 300);
        await this.storeMetricsHistory(policy, metrics);
      } catch (error) {
        this.logger.error(`Error collecting metrics for ${policy.name}`, error);
      }
    }
  }

  @Cron('*/2 * * * *')
  async evaluatePolicies(): Promise<void> {
    const policies = await this.policyRepository.findEnabled();
    const now = new Date();

    for (const policy of policies) {
      try {
        if (policy.lastEvaluatedAt) {
          const elapsed = now.getTime() - policy.lastEvaluatedAt.getTime();
          if (elapsed < policy.cooldownPeriod) continue;
        }
        await this.evaluatePolicy(policy);
      } catch (error) {
        this.logger.error(`Error evaluating policy ${policy.name}`, error);
      }
    }
  }

  private async evaluatePolicy(policy: ScalingPolicy): Promise<void> {
    const cached = await this.cacheService.get(`metrics:${policy.resourceId}`);
    if (!cached) return;

    const metrics: ScalingMetrics = JSON.parse(cached);
    const scaleOutTrigger = this.evaluateRules(policy.scaleOutRules, metrics);
    const scaleInTrigger = this.evaluateRules(policy.scaleInRules, metrics);

    let newCapacity = policy.currentInstances;
    let eventType: ScalingEventType = ScalingEventType.EVALUATION_PASSED;
    let triggeredBy = '';

    if (scaleOutTrigger) {
      newCapacity = this.calculateNewCapacity(policy, policy.scaleOutRules);
      eventType = ScalingEventType.SCALE_OUT;
      triggeredBy = scaleOutTrigger;
    } else if (scaleInTrigger) {
      newCapacity = this.calculateNewCapacity(policy, policy.scaleInRules);
      eventType = ScalingEventType.SCALE_IN;
      triggeredBy = scaleInTrigger;
    }

    if (eventType === ScalingEventType.SCALE_OUT || eventType === ScalingEventType.SCALE_IN) {
      await this.executeScaling(policy, newCapacity, eventType, triggeredBy, metrics);
    }

    await this.policyRepository.findByIdAndUpdate((policy as any).id || (policy as any)._id, {
      lastEvaluatedAt: new Date(),
    });
  }

  private evaluateRules(rules: ScalingRule[], metrics: ScalingMetrics): string | null {
    for (const rule of rules) {
      const metricValue = this.getMetricValue(rule.metric, metrics);
      let triggered = false;
      switch (rule.operator) {
        case 'greater_than': triggered = metricValue > rule.threshold; break;
        case 'less_than': triggered = metricValue < rule.threshold; break;
        case 'equals': triggered = Math.abs(metricValue - rule.threshold) < 0.01; break;
      }
      if (triggered) return `${rule.metric} ${rule.operator} ${rule.threshold}%`;
    }
    return null;
  }

  private getMetricValue(metricName: ScalingMetric, metrics: ScalingMetrics): number {
    const map: Record<string, number> = {
      cpu: metrics.cpuUtilization,
      memory: metrics.memoryUtilization,
      disk: metrics.diskUtilization,
      network: (metrics.networkInBytes + metrics.networkOutBytes) / 1000000,
      requests: metrics.requestCount,
    };
    return map[metricName] || 0;
  }

  private calculateNewCapacity(policy: ScalingPolicy, rules: ScalingRule[]): number {
    let newCapacity = policy.currentInstances;
    for (const rule of rules) {
      switch (rule.adjustmentType) {
        case 'change_in_capacity': newCapacity += rule.adjustment; break;
        case 'exact_capacity': newCapacity = rule.adjustment; break;
        case 'percent_change_in_capacity': newCapacity += Math.round((policy.currentInstances * rule.adjustment) / 100); break;
      }
    }
    return Math.max(policy.minInstances, Math.min(policy.maxInstances, newCapacity));
  }

  private async executeScaling(policy: ScalingPolicy, newCapacity: number, eventType: ScalingEventType, triggeredBy: string, metrics: ScalingMetrics): Promise<void> {
    const eventId = (policy as any).id || (policy as any)._id;
    const scalingEvent = new ScalingEvent(
      eventId, policy.name, eventType, policy.currentInstances, newCapacity,
      triggeredBy, metrics, ScalingStatus.IN_PROGRESS,
    );

    try {
      await this.eventRepository.save(scalingEvent);
      const provider = this.getCloudProvider(policy.cloudProvider);
      await provider.setCapacity(policy.resourceId, newCapacity);
      policy.updateCapacity(newCapacity);
      policy.applyCapacity();
      await this.policyRepository.findByIdAndUpdate(eventId, {
        currentInstances: newCapacity, targetInstances: newCapacity, lastEvaluatedAt: new Date(),
      });

      await this.eventPublisher.publish(new ScalingExecutedEvent(
        eventId, policy.name, eventType, scalingEvent.previousCapacity, newCapacity, triggeredBy,
      ));
    } catch (error) {
      await this.eventPublisher.publish(new ScalingFailedEvent(eventId, policy.name, (error as Error).message));
      await this.notificationService.notifyScalingEvent(scalingEvent);
    }
  }

  private async storeMetricsHistory(policy: ScalingPolicy, metrics: ScalingMetrics): Promise<void> {
    const history = new MetricsHistory(
      policy.resourceId, policy.cloudProvider,
      [metrics],
      {
        avgCpuUtilization: metrics.cpuUtilization,
        avgMemoryUtilization: metrics.memoryUtilization,
        maxCpuUtilization: metrics.cpuUtilization,
        maxMemoryUtilization: metrics.memoryUtilization,
        minCpuUtilization: metrics.cpuUtilization,
        minMemoryUtilization: metrics.memoryUtilization,
      },
      new Date(),
    );
    await this.metricsHistoryRepository.save(history);
  }

  private getCloudProvider(provider: CloudProvider): ICloudProviderService {
    const service = this.cloudProviders.get(provider);
    if (!service) throw new Error(`No cloud provider service for ${provider}`);
    return service;
  }
}
