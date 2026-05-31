import { ChatSessionProps, MessageProps } from '../../models';

export interface IChatQueryPort {
  getSession(sessionId: string): Promise<ChatSessionProps | null>;
  getSessionHistory(sessionId: string): Promise<MessageProps[]>;
  getActiveSessions(customerId: string): Promise<ChatSessionProps[]>;
  getCustomerSessions(customerId: string, status?: string, limit?: number): Promise<ChatSessionProps[]>;
}
