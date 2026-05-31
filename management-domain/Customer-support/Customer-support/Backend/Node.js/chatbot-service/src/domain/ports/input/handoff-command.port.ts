export interface IHandoffCommandPort {
  requestHandoff(sessionId: string, request: { reason: string; priority?: 'low' | 'medium' | 'high' | 'urgent'; requiredSkills?: string[] }): Promise<void>;
  cancelHandoff(sessionId: string): Promise<void>;
  acceptHandoff(sessionId: string, agentId: string): Promise<void>;
  rejectHandoff(sessionId: string, reason: string): Promise<void>;
  completeHandoff(sessionId: string, resolution: string): Promise<void>;
}
