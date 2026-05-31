import { WebSocketGateway, WebSocketServer, OnGatewayConnection, OnGatewayDisconnect } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import { Logger } from '@nestjs/common';

@WebSocketGateway({
  path: '/socket.io',
  cors: { origin: '*', credentials: true },
})
export class LogStreamGateway implements OnGatewayConnection, OnGatewayDisconnect {
  private readonly logger = new Logger(LogStreamGateway.name);

  @WebSocketServer()
  server: Server;

  handleConnection(client: Socket) {
    this.logger.debug(`Client connected: ${client.id}`);
  }

  handleDisconnect(client: Socket) {
    this.logger.debug(`Client disconnected: ${client.id}`);
  }

  broadcastLog(logEntry: any): void {
    this.server.to('logs').emit('log', logEntry);
  }

  broadcastStats(stats: any): void {
    this.server.to('logs').emit('stats', stats);
  }
}
