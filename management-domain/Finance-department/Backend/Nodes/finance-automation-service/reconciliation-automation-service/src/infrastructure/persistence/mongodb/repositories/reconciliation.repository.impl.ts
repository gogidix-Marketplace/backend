import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { Reconciliation, ReconciliationProps } from '../../../../domain/models/reconciliation.entity';
import {
  IReconciliationRepository,
  FindOptions,
  SearchFilters,
  StatsPeriod,
  ReconciliationStats,
} from '../../../../domain/repositories/reconciliation-repository.interface';
import { ReconciliationDocument, ReconciliationSchema } from '../mongoose/reconciliation.schema';

@Injectable()
export class ReconciliationRepository implements IReconciliationRepository {
  constructor(
    @InjectModel('Reconciliation')
    private readonly reconciliationModel: Model<ReconciliationDocument>,
  ) {}

  async save(reconciliation: Reconciliation): Promise<Reconciliation> {
    const doc = new this.reconciliationModel(reconciliation.toJSON());
    const saved = await doc.save();
    return this.documentToEntity(saved);
  }

  async findById(id: string, tenantId: string): Promise<Reconciliation | null> {
    const doc = await this.reconciliationModel.findOne({ _id: id, tenantId });
    return doc ? this.documentToEntity(doc) : null;
  }

  async findByTenant(tenantId: string, options?: FindOptions): Promise<Reconciliation[]> {
    const query = this.reconciliationModel.find({ tenantId });

    if (options?.sortBy) {
      const sortOrder = options.sortOrder === 'asc' ? 1 : -1;
      query.sort({ [options.sortBy]: sortOrder });
    }

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findByStatus(status: string, tenantId: string): Promise<Reconciliation[]> {
    const docs = await this.reconciliationModel.find({ tenantId, status }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findScheduledReconciliations(): Promise<Reconciliation[]> {
    const docs = await this.reconciliationModel
      .find({ scheduled: true, nextRunAt: { $lte: new Date() } })
      .exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async delete(id: string, tenantId: string): Promise<void> {
    await this.reconciliationModel.deleteOne({ _id: id, tenantId });
  }

  async update(reconciliation: Reconciliation): Promise<Reconciliation> {
    const updated = await this.reconciliationModel
      .findOneAndUpdate({ _id: reconciliation.id, tenantId: reconciliation.tenantId }, reconciliation.toJSON(), {
        new: true,
      })
      .exec();

    if (!updated) {
      throw new Error('Reconciliation not found');
    }

    return this.documentToEntity(updated);
  }

  async countByTenant(tenantId: string): Promise<number> {
    return this.reconciliationModel.countDocuments({ tenantId });
  }

  async countByStatus(tenantId: string, status: string): Promise<number> {
    return this.reconciliationModel.countDocuments({ tenantId, status });
  }

  async search(tenantId: string, filters: SearchFilters): Promise<Reconciliation[]> {
    const query: any = { tenantId };

    if (filters.status) {
      query.status = filters.status;
    }

    if (filters.dataSourceType) {
      query.dataSourceType = filters.dataSourceType;
    }

    if (filters.startDate || filters.endDate) {
      query.createdAt = {};
      if (filters.startDate) {
        query.createdAt.$gte = filters.startDate;
      }
      if (filters.endDate) {
        query.createdAt.$lte = filters.endDate;
      }
    }

    if (filters.searchTerm) {
      query.$or = [{ name: { $regex: filters.searchTerm, $options: 'i' } }, { description: { $regex: filters.searchTerm, $options: 'i' } }];
    }

    const docs = await this.reconciliationModel.find(query).sort({ createdAt: -1 }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async getStats(tenantId: string, period?: StatsPeriod): Promise<ReconciliationStats> {
    const matchQuery: any = { tenantId };

    if (period?.startDate || period?.endDate) {
      matchQuery.createdAt = {};
      if (period.startDate) {
        matchQuery.createdAt.$gte = period.startDate;
      }
      if (period.endDate) {
        matchQuery.createdAt.$lte = period.endDate;
      }
    }

    const [totalResult, statusResults, matchResults, differenceResults] = await Promise.all([
      this.reconciliationModel.countDocuments(matchQuery),
      this.reconciliationModel.aggregate([
        { $match: matchQuery },
        { $group: { _id: '$status', count: { $sum: 1 } } },
      ]),
      this.reconciliationModel.aggregate([
        { $match: matchQuery },
        { $group: { _id: null, total: { $sum: '$totalMatches' } } },
      ]),
      this.reconciliationModel.aggregate([
        { $match: matchQuery },
        { $group: { _id: null, total: { $sum: '$totalDifferences' } } },
      ]),
    ]);

    const byStatus: Record<string, number> = {
      PENDING: 0,
      RUNNING: 0,
      COMPLETED: 0,
      FAILED: 0,
      PARTIAL: 0,
    };

    for (const result of statusResults) {
      byStatus[result._id] = result.count;
    }

    const byDay = await this.getStatsByDay(matchQuery);

    return {
      total: totalResult,
      totalReconciliations: totalResult,
      completedReconciliations: byStatus['COMPLETED'] || 0,
      failedReconciliations: byStatus['FAILED'] || 0,
      pendingReconciliations: byStatus['PENDING'] || 0,
      runningReconciliations: byStatus['RUNNING'] || 0,
      partialReconciliations: byStatus['PARTIAL'] || 0,
      byStatus: byStatus as any,
      totalMatches: matchResults[0]?.total || 0,
      totalDifferences: differenceResults[0]?.total || 0,
      autoResolvedCount: 0,
      manualReviewCount: 0,
      averageProcessingTime: 0,
      byDay,
    } as any;
  }

  private async getStatsByDay(matchQuery: any): Promise<Array<{ date: string; count: number; matches: number; differences: number }>> {
    const results = await this.reconciliationModel
      .aggregate([
        {
          $match: matchQuery,
        },
        {
          $group: {
            _id: {
              date: { $dateToString: { format: '%Y-%m-%d', date: '$createdAt' } },
            },
            count: { $sum: 1 },
            matches: { $sum: '$totalMatches' },
            differences: { $sum: '$totalDifferences' },
          },
        },
        {
          $sort: { '_id.date': -1 },
        },
        {
          $limit: 30,
        },
      ])
      .exec();

    return results.map((r) => ({
      date: r._id.date,
      count: r.count,
      matches: r.matches,
      differences: r.differences,
    }));
  }

  private documentToEntity(doc: ReconciliationDocument): Reconciliation {
    const props: ReconciliationProps = {
      id: doc._id.toString() as any,
      tenantId: doc.tenantId,
      name: doc.name,
      description: doc.description,
      status: doc.status as any,
      startDate: doc.startDate,
      endDate: doc.endDate,
      dataSourceType: doc.dataSourceType as any,
      bankAccountId: doc.bankAccountId,
      internalAccountId: doc.internalAccountId,
      ruleIds: doc.ruleIds,
      matches: (doc.matches || []) as any,
      differences: (doc.differences || []) as any,
      totalTransactionsProcessed: doc.totalTransactionsProcessed,
      totalMatches: doc.totalMatches,
      totalDifferences: doc.totalDifferences,
      autoResolvedCount: doc.autoResolvedCount,
      manualReviewCount: doc.manualReviewCount,
      createdBy: doc.createdBy,
      completedBy: doc.completedBy,
      completedAt: doc.completedAt,
      errorDetails: doc.errorDetails,
      scheduled: doc.scheduled,
      scheduleExpression: doc.scheduleExpression,
      lastRunAt: doc.lastRunAt,
      nextRunAt: doc.nextRunAt,
      metadata: doc.metadata,
      createdAt: (doc as any).createdAt || new Date(),
      updatedAt: (doc as any).updatedAt || new Date(),
    };

    return new Reconciliation(props);
  }
}
