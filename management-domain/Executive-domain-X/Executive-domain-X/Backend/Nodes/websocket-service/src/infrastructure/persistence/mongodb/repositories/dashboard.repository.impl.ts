import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Dashboard } from '../../../../domain/models/dashboard.entity';
import { IDashboardRepository } from '../../../../domain/repositories/dashboard-repository.interface';
import { DashboardDocument } from '../mongoose/dashboard.schema';
import { WidgetType } from '../../../../domain/enums/widget-type.enum';

@Injectable()
export class DashboardRepositoryImpl implements IDashboardRepository {
  constructor(@InjectModel('Dashboard') private readonly model: Model<DashboardDocument>) {}

  async save(dashboard: Dashboard): Promise<Dashboard> { const doc = new this.model(dashboard.toJSON()); await doc.save(); return this.toEntity(doc); }
  async findById(dashboardId: string): Promise<Dashboard | null> { const doc = await this.model.findOne({ dashboardId }); return doc ? this.toEntity(doc) : null; }
  async findByExecutive(executiveId: string): Promise<Dashboard[]> { const docs = await this.model.find({ executiveId }); return docs.map(d => this.toEntity(d)); }
  async findByWidgetKpi(kpiId: string): Promise<Dashboard[]> { const docs = await this.model.find({ 'widgets.widgetId': kpiId, 'widgets.type': 'KPI' }); return docs.map(d => this.toEntity(d)); }
  async update(dashboard: Dashboard): Promise<Dashboard> { const u = await this.model.findOneAndUpdate({ dashboardId: dashboard.props.dashboardId }, dashboard.toJSON(), { new: true }); if (!u) throw new Error('Dashboard not found'); return this.toEntity(u); }
  async delete(dashboardId: string): Promise<void> { await this.model.deleteOne({ dashboardId }); }

  private toEntity(doc: DashboardDocument): Dashboard {
    return new Dashboard({
      id: doc._id.toString(), dashboardId: doc.dashboardId, name: doc.name, description: doc.description || '',
      executiveId: doc.executiveId?.toString() || '', widgets: (doc.widgets || []).map((w: any) => ({ widgetId: w.widgetId, type: w.type as WidgetType, title: w.title, data: w.data || {}, position: w.position || {}, config: w.config || {} })),
      isPublic: doc.isPublic, sharedWith: doc.sharedWith || [], lastUpdated: doc.lastUpdated || new Date(),
      createdAt: (doc as any).createdAt || new Date(), updatedAt: (doc as any).updatedAt || new Date(),
    });
  }
}
