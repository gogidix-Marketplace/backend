import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IPayrollRepository } from '@domain/repositories/payroll-repository.interface';
import { Payroll } from '@domain/models/payroll.entity';
import { PayrollStatus } from '@domain/enums/payroll-status.enum';

@Injectable()
export class PayrollRepositoryImpl implements IPayrollRepository {
  private readonly logger = new Logger(PayrollRepositoryImpl.name);

  constructor(@InjectModel('Payroll') private readonly payrollModel: Model<any>) {}

  async save(payroll: Payroll): Promise<Payroll> {
    const doc = new this.payrollModel(payroll);
    const saved = await doc.save();
    return this.toEntity(saved);
  }

  async findById(id: string): Promise<Payroll | null> {
    const doc = await this.payrollModel.findById(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  async findByTenantId(tenantId: string, limit = 50, offset = 0): Promise<{ data: Payroll[]; total: number }> {
    const [data, total] = await Promise.all([
      this.payrollModel.find({ tenantId }).sort({ createdAt: -1 }).skip(offset).limit(limit).lean(),
      this.payrollModel.countDocuments({ tenantId }),
    ]);
    return { data: data.map(d => this.toEntity(d)), total };
  }

  async findByTenantIdAndStatus(tenantId: string, status: string, limit = 50, offset = 0): Promise<{ data: Payroll[]; total: number }> {
    const [data, total] = await Promise.all([
      this.payrollModel.find({ tenantId, status }).sort({ createdAt: -1 }).skip(offset).limit(limit).lean(),
      this.payrollModel.countDocuments({ tenantId, status }),
    ]);
    return { data: data.map(d => this.toEntity(d)), total };
  }

  private toEntity(doc: any): Payroll {
    return new Payroll(
      doc.tenantId, doc.periodStart, doc.periodEnd, doc.status as PayrollStatus,
      doc.totalEmployees, doc.processedEmployees, doc.totalGrossPay, doc.totalTaxes,
      doc.totalDeductions, doc.totalNetPay, doc.currency,
      { id: doc._id?.toString(), createdAt: doc.createdAt, updatedAt: doc.updatedAt },
    );
  }
}
