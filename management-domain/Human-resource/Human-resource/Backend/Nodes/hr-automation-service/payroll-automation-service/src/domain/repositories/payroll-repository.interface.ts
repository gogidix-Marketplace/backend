import { Payroll } from '../models/payroll.entity';

export interface IPayrollRepository {
  save(payroll: Payroll): Promise<Payroll>;
  findById(id: string): Promise<Payroll | null>;
  findByTenantId(tenantId: string, limit?: number, offset?: number): Promise<{ data: Payroll[]; total: number }>;
  findByTenantIdAndStatus(tenantId: string, status: string, limit?: number, offset?: number): Promise<{ data: Payroll[]; total: number }>;
}
