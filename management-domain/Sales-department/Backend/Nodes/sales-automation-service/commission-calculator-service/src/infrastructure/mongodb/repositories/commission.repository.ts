import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Commission } from '../../../domain/models/commission.entity';
import { CommissionStatus } from '../../../domain/enums/commission-status.enum';
import {
  CommissionRepositoryPort,
  CommissionFilters,
  FindOptions,
  CommissionSummary,
} from '../../../domain/ports/out/commission.repository.port';
import { CommissionDocument } from '../schemas/commission.schema';

/**
 * MongoDB implementation of Commission repository
 */
@Injectable()
export class CommissionRepository implements CommissionRepositoryPort {
  constructor(
    @InjectModel(CommissionDocument.name)
    private readonly commissionModel: Model<CommissionDocument>,
  ) {}

  async save(commission: Commission): Promise<Commission> {
    const document = this.toDocument(commission);
    const savedDocument = await this.commissionModel.findOneAndUpdate(
      { _id: document._id },
      document,
      { upsert: true, new: true },
    );

    return this.toEntity(savedDocument);
  }

  async findById(commissionId: string): Promise<Commission | null> {
    const document = await this.commissionModel.findById(commissionId);
    return document ? this.toEntity(document) : null;
  }

  async findBySalesRepId(
    salesRepId: string,
    options?: FindOptions,
  ): Promise<Commission[]> {
    const query: any = { salesRepId };

    if (options?.status) {
      query.status = options.status;
    }

    if (options?.periodId) {
      query.periodId = options.periodId;
    }

    if (options?.startDate || options?.endDate) {
      query.transactionDate = {};
      if (options.startDate) {
        query.transactionDate.$gte = options.startDate;
      }
      if (options.endDate) {
        query.transactionDate.$lte = options.endDate;
      }
    }

    let queryBuilder = this.commissionModel.find(query);

    if (options?.sortBy) {
      const sortOrder = options.sortOrder === 'asc' ? 1 : -1;
      queryBuilder = queryBuilder.sort({ [options.sortBy]: sortOrder });
    }

    if (options?.limit) {
      queryBuilder = queryBuilder.limit(options.limit);
    }

    if (options?.offset) {
      queryBuilder = queryBuilder.skip(options.offset);
    }

    const documents = await queryBuilder.exec();
    return documents.map(doc => this.toEntity(doc));
  }

  async findByPeriodId(periodId: string): Promise<Commission[]> {
    const documents = await this.commissionModel.find({ periodId });
    return documents.map(doc => this.toEntity(doc));
  }

  async findByStatus(status: CommissionStatus, tenantId: string): Promise<Commission[]> {
    const documents = await this.commissionModel.find({ status, tenantId });
    return documents.map(doc => this.toEntity(doc));
  }

  async find(filters: CommissionFilters): Promise<Commission[]> {
    const query: any = { tenantId: filters.tenantId };

    if (filters.organizationId) {
      query.organizationId = filters.organizationId;
    }

    if (filters.salesRepId) {
      query.salesRepId = filters.salesRepId;
    }

    if (filters.status) {
      query.status = filters.status;
    }

    if (filters.periodId) {
      query.periodId = filters.periodId;
    }

    if (filters.startDate || filters.endDate) {
      query.transactionDate = {};
      if (filters.startDate) {
        query.transactionDate.$gte = filters.startDate;
      }
      if (filters.endDate) {
        query.transactionDate.$lte = filters.endDate;
      }
    }

    if (filters.minAmount !== undefined || filters.maxAmount !== undefined) {
      query.finalAmount = {};
      if (filters.minAmount !== undefined) {
        query.finalAmount.$gte = filters.minAmount;
      }
      if (filters.maxAmount !== undefined) {
        query.finalAmount.$lte = filters.maxAmount;
      }
    }

    if (filters.currency) {
      query.currency = filters.currency;
    }

    const documents = await this.commissionModel.find(query);
    return documents.map(doc => this.toEntity(doc));
  }

  async delete(commissionId: string): Promise<void> {
    await this.commissionModel.findByIdAndDelete(commissionId);
  }

  async count(filters: CommissionFilters): Promise<number> {
    const query: any = { tenantId: filters.tenantId };

    if (filters.organizationId) {
      query.organizationId = filters.organizationId;
    }

    if (filters.salesRepId) {
      query.salesRepId = filters.salesRepId;
    }

    if (filters.status) {
      query.status = filters.status;
    }

    if (filters.periodId) {
      query.periodId = filters.periodId;
    }

    return await this.commissionModel.countDocuments(query);
  }

  async getSummary(filters: CommissionFilters): Promise<CommissionSummary> {
    const matchQuery: any = { tenantId: filters.tenantId };

    if (filters.organizationId) {
      matchQuery.organizationId = filters.organizationId;
    }

    if (filters.salesRepId) {
      matchQuery.salesRepId = filters.salesRepId;
    }

    if (filters.periodId) {
      matchQuery.periodId = filters.periodId;
    }

    if (filters.startDate || filters.endDate) {
      matchQuery.transactionDate = {};
      if (filters.startDate) {
        matchQuery.transactionDate.$gte = filters.startDate;
      }
      if (filters.endDate) {
        matchQuery.transactionDate.$lte = filters.endDate;
      }
    }

    const result = await this.commissionModel.aggregate([
      { $match: matchQuery },
      {
        $group: {
          _id: '$currency',
          totalCommissions: { $sum: 1 },
          totalAmount: { $sum: '$finalAmount' },
          pendingAmount: {
            $sum: {
              $cond: [{ $eq: ['$status', 'PENDING'] }, '$finalAmount', 0],
            },
          },
          approvedAmount: {
            $sum: {
              $cond: [{ $eq: ['$status', 'APPROVED'] }, '$finalAmount', 0],
            },
          },
          paidAmount: {
            $sum: {
              $cond: [{ $eq: ['$status', 'PAID'] }, '$finalAmount', 0],
            },
          },
        },
      },
    ]);

    const summary = result[0] || {
      _id: filters.currency || 'USD',
      totalCommissions: 0,
      totalAmount: 0,
      pendingAmount: 0,
      approvedAmount: 0,
      paidAmount: 0,
    };

    return {
      totalCommissions: summary.totalCommissions,
      totalAmount: summary.totalAmount,
      pendingAmount: summary.pendingAmount,
      approvedAmount: summary.approvedAmount,
      paidAmount: summary.paidAmount,
      averageCommission: summary.totalCommissions > 0
        ? summary.totalAmount / summary.totalCommissions
        : 0,
      currency: summary._id,
    };
  }

  async findPendingApprovals(tenantId: string, limit = 50): Promise<Commission[]> {
    const documents = await this.commissionModel
      .find({
        tenantId,
        status: { $in: ['PENDING', 'CALCULATED'] },
      })
      .sort({ createdAt: -1 })
      .limit(limit);

    return documents.map(doc => this.toEntity(doc));
  }

  async findOverdue(tenantId: string): Promise<Commission[]> {
    const documents = await this.commissionModel
      .find({
        tenantId,
        status: { $nin: ['PAID', 'CLAWED_BACK', 'CANCELLED'] },
        settlementDate: { $lt: new Date() },
      })
      .sort({ settlementDate: 1 });

    return documents.map(doc => this.toEntity(doc));
  }

  async batchSave(commissions: Commission[]): Promise<Commission[]> {
    const documents = commissions.map(c => this.toDocument(c));
    const savedDocuments = await this.commissionModel.insertMany(documents, { ordered: false });
    return savedDocuments.map(doc => this.toEntity(doc));
  }

  private toDocument(entity: Commission): any {
    const obj = entity.toObject();
    return {
      _id: obj._id,
      ...obj,
    };
  }

  private toEntity(document: any): Commission {
    return Commission.fromObject({
      id: document._id?.toString(),
      salesRepId: document.salesRepId,
      salesRepName: document.salesRepName,
      periodId: document.periodId,
      ruleId: document.ruleId,
      ruleName: document.ruleName,
      status: document.status,
      salesAmount: document.salesAmount,
      commissionRate: document.commissionRate,
      calculatedAmount: document.calculatedAmount,
      adjustedAmount: document.adjustedAmount,
      finalAmount: document.finalAmount,
      currency: document.currency,
      transactionDate: document.transactionDate,
      settlementDate: document.settlementDate,
      paymentDate: document.paymentDate,
      splits: document.splits,
      adjustmentReason: document.adjustmentReason,
      clawbackAmount: document.clawbackAmount,
      clawbackReason: document.clawbackReason,
      clawbackDate: document.clawbackDate,
      quotaAttained: document.quotaAttained,
      quotaTarget: document.quotaTarget,
      acceleratorApplied: document.acceleratorApplied,
      productSales: document.productSales,
      tenantId: document.tenantId,
      organizationId: document.organizationId,
      approvedBy: document.approvedBy,
      approvedAt: document.approvedAt,
      paidVia: document.paidVia,
      paymentReference: document.paymentReference,
      createdAt: document.createdAt,
      updatedAt: document.updatedAt,
      version: document.version,
    });
  }
}
