export interface SentimentAnalysisInputPort {
  analyze(text: string, includeEmotions?: boolean): Promise<import('../../models').AnalysisResult>;
  analyzeSentiment(text: string): Promise<import('../../models').SentimentResult>;
  analyzeEmotions(text: string): Promise<import('../../models').EmotionResult>;
  batchAnalyze(texts: string[], includeEmotions?: boolean): Promise<{ results: import('../../models').AnalysisResult[]; summary: any }>;
  getStats(): Promise<Record<string, unknown>>;
  clearCache(): Promise<number>;
}
