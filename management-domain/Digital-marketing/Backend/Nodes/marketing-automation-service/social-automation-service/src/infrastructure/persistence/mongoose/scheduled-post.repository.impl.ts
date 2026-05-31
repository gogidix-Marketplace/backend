import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IScheduledPostRepository } from '../../../domain/ports/repositories/scheduled-post.repository';
import { ScheduledPost } from '../../../domain/models/scheduled-post';
import { ScheduledPostDocument } from '../schemas/scheduled-post.schema';

@Injectable()
export class ScheduledPostRepositoryImpl implements IScheduledPostRepository {
  constructor(@InjectModel(ScheduledPostDocument.name) private readonly model: Model<ScheduledPostDocument>) {}

  async save(post: ScheduledPost): Promise<ScheduledPost> {
    const props = post.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new ScheduledPost({ ...saved.toObject(), id: saved._id.toString() });
  }

  async findById(id: string): Promise<ScheduledPost | null> {
    const doc = await this.model.findById(id).exec();
    if (!doc) return null;
    return new ScheduledPost({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByTenantId(tenantId: string, options?: { status?: string; platform?: string; page?: number; limit?: number }): Promise<ScheduledPost[]> {
    const filter: any = { tenantId };
    if (options?.status) filter.status = options.status;
    if (options?.platform) filter.platform = options.platform;
    const page = options?.page ?? 1;
    const limit = options?.limit ?? 50;
    const docs = await this.model.find(filter).skip((page - 1) * limit).limit(limit).sort({ scheduledAt: -1 }).exec();
    return docs.map(doc => new ScheduledPost({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findReadyToPublish(): Promise<ScheduledPost[]> {
    const docs = await this.model.find({ status: 'scheduled', scheduledAt: { $lte: new Date() } }).exec();
    return docs.map(doc => new ScheduledPost({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findByAccountId(accountId: string): Promise<ScheduledPost[]> {
    const docs = await this.model.find({ accountId }).sort({ scheduledAt: -1 }).exec();
    return docs.map(doc => new ScheduledPost({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async update(post: ScheduledPost): Promise<ScheduledPost> {
    const props = post.toPlainObject();
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true }).exec();
    if (!doc) return post;
    return new ScheduledPost({ ...doc.toObject(), id: doc._id.toString() });
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
