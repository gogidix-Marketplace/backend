/**
 * Stream Handler Tests
 */

const {
  StreamHandler,
  StreamBuffer,
  StreamConfig,
  streamHandler,
} = require('./streamHandler');

jest.useFakeTimers();

describe('Stream Buffer', () => {
  let buffer;

  beforeEach(() => {
    jest.clearAllTimers();
    buffer = new StreamBuffer('test-room', {
      batchSize: 5,
      batchTimeout: 100,
      maxBufferSize: 10,
    });
  });

  afterEach(() => {
    if (buffer.flushTimer) {
      clearTimeout(buffer.flushTimer);
    }
  });

  describe('add', () => {
    it('should add data to buffer', () => {
      buffer.add({ id: 1, value: 'test' });

      expect(buffer.size).toBe(1);
      expect(buffer.buffer).toHaveLength(1);
    });

    it('should flush when batch size is reached', () => {
      const spy = jest.spyOn(buffer, 'flush');

      for (let i = 0; i < 5; i++) {
        buffer.add({ id: i });
      }

      expect(spy).toHaveBeenCalled();
    });

    it('should force flush when buffer is full', () => {
      buffer.maxBufferSize = 3;
      const spy = jest.spyOn(buffer, 'flush');

      buffer.add({ id: 1 });
      buffer.add({ id: 2 });
      buffer.add({ id: 3 });

      expect(spy).toHaveBeenCalled();
    });

    it('should set flush timer', () => {
      buffer.add({ id: 1 });

      expect(buffer.flushTimer).not.toBeNull();
    });
  });

  describe('flush', () => {
    it('should return buffered data and clear buffer', () => {
      buffer.add({ id: 1 });
      buffer.add({ id: 2 });

      const data = buffer.flush();

      expect(data).toEqual([{ id: 1 }, { id: 2 }]);
      expect(buffer.buffer).toHaveLength(0);
      expect(buffer.size).toBe(0);
    });

    it('should return null if buffer is empty', () => {
      const data = buffer.flush();

      expect(data).toBeNull();
    });

    it('should clear flush timer', () => {
      buffer.add({ id: 1 });
      buffer.flush();

      expect(buffer.flushTimer).toBeNull();
    });
  });

  describe('getStatus', () => {
    it('should return buffer status', () => {
      buffer.add({ id: 1 });

      const status = buffer.getStatus();

      expect(status).toMatchObject({
        roomId: 'test-room',
        size: 1,
      });
      expect(status.lastFlush).toBeDefined();
    });
  });

  describe('clear', () => {
    it('should clear buffer and timer', () => {
      buffer.add({ id: 1 });
      buffer.add({ id: 2 });

      buffer.clear();

      expect(buffer.buffer).toHaveLength(0);
      expect(buffer.size).toBe(0);
      expect(buffer.flushTimer).toBeNull();
    });
  });
});

describe('Stream Handler', () => {
  let handler;

  beforeEach(() => {
    jest.clearAllTimers();
    handler = new StreamHandler();
  });

  afterEach(() => {
    handler.cleanupAll();
  });

  describe('subscribe', () => {
    it('should subscribe socket to a room', () => {
      handler.subscribe('socket1', 'room1');

      expect(handler.subscribers.has('room1')).toBe(true);
      expect(handler.subscribers.get('room1').has('socket1')).toBe(true);
      expect(handler.buffers.has('room1')).toBe(true);
    });

    it('should add socket to existing room subscribers', () => {
      handler.subscribe('socket1', 'room1');
      handler.subscribe('socket2', 'room1');

      expect(handler.subscribers.get('room1').size).toBe(2);
    });
  });

  describe('unsubscribe', () => {
    it('should unsubscribe socket from room', () => {
      handler.subscribe('socket1', 'room1');

      const result = handler.unsubscribe('socket1', 'room1');

      expect(result).toBe(true);
      expect(handler.subscribers.get('room1').has('socket1')).toBe(false);
    });

    it('should clean up room when no subscribers', () => {
      handler.subscribe('socket1', 'room1');
      handler.unsubscribe('socket1', 'room1');

      expect(handler.subscribers.has('room1')).toBe(false);
      expect(handler.buffers.has('room1')).toBe(false);
    });

    it('should return false for non-existent subscription', () => {
      const result = handler.unsubscribe('socket1', 'room1');

      expect(result).toBe(false);
    });
  });

  describe('add', () => {
    it('should add data to room buffer', () => {
      handler.add('room1', { id: 1 });

      expect(handler.buffers.has('room1')).toBe(true);
      expect(handler.buffers.get('room1').size).toBe(1);
    });

    it('should create buffer on-demand', () => {
      handler.add('room1', { id: 1 });

      expect(handler.buffers.has('room1')).toBe(true);
    });
  });

  describe('flush', () => {
    it('should flush buffer and return subscribers', () => {
      handler.subscribe('socket1', 'room1');
      handler.subscribe('socket2', 'room1');
      handler.add('room1', { id: 1 });
      handler.add('room1', { id: 2 });

      const subscribers = handler.flush('room1');

      expect(subscribers).toEqual(expect.arrayContaining(['socket1', 'socket2']));
      expect(handler.buffers.get('room1').size).toBe(0);
    });

    it('should return empty array for non-existent room', () => {
      const subscribers = handler.flush('non-existent');

      expect(subscribers).toEqual([]);
    });

    it('should return empty array when buffer is empty', () => {
      handler.subscribe('socket1', 'room1');

      const subscribers = handler.flush('room1');

      expect(subscribers).toEqual([]);
    });
  });

  describe('throttledBroadcast', () => {
    it('should broadcast only once per interval', () => {
      const doBroadcastSpy = jest.spyOn(handler, '_doBroadcast');

      handler.throttledBroadcast('room1', 'test_event', { id: 1 }, 100);
      handler.throttledBroadcast('room1', 'test_event', { id: 2 }, 100);

      expect(doBroadcastSpy).not.toHaveBeenCalled();

      jest.advanceTimersByTime(100);

      expect(doBroadcastSpy).toHaveBeenCalledTimes(1);
    });

    it('should handle different events separately', () => {
      const doBroadcastSpy = jest.spyOn(handler, '_doBroadcast');

      handler.throttledBroadcast('room1', 'event1', { id: 1 }, 100);
      handler.throttledBroadcast('room1', 'event2', { id: 2 }, 100);

      jest.advanceTimersByTime(100);

      expect(doBroadcastSpy).toHaveBeenCalledTimes(2);
    });
  });

  describe('getBufferStatus', () => {
    it('should return buffer status for room', () => {
      handler.subscribe('socket1', 'room1');
      handler.add('room1', { id: 1 });

      const status = handler.getBufferStatus('room1');

      expect(status).toMatchObject({
        roomId: 'room1',
        size: 1,
      });
    });

    it('should return null for non-existent room', () => {
      const status = handler.getBufferStatus('non-existent');

      expect(status).toBeNull();
    });
  });

  describe('getAllBufferStatuses', () => {
    it('should return all buffer statuses', () => {
      handler.subscribe('socket1', 'room1');
      handler.subscribe('socket2', 'room2');
      handler.add('room1', { id: 1 });
      handler.add('room2', { id: 2 });

      const statuses = handler.getAllBufferStatuses();

      expect(statuses).toHaveProperty('room1');
      expect(statuses).toHaveProperty('room2');
    });
  });

  describe('cleanup', () => {
    it('should clean up room resources', () => {
      handler.subscribe('socket1', 'room1');
      handler.add('room1', { id: 1 });

      handler.cleanup('room1');

      expect(handler.subscribers.has('room1')).toBe(false);
      expect(handler.buffers.has('room1')).toBe(false);
    });
  });

  describe('cleanupAll', () => {
    it('should clean up all resources', () => {
      handler.subscribe('socket1', 'room1');
      handler.subscribe('socket2', 'room2');

      handler.cleanupAll();

      expect(handler.subscribers.size).toBe(0);
      expect(handler.buffers.size).toBe(0);
    });
  });
});

describe('Stream Config', () => {
  it('should have defined configuration', () => {
    expect(StreamConfig).toHaveProperty('batchSize');
    expect(StreamConfig).toHaveProperty('batchTimeout');
    expect(StreamConfig).toHaveProperty('throttleInterval');
    expect(StreamConfig).toHaveProperty('maxBufferSize');
  });
});
