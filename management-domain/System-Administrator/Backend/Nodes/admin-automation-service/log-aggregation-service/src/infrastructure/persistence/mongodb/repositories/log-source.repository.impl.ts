import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { ILogSourceRepository } from '@domain/repositories/log-source-repository.interface';
import { LogSource } from '@domain/models/log-source.entity';
import { SourceStatus } from '@domain/enums/source-status.enum';
import { LogCollectionType } from '@domain/enums/log-collection-type.enum';
import { LogSourceType } from '@domain/enums/log-source-type.enum';
import { LogFormat } from '@domain/enums/log-format.enum';

@Injectable()
export class LogSourceRepositoryImpl implements ILogSourceRepository {
  private readonly logger = new Logger(LogSourceRepositoryImpl.name);

  constructor(@InjectModel('LogSource') private readonly model: Model<any>) {}

  async save(source: LogSource): Promise<LogSource> {
    const doc = new this.model(source);
    const saved = await doc.save();
    return this.toEntity(saved);
  }

  async findById(id: string): Promise<LogSource | null> {
    const doc = await this.model.findById(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  async findEnabled(): Promise<LogSource[]> {
    const docs = await this.model.find({ enabled: true }).lean();
    return docs.map(d => this.toEntity(d));
  }

  async findWithRetention(): Promise<LogSource[]> {
    const docs = await this.model.find({ enabled: true, 'retention.enabled': true }).lean();
    return docs.map(d => this.toEntity(d));
  }

  async findByFilters(filters: any, limit: number, skip: number): Promise<{ data: LogSource[]; total: number }> {
    const filter: any = {};
    if (filters.enabled !== undefined) filter.enabled = filters.enabled === 'true';
    if (filters.type) filter.type = filters.type;
    if (filters.sourceType) filter.sourceType = filters.sourceType;

    const [data, total] = await Promise.all([
      this.model.find(filter).sort({ createdAt: -1 }).skip(skip).limit(limit).lean(),
      this.model.countDocuments(filter),
    ]);
    return { data: data.map(d => this.toEntity(d)), total };
  }

  async findByIdAndUpdate(id: string, update: any): Promise<LogSource | null> {
    const doc = await this.model.findByIdAndUpdate(id, update, { new: true, runValidators: true }).lean();
    return doc ? this.toEntity(doc) : null;
  }

  async findByIdAndDelete(id: string): Promise<LogSource | null> {
    const doc = await this.model.findByIdAndDelete(id).lean();
    return doc ? this.toEntity(doc) : null;
  }

  private toEntity(doc: any): LogSource {
    return new LogSource(
      doc.name, doc.description, doc.enabled,
      doc.type as LogCollectionType, doc.sourceType as LogSourceType,
      doc.config, doc.parsing, doc.retention,
      doc.status as SourceStatus, doc.lastCollectedAt,
      { id: doc._id?.toString(), createdAt: doc.createdAt, updatedAt: doc.updatedAt },
    );
  }
}
