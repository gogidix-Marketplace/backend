/**
 * Intent Detection Service Tests
 */

import { IntentDetectionService } from '../src/services/IntentDetectionService';
import { IntentModel } from '../src/models/Intent';
import { MongoMemoryServer } from 'mongodb-memory-server';
import mongoose from 'mongoose';

describe('IntentDetectionService', () => {
  let mongoServer: MongoMemoryServer;
  let intentService: IntentDetectionService;

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const mongoUri = mongoServer.getUri();
    await mongoose.connect(mongoUri);
    intentService = new IntentDetectionService();

    // Seed test intents
    await IntentModel.create([
      {
        name: 'greeting',
        category: 'greeting',
        description: 'User greeting',
        trainingPhrases: ['hello', 'hi', 'hey', 'good morning'],
        responses: ['Hello! How can I help you?', 'Hi there!'],
        language: 'en',
        isActive: true,
      },
      {
        name: 'order_status',
        category: 'order_status',
        description: 'Check order status',
        trainingPhrases: ['where is my order', 'order status', 'track my package'],
        responses: ['I can help you track your order. Please provide your order number.'],
        language: 'en',
        isActive: true,
      },
    ]);

    await intentService.reloadIntents();
  });

  afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
  });

  afterEach(async () => {
    await IntentModel.deleteMany({});
  });

  describe('detectIntent', () => {
    it('should detect greeting intent', async () => {
      const result = await intentService.detectIntent('Hello there!', 'en');

      expect(result).toBeDefined();
      expect(result.intent).toBe('greeting');
      expect(result.category).toBe('greeting');
      expect(result.confidence).toBeGreaterThan(0);
    });

    it('should detect order status intent', async () => {
      const result = await intentService.detectIntent('Where is my order?', 'en');

      expect(result).toBeDefined();
      expect(result.intent).toBe('order_status');
      expect(result.category).toBe('order_status');
    });

    it('should extract entities from message', async () => {
      const result = await intentService.detectIntent(
        'My email is test@example.com and order is ABC123',
        'en'
      );

      expect(result.entities).toBeDefined();
      expect(result.entities.length).toBeGreaterThan(0);
      expect(result.entities.some(e => e.type === 'email')).toBe(true);
    });

    it('should detect negative sentiment', async () => {
      const result = await intentService.detectIntent('This is terrible and awful!', 'en');

      expect(result.sentiment).toBeDefined();
      expect(result.sentiment.label).toBe('negative');
    });
  });

  describe('getIntent', () => {
    it('should return intent by name', () => {
      const intent = intentService.getIntent('greeting');

      expect(intent).toBeDefined();
      expect(intent?.name).toBe('greeting');
    });

    it('should return undefined for non-existent intent', () => {
      const intent = intentService.getIntent('nonexistent');
      expect(intent).toBeUndefined();
    });
  });
});
