/**
 * Session Management Middleware
 */

import { Request, Response, NextFunction } from 'express';
import { ChatSessionModel } from '../models/ChatSession';
import { SessionNotFoundError, ChatbotError } from '../types';
import { logger } from '../utils/logger';
import { redisClient } from '../utils/redis';
import config from '../config';

declare global {
  namespace Express {
    interface Request {
      chatSession?: any;
    }
  }
}

export const validateSession = async (
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> => {
  try {
    const sessionId = req.body?.sessionId || req.params?.sessionId || req.headers['x-session-id'] as string;

    if (!sessionId) {
      res.status(400).json({
        success: false,
        error: 'Session ID is required',
      });
      return;
    }

    // Try to get from Redis cache first
    const cachedSession = await redisClient.get(`session:${sessionId}`);

    if (cachedSession) {
      req.chatSession = JSON.parse(cachedSession);
      next();
      return;
    }

    // Get from MongoDB
    const session = await ChatSessionModel.findOne({ sessionId });

    if (!session) {
      throw new SessionNotFoundError(sessionId);
    }

    // Check if session is still active
    if (session.status === 'closed' || session.status === 'timeout') {
      throw new ChatbotError(
        'Session is closed or has timed out. Please start a new session.',
        'SESSION_CLOSED',
        400
      );
    }

    // Cache in Redis
    await redisClient.set(
      `session:${sessionId}`,
      JSON.stringify(session.toJSON()),
      config.session.timeoutMs / 1000
    );

    req.chatSession = session;
    next();
  } catch (error) {
    if (error instanceof ChatbotError) {
      res.status(error.statusCode).json({
        success: false,
        error: error.message,
        code: error.code,
      });
    } else {
      logger.error('Session validation error:', error);
      res.status(500).json({
        success: false,
        error: 'Failed to validate session',
      });
    }
  }
};

export const updateSessionActivity = async (
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> => {
  try {
    if (req.chatSession) {
      req.chatSession.lastActivityAt = new Date();
      await req.chatSession.save();

      // Update cache
      await redisClient.set(
        `session:${req.chatSession.sessionId}`,
        JSON.stringify(req.chatSession.toJSON()),
        config.session.timeoutMs / 1000
      );
    }

    next();
  } catch (error) {
    logger.error('Session activity update error:', error);
    // Don't block the request if activity update fails
    next();
  }
};

export const checkSessionTimeout = async (
  sessionId: string
): Promise<boolean> => {
  const session = await ChatSessionModel.findOne({ sessionId });

  if (!session) {
    return true; // Session doesn't exist, consider it timed out
  }

  const now = Date.now();
  const timeSinceActivity = now - session.lastActivityAt.getTime();
  const isTimedOut = timeSinceActivity > config.session.timeoutMs;

  if (isTimedOut && session.status === 'active') {
    session.status = 'timeout';
    session.endedAt = new Date();
    await session.save();

    // Remove from cache
    await redisClient.del(`session:${sessionId}`);

    logger.info(`Session ${sessionId} timed out after ${timeSinceActivity}ms of inactivity`);
  }

  return isTimedOut;
};

export const invalidateSessionCache = async (sessionId: string): Promise<void> => {
  await redisClient.del(`session:${sessionId}`);
};

export const getSessionFromCache = async (sessionId: string): Promise<any | null> => {
  const cached = await redisClient.get(`session:${sessionId}`);
  return cached ? JSON.parse(cached) : null;
};

export const setSessionInCache = async (
  sessionId: string,
  session: any
): Promise<void> => {
  await redisClient.set(
    `session:${sessionId}`,
    JSON.stringify(session),
    config.session.timeoutMs / 1000
  );
};
