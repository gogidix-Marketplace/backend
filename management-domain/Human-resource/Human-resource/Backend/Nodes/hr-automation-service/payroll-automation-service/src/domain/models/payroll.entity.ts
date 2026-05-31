import { AggregateRoot } from '@shared/base/base.entity';
import { PayrollStatus } from '../enums/payroll-status.enum';

export class Payroll extends AggregateRoot {
  constructor(
    public readonly tenantId: string,
    public readonly periodStart: Date,
    public readonly periodEnd: Date,
    public status: PayrollStatus,
    public totalEmployees: number,
    public processedEmployees: number,
    public totalGrossPay: number,
    public totalTaxes: number,
    public totalDeductions: number,
    public totalNetPay: number,
    public currency: string,
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }

  markProcessing(): void {
    this.status = PayrollStatus.PROCESSING;
  }

  complete(processedCount: number, gross: number, taxes: number, deductions: number, net: number): void {
    this.status = PayrollStatus.COMPLETED;
    this.processedEmployees = processedCount;
    this.totalGrossPay = gross;
    this.totalTaxes = taxes;
    this.totalDeductions = deductions;
    this.totalNetPay = net;
  }

  fail(): void {
    this.status = PayrollStatus.FAILED;
  }

  cancel(): void {
    this.status = PayrollStatus.CANCELLED;
  }
}
