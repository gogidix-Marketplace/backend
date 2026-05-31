import { Queue } from '../../models';

export interface QueueManagementInputPort {
  getQueues(): Promise<Queue[]>;
  getQueue(queueId: string): Promise<Queue | null>;
  getQueueStats(queueName?: string): Promise<{
    length: number;
    byPriority: Record<string, number>;
    avgWaitTime: number;
  }>;
  getTicketPosition(ticketId: string, queueName?: string): Promise<number>;
  clearQueue(queueName?: string): Promise<number>;
}
