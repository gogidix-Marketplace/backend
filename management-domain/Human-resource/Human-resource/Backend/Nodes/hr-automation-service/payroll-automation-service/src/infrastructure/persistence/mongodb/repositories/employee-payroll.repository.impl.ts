import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IEmployeePayrollRepository } from '@domain/repositories/employee-payroll-repository.interface';
import { EmployeePayroll } from '@domain/models/employee-payroll.entity';
import { TaxRegime } from '@domain/enums/tax-regime.enum';

@Injectable()
export class EmployeePayrollRepositoryImpl implements IEmployeePayrollRepository {
  private readonly logger = new Logger(EmployeePayrollRepositoryImpl.name);

  constructor(@InjectModel('EmployeePayroll') private readonly model: Model<any>) {}

  async save(employeePayroll: EmployeePayroll): Promise<EmployeePayroll> {
    const doc = new this.model(employeePayroll);
    const saved = await doc.save();
    return this.toEntity(saved);
  }

  async saveBatch(employeePayrolls: EmployeePayroll[]): Promise<void> {
    await this.model.insertMany(employeePayrolls);
  }

  async findByPayrollId(payrollId: string): Promise<EmployeePayroll[]> {
    const docs = await this.model.find({ payrollId }).lean();
    return docs.map(d => this.toEntity(d));
  }

  async findByPayrollIdAndEmployeeId(payrollId: string, employeeId: string): Promise<EmployeePayroll | null> {
    const doc = await this.model.findOne({ payrollId, employeeId }).lean();
    return doc ? this.toEntity(doc) : null;
  }

  private toEntity(doc: any): EmployeePayroll {
    return new EmployeePayroll(
      doc.payrollId, doc.employeeId, doc.tenantId, doc.basicSalary,
      doc.dearnessAllowance, doc.houseRentAllowance, doc.transportAllowance, doc.medicalAllowance,
      doc.grossEarnings, doc.incomeTax, doc.professionalTax, doc.providentFund,
      doc.totalDeductions, doc.netPay, doc.taxRegime as TaxRegime, doc.currency,
      { id: doc._id?.toString(), createdAt: doc.createdAt, updatedAt: doc.updatedAt },
    );
  }
}
