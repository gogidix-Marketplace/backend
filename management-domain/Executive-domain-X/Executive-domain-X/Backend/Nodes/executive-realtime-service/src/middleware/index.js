/**
 * Middleware configuration
 */

const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const compression = require('compression');
const { requestLogger } = require('./requestLogger');
const { errorHandler } = require('./errorHandler');
const { healthCheck } = require('./healthCheck');

/**
 * Setup all middleware
 */
async function setupMiddleware(app) {
  // Security headers
  app.use(helmet({
    contentSecurityPolicy: false, // Disable for WebSocket compatibility
    crossOriginEmbedderPolicy: false,
  }));

  // CORS
  app.use(cors({
    origin: process.env.CORS_ORIGINS ? process.env.CORS_ORIGINS.split(',') : '*',
    credentials: true,
  }));

  // Compression
  app.use(compression());

  // Body parsing
  app.use(express.json());
  app.use(express.urlencoded({ extended: true }));

  // Request logging
  app.use(requestLogger);

  // Health check endpoint (before auth)
  app.get('/health', healthCheck);

  // Metrics endpoint
  app.get('/metrics', (req, res) => {
    const wsServer = req.app.get('wsServer');
    const stats = wsServer ? wsServer.getStats() : {};
    res.json({
      status: 'healthy',
      timestamp: new Date().toISOString(),
      websocket: stats,
    });
  });
}

/**
 * Setup error handling (must be last)
 */
async function setupErrorHandling(app) {
  app.use(errorHandler);
}

module.exports = { setupMiddleware, setupErrorHandling };
