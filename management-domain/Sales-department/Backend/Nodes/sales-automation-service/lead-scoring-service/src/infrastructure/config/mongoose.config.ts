import { registerAs } from '@nestjs/config';

export default registerAs('mongoose', () => ({
  uri: process.env.MONGODB_URI || 'mongodb://localhost:27017/lead_scoring_db',
}));
