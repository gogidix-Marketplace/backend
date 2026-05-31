import { Injectable, Logger } from '@nestjs/common';
import { PresenceStatus } from '../../domain/enums/presence-status.enum';

@Injectable()
export class PresenceService {
  private readonly logger = new Logger(PresenceService.name);
  private presenceCache = new Map<string, any>();
  private typingIndicators = new Map<string, any>();

  async setOnline(socketId: string, user: { userId: string; tenantId: string; name?: string; email?: string; executiveId?: string; roles?: string[] }) {
    const data = { ...user, status: PresenceStatus.ONLINE, socketId, lastSeen: new Date().toISOString() };
    this.presenceCache.set(socketId, data);
    this.logger.debug(`User ${user.userId} is now online`);
    return data;
  }

  async setOffline(socketId: string) {
    const cached = this.presenceCache.get(socketId);
    if (cached) { cached.status = PresenceStatus.OFFLINE; cached.lastSeen = new Date().toISOString(); }
    this.presenceCache.delete(socketId);
    this.clearTypingIndicator(socketId);
    return cached;
  }

  async updateStatus(socketId: string, status: PresenceStatus) {
    const cached = this.presenceCache.get(socketId);
    if (cached) { cached.status = status; cached.lastSeen = new Date().toISOString(); }
    return cached;
  }

  async getPresence(tenantId: string, userId: string) {
    for (const [, data] of this.presenceCache.entries()) {
      if (data.userId === userId && data.tenantId === tenantId) return data;
    }
    return { userId, tenantId, status: PresenceStatus.OFFLINE, lastSeen: null };
  }

  async getOnlineUsers(tenantId: string) {
    return Array.from(this.presenceCache.values()).filter(d => d.tenantId === tenantId && d.status !== PresenceStatus.OFFLINE);
  }

  async setTyping(socketId: string, roomId: string, isTyping: boolean, user: any) {
    this.typingIndicators.set(`${roomId}:${socketId}`, { userId: user.userId, name: user.name || user.email, isTyping, timestamp: Date.now() });
  }

  async getTypingUsers(roomId: string) {
    const result = [];
    for (const [key, data] of this.typingIndicators.entries()) {
      if (key.startsWith(`${roomId}:`) && Date.now() - data.timestamp < 3000) result.push(data);
    }
    return result;
  }

  clearTypingIndicator(socketId: string) { for (const key of this.typingIndicators.keys()) { if (key.endsWith(`:${socketId}`)) this.typingIndicators.delete(key); } }
}
