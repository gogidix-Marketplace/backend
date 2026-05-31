import {Injectable, Logger, Inject} from '@nestjs/common';
import { Dashboard } from '../../domain/models/dashboard.entity';
import { IDashboardRepository } from '../../domain/repositories/dashboard-repository.interface';
import { BroadcastService } from './broadcast.service';

@Injectable()
export class DashboardService {
  private readonly logger = new Logger(DashboardService.name);
  private subscribedClients = new Map<string, Set<any>>();

  constructor(
    @Inject('IDashboardRepository')
    private readonly dashboardRepository: IDashboardRepository,
    private readonly broadcastService: BroadcastService,
  ) {}

  async createDashboard(data: { name: string; description?: string; executiveId: string; widgets?: any[]; isPublic?: boolean }) {
    const dashboard = new Dashboard({
      dashboardId: crypto.randomUUID(), name: data.name, description: data.description || '',
      executiveId: data.executiveId, widgets: data.widgets || [], isPublic: data.isPublic || false,
      sharedWith: [], lastUpdated: new Date(), createdAt: new Date(), updatedAt: new Date(),
    });
    return this.dashboardRepository.save(dashboard);
  }

  async getDashboard(dashboardId: string) { return this.dashboardRepository.findById(dashboardId); }
  async getExecutiveDashboards(executiveId: string) { return this.dashboardRepository.findByExecutive(executiveId); }

  async handleWidgetUpdate(dashboardId: string, widgetId: string, widgetData: any) {
    const dashboard = await this.dashboardRepository.findById(dashboardId);
    if (!dashboard) throw new Error('Dashboard not found');
    dashboard.updateWidget(widgetId, { data: widgetData, lastUpdated: new Date() } as any);
    await this.dashboardRepository.update(dashboard);
    await this.broadcastService.broadcastDashboardUpdate(dashboard.props.dashboardId, dashboardId, { widgetId, data: widgetData });
    return { success: true };
  }

  async handleKpiUpdate(kpiId: string, kpiData: any) {
    const dashboards = await this.dashboardRepository.findByWidgetKpi(kpiId);
    for (const d of dashboards) {
      await this.broadcastService.broadcastKpiUpdate(d.props.dashboardId, kpiId, kpiData);
    }
    return { success: true, updatedDashboards: dashboards.length };
  }

  subscribeToDashboard(dashboardId: string, ws: any) {
    if (!this.subscribedClients.has(dashboardId)) this.subscribedClients.set(dashboardId, new Set());
    this.subscribedClients.get(dashboardId)!.add(ws);
    ws.on('close', () => this.unsubscribeFromDashboard(dashboardId, ws));
  }

  unsubscribeFromDashboard(dashboardId: string, ws: any) {
    this.subscribedClients.get(dashboardId)?.delete(ws);
    if (this.subscribedClients.get(dashboardId)?.size === 0) this.subscribedClients.delete(dashboardId);
  }
}
