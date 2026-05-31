export const SCHEDULER_SERVICE_PORT = Symbol('SCHEDULER_SERVICE_PORT');
export interface ISchedulerService {
  schedule(name: string, cronExpression: string, handler: () => Promise<void>): void;
  stop(name: string): void;
}
