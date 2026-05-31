import { Injectable, Logger } from '@nestjs/common';
import { ILogSearchQuery } from '@domain/ports/input/log-search.query';
import { ILogSearch } from '@domain/ports/output/log-search.interface';

@Injectable()
export class LogSearchQueryService implements ILogSearchQuery {
  private readonly logger = new Logger(LogSearchQueryService.name);

  constructor(private readonly logSearch: ILogSearch) {}

  async searchLogs(query: any): Promise<any> {
    return this.logSearch.search(query);
  }

  async getLogById(id: string, index?: string): Promise<any> {
    return this.logSearch.getLogById(id, index);
  }

  async getLogStats(hours = 24): Promise<any> {
    const startDate = new Date(Date.now() - hours * 60 * 60 * 1000);
    const endDate = new Date();
    const stats = await this.logSearch.getLogStats(startDate, endDate);
    return { timeRange: { start: startDate, end: endDate }, stats };
  }

  async aggregateLogs(query: any, aggregation: any): Promise<any> {
    return this.logSearch.aggregateLogs(query, aggregation);
  }

  async getTimeSeries(hours = 24, interval = '1h'): Promise<any> {
    const startDate = new Date(Date.now() - hours * 60 * 60 * 1000);
    const endDate = new Date();
    const aggregation = {
      timeSeries: {
        date_histogram: { field: 'timestamp', fixed_interval: interval },
        aggs: { byLevel: { terms: { field: 'level' } } },
      },
    };
    const series = await this.logSearch.aggregateLogs({ startTime: startDate, endTime: endDate }, aggregation);
    return { timeRange: { start: startDate, end: endDate }, series };
  }

  async getLogLevels(): Promise<string[]> {
    return ['debug', 'info', 'warn', 'error', 'fatal'];
  }
}
