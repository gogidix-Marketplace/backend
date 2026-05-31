import { Injectable, Inject } from '@nestjs/common';
import { Model } from 'mongoose';
import { ForecastAccuracy } from '../../../../domain/entities/forecast-accuracy.entity';
import { ForecastAccuracyRepositoryPort } from '../../../../domain/ports/out/forecast-accuracy.repository.port';
import { ForecastAccuracyDocument } from '../schemas/forecast-accuracy.schema';

@Injectable()
export class ForecastAccuracyRepositoryImpl implements ForecastAccuracyRepositoryPort {
  constructor(
    @Inject('ForecastAccuracyModel')
    private readonly accuracyModel: Model<ForecastAccuracyDocument>,
  ) {}

  async save(accuracy: ForecastAccuracy): Promise<ForecastAccuracy> {
    const created = new this.accuracyModel(accuracy.toJSON());
    const saved = await created.save();
    return ForecastAccuracy.fromJSON(saved.toJSON());
  }

  async findById(id: string, tenantId: string): Promise<ForecastAccuracy | null> {
    const document = await this.accuracyModel.findOne({ _id: id, tenantId });
    return document ? ForecastAccuracy.fromJSON(document.toJSON()) : null;
  }

  async findByForecastId(forecastId: string, tenantId: string): Promise<ForecastAccuracy[]> {
    const documents = await this.accuracyModel
      .find({ forecastId, tenantId })
      .sort({ calculatedAt: -1 })
      .exec();

    return documents.map(doc => ForecastAccuracy.fromJSON(doc.toJSON()));
  }

  async findLatestByForecastId(forecastId: string, tenantId: string): Promise<ForecastAccuracy | null> {
    const document = await this.accuracyModel
      .findOne({ forecastId, tenantId })
      .sort({ calculatedAt: -1 })
      .exec();

    return document ? ForecastAccuracy.fromJSON(document.toJSON()) : null;
  }

  async findByForecastAndPeriod(
    forecastId: string,
    periodId: string,
    tenantId: string,
  ): Promise<ForecastAccuracy | null> {
    const document = await this.accuracyModel.findOne({
      forecastId,
      periodId,
      tenantId,
    });

    return document ? ForecastAccuracy.fromJSON(document.toJSON()) : null;
  }

  async findBelowThreshold(tenantId: string, threshold: number): Promise<ForecastAccuracy[]> {
    const documents = await this.accuracyModel
      .find({
        tenantId,
        overallAccuracy: { $lt: threshold },
      })
      .sort({ overallAccuracy: 1 })
      .exec();

    return documents.map(doc => ForecastAccuracy.fromJSON(doc.toJSON()));
  }

  async update(accuracy: ForecastAccuracy): Promise<ForecastAccuracy> {
    const updated = await this.accuracyModel
      .findOneAndUpdate(
        { _id: accuracy.id, tenantId: accuracy.tenantId },
        accuracy.toJSON(),
        { new: true },
      )
      .exec();

    if (!updated) {
      throw new Error(`ForecastAccuracy not found: ${accuracy.id}`);
    }

    return ForecastAccuracy.fromJSON(updated.toJSON());
  }

  async delete(id: string, tenantId: string): Promise<void> {
    await this.accuracyModel.deleteOne({ _id: id, tenantId }).exec();
  }
}

