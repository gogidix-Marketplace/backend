/**
 * Routes configuration
 */

const express = require('express');
const apiRoutes = require('./api');
const { notFoundHandler } = require('../middleware/errorHandler');

/**
 * Setup all routes
 */
async function setupRoutes(app) {
  // API routes
  app.use('/api', apiRoutes);

  // 404 handler
  app.use(notFoundHandler);
}

module.exports = { setupRoutes };
