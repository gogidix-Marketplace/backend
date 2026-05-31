import { Injectable } from '@nestjs/common';
import { ISchedulerService } from '../../../domain/ports/services/scheduler-service.port';
import * as cron from 'node-cron';

@Injectable()
export class CronSchedulerAdapter implements ISchedulerService {
  private tasks: Map<string, cron.ScheduledTask> = new Map();

  schedule(name: string, cronExpression: string, handler: () => Promise<void>): void {
    this.stop(name);
    const task = cron.schedule(cronExpression, async () => { await handler(); });
    this.tasks.set(name, task);
  }

  stop(name: string): void {
    const task = this.tasks.get(name);
    if (task) { task.stop(); this.tasks.delete(name); }
  }
}
