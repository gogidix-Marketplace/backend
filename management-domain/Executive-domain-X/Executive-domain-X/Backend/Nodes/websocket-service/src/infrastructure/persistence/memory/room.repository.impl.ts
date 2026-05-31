import { Injectable } from '@nestjs/common';
import { Room } from '../../../domain/models/room.entity';
import { IRoomRepository } from '../../../domain/repositories/room-repository.interface';

@Injectable()
export class RoomRepositoryImpl implements IRoomRepository {
  private rooms = new Map<string, Room>();

  async save(room: Room): Promise<Room> { this.rooms.set(room.props.id, room); return room; }
  async findById(roomId: string): Promise<Room | null> { return this.rooms.get(roomId) || null; }
  async findByTenant(tenantId: string, type?: string): Promise<Room[]> { return Array.from(this.rooms.values()).filter(r => r.props.tenantId === tenantId && (!type || r.props.type === type)); }
  async delete(roomId: string): Promise<void> { this.rooms.delete(roomId); }
  async update(room: Room): Promise<Room> { this.rooms.set(room.props.id, room); return room; }
}
