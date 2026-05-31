import { PayrollStatus } from '../../enums/payroll-status.enum';

export interface IPayrollCommand {
  submitPayrollJob(tenantId: string, employeeIds: string[], periodStart: Date, periodEnd: Date, priority?: number): Promise<{ jobId: string; payrollId: string }>;
  cancelPayrollJob(jobId: string): Promise<void>;
  getJobStatus(jobId: string): Promise<any>;
  getQueueStats(): Promise<{ waiting: number; active: number; completed: number; failed: number }>;
}
