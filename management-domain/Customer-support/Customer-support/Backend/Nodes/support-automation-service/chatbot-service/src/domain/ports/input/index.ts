import { BotResponse, ConversationContext } from '../../models';
import { Intent, HandoffReason } from '../../enums';

export interface IntentRecognitionResult {
  intent: Intent;
  confidence: number;
  entities: Record<string, any>;
  alternativeIntents: Array<{ intent: Intent; confidence: number }>;
}

export interface ChatbotInputPort {
  startConversation(customerId?: string, language?: string): Promise<{ sessionId: string; message: string }>;
  processMessage(sessionId: string, message: string, customerId?: string, language?: string): Promise<BotResponse>;
  endConversation(sessionId: string): Promise<{ success: boolean; summary?: any }>;
  getConversationHistory(sessionId: string): Promise<any[]>;
  getStats(): Promise<Record<string, unknown>>;
}

export interface IntentRecognitionInputPort {
  recognizeIntent(message: string): IntentRecognitionResult;
  getResponse(intent: Intent): string;
  isEscalation(message: string): boolean;
}
