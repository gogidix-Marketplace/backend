const express = require('express');
const http = require('http');
const config = require('./config');
const logger = require('./config/logger');
const routes = require('./routes');
const wsServer = require('./websocket/server');
const { MongoClient } = require('mongodb');

class Application {
  constructor() {
    this.app = express();
    this.server = http.createServer(this.app);
    this.isShuttingDown = false;
  }

  setupMiddleware() {
    this.app.use(express.json({ limit: '10mb' }));
    this.app.use(express.urlencoded({ extended: true, limit: '10mb' }));

    this.app.use(require('cors')({
      origin: config.cors.origin,
      credentials: config.cors.credentials,
      methods: config.cors.methods,
      allowedHeaders: config.cors.headers
    }));

    this.app.use(require('helmet')({
      contentSecurityPolicy: false,
      crossOriginEmbedderPolicy: false
    }));

    this.app.use(require('compression')());

    this.app.use((req, res, next) => {
      logger.debug(`${req.method} ${req.path}`);
      next();
    });
  }

  setupRoutes() {
    this.app.get('/', (req, res) => {
      res.json({
        name: 'Gogidix WebSocket Service',
        version: '1.0.0',
        status: 'running',
        timestamp: new Date().toISOString()
      });
    });

    this.app.use(config.server.apiPrefix, routes);

    this.app.use((req, res) => {
      res.status(404).json({
        success: false,
        message: 'Endpoint not found'
      });
    });

    this.app.use((err, req, res, next) => {
      logger.error('Unhandled error:', err);

      res.status(err.status || 500).json({
        success: false,
        message: config.env === 'development' ? err.message : 'Internal server error',
        ...(config.env === 'development' && { stack: err.stack })
      });
    });
  }

  async setupWebSocket() {
    wsServer.create(this.server);
    await wsServer.start();
  }

  async initialize() {
    this.setupMiddleware();
    this.setupRoutes();

    await this.connectDatabases();

    await this.setupWebSocket();

    this.setupGracefulShutdown();
  }

  async connectDatabases() {
    try {
      const mongodb = require('./config/mongodb');
      await mongodb.connect();
      logger.info('MongoDB connected');
    } catch (error) {
      logger.error('Failed to connect to MongoDB:', error);
      if (config.env === 'production') {
        throw error;
      }
    }

    try {
      const redis = require('./config/redis');
      await redis.connect();
      logger.info('Redis connected');
    } catch (error) {
      logger.error('Failed to connect to Redis:', error);
      if (config.env === 'production') {
        throw error;
      }
    }
  }

  setupGracefulShutdown() {
    const shutdown = async (signal) => {
      if (this.isShuttingDown) return;
      this.isShuttingDown = true;

      logger.info(`${signal} received, starting graceful shutdown...`);

      this.server.close(async () => {
        logger.info('HTTP server closed');

        try {
          await wsServer.stop();

          const mongodb = require('./config/mongodb');
          await mongodb.disconnect();

          const redis = require('./config/redis');
          await redis.disconnect();

          logger.info('Graceful shutdown completed');
          process.exit(0);
        } catch (error) {
          logger.error('Error during shutdown:', error);
          process.exit(1);
        }
      });

      setTimeout(() => {
        logger.error('Forced shutdown after timeout');
        process.exit(1);
      }, 30000);
    };

    process.on('SIGTERM', () => shutdown('SIGTERM'));
    process.on('SIGINT', () => shutdown('SIGINT'));

    process.on('uncaughtException', (error) => {
      logger.error('Uncaught exception:', error);
      shutdown('uncaughtException');
    });

    process.on('unhandledRejection', (reason, promise) => {
      logger.error('Unhandled rejection:', reason);
    });
  }

  listen() {
    return new Promise((resolve) => {
      this.server.listen(config.server.port, config.server.host, () => {
        logger.info(`Server listening on http://${config.server.host}:${config.server.port}`);
        logger.info(`WebSocket endpoint: ws://${config.server.host}:${config.server.port}${config.websocket.path}`);
        logger.info(`API prefix: ${config.server.apiPrefix}`);
        logger.info(`Environment: ${config.env}`);
        resolve();
      });
    });
  }

  getApp() {
    return this.app;
  }

  getServer() {
    return this.server;
  }
}

module.exports = new Application();
