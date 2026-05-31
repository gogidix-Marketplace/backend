import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ISocialAccountRepository } from '../../../domain/ports/repositories/social-account.repository';
import { SocialAccount } from '../../../domain/models/social-account';
import { SocialAccountDocument } from '../schemas/social-account.schema';

@Injectable()
export class SocialAccountRepositoryImpl implements ISocialAccountRepository {
  constructor(@InjectModel(SocialAccountDocument.name) private readonly model: Model<SocialAccountDocument>) {}

  async save(account: SocialAccount): Promise<SocialAccount> {
    const props = account.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new SocialAccount({ ...saved.toObject(), id: saved._id.toString() });
  }

  async findById(id: string): Promise<SocialAccount | null> {
    const doc = await this.model.findById(id).exec();
    if (!doc) return null;
    return new SocialAccount({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByTenantId(tenantId: string, options?: { platform?: string; isActive?: boolean }): Promise<SocialAccount[]> {
    const filter: any = { tenantId };
    if (options?.platform) filter.platform = options.platform;
    if (options?.isActive !== undefined) filter.isActive = options.isActive;
    const docs = await this.model.find(filter).sort({ platform: 1, username: 1 }).exec();
    return docs.map(doc => new SocialAccount({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async findByPlatformUserId(platformUserId: string, platform: string): Promise<SocialAccount | null> {
    const doc = await this.model.findOne({ platformUserId, platform }).exec();
    if (!doc) return null;
    return new SocialAccount({ ...doc.toObject(), id: doc._id.toString() });
  }

  async update(account: SocialAccount): Promise<SocialAccount> {
    const props = account.toPlainObject();
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true }).exec();
    if (!doc) return account;
    return new SocialAccount({ ...doc.toObject(), id: doc._id.toString() });
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.model.findByIdAndDelete(id).exec();
    return !!result;
  }
}
