export interface QueueJob<T = any> {
  id: string;
  data: T;
  attempts: number;
}
export const QUEUE_SERVICE_PORT = Symbol('QUEUE_SERVICE_PORT');
export interface IQueueService {
  addJob<T>(queueName: string, data: T, options?: { delay?: number; attempts?: number }): Promise<string>;
  processQueue<T>(queueName: string, handler: (job: QueueJob<T>) => Promise<void>): void;
  getJobCount(queueName: string): Promise<number>;
}
