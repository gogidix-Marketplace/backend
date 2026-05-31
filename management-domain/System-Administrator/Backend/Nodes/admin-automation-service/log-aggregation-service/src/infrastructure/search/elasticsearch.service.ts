import { Injectable, Logger, OnModuleInit } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Client } from '@elastic/elasticsearch';
import { ILogSearch } from '@domain/ports/output/log-search.interface';
import { LogEntry } from '@domain/models/log-entry.entity';

@Injectable()
export class ElasticsearchService implements ILogSearch, OnModuleInit {
  private readonly logger = new Logger(ElasticsearchService.name);
  private client: Client;
  private indexPrefix: string;

  constructor(private readonly configService: ConfigService) {
    const node = this.configService.get<string>('ELASTICSEARCH_NODE') || 'http://localhost:9200';
    const username = this.configService.get<string>('ELASTICSEARCH_USERNAME');
    const password = this.configService.get<string>('ELASTICSEARCH_PASSWORD');
    const apiKey = this.configService.get<string>('ELASTICSEARCH_API_KEY');

    const auth: any = {};
    if (apiKey) auth.apiKey = apiKey;
    else if (username && password) { auth.username = username; auth.password = password; }

    this.client = new Client({ node, auth: Object.keys(auth).length > 0 ? auth : undefined });
    this.indexPrefix = this.configService.get<string>('ELASTICSEARCH_INDEX_PREFIX') || 'logs-';
  }

  async onModuleInit() {
    await this.createIndexTemplate();
    this.logger.log('Elasticsearch initialized');
  }

  async indexLog(log: LogEntry): Promise<string> {
    const indexName = this.getIndexName(log.timestamp);
    const response = await this.client.index({ index: indexName, document: log, refresh: false });
    return response._id;
  }

  async bulkIndexLogs(logs: LogEntry[]): Promise<void> {
    if (logs.length === 0) return;
    const operations: any[] = [];
    for (const log of logs) {
      operations.push({ index: { _index: this.getIndexName(log.timestamp) } });
      operations.push(log);
    }
    await this.client.bulk({ refresh: false, operations });
  }

  async search(query: any): Promise<any> {
    const esQuery = this.buildQuery(query);
    const indexPattern = this.getIndexPattern(query.startTime, query.endTime);
    const response = await this.client.search({ index: indexPattern, body: esQuery, size: query.limit || 100, from: query.offset || 0 });
    return {
      total: typeof response.hits.total === 'number' ? response.hits.total : (response.hits.total as any)?.value || 0,
      hits: (response.hits.hits as any[]).map((h: any) => ({ ...h._source, id: h._id })),
      aggregations: response.aggregations,
      took: response.took,
    };
  }

  async aggregateLogs(query: any, aggregation: any): Promise<any> {
    const esQuery = this.buildQuery(query);
    esQuery.aggs = aggregation;
    const indexPattern = this.getIndexPattern(query.startTime, query.endTime);
    const response = await this.client.search({ index: indexPattern, body: esQuery, size: 0 });
    return response.aggregations;
  }

  async getLogById(id: string, index?: string): Promise<LogEntry | null> {
    try {
      const response = await this.client.get({ index: index || `${this.indexPrefix}*`, id });
      const source = response._source as Record<string, any>;
      return { ...source, id: response._id } as any;
    } catch (error: any) {
      if (error.meta?.statusCode === 404) return null;
      throw error;
    }
  }

  async deleteLogs(query: any): Promise<number> {
    const esQuery = this.buildQuery(query);
    const indexPattern = this.getIndexPattern(query.startTime, query.endTime);
    const response = await this.client.deleteByQuery({ index: indexPattern, body: esQuery });
    return response.deleted || 0;
  }

  async getLogStats(startDate: Date, endDate: Date): Promise<any> {
    const indexPattern = this.getIndexPattern(startDate, endDate);
    const response = await this.client.search({
      index: indexPattern,
      body: {
        query: { range: { timestamp: { gte: startDate.toISOString(), lte: endDate.toISOString() } } },
        aggs: {
          logsByLevel: { terms: { field: 'level' } },
          logsBySource: { terms: { field: 'source.type' } },
          logsByService: { terms: { field: 'source.service' } },
          timeSeries: { date_histogram: { field: 'timestamp', fixed_interval: '1h' } },
        },
        size: 0,
      },
    });
    return response.aggregations;
  }

  async deleteOldLogs(daysToKeep: number): Promise<number> {
    const cutoffDate = new Date(Date.now() - daysToKeep * 24 * 60 * 60 * 1000);
    try {
      const indices = await this.client.indices.get({ index: `${this.indexPrefix}*` });
      const toDelete = Object.keys(indices).filter(name => {
        const dateStr = name.replace(this.indexPrefix, '');
        const idxDate = new Date(dateStr);
        return idxDate < cutoffDate;
      });
      for (const idx of toDelete) {
        await this.client.indices.delete({ index: idx });
      }
      return toDelete.length;
    } catch { return 0; }
  }

  async createIndexTemplate(): Promise<void> {
    const templateName = this.configService.get<string>('ELASTICSEARCH_INDEX_TEMPLATE') || 'logs-template';
    try {
      await this.client.indices.putIndexTemplate({
        name: templateName,
        body: {
          index_patterns: [`${this.indexPrefix}*`],
          template: {
            settings: { number_of_shards: 1, number_of_replicas: 1, refresh_interval: '5s' },
            mappings: {
              properties: {
                timestamp: { type: 'date' },
                level: { type: 'keyword' },
                message: { type: 'text', fields: { keyword: { type: 'keyword' } } },
                'source.service': { type: 'keyword' },
                'source.host': { type: 'keyword' },
                'source.type': { type: 'keyword' },
                tags: { type: 'keyword' },
                correlationId: { type: 'keyword' },
              },
            },
          },
        },
      });
    } catch (error) { this.logger.warn('Index template creation skipped', error); }
  }

  async healthCheck(): Promise<boolean> {
    try { await this.client.ping(); return true; } catch { return false; }
  }

  private buildQuery(query: any): any {
    const must: any[] = [];
    const filter: any[] = [];
    if (query.query) must.push({ bool: { should: [{ match: { message: query.query } }] } });
    if (query.level?.length) filter.push({ terms: { level: query.level } });
    if (query.sources?.length) filter.push({ terms: { 'source.type': query.sources } });
    if (query.services?.length) filter.push({ terms: { 'source.service': query.services } });
    if (query.startTime || query.endTime) {
      const range: any = {};
      if (query.startTime) range.gte = new Date(query.startTime).toISOString();
      if (query.endTime) range.lte = new Date(query.endTime).toISOString();
      filter.push({ range: { timestamp: range } });
    }
    const bool: any = {};
    if (must.length) bool.must = must;
    if (filter.length) bool.filter = filter;
    return { query: Object.keys(bool).length ? { bool } : { match_all: {} }, sort: [{ timestamp: query.sort === 'asc' ? 'asc' : 'desc' }] };
  }

  private getIndexName(date: Date): string {
    return `${this.indexPrefix}${date.getUTCFullYear()}-${String(date.getUTCMonth() + 1).padStart(2, '0')}-${String(date.getUTCDate()).padStart(2, '0')}`;
  }

  private getIndexPattern(startDate?: Date, endDate?: Date): string {
    if (!startDate && !endDate) return `${this.indexPrefix}*`;
    const d = startDate || endDate || new Date();
    return `${this.indexPrefix}${d.getUTCFullYear()}-${String(d.getUTCMonth() + 1).padStart(2, '0')}-*`;
  }
}
