import { registerAs } from '@nestjs/config';

export default registerAs('config', () => ({
  port: parseInt(process.env.PORT || '8114', 10),
  host: process.env.HOST || 'localhost',
  nodeEnv: process.env.NODE_ENV || 'development',
  mongoUri: process.env.MONGODB_URI || 'mongodb://localhost:27017/sentiment-analysis',
  mongoDbName: process.env.MONGODB_DB_NAME || 'sentiment-analysis',
  kafka: {
    brokers: (process.env.KAFKA_BROKERS || 'localhost:9092').split(','),
    groupId: process.env.KAFKA_GROUP_ID || 'sentiment-analysis-service',
  },
  sentiment: {
    negativityThreshold: parseInt(process.env.NEGATIVITY_THRESHOLD || '30', 10),
    positivityThreshold: parseInt(process.env.POSITIVITY_THRESHOLD || '70', 10),
    alertThreshold: parseInt(process.env.ALERT_THRESHOLD || '25', 10),
  },
}));
