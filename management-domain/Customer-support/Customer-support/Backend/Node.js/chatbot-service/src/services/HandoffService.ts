/**
 * Handoff Service
 * Handles handoff from chatbot to human agents
 */

import axios, { AxiosInstance } from 'axios';
import { ChatSessionModel } from '../models/ChatSession';
import { IHandoffRequest, IBotResponse, ResponseType } from '../types';
import { logger } from '../utils/logger';
import config from '../config';
import { redisClient } from './KnowledgeBaseService';

interface HandoffRequest {
  sessionId: string;
  reason: string;
  priority: 'low' | 'medium' | 'high' | 'urgent';
  requiredSkills: string[];
}

export class HandoffService {
  private client: AxiosInstance;

  constructor() {
    this.client = axios.create({
      baseURL: config.services.agent,
      timeout: 15000,
      headers: {
        'Content-Type': 'application/json',
      },
    });
  }

  /**
   * Request handoff to human agent
   */
  async requestHandoff(
    sessionId: string,
    request: {
      reason: string;
      priority?: 'low' | 'medium' | 'high' | 'urgent';
      requiredSkills?: string[];
    }
  ): Promise<void> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      // Check if already with agent
      if (session.status === 'with_agent' || session.status === 'waiting_for_agent') {
        logger.info(`Session ${sessionId} already in handoff queue or with agent`);
        return;
      }

      // Create handoff request
      const handoffRequest: IHandoffRequest = {
        sessionId,
        reason: request.reason,
        priority: request.priority || 'medium',
        requiredSkills: request.requiredSkills || [],
        summary: this.generateSessionSummary(session),
        conversationHistory: session.messages.slice(-10).map(msg => ({
          id: msg.id,
          sessionId: msg.sessionId,
          content: msg.content,
          sender: msg.sender,
          timestamp: msg.timestamp,
          language: msg.language,
        })),
        customerInfo: {
          customerId: session.customerId,
          language: session.language,
          tags: session.tags,
          sentiment: session.sentiment,
        },
      };

      // Send to agent service
      await this.client.post('/api/v1/handoffs', handoffRequest);

      // Update session status
      session.updateStatus('waiting_for_agent');
      session.handoffRequest = handoffRequest;
      await session.save();

      // Update cache
      await redisClient.del(`session:${sessionId}`);

      logger.info(`Handoff requested for session ${sessionId} with priority ${handoffRequest.priority}`);
    } catch (error) {
      if (axios.isAxiosError(error)) {
        logger.error('Handoff request error:', {
          message: error.message,
          status: error.response?.status,
          data: error.response?.data,
        });
      } else {
        logger.error('Handoff request error:', error);
      }
      throw error;
    }
  }

  /**
   * Generate session summary for agent
   */
  private generateSessionSummary(session: any): string {
    const parts: string[] = [];

    parts.push(`Customer ${session.customerId || 'guest'} - Session duration: ${Math.round((Date.now() - session.startedAt.getTime()) / 60000)} minutes`);

    if (session.tags.length > 0) {
      parts.push(`Topics discussed: ${session.tags.join(', ')}`);
    }

    if (session.sentiment) {
      parts.push(`Customer sentiment: ${session.sentiment.label} (${session.sentiment.score}/100)`);
    }

    if (session.messages.length > 0) {
      const lastMessage = session.messages[session.messages.length - 1];
      parts.push(`Last message: "${lastMessage.content.substring(0, 100)}${lastMessage.content.length > 100 ? '...' : ''}"`);
    }

    return parts.join('\n');
  }

  /**
   * Accept handoff (called by agent service)
   */
  async acceptHandoff(sessionId: string, agentId: string): Promise<void> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      session.updateStatus('with_agent');
      session.assignedAgentId = agentId;
      await session.save();

      // Update cache
      await redisClient.del(`session:${sessionId}`);

      logger.info(`Handoff accepted for session ${sessionId} by agent ${agentId}`);
    } catch (error) {
      logger.error('Accept handoff error:', error);
      throw error;
    }
  }

  /**
   * Reject handoff (called by agent service)
   */
  async rejectHandoff(sessionId: string, reason: string): Promise<void> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      // Return to active chatbot session
      session.updateStatus('active');
      session.assignedAgentId = undefined;
      await session.save();

      // Update cache
      await redisClient.del(`session:${sessionId}`);

      logger.info(`Handoff rejected for session ${sessionId}: ${reason}`);
    } catch (error) {
      logger.error('Reject handoff error:', error);
      throw error;
    }
  }

  /**
   * Complete handoff (session ended by agent)
   */
  async completeHandoff(sessionId: string, resolution: string): Promise<void> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      session.updateStatus('closed');
      session.setMetadata('resolution', resolution);
      await session.save();

      // Update cache
      await redisClient.del(`session:${sessionId}`);

      logger.info(`Handoff completed for session ${sessionId}: ${resolution}`);
    } catch (error) {
      logger.error('Complete handoff error:', error);
      throw error;
    }
  }

  /**
   * Get handoff status
   */
  async getHandoffStatus(sessionId: string): Promise<{
    status: string;
    agentId?: string;
    queuePosition?: number;
    estimatedWaitTime?: number;
  }> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      if (session.status === 'with_agent') {
        return {
          status: 'with_agent',
          agentId: session.assignedAgentId,
        };
      }

      if (session.status === 'waiting_for_agent') {
        // Get queue position from agent service
        try {
          const response = await this.client.get(`/api/v1/queue/position/${sessionId}`);
          return {
            status: 'waiting',
            queuePosition: response.data.position,
            estimatedWaitTime: response.data.estimatedWaitTime,
          };
        } catch {
          return {
            status: 'waiting',
          };
        }
      }

      return {
        status: session.status,
      };
    } catch (error) {
      logger.error('Get handoff status error:', error);
      throw error;
    }
  }

  /**
   * Cancel handoff request
   */
  async cancelHandoff(sessionId: string): Promise<void> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      if (session.status !== 'waiting_for_agent') {
        throw new Error('Session is not waiting for agent');
      }

      // Cancel in agent service
      await this.client.delete(`/api/v1/handoffs/${sessionId}`);

      // Return to active
      session.updateStatus('active');
      session.handoffRequest = undefined;
      await session.save();

      // Update cache
      await redisClient.del(`session:${sessionId}`);

      logger.info(`Handoff cancelled for session ${sessionId}`);
    } catch (error) {
      logger.error('Cancel handoff error:', error);
      throw error;
    }
  }

  /**
   * Transfer to another agent
   */
  async transferToAgent(sessionId: string, newAgentId: string, reason: string): Promise<void> {
    try {
      const session = await ChatSessionModel.findOne({ sessionId });

      if (!session) {
        throw new Error(`Session ${sessionId} not found`);
      }

      const oldAgentId = session.assignedAgentId;

      // Update session
      session.assignedAgentId = newAgentId;
      await session.save();

      // Notify agent service
      await this.client.post(`/api/v1/transfer`, {
        sessionId,
        fromAgentId: oldAgentId,
        toAgentId: newAgentId,
        reason,
      });

      // Update cache
      await redisClient.del(`session:${sessionId}`);

      logger.info(`Session ${sessionId} transferred from agent ${oldAgentId} to ${newAgentId}`);
    } catch (error) {
      logger.error('Transfer agent error:', error);
      throw error;
    }
  }

  /**
   * Get handoff statistics
   */
  async getHandoffStats(startDate: Date, endDate: Date): Promise<{
    totalHandoffs: number;
    acceptedHandoffs: number;
    rejectedHandoffs: number;
    averageWaitTime: number;
    handoffsByPriority: Record<string, number>;
    handoffsByReason: Array<{ reason: string; count: number }>;
  }> {
    try {
      const sessions = await ChatSessionModel.find({
        status: { $in: ['with_agent', 'closed'] },
        startedAt: { $gte: startDate, $lte: endDate },
        handoffRequest: { $exists: true },
      });

      const totalHandoffs = sessions.length;
      const acceptedHandoffs = sessions.filter(s => s.status === 'with_agent' || s.assignedAgentId).length;
      const rejectedHandoffs = totalHandoffs - acceptedHandoffs;

      const handoffsByPriority: Record<string, number> = {
        low: 0,
        medium: 0,
        high: 0,
        urgent: 0,
      };

      const handoffsByReasonMap = new Map<string, number>();

      sessions.forEach(session => {
        if (session.handoffRequest) {
          handoffsByPriority[session.handoffRequest.priority] =
            (handoffsByPriority[session.handoffRequest.priority] || 0) + 1;

          const reason = session.handoffRequest.reason;
          handoffsByReasonMap.set(reason, (handoffsByReasonMap.get(reason) || 0) + 1);
        }
      });

      const handoffsByReason = Array.from(handoffsByReasonMap.entries())
        .map(([reason, count]) => ({ reason, count }))
        .sort((a, b) => b.count - a.count)
        .slice(0, 10);

      return {
        totalHandoffs,
        acceptedHandoffs,
        rejectedHandoffs,
        averageWaitTime: 0, // Would be calculated from agent service data
        handoffsByPriority,
        handoffsByReason,
      };
    } catch (error) {
      logger.error('Get handoff stats error:', error);
      throw error;
    }
  }
}

export const handoffService = new HandoffService();
