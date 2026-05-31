import { LogSource } from '../models/log-source.entity';

export interface ILogSourceRepository {
  save(source: LogSource): Promise<LogSource>;
  findById(id: string): Promise<LogSource | null>;
  findEnabled(): Promise<LogSource[]>;
  findWithRetention(): Promise<LogSource[]>;
  findByFilters(filters: any, limit: number, skip: number): Promise<{ data: LogSource[]; total: number }>;
  findByIdAndUpdate(id: string, update: any): Promise<LogSource | null>;
  findByIdAndDelete(id: string): Promise<LogSource | null>;
}
