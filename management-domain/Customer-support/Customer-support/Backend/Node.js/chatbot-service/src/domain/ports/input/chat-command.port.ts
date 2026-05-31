import { ChatSessionProps, BotResponseProps } from '../../models';

export interface IChatCommandPort {
  createSession(customerId?: string, language?: string, metadata?: Record<string, unknown>): Promise<ChatSessionProps>;
  sendMessage(sessionId: string, message: string, customerId?: string, language?: string, metadata?: Record<string, unknown>): Promise<BotResponseProps>;
  closeSession(sessionId: string): Promise<void>;
  submitFeedback(sessionId: string, rating: number, comment?: string, resolved?: boolean): Promise<void>;
}
