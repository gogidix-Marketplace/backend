export interface IHandoffQueryPort {
  getHandoffStatus(sessionId: string): Promise<{
    status: string;
    agentId?: string;
    queuePosition?: number;
    estimatedWaitTime?: number;
  }>;
}
