import { IntentCategory, IntentConfidence } from '../enums';
import { EntityProps } from './entity.model';

export interface IntentDetectionProps {
  intent: string;
  category: IntentCategory;
  confidence: number;
  confidenceLevel: IntentConfidence;
  entities: EntityProps[];
  sentiment: {
    score: number;
    label: 'positive' | 'neutral' | 'negative';
  };
  language: string;
  requiresHandoff: boolean;
  suggestedResponses: string[];
}
