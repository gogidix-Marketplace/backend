import {Injectable, Logger, Inject} from '@nestjs/common';
import { IScalingPolicyQuery } from '@domain/ports/input/scaling-policy.query';
import { IScalingPolicyRepository } from '@domain/repositories/scaling-policy-repository.interface';
import { IScalingEventRepository } from '@domain/repositories/scaling-event-repository.interface';
import { IMetricsHistoryRepository } from '@domain/repositories/metrics-history-repository.interface';
import { ICacheService } from '@domain/ports/output/cache.interface';

@Injectable()
export class ScalingPolicyQueryService implements IScalingPolicyQuery {
  private readonly logger = new Logger(ScalingPolicyQueryService.name);

  constructor(
    @Inject('IScalingPolicyRepository')
    private readonly policyRepository: IScalingPolicyRepository,
    @Inject('IScalingEventRepository')
    private readonly eventRepository: IScalingEventRepository,
    @Inject('IMetricsHistoryRepository')
    private readonly metricsHistoryRepository: IMetricsHistoryRepository,
    @Inject('ICacheService')
    private readonly cacheService: ICacheService,
  ) {}

  async getPolicies(filters: any = {}): Promise<{ data: any[]; total: number }> {
    return this.policyRepository.findByFilters(filters, filters.limit || 50, filters.skip || 0);
  }

  async getPolicyById(id: string): Promise<any> {
    return this.policyRepository.findById(id);
  }

  async getEvents(policyId?: string, limit = 50): Promise<any[]> {
    if (policyId) return this.eventRepository.findByPolicyId(policyId, limit);
    return [];
  }

  async getMetricsHistory(resourceId: string, hours = 24): Promise<any[]> {
    return this.metricsHistoryRepository.findByResourceId(resourceId, hours);
  }

  async getCurrentMetrics(resourceId: string): Promise<any> {
    const cached = await this.cacheService.get(`metrics:${resourceId}`);
    return cached ? JSON.parse(cached) : null;
  }
}
