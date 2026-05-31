import { Injectable } from '@nestjs/common';
import { ConnectionInfo } from '../../../domain/models/connection-info.entity';
import { IConnectionRepository } from '../../../domain/repositories/connection-repository.interface';

@Injectable()
export class ConnectionRepositoryImpl implements IConnectionRepository {
  private connections = new Map<string, ConnectionInfo>();
  private connectionsByTenant = new Map<string, Set<string>>();
  private rooms = new Map<string, Set<string>>();

  addConnection(connection: ConnectionInfo): void { this.connections.set(connection.props.id, connection); }
  getConnection(clientId: string): ConnectionInfo | undefined { return this.connections.get(clientId); }

  removeConnection(clientId: string): boolean {
    const conn = this.connections.get(clientId);
    if (!conn) return false;
    for (const room of conn.props.rooms) { this.rooms.get(room)?.delete(clientId); }
    if (conn.props.tenantId) this.connectionsByTenant.get(conn.props.tenantId)?.delete(clientId);
    this.connections.delete(clientId);
    return true;
  }

  getRoomConnections(roomId: string): ConnectionInfo[] {
    const ids = this.rooms.get(roomId);
    if (!ids) return [];
    return Array.from(ids).map(id => this.connections.get(id)).filter(Boolean) as ConnectionInfo[];
  }

  getTenantConnections(tenantId: string): ConnectionInfo[] {
    const ids = this.connectionsByTenant.get(tenantId);
    if (!ids) return [];
    return Array.from(ids).map(id => this.connections.get(id)).filter(Boolean) as ConnectionInfo[];
  }

  getStats() {
    return { totalConnections: this.connections.size, totalTenants: this.connectionsByTenant.size, totalRooms: this.rooms.size };
  }
}
