import {Injectable, Logger, Inject} from '@nestjs/common';
import { IPayrollQuery } from '@domain/ports/input/payroll.query';
import { IPayrollRepository } from '@domain/repositories/payroll-repository.interface';
import { IEmployeePayrollRepository } from '@domain/repositories/employee-payroll-repository.interface';
import { PayrollStatus } from '@domain/enums/payroll-status.enum';

@Injectable()
export class PayrollQueryService implements IPayrollQuery {
  private readonly logger = new Logger(PayrollQueryService.name);

  constructor(
    @Inject('IPayrollRepository')
    private readonly payrollRepository: IPayrollRepository,
    @Inject('IEmployeePayrollRepository')
    private readonly employeePayrollRepository: IEmployeePayrollRepository,
  ) {}

  async getPayrollById(payrollId: string): Promise<any> {
    return this.payrollRepository.findById(payrollId);
  }

  async getEmployeePayroll(payrollId: string, employeeId: string): Promise<any> {
    return this.employeePayrollRepository.findByPayrollIdAndEmployeeId(payrollId, employeeId);
  }

  async listPayrolls(tenantId: string, status?: PayrollStatus, limit = 50, offset = 0): Promise<{ data: any[]; total: number }> {
    if (status) {
      return this.payrollRepository.findByTenantIdAndStatus(tenantId, status, limit, offset);
    }
    return this.payrollRepository.findByTenantId(tenantId, limit, offset);
  }
}
