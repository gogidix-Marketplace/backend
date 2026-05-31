import { SentimentLabel } from '../../enums';

export interface AnalyzeTextRequest {
  text: string;
  ticketId?: string;
  language?: string;
}

export interface SentimentAnalysisResult {
  ticketId: string;
  text: string;
  sentiment: {
    score: number;
    normalized: number;
    label: SentimentLabel;
    confidence: number;
  };
  emotions: {
    joy: number;
    sadness: number;
    anger: number;
    fear: number;
    disgust: number;
    surprise: number;
  };
  keywords: Array<{ word: string; sentiment: string; score: number }>;
  language: string;
}

export interface SentimentAnalysisInputPort {
  analyzeText(request: AnalyzeTextRequest): Promise<SentimentAnalysisResult>;
  batchAnalyze(texts: string[], language?: string): Promise<SentimentAnalysisResult[]>;
  getAlerts(): Promise<any[]>;
  getAlertStats(): Promise<Record<string, unknown>>;
  acknowledgeAlert(alertId: string, userId: string): Promise<boolean>;
}
