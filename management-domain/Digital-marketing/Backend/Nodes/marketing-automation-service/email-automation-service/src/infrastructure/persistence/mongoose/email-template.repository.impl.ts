import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IEmailTemplateRepository } from '../../../domain/ports/repositories/email-template.repository';
import { EmailTemplate } from '../../../domain/models/email-template';
import { EmailTemplateDocument } from '../schemas/email-template.schema';

@Injectable()
export class EmailTemplateRepositoryImpl implements IEmailTemplateRepository {
  constructor(@InjectModel(EmailTemplateDocument.name) private readonly model: Model<EmailTemplateDocument>) {}

  async save(template: EmailTemplate): Promise<EmailTemplate> {
    const props = template.toPlainObject();
    const doc = new this.model(props);
    const saved = await doc.save();
    return new EmailTemplate({ ...props, id: saved._id.toString() });
  }

  async findById(id: string): Promise<EmailTemplate | null> {
    const doc = await this.model.findById(id).exec();
    if (!doc) return null;
    return new EmailTemplate({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findBySlug(slug: string, tenantId: string): Promise<EmailTemplate | null> {
    const doc = await this.model.findOne({ slug, tenantId }).exec();
    if (!doc) return null;
    return new EmailTemplate({ ...doc.toObject(), id: doc._id.toString() });
  }

  async findByTenantId(tenantId: string, options?: { category?: string; isActive?: boolean }): Promise<EmailTemplate[]> {
    const filter: any = { tenantId };
    if (options?.category) filter.category = options.category;
    if (options?.isActive !== undefined) filter.isActive = options.isActive;
    const docs = await this.model.find(filter).sort({ name: 1 }).exec();
    return docs.map(doc => new EmailTemplate({ ...doc.toObject(), id: doc._id.toString() }));
  }

  async update(template: EmailTemplate): Promise<EmailTemplate> {
    const props = template.toPlainObject();
    const doc = await this.model.findByIdAndUpdate(props.id, props, { new: true }).exec();
    if (!doc) return template;
    return new EmailTemplate({ ...doc.toObject(), id: doc._id.toString() });
  }

  async delete(id: string): Promise<boolean> {
    const result = await this.model.findByIdAndDelete(id).exec();
    return !!result;
  }
}
