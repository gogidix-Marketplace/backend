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
}

export interface IKpiRepository {
  save(kpi: Kpi): Promise<Kpi>;
  findById(id: string, tenantId: string): Promise<Kpi | null>;
  findByTenant(tenantId: string, options?: FindOptions): Promise<Kpi[]>;
  findByNameAndPeriod(tenantId: string, name: string, period: string): Promise<Kpi | null>;
  delete(id: string, tenantId: string): Promise<void>;
  update(kpi: Kpi): Promise<Kpi>;
  countByTenant(tenantId: string): Promise<number>;
  search(tenantId: string, filters: KpiSearchFilters): Promise<Kpi[]>;
}
