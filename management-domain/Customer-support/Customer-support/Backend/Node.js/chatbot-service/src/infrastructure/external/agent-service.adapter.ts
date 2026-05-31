import { Injectable, Logger } from '@nestjs/common';
import { IAgentServicePort } from '@domain/ports/output';
import { ConfigService } from '@nestjs/config';
import axios, { AxiosInstance } from 'axios';

@Injectable()
export class AgentServiceAdapter implements IAgentServicePort {
  private readonly logger = new Logger(AgentServiceAdapter.name);
  private client: AxiosInstance;

  constructor(private configService: ConfigService) {
    this.client = axios.create({
      baseURL: this.configService.get<string>('config.services.agent') || 'http://localhost:8116',
      timeout: 15000,
      headers: { 'Content-Type': 'application/json' },
    });
  }

  async requestHandoff(handoffRequest: any): Promise<void> {
    await this.client.post('/api/v1/handoffs', handoffRequest);
  }

  async getQueuePosition(sessionId: string): Promise<{ position: number; estimatedWaitTime: number }> {
    try {
      const response = await this.client.get(`/api/v1/queue/position/${sessionId}`);
      return {
        position: response.data.position,
        estimatedWaitTime: response.data.estimatedWaitTime,
      };
    } catch {
      return { position: 0, estimatedWaitTime: 0 };
    }
  }

  async cancelHandoff(sessionId: string): Promise<void> {
    await this.client.delete(`/api/v1/handoffs/${sessionId}`);
  }

  async transferSession(sessionId: string, fromAgentId: string, toAgentId: string, reason: string): Promise<void> {
    await this.client.post('/api/v1/transfer', { sessionId, fromAgentId, toAgentId, reason });
  }
}
