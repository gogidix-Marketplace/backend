export interface SessionAnalyticsProps {
  sessionId: string;
  userId?: string;
  duration: number;
  messageCount: number;
  turnCount: number;
  resolutionStatus: 'resolved' | 'escalated' | 'abandoned';
  intents: string[];
  sentiment: {
    average: number;
    trend: 'improving' | 'declining' | 'stable';
  };
  handoffRequired: boolean;
  agentId?: string;
  customerSatisfaction?: number;
}
