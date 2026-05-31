import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { IIntentRepository } from '@domain/ports/output';
import { IntentProps } from '@domain/models';
import { IntentCategory } from '@domain/enums';

@Injectable()
export class IntentRepository implements IIntentRepository {
  constructor(
    @InjectModel('Intent') private readonly model: any,
  ) {}

  async findById(intentId: string): Promise<IntentProps | null> {
    const doc = await this.model.findById(intentId).lean();
    return doc ? this.toProps(doc) : null;
  }

  async findOne(filter: any): Promise<IntentProps | null> {
    const doc = await this.model.findOne(filter).lean();
    return doc ? this.toProps(doc) : null;
  }

  async find(filter: any, sort?: any): Promise<IntentProps[]> {
    let query = this.model.find(filter);
    if (sort) query = query.sort(sort);
    else query = query.sort({ priority: -1, name: 1 });
    const docs = await query.lean();
    return docs.map(d => this.toProps(d));
  }

  async findActiveByLanguage(language: string): Promise<IntentProps[]> {
    return this.find({ language, isActive: true }, { priority: -1, name: 1 });
  }

  async findByCategory(category: IntentCategory, language: string = 'en'): Promise<IntentProps[]> {
    return this.find({ category, language, isActive: true });
  }

  async save(data: any): Promise<any> {
    if (data._id) {
      const updated = await this.model.findByIdAndUpdate(data._id, data, { new: true, runValidators: true }).lean();
      return updated;
    }
    const created = await this.model.create(data);
    return created.toObject();
  }

  async findByIdAndUpdate(id: string, update: any, options?: any): Promise<IntentProps | null> {
    const doc = await this.model.findByIdAndUpdate(id, update, { new: true, runValidators: true, ...options }).lean();
    return doc ? this.toProps(doc) : null;
  }

  async findByIdAndDelete(id: string): Promise<IntentProps | null> {
    const doc = await this.model.findByIdAndDelete(id).lean();
    return doc ? this.toProps(doc) : null;
  }

  private toProps(doc: any): IntentProps {
    return {
      name: doc.name,
      category: doc.category,
      description: doc.description,
      trainingPhrases: doc.trainingPhrases || [],
      responses: doc.responses || [],
      parameters: doc.parameters || [],
      requiredSkills: doc.requiredSkills || [],
      requiresHandoff: doc.requiresHandoff || false,
      priority: doc.priority || 0,
      language: doc.language || 'en',
      isActive: doc.isActive !== undefined ? doc.isActive : true,
    };
  }
}
