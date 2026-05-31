import { Injectable, Logger } from '@nestjs/common';

@Injectable()
export class BroadcastService {
  private readonly logger = new Logger(BroadcastService.name);
  private messageQueue: Array<() => Promise<void>> = [];
  private stats = { messagesBroadcast: 0, messagesToKafka: 0, messagesToRedis: 0, errors: 0 };

  async broadcastToRoom(roomId: string, event: string, data: any, _options: { exclude?: string[]; toKafka?: boolean; toRedis?: boolean; persist?: boolean } = {}) {
    this.stats.messagesBroadcast++;
    this.logger.debug(`Broadcast to room ${roomId}: ${event}`);
    return { success: true, messageId: this.generateMessageId() };
  }

  async broadcastToUser(_tenantId: string, _userId: string, _event: string, _data: any) {
    this.stats.messagesBroadcast++;
    return { success: true, messageId: this.generateMessageId() };
  }

  async broadcastToTenant(_tenantId: string, _event: string, _data: any) {
    this.stats.messagesBroadcast++;
    return { success: true, messageId: this.generateMessageId() };
  }

  async broadcastDashboardUpdate(tenantId: string, dashboardId: string, update: any) {
    return this.broadcastToRoom(`tenant:${tenantId}:dashboard:${dashboardId}`, 'dashboard:update', { dashboardId, ...update }, { toKafka: true, persist: true });
  }

  async broadcastKpiUpdate(tenantId: string, kpiId: string, update: any) {
    return this.broadcastToRoom(`tenant:${tenantId}:kpi:${kpiId}`, 'kpi:update', { kpiId, ...update }, { toKafka: true });
  }

  async broadcastPresenceChange(tenantId: string, userId: string, status: string, metadata?: any) {
    return this.broadcastToTenant(tenantId, 'presence:change', { userId, status, metadata, timestamp: new Date().toISOString() });
  }

  async broadcastTypingIndicator(roomId: string, userId: string, isTyping: boolean, userName?: string) {
    return this.broadcastToRoom(roomId, 'presence:typing', { userId, userName, isTyping, timestamp: Date.now() }, { toKafka: false, toRedis: false });
  }

  getStats() { return { ...this.stats, queueSize: this.messageQueue.length }; }
  private generateMessageId() { return `msg_${Date.now()}_${Math.random().toString(36).substr(2, 9)}`; }
}
