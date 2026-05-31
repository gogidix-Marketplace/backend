/**
 * Middleware Configuration
 */

const express = require('express');
const cors = require('cors');
const helmet = require('helmet');
const compression = require('compression');
const { authMiddleware } = require('./auth');
const { requestLogger } = require('./requestLogger');
const { errorHandler } = require('./errorHandler');
const { rateLimiter } = require('./rateLimiter');
const { healthCheck } = require('./healthCheck');

/**
 * Setup all middleware
 */
async function setupMiddleware(app) {
  // Security headers
  app.use(helmet());

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

  // Health check (before auth)
  app.get('/health', healthCheck);

  // Apply rate limiting to API routes
  app.use('/api', rateLimiter);
}

/**
 * Setup authentication for API routes
 */
function setupAuth(app) {
  app.use('/api', authMiddleware);
}

module.exports = { setupMiddleware, setupAuth };
