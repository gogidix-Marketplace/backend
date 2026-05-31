/**
 * WebSocket Module Entry Point
 */

const { WebSocketServer } = require('./server');
const { SocketIOServer, SocketEvents } = require('./socketioServer');
const { setupSubscriptions, cleanupSubscriptions } = require('./subscriptions');
const { connectionManager } = require('./connectionManager');
const { registerWebSocketServer } = require('./registry');
const { streamHandler } = require('./streamHandler');

module.exports = {
  // WebSocket (ws) server
  WebSocketServer,

  // Socket.io server
  SocketIOServer,
  SocketEvents,

  // Subscriptions
  setupSubscriptions,
  cleanupSubscriptions,

  // Connection management
  connectionManager,

  // Registry
  registerWebSocketServer,

  // Streaming
  streamHandler,
};
