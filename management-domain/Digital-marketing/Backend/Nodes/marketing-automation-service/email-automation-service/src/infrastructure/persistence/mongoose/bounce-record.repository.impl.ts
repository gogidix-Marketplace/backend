import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IBounceRecordRepository } from '../../../domain/ports/repositories/bounce-record.repository';
import { BounceRecord } from '../../../domain/models/bounce-record';
import { BounceRecordDocument } from '../schemas/bounce-record.schema';

@Injectable()
export class BounceRecordRepositoryImpl implements IBounceRecordRepository {
  constructor(@InjectModel(BounceRecordDocument.name) private readonly model: Model<BounceRecordDocument>) {}

  async save(record: BounceRecord): Promise<BounceRecord> {
    const props = record.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new BounceRecord({ ...props, id: saved._id.toString() });
  }

  async findByEmail(email: string): Promise<BounceRecord[]> {
    const docs = await this.model.find({ email }).exec();
    return docs.map(doc => new BounceRecord({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findByTenantId(tenantId: string, options?: { bounceType?: string }): Promise<BounceRecord[]> {
    const filter: any = { tenantId };
    if (options?.bounceType) filter.bounceType = options.bounceType;
    const docs = await this.model.find(filter).sort({ createdAt: -1 }).exec();
    return docs.map(doc => new BounceRecord({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async countByTenant(tenantId: string): Promise<number> {
    return this.model.countDocuments({ tenantId }).exec();
  }
}
