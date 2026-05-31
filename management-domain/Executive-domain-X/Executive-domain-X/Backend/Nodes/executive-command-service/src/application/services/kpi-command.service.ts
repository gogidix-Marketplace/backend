import {Injectable, Logger, NotFoundException, ConflictException, Inject} from '@nestjs/common';
import { Kpi, KpiProps } from '../../domain/models/kpi.entity';
import { IKpiRepository } from '../../domain/repositories/kpi-repository.interface';
import { IEventPublisher } from '../../domain/ports/output/event-publisher.interface';
import { ICacheService } from '../../domain/ports/output/cache.interface';
import { CreateKpiDto } from '../dto/requests/create-kpi.dto';
import { UpdateKpiDto } from '../dto/requests/update-kpi.dto';

@Injectable()
export class KpiCommandService {
  private readonly logger = new Logger(KpiCommandService.name);

  constructor(
    @Inject('IKpiRepository')
    private readonly kpiRepository: IKpiRepository,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
    @Inject('ICacheService')
    private readonly cacheService: ICacheService,
  ) {}

  async createKpi(tenantId: string, dto: CreateKpiDto): Promise<Kpi> {
    const existing = await this.kpiRepository.findByNameAndPeriod(tenantId, dto.name, dto.period);
    if (existing) {
      throw new ConflictException(`KPI "${dto.name}" already exists for period "${dto.period}"`);
    }

    const kpi = new Kpi({
      tenantId,
      name: dto.name,
      category: dto.category as any,
      executiveLevel: dto.executiveLevel as any,
      value: dto.value,
      unit: dto.unit,
      period: dto.period,
      target: dto.target,
      previousValue: dto.previousValue,
      dataSources: dto.dataSources || [],
      metadata: dto.metadata || {},
      visible: dto.visible,
      isCalculated: dto.isCalculated,
      status: 'ON_TRACK' as any,
      createdAt: new Date(),
      updatedAt: new Date(),
    } as KpiProps);

    if (kpi.props.previousValue) {
      kpi.calculatePercentChange();
    }
    if (kpi.props.target) {
      kpi.updateStatus();
    }

    const saved = await this.kpiRepository.save(kpi);

    await this.eventPublisher.publishKpiCreated({
      kpiId: saved.id,
      tenantId,
      name: saved.props.name,
      category: saved.props.category as string,
      executiveLevel: saved.props.executiveLevel as string,
    });

    this.logger.log(`KPI created: ${saved.id} for tenant ${tenantId}`);
    return saved;
  }

  async updateKpi(tenantId: string, kpiId: string, dto: UpdateKpiDto): Promise<Kpi> {
    const existing = await this.kpiRepository.findById(kpiId, tenantId);
    if (!existing) {
      throw new NotFoundException('KPI not found');
    }

    if (dto.name !== undefined) existing.props.name = dto.name;
    if (dto.category !== undefined) existing.props.category = dto.category as any;
    if (dto.executiveLevel !== undefined) existing.props.executiveLevel = dto.executiveLevel as any;
    if (dto.value !== undefined) existing.props.value = dto.value;
    if (dto.unit !== undefined) existing.props.unit = dto.unit;
    if (dto.period !== undefined) existing.props.period = dto.period;
    if (dto.target !== undefined) existing.props.target = dto.target;
    if (dto.previousValue !== undefined) existing.props.previousValue = dto.previousValue;
    if (dto.status !== undefined) existing.props.status = dto.status as any;
    if (dto.trend !== undefined) existing.props.trend = dto.trend as any;
    if (dto.visible !== undefined) existing.props.visible = dto.visible;

    if (dto.value !== undefined || dto.target !== undefined) {
      if (existing.props.previousValue || dto.previousValue !== undefined) {
        existing.calculatePercentChange();
      }
      if (existing.props.target) {
        existing.updateStatus();
      }
    }

    existing.props.updatedAt = new Date();
    const updated = await this.kpiRepository.update(existing);

    await this.eventPublisher.publishKpiUpdated({
      kpiId: updated.id,
      tenantId,
      name: updated.props.name,
      value: updated.props.value,
      status: updated.props.status as string,
      executiveLevel: updated.props.executiveLevel as string,
      category: updated.props.category as string,
    });

    await this.cacheService.invalidatePattern(`kpi:${tenantId}:*`);

    this.logger.log(`KPI updated: ${kpiId}`);
    return updated;
  }

  async deleteKpi(tenantId: string, kpiId: string): Promise<void> {
    await this.kpiRepository.delete(kpiId, tenantId);

    await this.eventPublisher.publishKpiDeleted({ kpiId, tenantId });
    await this.cacheService.invalidatePattern(`kpi:${tenantId}:*`);

    this.logger.log(`KPI deleted: ${kpiId}`);
  }

  async batchCreateKpis(tenantId: string, kpis: CreateKpiDto[]): Promise<{ created: number; failed: number; results: Kpi[]; errors: Array<{ name: string; error: string }> }> {
    const results: Kpi[] = [];
    const errors: Array<{ name: string; error: string }> = [];

    for (const kpiData of kpis) {
      try {
        const kpi = await this.createKpi(tenantId, kpiData);
        results.push(kpi);
      } catch (error) {
        errors.push({ name: kpiData.name, error: error.message });
      }
    }

    return { created: results.length, failed: errors.length, results, errors };
  }

  async recalculateKpi(tenantId: string, kpiId: string, newValue: number, metadata?: Record<string, unknown>): Promise<Kpi> {
    const existing = await this.kpiRepository.findById(kpiId, tenantId);
    if (!existing) {
      throw new NotFoundException('KPI not found');
    }

    existing.props.previousValue = existing.props.value;
    existing.props.value = newValue;

    existing.calculatePercentChange();
    existing.updateStatus();
    existing.props.lastCalculatedAt = new Date();
    existing.props.updatedAt = new Date();

    if (metadata) {
      Object.entries(metadata).forEach(([key, value]) => {
        existing.addMetadata(key, value);
      });
    }

    const updated = await this.kpiRepository.update(existing);

    await this.eventPublisher.publishKpiRecalculated({
      kpiId: updated.id,
      tenantId,
      name: updated.props.name,
      value: updated.props.value,
      previousValue: updated.props.previousValue!,
      percentChange: updated.props.percentChange ?? null,
      status: updated.props.status as string,
      executiveLevel: updated.props.executiveLevel as string,
      category: updated.props.category as string,
    });

    return updated;
  }
}
