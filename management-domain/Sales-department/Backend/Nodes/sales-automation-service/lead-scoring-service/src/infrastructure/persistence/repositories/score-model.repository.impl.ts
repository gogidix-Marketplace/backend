import { Injectable } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ScoreModelSchema, ScoreModelDocument } from '../mongo/schemas/score-model.schema';
import { ScoreModel } from '../../../domain/entities/score-model.entity';
import {
  ScoreModelRepositoryPort,
  PaginationOptions,
} from '../../../domain/ports/in/score-model.repository.port';

@Injectable()
export class ScoreModelRepositoryImpl implements ScoreModelRepositoryPort {
  constructor(
    @InjectModel(ScoreModelSchema.name)
    private readonly scoreModelModel: Model<ScoreModelDocument>,
  ) {}

  async save(scoreModel: ScoreModel): Promise<ScoreModel> {
    const data = scoreModel.toPrimitives();
    const doc = await this.scoreModelModel.findOneAndUpdate(
      { _id: data.id },
      data as any,
      { upsert: true, new: true }
    );
    return ScoreModel.fromPrimitives(doc.toObject());
  }

  async findById(id: string): Promise<ScoreModel | null> {
    const doc = await this.scoreModelModel.findOne({ _id: id }).lean();
    return doc ? ScoreModel.fromPrimitives(doc) : null;
  }

  async findByTenantId(tenantId: string, options?: PaginationOptions): Promise<ScoreModel[]> {
    const query = this.scoreModelModel.find({ tenantId });

    if (options?.sortBy) {
      const sortOrder = options.sortOrder === 'asc' ? 1 : -1;
      query.sort({ [options.sortBy]: sortOrder });
    } else {
      query.sort({ updatedAt: -1 });
    }

    if (options?.page && options?.limit) {
      query.skip((options.page - 1) * options.limit).limit(options.limit);
    } else if (options?.limit) {
      query.limit(options.limit);
    }

    const docs = await query.lean();
    return docs.map(doc => ScoreModel.fromPrimitives(doc));
  }

  async findActiveByTenantId(tenantId: string): Promise<ScoreModel[]> {
    const docs = await this.scoreModelModel
      .find({ tenantId, status: 'active' })
      .sort({ updatedAt: -1 })
      .lean();
    return docs.map(doc => ScoreModel.fromPrimitives(doc));
  }

  async findDefaultByTenantId(tenantId: string): Promise<ScoreModel | null> {
    const doc = await this.scoreModelModel
      .findOne({ tenantId, isDefault: true })
      .lean();
    return doc ? ScoreModel.fromPrimitives(doc) : null;
  }

  async findByVersion(tenantId: string, modelName: string, version: number): Promise<ScoreModel | null> {
    const doc = await this.scoreModelModel
      .findOne({ tenantId, name: modelName, version })
      .lean();
    return doc ? ScoreModel.fromPrimitives(doc) : null;
  }

  async findVersionsByName(tenantId: string, modelName: string): Promise<ScoreModel[]> {
    const docs = await this.scoreModelModel
      .find({ tenantId, name: modelName })
      .sort({ version: -1 })
      .lean();
    return docs.map(doc => ScoreModel.fromPrimitives(doc));
  }

  async delete(id: string): Promise<void> {
    await this.scoreModelModel.deleteOne({ _id: id });
  }

  async exists(id: string, tenantId: string): Promise<boolean> {
    const count = await this.scoreModelModel.countDocuments({ _id: id, tenantId });
    return count > 0;
  }

  async count(tenantId: string): Promise<number> {
    return this.scoreModelModel.countDocuments({ tenantId });
  }
}

