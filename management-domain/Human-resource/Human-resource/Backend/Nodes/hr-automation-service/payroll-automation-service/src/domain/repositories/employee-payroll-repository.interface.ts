import { EmployeePayroll } from '../models/employee-payroll.entity';

export interface IEmployeePayrollRepository {
  save(employeePayroll: EmployeePayroll): Promise<EmployeePayroll>;
  saveBatch(employeePayrolls: EmployeePayroll[]): Promise<void>;
  findByPayrollId(payrollId: string): Promise<EmployeePayroll[]>;
  findByPayrollIdAndEmployeeId(payrollId: string, employeeId: string): Promise<EmployeePayroll | null>;
}
