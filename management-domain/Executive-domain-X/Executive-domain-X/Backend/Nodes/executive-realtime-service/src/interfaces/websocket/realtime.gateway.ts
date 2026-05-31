import { WebSocketGateway, WebSocketServer, SubscribeMessage, OnGatewayConnection, OnGatewayDisconnect, OnGatewayInit } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import { Logger } from '@nestjs/common';
import { ConnectionManagerService } from '../../application/services/connection-manager.service';
import { StreamHandlerService } from '../../application/services/stream-handler.service';

@WebSocketGateway({ cors: { origin: '*' } })
export class RealtimeGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {
  @WebSocketServer() server: Server;
  private readonly logger = new Logger(RealtimeGateway.name);

  constructor(
    private readonly connectionManager: ConnectionManagerService,
    private readonly streamHandler: StreamHandlerService,
  ) {}

  afterInit(_server: Server) { this.logger.log('WebSocket Gateway initialized'); }

  handleConnection(client: Socket) {
    const connection = this.connectionManager.createConnection(client.handshake.address);
    (client as any).clientId = connection.props.id;
    this.logger.debug(`Client connected: ${connection.props.id}`);
  }

  handleDisconnect(client: Socket) {
    const clientId = (client as any).clientId;
    if (clientId) this.connectionManager.removeConnection(clientId);
  }

  @SubscribeMessage('authenticate')
  handleAuthenticate(client: Socket, payload: { tenantId: string; userId: string; executiveLevel: string }) {
    const clientId = (client as any).clientId;
    this.connectionManager.authenticateConnection(clientId, payload);
    client.join(`tenant:${payload.tenantId}`);
    client.emit('authenticated', { success: true });
  }

  @SubscribeMessage('subscribe')
  handleSubscribe(client: Socket, payload: { roomId: string }) {
    const clientId = (client as any).clientId;
    this.streamHandler.subscribe(clientId, payload.roomId);
    client.join(payload.roomId);
  }

  @SubscribeMessage('unsubscribe')
  handleUnsubscribe(client: Socket, payload: { roomId: string }) {
    const clientId = (client as any).clientId;
    this.streamHandler.unsubscribe(clientId, payload.roomId);
    client.leave(payload.roomId);
  }
}
