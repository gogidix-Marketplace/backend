import { IntentProps } from '../../models';

export interface IIntentCommandPort {
  createIntent(data: Partial<IntentProps> & { name: string; category: any; description: string; trainingPhrases: string[]; responses: string[] }): Promise<IntentProps>;
  updateIntent(intentId: string, data: Partial<IntentProps>): Promise<IntentProps | null>;
  deleteIntent(intentId: string): Promise<void>;
  addTrainingPhrase(intentId: string, phrase: string): Promise<IntentProps | null>;
  addResponse(intentId: string, response: string): Promise<IntentProps | null>;
  toggleIntent(intentId: string): Promise<IntentProps | null>;
}
