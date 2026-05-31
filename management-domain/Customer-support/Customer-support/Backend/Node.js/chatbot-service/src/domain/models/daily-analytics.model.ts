export interface DailyAnalyticsProps {
  date: Date;
  totalSessions: number;
  activeSessions: number;
  resolvedByBot: number;
  escalatedToAgent: number;
  averageResolutionTime: number;
  averageSessionDuration: number;
  topIntents: Array<{ intent: string; count: number }>;
  sentimentDistribution: {
    positive: number;
    neutral: number;
    negative: number;
  };
  languageDistribution: Record<string, number>;
}
