/**
 * Application factory for Executive Command Service
 */

const express = require('express');
const { setupMiddleware } = require('./middleware');
const { setupRoutes } = require('./routes');
const { setupErrorHandling } = require('./middleware/errorHandler');
const logger = require('./config/logger');

/**
 * Create and configure the Express application
 */
async function createServer() {
  const app = express();

  // Setup middleware
  await setupMiddleware(app);

  // Setup routes
  await setupRoutes(app);

  // Setup error handling
  await setupErrorHandling(app);

  return app;
}

module.exports = { createServer };
