import { Injectable, Logger } from '@nestjs/common';
import { TaxRegime } from '@domain/enums/tax-regime.enum';
import { EmployeePayroll } from '@domain/models/employee-payroll.entity';

@Injectable()
export class PayrollCalculationService {
  private readonly logger = new Logger(PayrollCalculationService.name);

  calculateEmployeePayroll(
    payrollId: string,
    employeeId: string,
    tenantId: string,
    basicSalary: number,
    taxRegime: TaxRegime = TaxRegime.NEW,
    currency: string = 'USD',
  ): EmployeePayroll {
    const dearnessAllowance = basicSalary * 0.4;
    const houseRentAllowance = basicSalary * 0.2;
    const transportAllowance = 200;
    const medicalAllowance = 250;

    const grossEarnings = basicSalary + dearnessAllowance + houseRentAllowance + transportAllowance + medicalAllowance;

    const incomeTax = this.calculateIncomeTax(grossEarnings, taxRegime);
    const professionalTax = grossEarnings * 0.02;
    const providentFund = basicSalary * 0.12;

    const totalDeductions = incomeTax + professionalTax + providentFund;
    const netPay = grossEarnings - totalDeductions;

    return new EmployeePayroll(
      payrollId, employeeId, tenantId,
      basicSalary, dearnessAllowance, houseRentAllowance, transportAllowance, medicalAllowance,
      grossEarnings, incomeTax, professionalTax, providentFund, totalDeductions, netPay,
      taxRegime, currency,
    );
  }

  private calculateIncomeTax(grossIncome: number, regime: TaxRegime): number {
    if (regime === TaxRegime.NEW) {
      return this.calculateNewRegimeTax(grossIncome);
    }
    return this.calculateOldRegimeTax(grossIncome);
  }

  private calculateNewRegimeTax(income: number): number {
    if (income <= 250000) return 0;
    if (income <= 500000) return (income - 250000) * 0.05;
    if (income <= 750000) return 12500 + (income - 500000) * 0.1;
    if (income <= 1000000) return 37500 + (income - 750000) * 0.15;
    if (income <= 1250000) return 75000 + (income - 1000000) * 0.2;
    if (income <= 1500000) return 125000 + (income - 1250000) * 0.25;
    return 187500 + (income - 1500000) * 0.3;
  }

  private calculateOldRegimeTax(income: number): number {
    if (income <= 250000) return 0;
    if (income <= 500000) return (income - 250000) * 0.05;
    if (income <= 1000000) return 12500 + (income - 500000) * 0.2;
    return 112500 + (income - 1000000) * 0.3;
  }
}
