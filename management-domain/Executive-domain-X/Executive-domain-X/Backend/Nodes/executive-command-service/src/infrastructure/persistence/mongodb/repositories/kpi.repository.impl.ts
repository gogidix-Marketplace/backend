import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Kpi, KpiProps } from '../../../../domain/models/kpi.entity';
import { IKpiRepository, FindOptions, KpiSearchFilters } from '../../../../domain/repositories/kpi-repository.interface';
import { KpiDocument } from '../mongoose/kpi.schema';

@Injectable()
export class KpiRepository implements IKpiRepository {
  constructor(
    @InjectModel('Kpi')
    private readonly kpiModel: Model<KpiDocument>,
  ) {}

  async save(kpi: Kpi): Promise<Kpi> {
    const doc = new this.kpiModel(kpi.toJSON());
    const saved = await doc.save();
    return this.documentToEntity(saved);
  }

  async findById(id: string, tenantId: string): Promise<Kpi | null> {
    const doc = await this.kpiModel.findOne({ _id: id, tenantId });
    return doc ? this.documentToEntity(doc) : null;
  }

  async findByTenant(tenantId: string, options?: FindOptions): Promise<Kpi[]> {
    const query = this.kpiModel.find({ tenantId });

    if (options?.sortBy) {
      const sortOrder = options.sortOrder === 'asc' ? 1 : -1;
      query.sort({ [options.sortBy]: sortOrder });
    }

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findByNameAndPeriod(tenantId: string, name: string, period: string): Promise<Kpi | null> {
    const doc = await this.kpiModel.findOne({ tenantId, name, period });
    return doc ? this.documentToEntity(doc) : null;
  }

  async delete(id: string, tenantId: string): Promise<void> {
    await this.kpiModel.deleteOne({ _id: id, tenantId });
  }

  async update(kpi: Kpi): Promise<Kpi> {
    const updated = await this.kpiModel
      .findOneAndUpdate({ _id: kpi.id, tenantId: kpi.tenantId }, kpi.toJSON(), { new: true })
      .exec();

    if (!updated) {
      throw new Error('KPI not found');
    }

    return this.documentToEntity(updated);
  }

  async countByTenant(tenantId: string): Promise<number> {
    return this.kpiModel.countDocuments({ tenantId });
  }

  async search(tenantId: string, filters: KpiSearchFilters): Promise<Kpi[]> {
    const query: any = { tenantId };

    if (filters.category) query.category = filters.category;
    if (filters.status) query.status = filters.status;
    if (filters.period) query.period = filters.period;
    if (filters.executiveLevel) query.executiveLevel = { $in: [filters.executiveLevel, 'ALL'] };
    if (filters.searchTerm) {
      query.$or = [{ name: { $regex: filters.searchTerm, $options: 'i' } }];
    }

    const docs = await this.kpiModel.find(query).sort({ createdAt: -1 }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  private documentToEntity(doc: KpiDocument): Kpi {
    const props: KpiProps = {
      id: doc._id.toString(),
      tenantId: doc.tenantId,
      name: doc.name,
      category: doc.category as any,
      executiveLevel: doc.executiveLevel as any,
      value: doc.value,
      unit: doc.unit,
      period: doc.period,
      target: doc.target,
      previousValue: doc.previousValue,
      percentChange: doc.percentChange,
      status: doc.status as any,
      trend: doc.trend as any,
      dataSources: doc.dataSources || [],
      metadata: doc.metadata || {},
      visible: doc.visible,
      isCalculated: doc.isCalculated,
      lastCalculatedAt: doc.lastCalculatedAt || new Date(),
      createdAt: (doc as any).createdAt || new Date(),
      updatedAt: (doc as any).updatedAt || new Date(),
    };

    return new Kpi(props);
  }
}
