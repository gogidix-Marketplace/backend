export interface ILogSearchQuery {
  searchLogs(query: any): Promise<any>;
  getLogById(id: string, index?: string): Promise<any>;
  getLogStats(hours?: number): Promise<any>;
  aggregateLogs(query: any, aggregation: any): Promise<any>;
  getTimeSeries(hours?: number, interval?: string): Promise<any>;
  getLogLevels(): Promise<string[]>;
}
