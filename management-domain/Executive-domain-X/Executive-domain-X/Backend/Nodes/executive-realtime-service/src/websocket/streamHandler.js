/**
 * Stream Handler
 *
 * Handles real-time data streaming to WebSocket clients.
 * Supports batching, throttling, and delta updates.
 */

const logger = require('../config/logger');

/**
 * Stream configuration
 */
const StreamConfig = {
  batchSize: parseInt(process.env.STREAM_BATCH_SIZE) || 100,
  batchTimeout: parseInt(process.env.STREAM_BATCH_TIMEOUT) || 100, // ms
  throttleInterval: parseInt(process.env.STREAM_THROTTLE_INTERVAL) || 1000, // ms
  maxBufferSize: parseInt(process.env.STREAM_MAX_BUFFER_SIZE) || 1000,
};

/**
 * Stream buffer for each room
 */
class StreamBuffer {
  constructor(roomId, options = {}) {
    this.roomId = roomId;
    this.buffer = [];
    this.lastFlush = Date.now();
    this.size = 0;
    this.options = {
      batchSize: options.batchSize || StreamConfig.batchSize,
      batchTimeout: options.batchTimeout || StreamConfig.batchTimeout,
      maxBufferSize: options.maxBufferSize || StreamConfig.maxBufferSize,
    };
    this.flushTimer = null;
  }

  /**
   * Add data to buffer
   */
  add(data) {
    // Check buffer size
    if (this.buffer.length >= this.options.maxBufferSize) {
      logger.warn(`Stream buffer full for room ${this.roomId}, forcing flush`);
      this.flush();
    }

    this.buffer.push(data);
    this.size++;

    // Check if we should flush
    if (this.buffer.length >= this.options.batchSize) {
      this.flush();
      return;
    }

    // Set flush timer if not already set
    if (!this.flushTimer) {
      this.flushTimer = setTimeout(() => {
        this.flush();
      }, this.options.batchTimeout);
    }
  }

  /**
   * Flush buffer and return data
   */
  flush() {
    if (this.flushTimer) {
      clearTimeout(this.flushTimer);
      this.flushTimer = null;
    }

    if (this.buffer.length === 0) {
      return null;
    }

    const data = [...this.buffer];
    this.buffer = [];
    this.size = 0;
    this.lastFlush = Date.now();

    return data;
  }

  /**
   * Get buffer status
   */
  getStatus() {
    return {
      roomId: this.roomId,
      size: this.size,
      lastFlush: this.lastFlush,
    };
  }

  /**
   * Clear buffer
   */
  clear() {
    if (this.flushTimer) {
      clearTimeout(this.flushTimer);
      this.flushTimer = null;
    }
    this.buffer = [];
    this.size = 0;
  }
}

/**
 * Stream Handler class
 */
class StreamHandler {
  constructor() {
    this.buffers = new Map(); // roomId -> StreamBuffer
    this.subscribers = new Map(); // roomId -> Set of socketIds
    this.throttleTimers = new Map(); // roomId -> timer
  }

  /**
   * Subscribe a socket to a stream
   */
  subscribe(socketId, roomId, options = {}) {
    if (!this.subscribers.has(roomId)) {
      this.subscribers.set(roomId, new Set());
      this.buffers.set(roomId, new StreamBuffer(roomId, options));
    }

    this.subscribers.get(roomId).add(socketId);

    logger.debug('Socket subscribed to stream', {
      socketId,
      roomId,
      subscribers: this.subscribers.get(roomId).size,
    });
  }

  /**
   * Unsubscribe a socket from a stream
   */
  unsubscribe(socketId, roomId) {
    if (!this.subscribers.has(roomId)) {
      return false;
    }

    this.subscribers.get(roomId).delete(socketId);

    // Clean up if no subscribers
    if (this.subscribers.get(roomId).size === 0) {
      this.subscribers.delete(roomId);
      const buffer = this.buffers.get(roomId);
      if (buffer) {
        buffer.clear();
        this.buffers.delete(roomId);
      }

      const timer = this.throttleTimers.get(roomId);
      if (timer) {
        clearTimeout(timer);
        this.throttleTimers.delete(roomId);
      }
    }

    logger.debug('Socket unsubscribed from stream', {
      socketId,
      roomId,
    });

    return true;
  }

  /**
   * Add data to a stream
   */
  add(roomId, data) {
    if (!this.buffers.has(roomId)) {
      // Create buffer on-demand
      this.buffers.set(roomId, new StreamBuffer(roomId));
    }

    const buffer = this.buffers.get(roomId);
    buffer.add(data);

    // Auto-flush if buffer is full
    if (buffer.buffer.length >= buffer.options.batchSize) {
      this.flush(roomId);
    }
  }

  /**
   * Flush stream buffer and send to subscribers
   */
  flush(roomId) {
    const buffer = this.buffers.get(roomId);
    const subscribers = this.subscribers.get(roomId);

    if (!buffer || !subscribers) {
      return [];
    }

    const data = buffer.flush();
    if (!data || data.length === 0) {
      return [];
    }

    return Array.from(subscribers);
  }

  /**
   * Throttled broadcast - sends at most once per interval
   */
  throttledBroadcast(roomId, event, data, interval = StreamConfig.throttleInterval) {
    // Mark that we have pending data for this room
    if (!this.pendingData) {
      this.pendingData = new Map();
    }

    const key = `${roomId}:${event}`;
    this.pendingData.set(key, { roomId, event, data });

    // Set timer if not already set
    if (!this.throttleTimers.has(key)) {
      const timer = setTimeout(() => {
        const pending = this.pendingData.get(key);
        if (pending) {
          // Trigger broadcast
          this._doBroadcast(pending.roomId, pending.event, [pending.data]);
          this.pendingData.delete(key);
        }
        this.throttleTimers.delete(key);
      }, interval);

      this.throttleTimers.set(key, timer);
    }
  }

  /**
   * Get buffer status for a room
   */
  getBufferStatus(roomId) {
    const buffer = this.buffers.get(roomId);
    return buffer ? buffer.getStatus() : null;
  }

  /**
   * Get all buffer statuses
   */
  getAllBufferStatuses() {
    const statuses = {};
    for (const [roomId, buffer] of this.buffers.entries()) {
      statuses[roomId] = buffer.getStatus();
    }
    return statuses;
  }

  /**
   * Clean up a room's resources
   */
  cleanup(roomId) {
    const buffer = this.buffers.get(roomId);
    if (buffer) {
      buffer.clear();
      this.buffers.delete(roomId);
    }

    this.subscribers.delete(roomId);

    const timer = this.throttleTimers.get(roomId);
    if (timer) {
      clearTimeout(timer);
      this.throttleTimers.delete(roomId);
    }
  }

  /**
   * Clean up all resources
   */
  cleanupAll() {
    for (const roomId of this.buffers.keys()) {
      this.cleanup(roomId);
    }
  }

  /**
   * Internal broadcast method (to be called with actual socket.io/ws instance)
   */
  _doBroadcast(roomId, event, data) {
    // This is a placeholder - actual implementation would be provided by the caller
    logger.debug('Stream broadcast', {
      roomId,
      event,
      dataCount: data.length,
    });
  }
}

// Singleton instance
const streamHandler = new StreamHandler();

module.exports = {
  StreamHandler,
  StreamBuffer,
  StreamConfig,
  streamHandler,
};
