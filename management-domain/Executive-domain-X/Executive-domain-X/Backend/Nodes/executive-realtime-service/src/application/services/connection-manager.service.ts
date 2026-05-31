import { Injectable, Logger } from '@nestjs/common';
import { v4 as uuidv4 } from 'uuid';
import { ConnectionInfo } from '../../domain/models/connection-info.entity';
import { IConnectionRepository } from '../../domain/repositories/connection-repository.interface';

@Injectable()
export class ConnectionManagerService {
  private readonly logger = new Logger(ConnectionManagerService.name);

  constructor(private readonly connectionRepository: IConnectionRepository) {}

  createConnection(ip: string): ConnectionInfo {
    const id = uuidv4();
    const connection = new ConnectionInfo(id, ip);
    this.connectionRepository.addConnection(connection);
    this.logger.debug(`Connection added: ${id} from ${ip}`);
    return connection;
  }

  getConnection(clientId: string): ConnectionInfo | undefined {
    return this.connectionRepository.getConnection(clientId);
  }

  removeConnection(clientId: string): boolean {
    return this.connectionRepository.removeConnection(clientId);
  }

  authenticateConnection(clientId: string, userInfo: { tenantId: string; userId: string; executiveLevel: string; roles?: string[] }): boolean {
    const conn = this.connectionRepository.getConnection(clientId);
    if (!conn) return false;
    conn.authenticate(userInfo);
    this.logger.log(`Connection authenticated: ${clientId} tenant=${userInfo.tenantId}`);
    return true;
  }

  getStats() { return this.connectionRepository.getStats(); }
}
