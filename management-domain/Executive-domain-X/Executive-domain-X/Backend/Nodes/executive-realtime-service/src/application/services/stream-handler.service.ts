import { Injectable, Logger } from '@nestjs/common';
import { StreamBuffer } from '../../domain/models/stream-buffer.entity';

@Injectable()
export class StreamHandlerService {
  private readonly logger = new Logger(StreamHandlerService.name);
  private buffers = new Map<string, StreamBuffer>();
  private subscribers = new Map<string, Set<string>>();

  subscribe(socketId: string, roomId: string, options?: { batchSize?: number; batchTimeout?: number; maxBufferSize?: number }) {
    if (!this.subscribers.has(roomId)) {
      this.subscribers.set(roomId, new Set());
      this.buffers.set(roomId, new StreamBuffer(roomId, options));
    }
    this.subscribers.get(roomId)!.add(socketId);
    this.logger.debug(`Socket ${socketId} subscribed to stream ${roomId}`);
  }

  unsubscribe(socketId: string, roomId: string): boolean {
    if (!this.subscribers.has(roomId)) return false;
    this.subscribers.get(roomId)!.delete(socketId);
    if (this.subscribers.get(roomId)!.size === 0) {
      this.subscribers.delete(roomId);
      const buf = this.buffers.get(roomId);
      if (buf) { buf.clear(); this.buffers.delete(roomId); }
    }
    return true;
  }

  add(roomId: string, data: any): string[] {
    if (!this.buffers.has(roomId)) this.buffers.set(roomId, new StreamBuffer(roomId));
    const flushed = this.buffers.get(roomId)!.add(data);
    if (flushed) return this.flush(roomId);
    return [];
  }

  flush(roomId: string): string[] {
    const buffer = this.buffers.get(roomId);
    const subs = this.subscribers.get(roomId);
    if (!buffer || !subs) return [];
    const data = buffer.flush();
    if (!data || data.length === 0) return [];
    return Array.from(subs);
  }

  getBufferStatus(roomId: string) { return this.buffers.get(roomId)?.getStatus() || null; }
  getAllBufferStatuses() { const s: any = {}; for (const [r, b] of this.buffers.entries()) s[r] = b.getStatus(); return s; }
}
