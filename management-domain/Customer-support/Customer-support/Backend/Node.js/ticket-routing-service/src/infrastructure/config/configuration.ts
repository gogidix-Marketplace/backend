import { registerAs } from '@nestjs/config';

export default registerAs('config', () => ({
  port: parseInt(process.env.PORT || '8113', 10),
  host: process.env.HOST || 'localhost',
  nodeEnv: process.env.NODE_ENV || 'development',
  mongoUri: process.env.MONGODB_URI || 'mongodb://localhost:27017/ticket-routing',
  mongoDbName: process.env.MONGODB_DB_NAME || 'ticket-routing',
  redis: {
    host: process.env.REDIS_HOST || 'localhost',
    port: parseInt(process.env.REDIS_PORT || '6379', 10),
    password: process.env.REDIS_PASSWORD || undefined,
    db: parseInt(process.env.REDIS_DB || '1', 10),
  },
  jwt: {
    secret: process.env.JWT_SECRET || 'your-super-secret-jwt-key',
    expiresIn: process.env.JWT_EXPIRES_IN || '24h',
  },
  kafka: {
    brokers: (process.env.KAFKA_BROKERS || 'localhost:9092').split(','),
    groupId: process.env.KAFKA_GROUP_ID || 'ticket-routing-service',
  },
  routing: {
    maxQueueSize: parseInt(process.env.MAX_QUEUE_SIZE || '1000', 10),
    defaultPriorityWeight: parseInt(process.env.DEFAULT_PRIORITY_WEIGHT || '5', 10),
    skillMatchThreshold: parseFloat(process.env.SKILL_MATCH_THRESHOLD || '0.6'),
    loadBalancingStrategy: process.env.LOAD_BALANCING_STRATEGY || 'least-busy',
    autoAssignmentEnabled: process.env.AUTO_ASSIGNMENT_ENABLED !== 'false',
    escalationTimeout: parseInt(process.env.ESCALATION_TIMEOUT || '1800', 10),
  },
}));
