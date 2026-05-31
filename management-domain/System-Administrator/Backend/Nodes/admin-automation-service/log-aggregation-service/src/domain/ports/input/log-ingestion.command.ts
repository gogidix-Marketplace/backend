export interface ILogIngestionCommand {
  ingestLog(logData: any, sourceName: string): Promise<void>;
  ingestBulkLogs(logs: any[], sourceName: string): Promise<{ success: number; failed: number }>;
}
