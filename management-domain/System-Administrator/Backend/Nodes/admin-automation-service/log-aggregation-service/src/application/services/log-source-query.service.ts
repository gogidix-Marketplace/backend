import { Injectable, Logger } from '@nestjs/common';
import { ILogSourceQuery } from '@domain/ports/input/log-source.query';
import { ILogSourceRepository } from '@domain/repositories/log-source-repository.interface';

@Injectable()
export class LogSourceQueryService implements ILogSourceQuery {
  private readonly logger = new Logger(LogSourceQueryService.name);

  constructor(private readonly sourceRepository: ILogSourceRepository) {}

  async getSources(filters: any = {}): Promise<{ data: any[]; total: number }> {
    return this.sourceRepository.findByFilters(filters, filters.limit || 50, filters.skip || 0);
  }

  async getSourceById(id: string): Promise<any> {
    return this.sourceRepository.findById(id);
  }

  async getRetentionStats(): Promise<any> {
    const sources = await this.sourceRepository.findWithRetention();
    return {
      sources: sources.map(s => ({ name: s.name, retentionDays: s.retention.days, archiveEnabled: s.retention.archive })),
      totalSources: sources.length,
      defaultRetention: 30,
    };
  }
}
