import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ReconciliationMatch, ReconciliationMatchProps } from '../../../../domain/models/reconciliation-match.entity';
import { IReconciliationMatchRepository, MatchFindOptions } from '../../../../domain/repositories/reconciliation-repository.interface';
import { ReconciliationMatchDocument, ReconciliationMatchSchema } from '../mongoose/reconciliation-match.schema';
import { MatchType } from '../../../../domain/enums/match-type.enum';

@Injectable()
export class ReconciliationMatchRepository implements IReconciliationMatchRepository {
  constructor(
    @InjectModel('ReconciliationMatch')
    private readonly matchModel: Model<ReconciliationMatchDocument>,
  ) {}

  async save(match: ReconciliationMatch): Promise<ReconciliationMatch> {
    const doc = new this.matchModel(match.toJSON());
    const saved = await doc.save();
    return this.documentToEntity(saved);
  }

  async findById(id: string, tenantId: string): Promise<ReconciliationMatch | null> {
    const doc = await this.matchModel.findOne({ _id: id, tenantId });
    return doc ? this.documentToEntity(doc) : null;
  }

  async findByReconciliation(reconciliationId: string, tenantId: string, options?: MatchFindOptions): Promise<ReconciliationMatch[]> {
    const query: any = { reconciliationId, tenantId };

    if (options?.matchType) {
      query.matchType = options.matchType;
    }

    if (options?.minConfidence !== undefined) {
      query.confidence = { $gte: options.minConfidence };
    }

    if (options?.verified !== undefined) {
      query.verified = options.verified;
    }

    let docsQuery = this.matchModel.find(query);

    if (options?.page && options?.limit) {
      docsQuery = docsQuery.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await docsQuery.sort({ matchDate: -1 }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async findByTransactions(bankTransactionId: string, internalTransactionId: string, tenantId: string): Promise<ReconciliationMatch[]> {
    const docs = await this.matchModel.find({ bankTransactionId, internalTransactionId, tenantId }).exec();
    return docs.map((doc) => this.documentToEntity(doc));
  }

  async delete(id: string, tenantId: string): Promise<void> {
    await this.matchModel.deleteOne({ _id: id, tenantId });
  }

  async update(match: ReconciliationMatch): Promise<ReconciliationMatch> {
    const updated = await this.matchModel
      .findOneAndUpdate({ _id: match.id, tenantId: match.props.tenantId }, match.toJSON(), { new: true })
      .exec();

    if (!updated) {
      throw new Error('Match not found');
    }

    return this.documentToEntity(updated);
  }

  async countByReconciliation(reconciliationId: string): Promise<number> {
    return this.matchModel.countDocuments({ reconciliationId });
  }

  private documentToEntity(doc: ReconciliationMatchDocument): ReconciliationMatch {
    const props: ReconciliationMatchProps = {
      id: doc._id.toString() as any,
      reconciliationId: doc.reconciliationId as any,
      tenantId: doc.tenantId,
      bankTransactionId: doc.bankTransactionId,
      internalTransactionId: doc.internalTransactionId,
      matchType: doc.matchType as MatchType,
      confidence: doc.confidence,
      matchDate: doc.matchDate,
      matchedBy: doc.matchedBy,
      verified: doc.verified,
      verifiedBy: doc.verifiedBy,
      verifiedAt: doc.verifiedAt,
      notes: doc.notes,
      bankTransaction: doc.bankTransaction,
      internalTransaction: doc.internalTransaction,
      metadata: doc.metadata,
      createdAt: (doc as any).createdAt || new Date(),
    };

    return new ReconciliationMatch(props);
  }
}
