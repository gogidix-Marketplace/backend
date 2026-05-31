import { registerAs } from '@nestjs/config';

export const mongodbConfig = registerAs('mongodb', () => ({
  uri: process.env.MONGODB_URI || 'mongodb://localhost:27017/sales-automation',
  options: {
    maxPoolSize: parseInt(process.env.MONGO_MAX_POOL_SIZE, 10) || 10,
    minPoolSize: parseInt(process.env.MONGO_MIN_POOL_SIZE, 10) || 2,
    socketTimeoutMS: parseInt(process.env.MONGO_SOCKET_TIMEOUT_MS, 10) || 45000,
    serverSelectionTimeoutMS: parseInt(process.env.MONGO_SERVER_SELECTION_TIMEOUT_MS, 10) || 30000,
  },
}));
