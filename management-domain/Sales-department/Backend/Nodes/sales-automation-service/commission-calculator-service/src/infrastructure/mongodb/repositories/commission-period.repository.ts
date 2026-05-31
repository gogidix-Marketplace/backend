import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { CommissionPeriod } from '../../../domain/models/commission-period.entity';
import { CommissionPeriodType, CommissionPeriodStatus } from '../../../domain/enums/commission-period-type.enum';
import { CommissionPeriodRepositoryPort } from '../../../domain/ports/out/commission-period.repository.port';
import { CommissionPeriodDocument } from '../schemas/commission-period.schema';

/**
 * MongoDB implementation of CommissionPeriod repository
 */
@Injectable()
export class CommissionPeriodRepository implements CommissionPeriodRepositoryPort {
  constructor(
    @InjectModel(CommissionPeriodDocument.name)
    private readonly commissionPeriodModel: Model<CommissionPeriodDocument>,
  ) {}

  async save(period: CommissionPeriod): Promise<CommissionPeriod> {
    const document = this.toDocument(period);
    const savedDocument = await this.commissionPeriodModel.findOneAndUpdate(
      { _id: document._id },
      document,
      { upsert: true, new: true },
    );

    return this.toEntity(savedDocument);
  }

  async findById(periodId: string): Promise<CommissionPeriod | null> {
    const document = await this.commissionPeriodModel.findById(periodId);
    return document ? this.toEntity(document) : null;
  }

  async findByTenantId(tenantId: string, organizationId?: string): Promise<CommissionPeriod[]> {
    const query: any = { tenantId };
    if (organizationId) {
      query.organizationId = organizationId;
    }

    const documents = await this.commissionPeriodModel.find(query).sort({ startDate: -1 });
    return documents.map(doc => this.toEntity(doc));
  }

  async findByType(tenantId: string, type: CommissionPeriodType): Promise<CommissionPeriod[]> {
    const documents = await this.commissionPeriodModel.find({
      tenantId,
      periodType: type,
    });
    return documents.map(doc => this.toEntity(doc));
  }

  async findByStatus(tenantId: string, status: CommissionPeriodStatus): Promise<CommissionPeriod[]> {
    const documents = await this.commissionPeriodModel.find({
      tenantId,
      status,
    });
    return documents.map(doc => this.toEntity(doc));
  }

  async findForDate(tenantId: string, date: Date): Promise<CommissionPeriod | null> {
    const document = await this.commissionPeriodModel.findOne({
      tenantId,
      startDate: { $lte: date },
      endDate: { $gte: date },
    });

    return document ? this.toEntity(document) : null;
  }

  async findActive(tenantId: string, organizationId?: string): Promise<CommissionPeriod[]> {
    const query: any = {
      tenantId,
      status: { $in: ['OPEN', 'CALCULATING'] },
    };

    if (organizationId) {
      query.organizationId = organizationId;
    }

    const documents = await this.commissionPeriodModel.find(query).sort({ startDate: -1 });
    return documents.map(doc => this.toEntity(doc));
  }

  async delete(periodId: string): Promise<void> {
    await this.commissionPeriodModel.findByIdAndDelete(periodId);
  }

  async exists(periodId: string): Promise<boolean> {
    const count = await this.commissionPeriodModel.countDocuments({ _id: periodId });
    return count > 0;
  }

  private toDocument(entity: CommissionPeriod): any {
    const obj = entity.toObject();
    return {
      _id: obj._id,
      ...obj,
    };
  }

  private toEntity(document: any): CommissionPeriod {
    return CommissionPeriod.fromObject({
      id: document._id?.toString(),
      name: document.name,
      periodType: document.periodType,
      startDate: document.startDate,
      endDate: document.endDate,
      status: document.status,
      processingDate: document.processingDate,
      tenantId: document.tenantId,
      organizationId: document.organizationId,
      createdAt: document.createdAt,
      updatedAt: document.updatedAt,
      version: document.version,
    });
  }
}
