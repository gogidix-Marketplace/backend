import { Room } from '../models/room.entity';

export interface IRoomRepository {
  save(room: Room): Promise<Room>;
  findById(roomId: string): Promise<Room | null>;
  findByTenant(tenantId: string, type?: string): Promise<Room[]>;
  delete(roomId: string): Promise<void>;
  update(room: Room): Promise<Room>;
}
