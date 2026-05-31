import { Injectable, Inject } from '@nestjs/common';
import { Model } from 'mongoose';
import { Forecast } from '../../../../domain/entities/forecast.entity';
import {
  ForecastRepositoryPort,
  ForecastFilter,
} from '../../../../domain/ports/out/forecast.repository.port';
import { ForecastDocument, ForecastSchema } from '../schemas/forecast.schema';

@Injectable()
export class ForecastRepositoryImpl implements ForecastRepositoryPort {
  constructor(
    @Inject('ForecastModel')
    private readonly forecastModel: Model<ForecastDocument>,
  ) {}

  async save(forecast: Forecast): Promise<Forecast> {
    const created = new this.forecastModel(forecast.toJSON());
    const saved = await created.save();
    return Forecast.fromJSON(saved.toJSON());
  }

  async findById(id: string, tenantId: string): Promise<Forecast | null> {
    const document = await this.forecastModel.findOne({ _id: id, tenantId });
    return document ? Forecast.fromJSON(document.toJSON()) : null;
  }

  async findByTenantAndFilters(
    filters: ForecastFilter,
    page: number,
    limit: number,
    sortBy = 'createdAt',
    sortOrder: 'ASC' | 'DESC' = 'DESC',
  ): Promise<{ forecasts: Forecast[]; total: number }> {
    const query: any = { tenantId: filters.tenantId };

    if (filters.status) query.status = filters.status;
    if (filters.model) query.model = filters.model;
    if (filters.period) query.period = filters.period;
    if (filters.granularity) query.granularity = filters.granularity;
    if (filters.granularityId) query.granularityId = filters.granularityId;
    if (filters.startDate || filters.endDate) {
      query.startDate = {};
      if (filters.startDate) query.startDate.$gte = filters.startDate;
      if (filters.endDate) query.startDate.$lte = filters.endDate;
    }

    const skip = (page - 1) * limit;
    const sort: any = {};
    sort[sortBy] = sortOrder === 'ASC' ? 1 : -1;

    const [documents, total] = await Promise.all([
      this.forecastModel.find(query).sort(sort).skip(skip).limit(limit).exec(),
      this.forecastModel.countDocuments(query),
    ]);

    const forecasts = documents.map(doc => Forecast.fromJSON(doc.toJSON()));

    return { forecasts, total };
  }

  async findActiveByTenant(tenantId: string): Promise<Forecast[]> {
    const documents = await this.forecastModel
      .find({ tenantId, status: 'ACTIVE' })
      .sort({ createdAt: -1 })
      .exec();

    return documents.map(doc => Forecast.fromJSON(doc.toJSON()));
  }

  async findLatestByGranularity(
    tenantId: string,
    granularity: string,
    granularityId?: string,
  ): Promise<Forecast | null> {
    const query: any = { tenantId, granularity };
    if (granularityId) query.granularityId = granularityId;

    const document = await this.forecastModel.findOne(query).sort({ createdAt: -1 }).exec();
    return document ? Forecast.fromJSON(document.toJSON()) : null;
  }

  async update(forecast: Forecast): Promise<Forecast> {
    const updated = await this.forecastModel
      .findOneAndUpdate({ _id: forecast.id, tenantId: forecast.tenantId }, forecast.toJSON(), {
        new: true,
      })
      .exec();

    if (!updated) {
      throw new Error(`Forecast not found: ${forecast.id}`);
    }

    return Forecast.fromJSON(updated.toJSON());
  }

  async delete(id: string, tenantId: string): Promise<void> {
    await this.forecastModel.deleteOne({ _id: id, tenantId }).exec();
  }

  async existsByName(name: string, tenantId: string, excludeId?: string): Promise<boolean> {
    const query: any = { name, tenantId };
    if (excludeId) query._id = { $ne: excludeId };

    const count = await this.forecastModel.countDocuments(query);
    return count > 0;
  }
}

