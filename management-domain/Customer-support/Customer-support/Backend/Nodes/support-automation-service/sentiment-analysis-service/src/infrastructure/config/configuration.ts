import { registerAs } from '@nestjs/config';
export default registerAs('config', () => ({
  port: parseInt(process.env.PORT || '3003', 10),
  host: process.env.HOST || 'localhost',
  nodeEnv: process.env.NODE_ENV || 'development',
  kafka: { brokers: (process.env.KAFKA_BROKERS || 'localhost:9092').split(','), groupId: process.env.KAFKA_GROUP_ID || 'sentiment-analysis-service' },
}));
