/**
 * Authentication Middleware
 */

import { Request, Response, NextFunction } from 'express';
import { verifyToken, createServiceToken } from '../utils/jwt';
import { logger } from '../utils/logger';
import config from '../config';

declare global {
  namespace Express {
    interface Request {
      user?: {
        sub: string;
        userId: string;
        sessionId: string;
        role: string;
      };
    }
  }
}

export const authenticate = async (
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> => {
  try {
    const authHeader = req.headers.authorization;

    if (!authHeader || !authHeader.startsWith('Bearer ')) {
      res.status(401).json({
        success: false,
        error: 'Authentication token is required',
      });
      return;
    }

    const token = authHeader.substring(7);

    try {
      const decoded = verifyToken(token);
      req.user = {
        sub: decoded.sub,
        userId: decoded.userId,
        sessionId: decoded.sessionId,
        role: decoded.role,
      };
      next();
    } catch (error) {
      if (error instanceof Error && error.message === 'Token expired') {
        res.status(401).json({
          success: false,
          error: 'Token has expired',
        });
        return;
      }
      res.status(401).json({
        success: false,
        error: 'Invalid authentication token',
      });
    }
  } catch (error) {
    logger.error('Authentication error:', error);
    res.status(500).json({
      success: false,
      error: 'Authentication failed',
    });
  }
};

export const authenticateOptional = async (
  req: Request,
  res: Response,
  next: NextFunction
): Promise<void> => {
  try {
    const authHeader = req.headers.authorization;

    if (authHeader && authHeader.startsWith('Bearer ')) {
      const token = authHeader.substring(7);
      try {
        const decoded = verifyToken(token);
        req.user = {
          sub: decoded.sub,
          userId: decoded.userId,
          sessionId: decoded.sessionId,
          role: decoded.role,
        };
      } catch {
        // Token invalid but continue anyway
      }
    }

    next();
  } catch (error) {
    logger.error('Optional authentication error:', error);
    next();
  }
};

export const requireRole = (...roles: string[]) => {
  return (req: Request, res: Response, next: NextFunction): void => {
    if (!req.user) {
      res.status(401).json({
        success: false,
        error: 'Authentication required',
      });
      return;
    }

    if (!roles.includes(req.user.role)) {
      res.status(403).json({
        success: false,
        error: 'Insufficient permissions',
      });
      return;
    }

    next();
  };
};

export const serviceAuth = (req: Request, res: Response, next: NextFunction): void => {
  const serviceToken = req.headers['x-service-token'] as string;

  if (!serviceToken) {
    res.status(401).json({
      success: false,
      error: 'Service token is required',
    });
    return;
  }

  // Simple token validation for service-to-service communication
  const validServiceTokens = [
    config.services.knowledgeBase,
    config.services.ticket,
    config.services.agent,
  ];

  // In production, use proper JWT validation
  const isValid = validServiceTokens.some(token =>
    serviceToken.includes(token.substring(token.length - 10))
  );

  if (!isValid) {
    res.status(401).json({
      success: false,
      error: 'Invalid service token',
    });
    return;
  }

  next();
};

export const generateServiceHeaders = (): Record<string, string> => {
  return {
    'x-service-token': createServiceToken('chatbot-service'),
    'Content-Type': 'application/json',
  };
};
