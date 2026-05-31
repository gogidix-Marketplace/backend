import { SentimentAnalysis, SentimentAlert } from '../../models';

export interface SentimentAnalysisRepository {
  save(analysis: SentimentAnalysis): Promise<SentimentAnalysis>;
  findByTicketId(ticketId: string): Promise<SentimentAnalysis[]>;
  findRecent(limit: number): Promise<SentimentAnalysis[]>;
}

export interface SentimentAlertRepository {
  save(alert: SentimentAlert): Promise<SentimentAlert>;
  findActive(): Promise<SentimentAlert[]>;
  findById(alertId: string): Promise<SentimentAlert | null>;
  update(alert: SentimentAlert): Promise<SentimentAlert>;
}
