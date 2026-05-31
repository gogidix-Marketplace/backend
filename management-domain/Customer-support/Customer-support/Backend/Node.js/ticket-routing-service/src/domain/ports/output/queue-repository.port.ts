import { Queue } from '../../models';

export interface QueueRepository {
  findById(id: string): Promise<Queue | null>;
  findByQueueId(queueId: string): Promise<Queue | null>;
  findActive(): Promise<Queue[]>;
  findByType(type: string): Promise<Queue[]>;
  findByTeam(teamId: string): Promise<Queue[]>;
  save(queue: Queue): Promise<Queue>;
  getDefaultQueue(): Promise<Queue | null>;
}
