import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ScoreAttributeSchema, ScoreAttributeDocument } from '../mongo/schemas/score-attribute.schema';
import { ScoreAttribute } from '../../../domain/entities/score-attribute.entity';
import {
  ScoreAttributeRepositoryPort,
  PaginationOptions,
} from '../../../domain/ports/in/score-attribute.repository.port';

@Injectable()
export class ScoreAttributeRepositoryImpl implements ScoreAttributeRepositoryPort {
  constructor(
    @InjectModel(ScoreAttributeSchema.name)
    private readonly scoreAttributeModel: Model<ScoreAttributeDocument>,
  ) {}

  async save(scoreAttribute: ScoreAttribute): Promise<ScoreAttribute> {
    const data = scoreAttribute.toPrimitives();
    const doc = await this.scoreAttributeModel.findOneAndUpdate(
      { _id: data.id },
      data as any,
      { upsert: true, new: true }
    );
    return ScoreAttribute.fromPrimitives(doc.toObject());
  }

  async findById(id: string): Promise<ScoreAttribute | null> {
    const doc = await this.scoreAttributeModel.findOne({ _id: id }).lean();
    return doc ? ScoreAttribute.fromPrimitives(doc) : null;
  }

  async findByTenantId(tenantId: string, options?: PaginationOptions): Promise<ScoreAttribute[]> {
    const query = this.scoreAttributeModel.find({ tenantId });

    if (options?.sortBy) {
      const sortOrder = options.sortOrder === 'asc' ? 1 : -1;
      query.sort({ [options.sortBy]: sortOrder });
    } else {
      query.sort({ displayOrder: 1, name: 1 });
    }

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    } else if (options?.limit) {
      query.limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => ScoreAttribute.fromPrimitives(doc));
  }

  async findByType(tenantId: string, type: string): Promise<ScoreAttribute[]> {
    const docs = await this.scoreAttributeModel
      .find({ tenantId, type })
      .sort({ displayOrder: 1, name: 1 })
      .lean();
    return docs.map(doc => ScoreAttribute.fromPrimitives(doc));
  }

  async findActiveByTenantId(tenantId: string): Promise<ScoreAttribute[]> {
    const docs = await this.scoreAttributeModel
      .find({ tenantId, isActive: true })
      .sort({ displayOrder: 1, name: 1 })
      .lean();
    return docs.map(doc => ScoreAttribute.fromPrimitives(doc));
  }

  async delete(id: string): Promise<void> {
    await this.scoreAttributeModel.deleteOne({ _id: id });
  }

  async exists(id: string, tenantId: string): Promise<boolean> {
    const count = await this.scoreAttributeModel.countDocuments({ _id: id, tenantId });
    return count > 0;
  }

  async count(tenantId: string): Promise<number> {
    return this.scoreAttributeModel.countDocuments({ tenantId });
  }
}

