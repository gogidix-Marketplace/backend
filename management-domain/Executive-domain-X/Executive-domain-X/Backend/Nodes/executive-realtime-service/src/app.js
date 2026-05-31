/**
 * Application factory for Executive Real-time Service
 */

const http = require('http');
const express = require('express');
const { WebSocketServer } = require('./websocket/server');
const { setupSubscriptions } = require('./websocket/subscriptions');
const { setupMiddleware } = require('./middleware');
const { setupRoutes } = require('./routes');
const logger = require('./config/logger');

/**
 * Create and configure the Express server with WebSocket support
 */
async function createServer() {
  const app = express();

  // Setup middleware
  await setupMiddleware(app);

  // Setup HTTP routes
  await setupRoutes(app);

  // Create HTTP server
  const server = http.createServer(app);

  // Setup WebSocket server
  const wsServer = new WebSocketServer({ server });
  await wsServer.initialize();

  // Setup Redis pub/sub subscriptions for KPI updates
  await setupSubscriptions(wsServer);

  // Add WebSocket server to app for access in routes
  app.set('wsServer', wsServer);

  return server;
}

module.exports = { createServer };
