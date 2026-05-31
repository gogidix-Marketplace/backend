import { PayrollStatus } from '../../enums/payroll-status.enum';

export interface IPayrollQuery {
  getPayrollById(payrollId: string): Promise<any>;
  getEmployeePayroll(payrollId: string, employeeId: string): Promise<any>;
  listPayrolls(tenantId: string, status?: PayrollStatus, limit?: number, offset?: number): Promise<{ data: any[]; total: number }>;
}
