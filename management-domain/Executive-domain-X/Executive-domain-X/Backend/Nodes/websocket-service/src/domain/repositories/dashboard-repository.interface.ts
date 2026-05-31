import { Dashboard } from '../models/dashboard.entity';

export interface IDashboardRepository {
  save(dashboard: Dashboard): Promise<Dashboard>;
  findById(dashboardId: string): Promise<Dashboard | null>;
  findByExecutive(executiveId: string): Promise<Dashboard[]>;
  findByWidgetKpi(kpiId: string): Promise<Dashboard[]>;
  update(dashboard: Dashboard): Promise<Dashboard>;
  delete(dashboardId: string): Promise<void>;
}
