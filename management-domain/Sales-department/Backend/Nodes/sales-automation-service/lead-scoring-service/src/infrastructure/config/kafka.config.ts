import { registerAs } from '@nestjs/config';

export default registerAs('kafka', () => ({
  clientId: process.env.KAFKA_CLIENT_ID || 'lead-scoring-service',
  brokers: (process.env.KAFKA_BROKERS || 'localhost:9092').split(','),
  groupId: process.env.KAFKA_CONSUMER_GROUP_ID || 'lead-scoring-consumer',
}));
