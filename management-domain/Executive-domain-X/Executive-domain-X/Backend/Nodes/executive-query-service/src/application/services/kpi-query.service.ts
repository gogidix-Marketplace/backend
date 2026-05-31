import {Injectable, Logger, NotFoundException, Inject} from '@nestjs/common';
import { Kpi } from '../../domain/models/kpi.entity';
import { IKpiRepository, FindOptions, KpiSummary } from '../../domain/repositories/kpi-repository.interface';
import { ICacheService } from '../../domain/ports/output/cache.interface';

@Injectable()
export class KpiQueryService {
  private readonly logger = new Logger(KpiQueryService.name);

  constructor(
    @Inject('IKpiRepository')
    private readonly kpiRepository: IKpiRepository,
    @Inject('ICacheService')
    private readonly cacheService: ICacheService,
  ) {}

  async getKpiById(tenantId: string, kpiId: string): Promise<Kpi> {
    const cacheKey = `kpi:${tenantId}:${kpiId}`;
    const cached = await this.cacheService.get(cacheKey);
    if (cached) return cached as Kpi;

    const kpi = await this.kpiRepository.findById(kpiId, tenantId);
    if (!kpi) throw new NotFoundException('KPI not found');

    await this.cacheService.set(cacheKey, kpi.toJSON(), 300);
    return kpi;
  }

  async getKpis(tenantId: string, options: FindOptions = {}): Promise<{ data: Kpi[]; pagination: { page: number; limit: number; total: number; pages: number } }> {
    const cacheKey = `kpi:${tenantId}:list:${JSON.stringify(options)}`;
    const cached = await this.cacheService.get(cacheKey);
    if (cached) return cached as any;

    const kpis = await this.kpiRepository.findByTenant(tenantId, options);
    const total = await this.kpiRepository.countByTenant(tenantId);
    const result = {
      data: kpis,
      pagination: { page: options.page || 1, limit: options.limit || 20, total, pages: Math.ceil(total / (options.limit || 20)) },
    };

    await this.cacheService.set(cacheKey, result, 60);
    return result;
  }

  async getKpiSummary(tenantId: string): Promise<KpiSummary> {
    const cacheKey = `kpi:${tenantId}:summary`;
    const cached = await this.cacheService.get(cacheKey);
    if (cached) return cached as KpiSummary;

    const summary = await this.kpiRepository.getSummary(tenantId);
    await this.cacheService.set(cacheKey, summary, 300);
    return summary;
  }

  async getDashboardKpis(tenantId: string, level: string = 'CEO'): Promise<{ kpis: Kpi[]; summary: { total: number; ahead: number; onTrack: number; atRisk: number; behind: number } }> {
    const cacheKey = `kpi:${tenantId}:dashboard:${level}`;
    const cached = await this.cacheService.get(cacheKey);
    if (cached) return cached as any;

    const kpis = await this.kpiRepository.getDashboardKpis(tenantId, level);
    const summary = { total: kpis.length, ahead: 0, onTrack: 0, atRisk: 0, behind: 0 };
    for (const kpi of kpis) {
      const s = (kpi.props.status as string).toLowerCase();
      if (s in summary) (summary as any)[s]++;
    }
    const result = { kpis, summary };
    await this.cacheService.set(cacheKey, result, 300);
    return result;
  }

  async getKpiTrends(tenantId: string, kpiId: string, periods: number = 12): Promise<{ current: Kpi; historical: Kpi[]; trend: string }> {
    const cacheKey = `kpi:${tenantId}:trends:${kpiId}:${periods}`;
    const cached = await this.cacheService.get(cacheKey);
    if (cached) return cached as any;

    const kpi = await this.kpiRepository.findById(kpiId, tenantId);
    if (!kpi) throw new NotFoundException('KPI not found');

    const historical = await this.kpiRepository.getTrends(tenantId, kpiId, periods);
    const trend = this.calculateTrend([kpi, ...historical]);
    const result = { current: kpi, historical: historical.reverse(), trend };
    await this.cacheService.set(cacheKey, result, 300);
    return result;
  }

  async searchKpis(tenantId: string, searchTerm: string, limit: number = 10): Promise<Kpi[]> {
    return this.kpiRepository.search(tenantId, { searchTerm, limit: limit as any });
  }

  private calculateTrend(dataPoints: Kpi[]): string {
    if (dataPoints.length < 2) return 'STABLE';
    const sorted = [...dataPoints].sort((a, b) => a.props.period.localeCompare(b.props.period));
    let upCount = 0, downCount = 0;
    for (let i = 1; i < sorted.length; i++) {
      if (sorted[i].props.value > sorted[i - 1].props.value) upCount++;
      else if (sorted[i].props.value < sorted[i - 1].props.value) downCount++;
    }
    if (upCount > downCount * 1.5) return 'UP';
    if (downCount > upCount * 1.5) return 'DOWN';
    return 'STABLE';
  }
}
