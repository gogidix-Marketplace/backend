/**
 * WebSocket Server Registry
 *
 * Global registry for WebSocket server instance.
 * Allows access to the WebSocket server from other modules.
 */

let wsServerInstance = null;

/**
 * Register WebSocket server instance
 */
function registerWebSocketServer(wsServer) {
  wsServerInstance = wsServer;
}

/**
 * Get WebSocket server instance
 */
function getWebSocketServer() {
  return wsServerInstance;
}

/**
 * Clear WebSocket server instance
 */
function clearWebSocketServer() {
  wsServerInstance = null;
}

module.exports = {
  registerWebSocketServer,
  getWebSocketServer,
  clearWebSocketServer,
};
