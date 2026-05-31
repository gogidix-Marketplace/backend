import { ChatSessionProps } from '../../models';
import { SessionStatus } from '../../enums';

export interface IChatSessionRepository {
  findOne(sessionId: string): Promise<ChatSessionProps | null>;
  findActiveByCustomerId(customerId: string): Promise<ChatSessionProps[]>;
  findExpiredSessions(timeoutMs: number): Promise<ChatSessionProps[]>;
  findByCustomerId(customerId: string, status?: string, limit?: number): Promise<ChatSessionProps[]>;
  countDocuments(filter: any): Promise<number>;
  getSessionStats(startDate: Date, endDate: Date): Promise<any>;
  save(session: ChatSessionProps): Promise<ChatSessionProps>;
  updateOne(sessionId: string, update: Partial<ChatSessionProps>): Promise<void>;
}
