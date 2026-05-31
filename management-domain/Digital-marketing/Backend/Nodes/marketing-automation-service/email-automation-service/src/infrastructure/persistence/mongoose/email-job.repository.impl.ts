import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IEmailJobRepository } from '../../../domain/ports/repositories/email-job.repository';
import { EmailJob } from '../../../domain/models/email-job';
import { EmailJobDocument } from '../schemas/email-job.schema';

@Injectable()
export class EmailJobRepositoryImpl implements IEmailJobRepository {
  constructor(@InjectModel(EmailJobDocument.name) private readonly model: Model<EmailJobDocument>) {}

  async save(job: EmailJob): Promise<EmailJob> {
    const props = job.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new EmailJob({ ...props, id: saved._id.toString() });
  }

  async findById(id: string): Promise<EmailJob | null> {
    const doc = await this.model.findById(id).exec();
    if (!doc) return null;
    return new EmailJob({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByTenantId(tenantId: string, options?: { status?: string; page?: number; limit?: number }): Promise<EmailJob[]> {
    const filter: any = { tenantId };
    if (options?.status) filter.status = options.status;
    const page = options?.page ?? 1;
    const limit = options?.limit ?? 50;
    const docs = await this.model.find(filter).skip((page - 1) * limit).limit(limit).sort({ createdAt: -1 }).exec();
    return docs.map(doc => new EmailJob({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findByStatus(status: string): Promise<EmailJob[]> {
    const docs = await this.model.find({ status }).exec();
    return docs.map(doc => new EmailJob({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findScheduledReady(): Promise<EmailJob[]> {
    const docs = await this.model.find({ status: 'pending', scheduledAt: { $lte: new Date() } }).exec();
    return docs.map(doc => new EmailJob({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async update(job: EmailJob): Promise<EmailJob> {
    const props = job.toPlainObject();
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true }).exec();
    if (!doc) return job;
    return new EmailJob({ ...doc.toObject(), id: doc._id.toString() });
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.model.findByIdAndDelete(id).exec();
    return !!result;
  }

  async countByTenant(tenantId: string, status?: string): Promise<number> {
    const filter: any = { tenantId };
    if (status) filter.status = status;
    return this.model.countDocuments(filter).exec();
  }
}
