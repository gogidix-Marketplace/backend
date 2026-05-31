import { ChatSession, ChatSessionProps } from '@domain/models/chat-session.model';
import { SessionStatus } from '@domain/enums/session-status.enum';
import { Message } from '@domain/models/message.model';
import { SessionContext } from '@domain/models/session-context.model';

describe('ChatSession', () => {
  const validProps: ChatSessionProps = {
    sessionId: 'session_test123',
    status: SessionStatus.ACTIVE,
    language: 'en',
    startedAt: new Date(),
    lastActivityAt: new Date(),
    context: SessionContext.create({}).toPlainObject(),
    messages: [],
    tags: [],
    metadata: {},
  };

  describe('create', () => {
    it('should create a chat session with valid props', () => {
      const session = ChatSession.create(validProps);
      expect(session.sessionId).toBe('session_test123');
      expect(session.status).toBe(SessionStatus.ACTIVE);
      expect(session.language).toBe('en');
      expect(session.messages).toEqual([]);
      expect(session.tags).toEqual([]);
    });

    it('should default status to ACTIVE', () => {
      const session = ChatSession.create({ sessionId: 'session_abc' });
      expect(session.status).toBe(SessionStatus.ACTIVE);
    });

    it('should default language to en', () => {
      const session = ChatSession.create({ sessionId: 'session_abc' });
      expect(session.language).toBe('en');
    });
  });

  describe('addMessage', () => {
    it('should add a message and increment turn count', () => {
      const session = ChatSession.create(validProps);
      const message = Message.create({
        id: 'msg_1',
        sessionId: 'session_test123',
        content: 'Hello',
        sender: 'user',
        timestamp: new Date(),
        language: 'en',
      });

      session.addMessage(message);
      expect(session.messages.length).toBe(1);
      expect(session.context.turnCount).toBe(1);
    });
  });

  describe('updateStatus', () => {
    it('should update status to CLOSED and set endedAt', () => {
      const session = ChatSession.create(validProps);
      session.updateStatus(SessionStatus.CLOSED);
      expect(session.status).toBe(SessionStatus.CLOSED);
      expect(session.endedAt).toBeDefined();
    });

    it('should update status without setting endedAt for non-terminal statuses', () => {
      const session = ChatSession.create(validProps);
      session.updateStatus(SessionStatus.WAITING_FOR_AGENT);
      expect(session.status).toBe(SessionStatus.WAITING_FOR_AGENT);
      expect(session.endedAt).toBeUndefined();
    });
  });

  describe('updateSentiment', () => {
    it('should update sentiment', () => {
      const session = ChatSession.create(validProps);
      session.updateSentiment(80, 'positive');
      expect(session.sentiment).toEqual({ score: 80, label: 'positive' });
    });
  });

  describe('addTag', () => {
    it('should add a tag', () => {
      const session = ChatSession.create(validProps);
      session.addTag('greeting');
      expect(session.tags).toContain('greeting');
    });

    it('should not add duplicate tags', () => {
      const session = ChatSession.create(validProps);
      session.addTag('greeting');
      session.addTag('greeting');
      expect(session.tags.length).toBe(1);
    });
  });

  describe('duration', () => {
    it('should calculate duration from startedAt to now if not ended', () => {
      const startedAt = new Date(Date.now() - 60000);
      const session = ChatSession.create({ ...validProps, startedAt });
      expect(session.duration).toBeGreaterThanOrEqual(60000);
    });
  });
});
