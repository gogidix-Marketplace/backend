import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { Room } from '../../domain/models/room.entity';
import { IRoomRepository } from '../../domain/repositories/room-repository.interface';
import { RoomType } from '../../domain/enums/room-type.enum';

@Injectable()
export class RoomService {
  private readonly logger = new Logger(RoomService.name);

  constructor(private readonly roomRepository: IRoomRepository) {}

  async createRoom(tenantId: string, name: string, type: RoomType = RoomType.PUBLIC, options: { createdBy?: string; maxUsers?: number; persistent?: boolean; metadata?: Record<string, unknown> } = {}) {
    const roomId = `tenant:${tenantId}:room:${uuidv4()}`;
    const room = new Room({
      id: roomId, tenantId, name, type, createdBy: options.createdBy || null,
      maxUsers: options.maxUsers || 100, currentUsers: 0, persistent: options.persistent || false,
      metadata: options.metadata || {}, createdAt: new Date().toISOString(),
    });
    const saved = await this.roomRepository.save(room);
    this.logger.log(`Room created: ${roomId} (${name}) for tenant: ${tenantId}`);
    return saved;
  }

  async getRoom(roomId: string) { return this.roomRepository.findById(roomId); }
  async listRooms(tenantId: string, type?: string) { return this.roomRepository.findByTenant(tenantId, type); }
  async deleteRoom(roomId: string) { return this.roomRepository.delete(roomId); }

  getTenantRoomPrefix(tenantId: string) { return `tenant:${tenantId}:`; }
  getUserRoom(tenantId: string, userId: string) { return `tenant:${tenantId}:user:${userId}`; }
  getDashboardRoom(tenantId: string, dashboardId: string) { return `tenant:${tenantId}:dashboard:${dashboardId}`; }
  getKpiRoom(tenantId: string, kpiId: string) { return `tenant:${tenantId}:kpi:${kpiId}`; }
  getNotificationRoom(tenantId: string, userId: string) { return `tenant:${tenantId}:notifications:${userId}`; }
}
