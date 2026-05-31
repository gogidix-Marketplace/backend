export interface ILogSourceQuery {
  getSources(filters?: any): Promise<{ data: any[]; total: number }>;
  getSourceById(id: string): Promise<any>;
  getRetentionStats(): Promise<any>;
}
