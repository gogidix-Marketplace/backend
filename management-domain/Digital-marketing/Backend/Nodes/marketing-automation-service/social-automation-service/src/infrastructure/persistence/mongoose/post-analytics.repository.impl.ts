import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IPostAnalyticsRepository } from '../../../domain/ports/repositories/post-analytics.repository';
import { PostAnalytics } from '../../../domain/models/post-analytics';
import { PostAnalyticsDocument } from '../schemas/post-analytics.schema';

@Injectable()
export class PostAnalyticsRepositoryImpl implements IPostAnalyticsRepository {
  constructor(@InjectModel(PostAnalyticsDocument.name) private readonly model: Model<PostAnalyticsDocument>) {}

  async save(analytics: PostAnalytics): Promise<PostAnalytics> {
    const props = analytics.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new PostAnalytics({ ...saved.toObject(), id: saved._id.toString() });
  }

  async findByAccountAndDateRange(accountId: string, startDate: Date, endDate: Date): Promise<PostAnalytics[]> {
    const docs = await this.model.find({ accountId, date: { $gte: startDate, $lte: endDate } }).sort({ date: -1 }).exec();
    return docs.map(doc => new PostAnalytics({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findByPostId(postId: string): Promise<PostAnalytics[]> {
    const docs = await this.model.find({ postId }).exec();
    return docs.map(doc => new PostAnalytics({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findByTenantId(tenantId: string, options?: { platform?: string; startDate?: Date; endDate?: Date }): Promise<PostAnalytics[]> {
    const filter: any = { tenantId };
    if (options?.platform) filter.platform = options.platform;
    if (options?.startDate || options?.endDate) {
      filter.date = {};
      if (options?.startDate) filter.date.$gte = options.startDate;
      if (options?.endDate) filter.date.$lte = options.endDate;
    }
    const docs = await this.model.find(filter).sort({ date: -1 }).exec();
    return docs.map(doc => new PostAnalytics({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async update(analytics: PostAnalytics): Promise<PostAnalytics> {
    const props = analytics.toPlainObject();
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true }).exec();
    if (!doc) return analytics;
    return new PostAnalytics({ ...doc.toObject(), id: doc._id.toString() });
  }
}
