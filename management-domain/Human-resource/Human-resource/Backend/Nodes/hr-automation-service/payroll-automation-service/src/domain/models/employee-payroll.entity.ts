import { BaseEntity } from '@shared/base/base.entity';
import { TaxRegime } from '../enums/tax-regime.enum';

export class EmployeePayroll extends BaseEntity {
  constructor(
    public readonly payrollId: string,
    public readonly employeeId: string,
    public readonly tenantId: string,
    public basicSalary: number,
    public dearnessAllowance: number,
    public houseRentAllowance: number,
    public transportAllowance: number,
    public medicalAllowance: number,
    public grossEarnings: number,
    public incomeTax: number,
    public professionalTax: number,
    public providentFund: number,
    public totalDeductions: number,
    public netPay: number,
    public taxRegime: TaxRegime,
    public currency: string,
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }
}
