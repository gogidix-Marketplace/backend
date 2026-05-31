/**
 * Configuration management for Chatbot Service
 */

import dotenv from 'dotenv';
import { ServiceConfig } from '../types';

dotenv.config();

const validateConfig = (): void => {
  const requiredEnvVars = [
    'PORT',
    'MONGODB_URI',
    'JWT_SECRET',
    'OPENAI_API_KEY',
  ];

  const missingVars = requiredEnvVars.filter(varName => !process.env[varName]);

  if (missingVars.length > 0) {
    throw new Error(
      `Missing required environment variables: ${missingVars.join(', ')}`
    );
  }

  if (process.env.NODE_ENV === 'production' && process.env.JWT_SECRET === 'your-super-secret-jwt-key-change-in-production') {
    throw new Error('JWT_SECRET must be changed in production');
  }
};

validateConfig();

export const config: ServiceConfig = {
  port: parseInt(process.env.PORT || '8112', 10),
  host: process.env.HOST || 'localhost',
  nodeEnv: process.env.NODE_ENV || 'development',
  mongoUri: process.env.MONGODB_URI || 'mongodb://localhost:27017/gogidix-chatbot',
  mongoDbName: process.env.MONGODB_DB_NAME || 'gogidix-chatbot',
  redis: {
    host: process.env.REDIS_HOST || 'localhost',
    port: parseInt(process.env.REDIS_PORT || '6379', 10),
    password: process.env.REDIS_PASSWORD || undefined,
    db: parseInt(process.env.REDIS_DB || '0', 10),
  },
  jwt: {
    secret: process.env.JWT_SECRET || 'your-super-secret-jwt-key-change-in-production',
    expiresIn: process.env.JWT_EXPIRES_IN || '24h',
    refreshSecret: process.env.JWT_REFRESH_SECRET || 'your-super-secret-refresh-jwt-key-change-in-production',
    refreshExpiresIn: process.env.JWT_REFRESH_EXPIRES_IN || '7d',
  },
  openai: {
    apiKey: process.env.OPENAI_API_KEY || '',
    model: process.env.OPENAI_MODEL || 'gpt-4',
    maxTokens: parseInt(process.env.OPENAI_MAX_TOKENS || '500', 10),
    temperature: parseFloat(process.env.OPENAI_TEMPERATURE || '0.7'),
  },
  services: {
    knowledgeBase: process.env.KNOWLEDGE_BASE_SERVICE_URL || 'http://localhost:8115',
    ticket: process.env.TICKET_SERVICE_URL || 'http://localhost:8110',
    agent: process.env.AGENT_SERVICE_URL || 'http://localhost:8116',
  },
  rateLimit: {
    windowMs: parseInt(process.env.RATE_LIMIT_WINDOW_MS || '900000', 10),
    maxRequests: parseInt(process.env.RATE_LIMIT_MAX_REQUESTS || '100', 10),
  },
  logging: {
    level: process.env.LOG_LEVEL || 'info',
    filePath: process.env.LOG_FILE_PATH || 'logs',
  },
  cors: {
    origin: process.env.CORS_ORIGIN || 'http://localhost:3000',
  },
  session: {
    timeoutMs: parseInt(process.env.SESSION_TIMEOUT_MS || '1800000', 10), // 30 minutes
    maxContextTurns: parseInt(process.env.MAX_CONTEXT_TURNS || '20', 10),
  },
  handoff: {
    threshold: parseFloat(process.env.HANDOFF_THRESHOLD || '0.3'),
    maxAutomatedTurns: parseInt(process.env.MAX_AUTOMATED_TURNS || '5', 10),
    escalationTopics: process.env.ESCALATION_TOPICS?.split(',') || [
      'refund',
      'complaint',
      'legal',
      'sue',
      'fraud',
    ],
  },
  i18n: {
    defaultLanguage: process.env.DEFAULT_LANGUAGE || 'en',
    supportedLanguages: process.env.SUPPORTED_LANGUAGES?.split(',') || [
      'en',
      'es',
      'fr',
      'de',
      'pt',
      'zh',
      'ja',
      'ar',
    ],
  },
};

export default config;
