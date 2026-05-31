import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IAuditLogRepository, AuditLog, FindOptions } from '../../../../domain/repositories/reconciliation-repository.interface';
import { AuditLogDocument, AuditLogSchema } from '../mongoose/audit-log.schema';
import { v4 as uuidv4 } from 'uuid';

@Injectable()
export class AuditLogRepository implements IAuditLogRepository {
  constructor(
    @InjectModel('AuditLog')
    private readonly auditLogModel: Model<AuditLogDocument>,
  ) {}

  async save(log: Omit<AuditLog, 'id'>): Promise<AuditLog> {
    const doc = new this.auditLogModel({
      ...log,
      _id: uuidv4(),
    });
    const saved = await doc.save();
    return {
      id: saved._id.toString() as any,
      reconciliationId: saved.reconciliationId as any,
      action: saved.action,
      userId: saved.userId,
      timestamp: saved.timestamp,
      details: saved.details,
      ipAddress: saved.ipAddress,
    };
  }

  async findByReconciliation(reconciliationId: string, options?: FindOptions): Promise<AuditLog[]> {
    const query = this.auditLogModel.find({ reconciliationId });

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.sort({ timestamp: -1 }).exec();
    return docs.map((doc) => this.documentToAuditLog(doc));
  }

  async findByUser(userId: string, tenantId: string, options?: FindOptions): Promise<AuditLog[]> {
    const reconciliations = await this.auditLogModel
      .distinct('reconciliationId', { userId })
      .exec();

    const query = this.auditLogModel.find({ userId, reconciliationId: { $in: reconciliations } });

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    }

    const docs = await query.sort({ timestamp: -1 }).exec();
    return docs.map((doc) => this.documentToAuditLog(doc));
  }

  async findByAction(action: string, tenantId: string, options?: FindOptions): Promise<AuditLog[]> {
    let query = this.auditLogModel.find({ action });

    if (options?.page && options?.limit) {
      query = query.skip((options.page - 1) * options.limit).limit(options.limit) as any;
    }

    const docs = await query.sort({ timestamp: -1 }).exec();
    return docs.map((doc) => this.documentToAuditLog(doc));
  }

  private documentToAuditLog(doc: AuditLogDocument): AuditLog {
    return {
      id: doc._id.toString() as any,
      reconciliationId: doc.reconciliationId as any,
      action: doc.action,
      userId: doc.userId,
      timestamp: doc.timestamp,
      details: doc.details,
      ipAddress: doc.ipAddress,
    };
  }
}
