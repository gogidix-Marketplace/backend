export interface IAgentServicePort {
  requestHandoff(handoffRequest: any): Promise<void>;
  getQueuePosition(sessionId: string): Promise<{ position: number; estimatedWaitTime: number }>;
  cancelHandoff(sessionId: string): Promise<void>;
  transferSession(sessionId: string, fromAgentId: string, toAgentId: string, reason: string): Promise<void>;
}
