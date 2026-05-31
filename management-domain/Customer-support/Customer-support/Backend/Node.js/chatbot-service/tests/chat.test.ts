/**
 * Chat Service Tests
 */

import { ChatService } from '../src/services/ChatService';
import { ChatSessionModel } from '../src/models/ChatSession';
import { SessionStatus } from '../src/types';
import { MongoMemoryServer } from 'mongodb-memory-server';
import mongoose from 'mongoose';

describe('ChatService', () => {
  let mongoServer: MongoMemoryServer;
  let chatService: ChatService;

  beforeAll(async () => {
    mongoServer = await MongoMemoryServer.create();
    const mongoUri = mongoServer.getUri();
    await mongoose.connect(mongoUri);
    chatService = new ChatService();
  });

  afterAll(async () => {
    await mongoose.disconnect();
    await mongoServer.stop();
  });

  afterEach(async () => {
    await ChatSessionModel.deleteMany({});
  });

  describe('createSession', () => {
    it('should create a new chat session', async () => {
      const session = await chatService.createSession('customer123', 'en');

      expect(session).toBeDefined();
      expect(session.sessionId).toBeDefined();
      expect(session.customerId).toBe('customer123');
      expect(session.language).toBe('en');
      expect(session.status).toBe(SessionStatus.ACTIVE);
    });

    it('should create session with default language en', async () => {
      const session = await chatService.createSession('customer456');

      expect(session.language).toBe('en');
    });
  });

  describe('sendMessage', () => {
    it('should send message and get response', async () => {
      const session = await chatService.createSession('customer789', 'en');

      const response = await chatService.sendMessage(
        session.sessionId,
        'Hello, I need help',
        'customer789',
        'en'
      );

      expect(response).toBeDefined();
      expect(response.text).toBeDefined();
      expect(response.language).toBe('en');
    });

    it('should create session if not exists', async () => {
      const response = await chatService.sendMessage(
        'nonexistent-session',
        'Hello',
        'customer999',
        'en'
      );

      expect(response).toBeDefined();
      expect(response.text).toBeDefined();
    });
  });

  describe('getSession', () => {
    it('should retrieve existing session', async () => {
      const created = await chatService.createSession('customer111', 'en');
      const retrieved = await chatService.getSession(created.sessionId);

      expect(retrieved).toBeDefined();
      expect(retrieved?.sessionId).toBe(created.sessionId);
    });

    it('should return null for non-existent session', async () => {
      const retrieved = await chatService.getSession('nonexistent-id');
      expect(retrieved).toBeNull();
    });
  });

  describe('closeSession', () => {
    it('should close active session', async () => {
      const session = await chatService.createSession('customer222', 'en');
      await chatService.closeSession(session.sessionId);

      const closed = await chatService.getSession(session.sessionId);
      expect(closed?.status).toBe(SessionStatus.CLOSED);
    });
  });
});
