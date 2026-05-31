import { Injectable } from '@nestjs/common';
import { IQueueService, QueueJob } from '../../../domain/ports/services/queue-service.port';
import * as Bull from 'bullmq';

@Injectable()
export class BullQueueAdapter implements IQueueService {
  private queues: Map<string, Bull.Queue> = new Map();

  private getQueue(name: string): Bull.Queue {
    if (!this.queues.has(name)) {
      this.queues.set(name, new Bull.Queue(name, { connection: { host: process.env.REDIS_HOST ?? 'localhost', port: parseInt(process.env.REDIS_PORT ?? '6379') } }));
    }
    return this.queues.get(name)!;
  }

  async addJob<T>(queueName: string, data: T, options?: { delay?: number; attempts?: number }): Promise<string> {
    const queue = this.getQueue(queueName);
    const job = await queue.add('process', data, { delay: options?.delay, attempts: options?.attempts ?? 3 });
    return job.id ?? '';
  }

  async processQueue<T>(queueName: string, handler: (job: QueueJob<T>) => Promise<void>): Promise<void> {
    const worker = new Bull.Worker(queueName, async (job) => {
      await handler({ id: job.id ?? '', data: job.data, attempts: job.attemptsMade });
    }, { connection: { host: process.env.REDIS_HOST ?? 'localhost', port: parseInt(process.env.REDIS_PORT ?? '6379') } });
  }

  async getJobCount(queueName: string): Promise<number> {
    const queue = this.getQueue(queueName);
    const counts = await queue.getJobCounts();
    return Object.values(counts).reduce((sum, count) => sum + count, 0);
  }
}
