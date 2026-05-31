import { LogEntry } from '../../models/log-entry.entity';

export interface ILogSearch {
  indexLog(log: LogEntry): Promise<string>;
  bulkIndexLogs(logs: LogEntry[]): Promise<void>;
  search(query: any): Promise<any>;
  aggregateLogs(query: any, aggregation: any): Promise<any>;
  getLogById(id: string, index?: string): Promise<LogEntry | null>;
  deleteLogs(query: any): Promise<number>;
  getLogStats(startDate: Date, endDate: Date): Promise<any>;
  deleteOldLogs(daysToKeep: number): Promise<number>;
  createIndexTemplate(): Promise<void>;
  healthCheck(): Promise<boolean>;
}
