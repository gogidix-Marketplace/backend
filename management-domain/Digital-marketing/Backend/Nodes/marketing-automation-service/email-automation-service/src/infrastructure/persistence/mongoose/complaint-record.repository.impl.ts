import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IComplaintRecordRepository } from '../../../domain/ports/repositories/complaint-record.repository';
import { ComplaintRecord } from '../../../domain/models/complaint-record';
import { ComplaintRecordDocument } from '../schemas/complaint-record.schema';

@Injectable()
export class ComplaintRecordRepositoryImpl implements IComplaintRecordRepository {
  constructor(@InjectModel(ComplaintRecordDocument.name) private readonly model: Model<ComplaintRecordDocument>) {}

  async save(record: ComplaintRecord): Promise<ComplaintRecord> {
    const props = record.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new ComplaintRecord({ ...props, id: saved._id.toString() });
  }

  async findByEmail(email: string): Promise<ComplaintRecord[]> {
    const docs = await this.model.find({ email }).exec();
    return docs.map(doc => new ComplaintRecord({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findByTenantId(tenantId: string): Promise<ComplaintRecord[]> {
    const docs = await this.model.find({ tenantId }).sort({ createdAt: -1 }).exec();
    return docs.map(doc => new ComplaintRecord({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async countByTenant(tenantId: string): Promise<number> {
    return this.model.countDocuments({ tenantId }).exec();
  }
}
