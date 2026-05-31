import { Kpi } from '../models/kpi.entity';
import { KpiCategory } from '../enums/kpi-category.enum';
import { KpiStatus } from '../enums/kpi-status.enum';
import { ExecutiveLevel } from '../enums/executive-level.enum';

export interface FindOptions {
  page?: number;
  limit?: number;
  sortBy?: string;
  sortOrder?: 'asc' | 'desc';
}

export interface KpiSearchFilters {
  category?: KpiCategory;
  executiveLevel?: ExecutiveLevel;
  status?: KpiStatus;
  period?: string;
  searchTerm?: string;
  startDate?: Date;
  endDate?: Date;
  limit?: number;
}

export interface KpiSummary {
  [category: string]: {
    total: number;
    ahead: number;
    onTrack: number;
    atRisk: number;
    behind: number;
  };
}

export interface IKpiRepository {
  findById(id: string, tenantId: string): Promise<Kpi | null>;
  findByTenant(tenantId: string, options?: FindOptions): Promise<Kpi[]>;
  findByLevel(tenantId: string, level: string): Promise<Kpi[]>;
  getSummary(tenantId: string): Promise<KpiSummary>;
  getDashboardKpis(tenantId: string, level: string): Promise<Kpi[]>;
  getTrends(tenantId: string, kpiId: string, periods: number): Promise<Kpi[]>;
  search(tenantId: string, filters: KpiSearchFilters): Promise<Kpi[]>;
  countByTenant(tenantId: string): Promise<number>;
}
