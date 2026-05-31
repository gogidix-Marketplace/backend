import { Injectable, Logger } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { IMetricsHistoryRepository } from '@domain/repositories/metrics-history-repository.interface';
import { MetricsHistory } from '@domain/models/metrics-history.entity';
import { CloudProvider } from '@domain/enums/cloud-provider.enum';

@Injectable()
export class MetricsHistoryRepositoryImpl implements IMetricsHistoryRepository {
  private readonly logger = new Logger(MetricsHistoryRepositoryImpl.name);

  constructor(@InjectModel('MetricsHistory') private readonly model: Model<any>) {}

  async save(history: MetricsHistory): Promise<MetricsHistory> {
    const doc = new this.model(history);
    const saved = await doc.save();
    return this.toEntity(saved);
  }

  async findByResourceId(resourceId: string, hours: number): Promise<MetricsHistory[]> {
    const startTime = new Date(Date.now() - hours * 60 * 60 * 1000);
    const docs = await this.model.find({ resourceId, collectedAt: { $gte: startTime } }).sort({ collectedAt: -1 }).limit(100).lean();
    return docs.map(d => this.toEntity(d));
  }

  private toEntity(doc: any): MetricsHistory {
    return new MetricsHistory(
      doc.resourceId, doc.cloudProvider as CloudProvider, doc.metrics,
      doc.aggregatedMetrics, doc.collectedAt,
      { id: doc._id?.toString(), createdAt: doc.createdAt, updatedAt: doc.updatedAt },
    );
  }
}
