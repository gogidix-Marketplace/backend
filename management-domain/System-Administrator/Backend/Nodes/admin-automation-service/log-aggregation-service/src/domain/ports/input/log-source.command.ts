export interface ILogSourceCommand {
  createSource(data: any): Promise<any>;
  updateSource(id: string, data: any): Promise<any>;
  deleteSource(id: string): Promise<void>;
  enableSource(id: string): Promise<any>;
  disableSource(id: string): Promise<any>;
}
