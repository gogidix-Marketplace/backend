import { IntentDetectionProps, IntentProps } from '../../models';

export interface IIntentQueryPort {
  detectIntent(message: string, language?: string): Promise<IntentDetectionProps>;
  batchDetect(messages: string[], language?: string): Promise<IntentDetectionProps[]>;
  getIntent(intentId: string): Promise<IntentProps | null>;
  getIntents(filters?: { language?: string; category?: string; isActive?: boolean }): Promise<IntentProps[]>;
  getIntentCategories(): Array<{ value: string; label: string }>;
}
