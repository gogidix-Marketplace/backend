import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { TransactionDifference, TransactionDifferenceProps } from '../../../../domain/models/transaction-difference.entity';
import { ITransactionDifferenceRepository, DifferenceFindOptions, UnresolvedOptions } from '../../../../domain/repositories/reconciliation-repository.interface';
import { TransactionDifferenceDocument, TransactionDifferenceSchema } from '../mongoose/transaction-difference.schema';
import { DifferenceStatus } from '../../../../domain/enums/difference-status.enum';
import { DifferenceType } from '../../../../domain/models/transaction-difference.entity';

@Injectable()
export class TransactionDifferenceRepository implements ITransactionDifferenceRepository {
  constructor(
    @InjectModel('TransactionDifference')
    private readonly differenceModel: Model<TransactionDifferenceDocument>,
  ) {}

  async save(difference: TransactionDifference): Promise<TransactionDifference> {
    const doc = new this.differenceModel(difference.toJSON());
    const saved = await doc.save();
    return this.documentToEntity(saved);
  }

  async findById(id: string, tenantId: string): Promise<TransactionDifference | null> {
    const doc = await this.differenceModel.findOne({ _id: id, tenantId });
    return doc ? this.documentToEntity(doc) : null;
  }

  async findByReconciliation(reconciliationId: string, tenantId: string, options?: DifferenceFindOptions): Promise<TransactionDifference[]> {
    const query: any = { reconciliationId, tenantId };

    if (options?.status) {
      query.status = options.status;
    }

    if (options?.differenceType) {
      query.differenceType = options.differenceType;
    }

    if (options?.severity) {
      query.severity = options.severity;
    }

    let docsQuery = this.differenceModel.find(query);

    if (options?.page && options?.limit) {
      docsQuery = docsQuery.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await docsQuery.sort({ detectedAt: -1 }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findByStatus(status: DifferenceStatus, tenantId: string): Promise<TransactionDifference[]> {
    const docs = await this.differenceModel.find({ tenantId, status }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findUnresolved(tenantId: string, options?: UnresolvedOptions): Promise<TransactionDifference[]> {
    const query: any = {
      tenantId,
      status: { $in: ['PENDING_REVIEW', 'ESCALATED'] },
    };

    if (options?.severity) {
      query.severity = options.severity;
    }

    if (options?.overdueOnly) {
      query.dueDate = { $lt: new Date() };
    }

    let docsQuery = this.differenceModel.find(query);

    if (options?.page && options?.limit) {
      docsQuery = docsQuery.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await docsQuery.sort({ severity: -1, detectedAt: -1 }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findOverdue(tenantId: string): Promise<TransactionDifference[]> {
    const docs = await this.differenceModel
      .find({
        tenantId,
        status: { $in: ['PENDING_REVIEW', 'ESCALATED'] },
        dueDate: { $lt: new Date() },
      })
      .sort({ dueDate: 1 })
      .exec();

    return docs.map((doc) => this.documentToEntity(doc));
  }

  async delete(id: string, tenantId: string): Promise<void> {
    await this.differenceModel.deleteOne({ _id: id, tenantId });
  }

  async update(difference: TransactionDifference): Promise<TransactionDifference> {
    const updated = await this.differenceModel
      .findOneAndUpdate({ _id: difference.id, tenantId: difference.props.tenantId }, difference.toJSON(), { new: true })
      .exec();

    if (!updated) {
      throw new Error('Difference not found');
    }

    return this.documentToEntity(updated);
  }

  async countByReconciliation(reconciliationId: string): Promise<number> {
    return this.differenceModel.countDocuments({ reconciliationId });
  }

  async countByStatus(tenantId: string, status: DifferenceStatus): Promise<number> {
    return this.differenceModel.countDocuments({ tenantId, status });
  }

  private documentToEntity(doc: TransactionDifferenceDocument): TransactionDifference {
    const props: TransactionDifferenceProps = {
      id: doc._id.toString() as any,
      reconciliationId: doc.reconciliationId as any,
      tenantId: doc.tenantId,
      bankTransactionId: doc.bankTransactionId,
      internalTransactionId: doc.internalTransactionId,
      differenceType: doc.differenceType as DifferenceType,
      status: doc.status as DifferenceStatus,
      bankAmount: doc.bankAmount,
      internalAmount: doc.internalAmount,
      amountDifference: doc.amountDifference,
      currency: doc.currency,
      bankDate: doc.bankDate,
      internalDate: doc.internalDate,
      description: doc.description,
      detectedAt: doc.detectedAt,
      resolvedAt: doc.resolvedAt,
      resolvedBy: doc.resolvedBy,
      resolutionNotes: doc.resolutionNotes,
      autoResolution: doc.autoResolution,
      severity: doc.severity as any,
      assignee: doc.assignee,
      dueDate: doc.dueDate,
      bankTransaction: doc.bankTransaction,
      internalTransaction: doc.internalTransaction,
      metadata: doc.metadata,
      createdAt: (doc as any).createdAt || new Date(),
    };

    return new TransactionDifference(props);
  }
}
