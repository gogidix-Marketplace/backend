import { ConnectionInfo } from '../models/connection-info.entity';

export interface IConnectionRepository {
  addConnection(connection: ConnectionInfo): void;
  getConnection(clientId: string): ConnectionInfo | undefined;
  removeConnection(clientId: string): boolean;
  getRoomConnections(roomId: string): ConnectionInfo[];
  getTenantConnections(tenantId: string): ConnectionInfo[];
  getStats(): { totalConnections: number; totalTenants: number; totalRooms: number };
}
