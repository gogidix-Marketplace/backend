/**
 * Chatbot Service - Main Entry Point
 * AI-powered customer support chatbot with NLP capabilities
 */

import express from 'express';
import cors from 'cors';
import helmet from 'helmet';
import compression from 'compression';
import morgan from 'morgan';
import { createServer } from 'http';
import { Server as SocketIOServer } from 'socket.io';
import * as cron from 'node-cron';

import config from './config';
import { logger } from './utils/logger';
import { connectDatabase, connectRedis } from './config/database';
import { errorHandler, notFoundHandler } from './middleware/errorHandler';
import { apiRateLimiter } from './middleware/rateLimiter';

// Import routes
import chatRoutes from './routes/chat';
import intentRoutes from './routes/intents';
import analyticsRoutes from './routes/analytics';
import healthRoutes from './routes/health';

// Import services
import { analyticsService } from './services/AnalyticsService';
import { ChatSessionModel } from './models/ChatSession';

class ChatbotService {
  private app: express.Application;
  private httpServer: any;
  private io: SocketIOServer;

  constructor() {
    this.app = express();
    this.httpServer = createServer(this.app);
    this.io = new SocketIOServer(this.httpServer, {
      cors: {
        origin: config.cors.origin,
        methods: ['GET', 'POST'],
      },
      path: '/socket.io',
    });

    this.setupMiddleware();
    this.setupRoutes();
    this.setupWebSocket();
    this.setupErrorHandlers();
    this.setupCronJobs();
  }

  private setupMiddleware(): void {
    // Security
    this.app.use(helmet());

    // CORS
    this.app.use(cors({
      origin: config.cors.origin,
      credentials: true,
    }));

    // Compression
    this.app.use(compression());

    // Body parsing
    this.app.use(express.json({ limit: '10mb' }));
    this.app.use(express.urlencoded({ extended: true, limit: '10mb' }));

    // Logging
    if (config.nodeEnv !== 'test') {
      this.app.use(morgan('combined', {
        stream: {
          write: (message: string) => logger.info(message.trim()),
        },
      }));
    }

    // Rate limiting
    this.app.use('/api', apiRateLimiter);

    // Request ID
    this.app.use((req, res, next) => {
      req.id = req.headers['x-request-id'] || `req_${Date.now()}_${Math.random()}`;
      res.setHeader('X-Request-ID', req.id);
      next();
    });
  }

  private setupRoutes(): void {
    // API routes
    this.app.use('/api/v1/health', healthRoutes);
    this.app.use('/api/v1/chat', chatRoutes);
    this.app.use('/api/v1/intents', intentRoutes);
    this.app.use('/api/v1/analytics', analyticsRoutes);

    // API documentation
    this.app.get('/api', (req, res) => {
      res.json({
        name: '@gogidix/chatbot-service',
        version: '1.0.0',
        description: 'AI-powered customer support chatbot service',
        endpoints: {
          health: '/api/v1/health',
          chat: '/api/v1/chat',
          intents: '/api/v1/intents',
          analytics: '/api/v1/analytics',
        },
        documentation: '/api/docs',
      });
    });

    // Root endpoint
    this.app.get('/', (req, res) => {
      res.json({
        service: 'Chatbot Service',
        status: 'running',
        version: '1.0.0',
        timestamp: new Date().toISOString(),
      });
    });
  }

  private setupWebSocket(): void {
    this.io.on('connection', (socket) => {
      logger.info(`WebSocket client connected: ${socket.id}`);

      // Join session room
      socket.on('join-session', (sessionId: string) => {
        socket.join(`session:${sessionId}`);
        logger.info(`Socket ${socket.id} joined session ${sessionId}`);
      });

      // Leave session room
      socket.on('leave-session', (sessionId: string) => {
        socket.leave(`session:${sessionId}`);
        logger.info(`Socket ${socket.id} left session ${sessionId}`);
      });

      // Handle incoming messages
      socket.on('message', async (data) => {
        try {
          const { sessionId, message, customerId, language } = data;

          // Import chatService here to avoid circular dependency
          const { chatService } = await import('./services/ChatService');

          const response = await chatService.sendMessage(
            sessionId,
            message,
            customerId,
            language
          );

          // Emit response to the session room
          this.io.to(`session:${sessionId}`).emit('message', {
            sessionId,
            response,
            timestamp: new Date(),
          });
        } catch (error) {
          logger.error('WebSocket message error:', error);
          socket.emit('error', {
            message: 'Failed to process message',
            error: error instanceof Error ? error.message : 'Unknown error',
          });
        }
      });

      // Handle typing indicators
      socket.on('typing', (data) => {
        const { sessionId, isTyping } = data;
        socket.to(`session:${sessionId}`).emit('typing', {
          sessionId,
          isTyping,
          socketId: socket.id,
        });
      });

      // Handle disconnection
      socket.on('disconnect', () => {
        logger.info(`WebSocket client disconnected: ${socket.id}`);
      });
    });
  }

  private setupErrorHandlers(): void {
    // 404 handler
    this.app.use(notFoundHandler);

    // Global error handler
    this.app.use(errorHandler);

    // Handle uncaught exceptions
    process.on('uncaughtException', (error) => {
      logger.error('Uncaught Exception:', error);
      this.gracefulShutdown('UNCAUGHT_EXCEPTION');
    });

    // Handle unhandled promise rejections
    process.on('unhandledRejection', (reason, promise) => {
      logger.error('Unhandled Rejection at:', promise, 'reason:', reason);
      this.gracefulShutdown('UNHANDLED_REJECTION');
    });

    // Handle termination signals
    process.on('SIGTERM', () => {
      logger.info('SIGTERM received');
      this.gracefulShutdown('SIGTERM');
    });

    process.on('SIGINT', () => {
      logger.info('SIGINT received');
      this.gracefulShutdown('SIGINT');
    });
  }

  private setupCronJobs(): void {
    // Generate daily analytics at 1 AM every day
    cron.schedule('0 1 * * *', async () => {
      try {
        logger.info('Running daily analytics generation');
        await analyticsService.generateDailyAnalytics();
      } catch (error) {
        logger.error('Error generating daily analytics:', error);
      }
    });

    // Clean up expired sessions every hour
    cron.schedule('0 * * * *', async () => {
      try {
        logger.info('Cleaning up expired sessions');
        const expiredSessions = await ChatSessionModel.findExpiredSessions(
          config.session.timeoutMs
        );

        for (const session of expiredSessions) {
          session.updateStatus('timeout');
          await session.save();

          // Generate analytics for timed-out sessions
          await analyticsService.createSessionAnalytics(session.sessionId);

          logger.info(`Cleaned up expired session ${session.sessionId}`);
        }

        logger.info(`Cleaned up ${expiredSessions.length} expired sessions`);
      } catch (error) {
        logger.error('Error cleaning up expired sessions:', error);
      }
    });

    // Generate weekly analytics report every Sunday at 2 AM
    cron.schedule('0 2 * * 0', async () => {
      try {
        logger.info('Running weekly analytics generation');
        const now = new Date();
        const startOfWeek = new Date(now.setDate(now.getDate() - 7));

        await analyticsService.generateDailyAnalytics(startOfWeek);
      } catch (error) {
        logger.error('Error generating weekly analytics:', error);
      }
    });

    logger.info('Cron jobs scheduled');
  }

  private async gracefulShutdown(signal: string): Promise<void> {
    logger.info(`${signal} received. Starting graceful shutdown...`);

    // Stop accepting new connections
    this.httpServer.close(() => {
      logger.info('HTTP server closed');
    });

    // Close WebSocket connections
    this.io.close(() => {
      logger.info('WebSocket server closed');
    });

    // Close database connections
    const { disconnectDatabase } = await import('./config/database');
    await disconnectDatabase();

    logger.info('Graceful shutdown complete');
    process.exit(0);
  }

  public async start(): Promise<void> {
    try {
      // Connect to databases
      await connectDatabase();
      await connectRedis();

      // Start listening
      this.httpServer.listen(config.port, config.host, () => {
        logger.info(`Chatbot Service listening on http://${config.host}:${config.port}`);
        logger.info(`Environment: ${config.nodeEnv}`);
        logger.info(`WebSocket endpoint: ws://${config.host}:${config.port}/socket.io`);
      });
    } catch (error) {
      logger.error('Failed to start Chatbot Service:', error);
      process.exit(1);
    }
  }
}

// Start the service
if (require.main === module) {
  const service = new ChatbotService();
  service.start();
}

export { ChatbotService };
