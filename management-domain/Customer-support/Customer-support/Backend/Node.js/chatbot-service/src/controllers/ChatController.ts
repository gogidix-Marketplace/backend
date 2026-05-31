/**
 * Chat Controller
 * Handles HTTP requests for chat operations
 */

import { Response } from 'express';
import { AuthenticatedRequest } from '../types';
import { chatService } from '../services/ChatService';
import { translationService } from '../services/TranslationService';
import { handoffService } from '../services/HandoffService';
import { analyticsService } from '../services/AnalyticsService';
import { logger } from '../utils/logger';
import { asyncHandler } from '../middleware/errorHandler';

export class ChatController {
  /**
   * Create a new chat session
   */
  createSession = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { customerId, language = 'en', metadata = {} } = req.body;

    logger.info(`Creating session for customer ${customerId} in language ${language}`);

    const session = await chatService.createSession(customerId, language, metadata);

    res.status(201).json({
      success: true,
      data: {
        sessionId: session.sessionId,
        status: session.status,
        language: session.language,
        startedAt: session.startedAt,
      },
    });
  });

  /**
   * Send a message
   */
  sendMessage = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId, message, language = 'en', metadata = {}, customerId } = req.body;

    logger.info(`Sending message for session ${sessionId}`);

    const response = await chatService.sendMessage(
      sessionId,
      message,
      customerId,
      language,
      metadata
    );

    res.json({
      success: true,
      data: response,
    });
  });

  /**
   * Get session details
   */
  getSession = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId } = req.params;

    const session = await chatService.getSession(sessionId);

    if (!session) {
      res.status(404).json({
        success: false,
        error: 'Session not found',
      });
      return;
    }

    res.json({
      success: true,
      data: {
        sessionId: session.sessionId,
        status: session.status,
        language: session.language,
        startedAt: session.startedAt,
        lastActivityAt: session.lastActivityAt,
        messageCount: session.messages.length,
        turnCount: session.context.turnCount,
        tags: session.tags,
        sentiment: session.sentiment,
        assignedAgentId: session.assignedAgentId,
      },
    });
  });

  /**
   * Get session history
   */
  getSessionHistory = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId } = req.params;
    const { limit = 50 } = req.query;

    const history = await chatService.getSessionHistory(sessionId);

    const limitedHistory = history.slice(-parseInt(limit as string));

    res.json({
      success: true,
      data: limitedHistory,
    });
  });

  /**
   * Close a session
   */
  closeSession = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId } = req.params;

    await chatService.closeSession(sessionId);

    // Generate analytics for the closed session
    await analyticsService.createSessionAnalytics(sessionId);

    res.json({
      success: true,
      message: 'Session closed successfully',
    });
  });

  /**
   * Request handoff to human agent
   */
  requestHandoff = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId } = req.params;
    const { reason, priority = 'medium', requiredSkills = [] } = req.body;

    await handoffService.requestHandoff(sessionId, {
      reason,
      priority,
      requiredSkills,
    });

    res.json({
      success: true,
      message: 'Handoff requested successfully',
    });
  });

  /**
   * Get handoff status
   */
  getHandoffStatus = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId } = req.params;

    const status = await handoffService.getHandoffStatus(sessionId);

    res.json({
      success: true,
      data: status,
    });
  });

  /**
   * Cancel handoff
   */
  cancelHandoff = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId } = req.params;

    await handoffService.cancelHandoff(sessionId);

    res.json({
      success: true,
      message: 'Handoff cancelled successfully',
    });
  });

  /**
   * Submit feedback
   */
  submitFeedback = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { sessionId, rating, comment, resolved } = req.body;

    const session = await chatService.getSession(sessionId);

    if (!session) {
      res.status(404).json({
        success: false,
        error: 'Session not found',
      });
      return;
    }

    session.setMetadata('feedback', { rating, comment, resolved });
    session.setMetadata('feedbackSubmittedAt', new Date());
    await session.save();

    res.json({
      success: true,
      message: 'Feedback submitted successfully',
    });
  });

  /**
   * Get customer sessions
   */
  getCustomerSessions = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { customerId } = req.params;
    const { status, limit = 20 } = req.query;

    let sessions = await chatService.getActiveSessions(customerId);

    if (status) {
      sessions = sessions.filter(s => s.status === status);
    }

    sessions = sessions.slice(0, parseInt(limit as string));

    res.json({
      success: true,
      data: sessions.map(s => ({
        sessionId: s.sessionId,
        status: s.status,
        language: s.language,
        startedAt: s.startedAt,
        lastActivityAt: s.lastActivityAt,
        messageCount: s.messages.length,
        tags: s.tags,
      })),
    });
  });

  /**
   * Translate text
   */
  translateText = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const { text, targetLanguage, sourceLanguage } = req.body;

    const result = await translationService.translate({
      text,
      targetLanguage,
      sourceLanguage,
    });

    res.json({
      success: true,
      data: result,
    });
  });

  /**
   * Get supported languages
   */
  getSupportedLanguages = asyncHandler(async (req: AuthenticatedRequest, res: Response) => {
    const languages = translationService.getSupportedLanguages();

    res.json({
      success: true,
      data: languages,
    });
  });
}

export const chatController = new ChatController();
