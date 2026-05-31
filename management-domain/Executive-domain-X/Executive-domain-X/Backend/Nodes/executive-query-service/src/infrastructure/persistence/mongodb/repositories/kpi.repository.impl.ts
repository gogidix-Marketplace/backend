import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Kpi } from '../../../../domain/models/kpi.entity';
import { IKpiRepository, FindOptions, KpiSearchFilters, KpiSummary } from '../../../../domain/repositories/kpi-repository.interface';
import { KpiDocument } from '../mongoose/kpi.schema';

@Injectable()
export class KpiRepository implements IKpiRepository {
  constructor(@InjectModel('Kpi') private readonly kpiModel: Model<KpiDocument>) {}

  async findById(id: string, tenantId: string): Promise<Kpi | null> {
    const doc = await this.kpiModel.findOne({ _id: id, tenantId });
    return doc ? this.toEntity(doc) : null;
  }

  async findByTenant(tenantId: string, options?: FindOptions): Promise<Kpi[]> {
    const query = this.kpiModel.find({ tenantId });
    if (options?.sortBy) query.sort({ [options.sortBy]: options.sortOrder === 'asc' ? 1 : -1 });
    if (options?.page && options?.limit) query.skip((options.page - 1) * options.limit).limit(options.limit);
    const docs = await query.exec();
    return docs.map(d => this.toEntity(d));
  }

  async findByLevel(tenantId: string, level: string): Promise<Kpi[]> {
    const query = level === 'CEO'
      ? { tenantId }
      : { $or: [{ tenantId, executiveLevel: level }, { tenantId, executiveLevel: 'ALL' }] };
    const docs = await this.kpiModel.find(query).exec();
    return docs.map(d => this.toEntity(d));
  }

  async getSummary(tenantId: string): Promise<KpiSummary> {
    const results = await this.kpiModel.aggregate([
      { $match: { tenantId } },
      { $group: { _id: '$category', total: { $sum: 1 }, ahead: { $sum: { $cond: [{ $eq: ['$status', 'AHEAD'] }, 1, 0] } }, onTrack: { $sum: { $cond: [{ $eq: ['$status', 'ON_TRACK'] }, 1, 0] } }, atRisk: { $sum: { $cond: [{ $eq: ['$status', 'AT_RISK'] }, 1, 0] } }, behind: { $sum: { $cond: [{ $eq: ['$status', 'BEHIND'] }, 1, 0] } } } },
    ]).exec();
    const summary: KpiSummary = {};
    for (const r of results) { summary[r._id] = { total: r.total, ahead: r.ahead, onTrack: r.onTrack, atRisk: r.atRisk, behind: r.behind }; }
    return summary;
  }

  async getDashboardKpis(tenantId: string, level: string): Promise<Kpi[]> {
    return this.findByLevel(tenantId, level);
  }

  async getTrends(tenantId: string, kpiId: string, periods: number): Promise<Kpi[]> {
    const kpi = await this.findById(kpiId, tenantId);
    if (!kpi) return [];
    const docs = await this.kpiModel.find({ tenantId, name: kpi.props.name, _id: { $ne: kpiId } }).sort({ period: -1 }).limit(periods).exec();
    return docs.map(d => this.toEntity(d));
  }

  async search(tenantId: string, filters: KpiSearchFilters): Promise<Kpi[]> {
    const query: any = { tenantId };
    if (filters.category) query.category = filters.category;
    if (filters.status) query.status = filters.status;
    if (filters.period) query.period = filters.period;
    if (filters.executiveLevel) query.executiveLevel = { $in: [filters.executiveLevel, 'ALL'] };
    if (filters.searchTerm) query.$or = [{ name: { $regex: filters.searchTerm, $options: 'i' } }];
    const docs = await this.kpiModel.find(query).sort({ createdAt: -1 }).exec();
    return docs.map(d => this.toEntity(d));
  }

  async countByTenant(tenantId: string): Promise<number> {
    return this.kpiModel.countDocuments({ tenantId });
  }

  private toEntity(doc: KpiDocument): Kpi {
    return new Kpi({
      id: doc._id.toString(), tenantId: doc.tenantId, name: doc.name, category: doc.category as any,
      executiveLevel: doc.executiveLevel as any, value: doc.value, unit: doc.unit, period: doc.period,
      target: doc.target, previousValue: doc.previousValue, percentChange: doc.percentChange,
      status: doc.status as any, trend: doc.trend as any, dataSources: doc.dataSources || [],
      metadata: doc.metadata || {}, visible: doc.visible, isCalculated: doc.isCalculated,
      lastCalculatedAt: doc.lastCalculatedAt || new Date(),
      createdAt: (doc as any).createdAt || new Date(), updatedAt: (doc as any).updatedAt || new Date(),
    });
  }
}
