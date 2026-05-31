/**
 * Analytics Service Tests
 */

import { AnalyticsService } from '../src/services/AnalyticsService';
import { ChatSessionModel } from '../src/models/ChatSession';
import { ChatService } from '../src/services/ChatService';
import { MongoMemoryServer } from 'mongodb-memory-server';
import mongoose from 'mongoose';

describe('AnalyticsService', () => {
  let mongoServer: MongoMemoryServer;
  let analyticsService: AnalyticsService;
  let chatService: ChatService;

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const mongoUri = mongoServer.getUri();
    await mongoose.connect(mongoUri);
    analyticsService = new AnalyticsService();
    chatService = new ChatService();
  });

  afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
  });

  afterEach(async () => {
    await ChatSessionModel.deleteMany({});
  });

  describe('createSessionAnalytics', () => {
    it('should create analytics for closed session', async () => {
      const session = await chatService.createSession('customer123', 'en');
      await chatService.sendMessage(session.sessionId, 'Hello', 'customer123', 'en');
      await chatService.closeSession(session.sessionId);

      const analytics = await analyticsService.createSessionAnalytics(session.sessionId);

      expect(analytics).toBeDefined();
      expect(analytics.sessionId).toBe(session.sessionId);
      expect(analytics.messageCount).toBeGreaterThan(0);
    });

    it('should calculate correct duration', async () => {
      const session = await chatService.createSession('customer456', 'en');
      await chatService.closeSession(session.sessionId);

      const analytics = await analyticsService.createSessionAnalytics(session.sessionId);

      expect(analytics.duration).toBeGreaterThan(0);
    });
  });

  describe('generateDailyAnalytics', () => {
    it('should generate daily analytics', async () => {
      // Create some sessions
      await chatService.createSession('customer789', 'en');
      await chatService.createSession('customer999', 'en');

      const daily = await analyticsService.generateDailyAnalytics();

      expect(daily).toBeDefined();
      expect(daily.totalSessions).toBeGreaterThanOrEqual(0);
      expect(daily.date).toBeDefined();
    });
  });

  describe('getAggregatedAnalytics', () => {
    it('should return aggregated stats', async () => {
      const startDate = new Date();
      startDate.setHours(0, 0, 0, 0);
      const endDate = new Date();

      const stats = await analyticsService.getAggregatedAnalytics(startDate, endDate);

      expect(stats).toBeDefined();
      expect(stats.totalSessions).toBeDefined();
      expect(stats.avgSessionDuration).toBeDefined();
    });
  });
});
