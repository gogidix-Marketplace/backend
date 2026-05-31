import { registerAs } from '@nestjs/config';

export default registerAs('config', () => ({
  port: parseInt(process.env.PORT || '8112', 10),
  host: process.env.HOST || 'localhost',
  nodeEnv: process.env.NODE_ENV || 'development',
  mongoUri: process.env.MONGODB_URI || 'mongodb://localhost:27017/gogidix-chatbot',
  mongoDbName: process.env.MONGODB_DB_NAME || 'gogidix-chatbot',
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
    timeoutMs: parseInt(process.env.SESSION_TIMEOUT_MS || '1800000', 10),
    maxContextTurns: parseInt(process.env.MAX_CONTEXT_TURNS || '20', 10),
  },
  handoff: {
    threshold: parseFloat(process.env.HANDOFF_THRESHOLD || '0.3'),
    maxAutomatedTurns: parseInt(process.env.MAX_AUTOMATED_TURNS || '5', 10),
    escalationTopics: process.env.ESCALATION_TOPICS?.split(',') || ['refund', 'complaint', 'legal', 'sue', 'fraud'],
  },
  i18n: {
    defaultLanguage: process.env.DEFAULT_LANGUAGE || 'en',
    supportedLanguages: process.env.SUPPORTED_LANGUAGES?.split(',') || ['en', 'es', 'fr', 'de', 'pt', 'zh', 'ja', 'ar'],
  },
  kafka: {
    brokers: process.env.KAFKA_BROKERS?.split(',') || ['localhost:9092'],
    clientId: process.env.KAFKA_CLIENT_ID || 'chatbot-service',
    groupId: process.env.KAFKA_GROUP_ID || 'chatbot-group',
  },
}));
