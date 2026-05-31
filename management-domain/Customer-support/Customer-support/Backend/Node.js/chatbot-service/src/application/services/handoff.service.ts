import { Injectable, Inject, Logger } from '@nestjs/common';
import { IHandoffCommandPort, IHandoffQueryPort } from '@domain/ports/input';
import { IChatSessionRepository, IAgentServicePort, ICachePort } from '@domain/ports/output';
import { ChatSession, HandoffRequestProps } from '@domain/models';
import { SessionStatus } from '@domain/enums';

@Injectable()
export class HandoffApplicationService implements IHandoffCommandPort, IHandoffQueryPort {
  private readonly logger = new Logger(HandoffApplicationService.name);

  constructor(
    @Inject('IChatSessionRepository')
    private readonly sessionRepo: IChatSessionRepository,
    @Inject('IAgentServicePort')
    private readonly agentServicePort: IAgentServicePort,
    @Inject('ICachePort')
    private readonly cachePort: ICachePort,
  ) {}

  async requestHandoff(sessionId: string, request: { reason: string; priority?: 'low' | 'medium' | 'high' | 'urgent'; requiredSkills?: string[] }): Promise<void> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (!sessionProps) throw new Error(`Session ${sessionId} not found`);

    const session = new ChatSession(sessionProps.sessionId, sessionProps);

    if (session.status === SessionStatus.WITH_AGENT || session.status === SessionStatus.WAITING_FOR_AGENT) {
      this.logger.log(`Session ${sessionId} already in handoff queue`);
      return;
    }

    const handoffRequest: HandoffRequestProps = {
      sessionId,
      reason: request.reason,
      priority: request.priority || 'medium',
      requiredSkills: request.requiredSkills || [],
      summary: this.generateSessionSummary(sessionProps),
      conversationHistory: sessionProps.messages.slice(-10),
      customerInfo: {
        customerId: sessionProps.customerId,
        language: sessionProps.language,
        tags: sessionProps.tags,
        sentiment: sessionProps.sentiment,
      },
    };

    await this.agentServicePort.requestHandoff(handoffRequest);

    session.updateStatus(SessionStatus.WAITING_FOR_AGENT);
    const updated = session.toPlainObject();
    updated.handoffRequest = handoffRequest;
    await this.sessionRepo.save(updated);
    await this.cachePort.del(`session:${sessionId}`);

    this.logger.log(`Handoff requested for session ${sessionId}`);
  }

  async cancelHandoff(sessionId: string): Promise<void> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (!sessionProps) throw new Error(`Session ${sessionId} not found`);
    if (sessionProps.status !== SessionStatus.WAITING_FOR_AGENT) throw new Error('Session is not waiting for agent');

    await this.agentServicePort.cancelHandoff(sessionId);

    const session = new ChatSession(sessionProps.sessionId, sessionProps);
    session.updateStatus(SessionStatus.ACTIVE);
    const updated = session.toPlainObject();
    delete updated.handoffRequest;
    await this.sessionRepo.save(updated);
    await this.cachePort.del(`session:${sessionId}`);

    this.logger.log(`Handoff cancelled for session ${sessionId}`);
  }

  async acceptHandoff(sessionId: string, agentId: string): Promise<void> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (!sessionProps) throw new Error(`Session ${sessionId} not found`);

    const session = new ChatSession(sessionProps.sessionId, sessionProps);
    session.updateStatus(SessionStatus.WITH_AGENT);
    const updated = session.toPlainObject();
    updated.assignedAgentId = agentId;
    await this.sessionRepo.save(updated);
    await this.cachePort.del(`session:${sessionId}`);
  }

  async rejectHandoff(sessionId: string, reason: string): Promise<void> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (!sessionProps) throw new Error(`Session ${sessionId} not found`);

    const session = new ChatSession(sessionProps.sessionId, sessionProps);
    session.updateStatus(SessionStatus.ACTIVE);
    const updated = session.toPlainObject();
    updated.assignedAgentId = undefined;
    await this.sessionRepo.save(updated);
    await this.cachePort.del(`session:${sessionId}`);
  }

  async completeHandoff(sessionId: string, resolution: string): Promise<void> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (!sessionProps) throw new Error(`Session ${sessionId} not found`);

    const session = new ChatSession(sessionProps.sessionId, sessionProps);
    session.updateStatus(SessionStatus.CLOSED);
    session.setMetadata('resolution', resolution);
    await this.sessionRepo.save(session.toPlainObject());
    await this.cachePort.del(`session:${sessionId}`);
  }

  async getHandoffStatus(sessionId: string): Promise<{ status: string; agentId?: string; queuePosition?: number; estimatedWaitTime?: number }> {
    const sessionProps = await this.sessionRepo.findOne(sessionId);
    if (!sessionProps) throw new Error(`Session ${sessionId} not found`);

    if (sessionProps.status === SessionStatus.WITH_AGENT) {
      return { status: 'with_agent', agentId: sessionProps.assignedAgentId };
    }

    if (sessionProps.status === SessionStatus.WAITING_FOR_AGENT) {
      try {
        const queueInfo = await this.agentServicePort.getQueuePosition(sessionId);
        return { status: 'waiting', ...queueInfo };
      } catch {
        return { status: 'waiting' };
      }
    }

    return { status: sessionProps.status };
  }

  private generateSessionSummary(session: any): string {
    const parts: string[] = [];
    parts.push(`Customer ${session.customerId || 'guest'} - Session duration: ${Math.round((Date.now() - new Date(session.startedAt).getTime()) / 60000)} minutes`);
    if (session.tags?.length > 0) parts.push(`Topics discussed: ${session.tags.join(', ')}`);
    if (session.sentiment) parts.push(`Customer sentiment: ${session.sentiment.label} (${session.sentiment.score}/100)`);
    if (session.messages?.length > 0) {
      const lastMessage = session.messages[session.messages.length - 1];
      parts.push(`Last message: "${lastMessage.content.substring(0, 100)}${lastMessage.content.length > 100 ? '...' : ''}"`);
    }
    return parts.join('\n');
  }
}
