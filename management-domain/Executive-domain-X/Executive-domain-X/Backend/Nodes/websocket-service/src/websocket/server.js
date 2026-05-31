const { Server } = require('socket.io');
const { createAdapter } = require('@socket.io/redis-adapter');
const Redis = require('ioredis');
const config = require('../config');
const logger = require('../config/logger');
const redisClient = require('../config/redis');
const handler = require('./handler');

class WebSocketServer {
  constructor() {
    this.io = null;
    this.httpServer = null;
    this.isStarted = false;
  }

  create(httpServer) {
    if (this.io) {
      return this.io;
    }

    const ioConfig = {
      cors: {
        origin: config.websocket.corsOrigin,
        credentials: config.websocket.corsOrigin.length > 0
      },
      path: config.websocket.path,
      pingInterval: config.websocket.pingInterval,
      pingTimeout: config.websocket.pingTimeout,
      maxHttpBufferSize: config.websocket.maxHttpBufferSize,
      transports: ['websocket', 'polling'],
      allowUpgrades: true,
      serveClient: false,
      connectTimeout: 45000
    };

    this.io = new Server(httpServer, ioConfig);

    this.setupAdapter().then(() => {
      logger.info('Socket.IO Redis adapter configured');
    }).catch(error => {
      logger.warn('Failed to setup Redis adapter, continuing without it:', error.message);
    });

    this.io.on('error', (error) => {
      logger.error('Socket.IO server error:', error);
    });

    this.httpServer = httpServer;

    return this.io;
  }

  async setupAdapter() {
    try {
      await redisClient.connect();

      const { pubClient, subClient } = redisClient;

      if (!pubClient || !subClient) {
        throw new Error('Redis clients not available');
      }

      const adapter = createAdapter(pubClient, subClient, {
        requestsTimeout: 5000
      });

      this.io.adapter(adapter);

      logger.info('Socket.IO Redis adapter configured successfully');
    } catch (error) {
      logger.warn('Redis adapter setup failed:', error.message);
    }
  }

  async start() {
    if (this.isStarted) {
      logger.warn('WebSocket server already started');
      return;
    }

    if (!this.io) {
      throw new Error('Socket.IO server not created. Call create() first.');
    }

    try {
      await handler.initialize(this.io);

      this.isStarted = true;

      this.io.engine.on('connection_error', (err) => {
        logger.error('Socket.IO connection error:', err);
      });

      logger.info(`WebSocket server started on port ${config.websocket.port}`);
    } catch (error) {
      logger.error('Failed to start WebSocket server:', error);
      throw error;
    }
  }

  async stop() {
    if (!this.isStarted) {
      return;
    }

    try {
      await handler.shutdown();

      if (this.io) {
        const closePromise = new Promise((resolve) => {
          this.io.close(() => {
            resolve();
          });
        });
        await Promise.race([
          closePromise,
          new Promise((resolve) => setTimeout(resolve, 5000))
        ]);
      }

      this.isStarted = false;
      logger.info('WebSocket server stopped');
    } catch (error) {
      logger.error('Error stopping WebSocket server:', error);
    }
  }

  getInstance() {
    return this.io;
  }

  emit(event, data) {
    if (!this.io) {
      throw new Error('Socket.IO server not initialized');
    }
    return this.io.emit(event, data);
  }

  to(room) {
    if (!this.io) {
      throw new Error('Socket.IO server not initialized');
    }
    return this.io.to(room);
  }

  emitToRoom(room, event, data) {
    if (!this.io) {
      throw new Error('Socket.IO server not initialized');
    }
    return this.io.to(room).emit(event, data);
  }

  getSocket(socketId) {
    if (!this.io) {
      throw new Error('Socket.IO server not initialized');
    }
    return this.io.sockets.sockets.get(socketId);
  }

  async getSocketsInRoom(room) {
    if (!this.io) {
      throw new Error('Socket.IO server not initialized');
    }
    const sockets = await this.io.in(room).fetchSockets();
    return sockets;
  }

  getStats() {
    return {
      isStarted: this.isStarted,
      ...handler.getStats()
    };
  }

  isRunning() {
    return this.isStarted;
  }
}

module.exports = new WebSocketServer();
