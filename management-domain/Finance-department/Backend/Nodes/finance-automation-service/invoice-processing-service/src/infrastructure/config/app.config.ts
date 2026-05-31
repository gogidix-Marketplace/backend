import { registerAs } from '@nestjs/config';

/**
 * Application configuration
 * Centralized configuration using NestJS ConfigModule
 */
export default registerAs('app', () => ({
  name: process.env.APP_NAME || 'invoice-processing-service',
  version: process.env.APP_VERSION || '1.0.0',
  environment: process.env.NODE_ENV || 'development',
  port: parseInt(process.env.PORT || '3000', 10),
  host: process.env.HOST || '0.0.0.0',

  // API configuration
  api: {
    prefix: process.env.API_PREFIX || 'api',
    version: process.env.API_VERSION || 'v1',
  },

  // CORS configuration
  cors: {
    enabled: process.env.CORS_ENABLED === 'true',
    origin: process.env.CORS_ORIGIN || '*',
    credentials: process.env.CORS_CREDENTIALS === 'true',
  },

  // Rate limiting
  rateLimit: {
    enabled: process.env.RATE_LIMIT_ENABLED === 'true',
    ttl: parseInt(process.env.RATE_LIMIT_TTL || '60', 10),
    limit: parseInt(process.env.RATE_LIMIT_LIMIT || '100', 10),
  },

  // Logging
  logging: {
    level: process.env.LOG_LEVEL || 'info',
    format: process.env.LOG_FORMAT || 'json',
  },

  // Validation
  validation: {
    whitelist: process.env.VALIDATION_WHITELIST !== 'false',
    transform: process.env.VALIDATION_TRANSFORM === 'true',
    forbidNonWhitelisted: process.env.VALIDATION_FORBID_NON_WHITELISTED === 'true',
  },

  // Pagination defaults
  pagination: {
    defaultPageSize: parseInt(process.env.DEFAULT_PAGE_SIZE || '20', 10),
    maxPageSize: parseInt(process.env.MAX_PAGE_SIZE || '100', 10),
  },

  // File upload
  upload: {
    enabled: process.env.UPLOAD_ENABLED === 'true',
    maxFileSize: parseInt(process.env.MAX_FILE_SIZE || '10485760', 10), // 10MB default
    allowedTypes: (process.env.ALLOWED_FILE_TYPES || 'image/pdf,image/jpeg,image/png').split(','),
  },

  // OCR configuration
  ocr: {
    provider: process.env.OCR_PROVIDER || 'stub', // 'stub', 'azure', 'google', 'aws'
    timeout: parseInt(process.env.OCR_TIMEOUT || '30000', 10),
    confidence: parseFloat(process.env.OCR_MIN_CONFIDENCE || '0.7'),
  },
}));

/**
 * MongoDB configuration
 */
export const mongodbConfig = registerAs('mongodb', () => ({
  uri: process.env.MONGODB_URI || 'mongodb://localhost:27017/invoice-processing',
  username: process.env.MONGODB_USERNAME,
  password: process.env.MONGODB_PASSWORD,
  authDb: process.env.MONGODB_AUTH_DB || 'admin',
  maxPoolSize: parseInt(process.env.MONGODB_MAX_POOL_SIZE || '10', 10),
  minPoolSize: parseInt(process.env.MONGODB_MIN_POOL_SIZE || '2', 10),
  maxIdleTimeMS: parseInt(process.env.MONGODB_MAX_IDLE_TIME_MS || '60000', 10),
  waitQueueTimeoutMS: parseInt(process.env.MONGODB_WAIT_QUEUE_TIMEOUT_MS || '5000', 10),
  retryWrites: process.env.MONGODB_RETRY_WRITES !== 'false',
  retryReads: process.env.MONGODB_RETRY_READS !== 'false',
}));

/**
 * Kafka configuration
 */
export const kafkaConfig = registerAs('kafka', () => ({
  brokers: process.env.KAFKA_BROKERS || 'localhost:9092',
  clientId: process.env.KAFKA_CLIENT_ID || 'invoice-processing-service',
  groupId: process.env.KAFKA_GROUP_ID || 'invoice-processing-group',
  queue: process.env.KAFKA_QUEUE || 'invoice-events',
  username: process.env.KAFKA_USERNAME,
  password: process.env.KAFKA_PASSWORD,
  mechanism: process.env.KAFKA_AUTH_MECHANISM || 'plain',
  ssl: process.env.KAFKA_SSL === 'true',
}));

/**
 * JWT configuration (if authentication is needed)
 */
export const jwtConfig = registerAs('jwt', () => ({
  secret: process.env.JWT_SECRET || 'change-me-in-production',
  expiresIn: process.env.JWT_EXPIRES_IN || '1h',
  refreshSecret: process.env.JWT_REFRESH_SECRET || 'change-me-too',
  refreshExpiresIn: process.env.JWT_REFRESH_EXPIRES_IN || '7d',
}));
