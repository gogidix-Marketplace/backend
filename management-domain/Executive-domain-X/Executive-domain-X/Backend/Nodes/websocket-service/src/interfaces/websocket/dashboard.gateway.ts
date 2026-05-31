import { WebSocketGateway, WebSocketServer, SubscribeMessage, OnGatewayConnection, OnGatewayDisconnect, OnGatewayInit } from '@nestjs/websockets';
import { Server, Socket } from 'socket.io';
import { Logger } from '@nestjs/common';
import { RoomService } from '../../application/services/room.service';
import { PresenceService } from '../../application/services/presence.service';
import { BroadcastService } from '../../application/services/broadcast.service';
import { DashboardService } from '../../application/services/dashboard.service';

@WebSocketGateway({ cors: { origin: '*' } })
export class DashboardGateway implements OnGatewayInit, OnGatewayConnection, OnGatewayDisconnect {
  @WebSocketServer() server: Server;
  private readonly logger = new Logger(DashboardGateway.name);

  constructor(
    private readonly roomService: RoomService,
    private readonly presenceService: PresenceService,
    private readonly broadcastService: BroadcastService,
    private readonly dashboardService: DashboardService,
  ) {}

  afterInit(_server: Server) { this.logger.log('WebSocket Gateway initialized'); }

  handleConnection(client: Socket) {
    const user = (client.handshake as any).user;
    if (user) { (client as any).data = { user }; this.presenceService.setOnline(client.id, user); }
    this.logger.debug(`Client connected: ${client.id}`);
  }

  handleDisconnect(client: Socket) {
    this.presenceService.setOffline(client.id);
    this.logger.debug(`Client disconnected: ${client.id}`);
  }

  @SubscribeMessage('room:join')
  handleJoinRoom(client: Socket, payload: { roomId: string }) {
    client.join(payload.roomId);
    const user = (client as any).data?.user;
    if (user) this.logger.debug(`User ${user.userId} joined room ${payload.roomId}`);
  }

  @SubscribeMessage('room:leave')
  handleLeaveRoom(client: Socket, payload: { roomId: string }) { client.leave(payload.roomId); }

  @SubscribeMessage('presence:update')
  handlePresenceUpdate(client: Socket, payload: { status: string }) {
    const user = (client as any).data?.user;
    if (user) this.presenceService.updateStatus(client.id, payload.status as any);
  }

  @SubscribeMessage('presence:typing')
  handleTyping(client: Socket, payload: { roomId: string; isTyping: boolean }) {
    const user = (client as any).data?.user;
    if (user) { this.presenceService.setTyping(client.id, payload.roomId, payload.isTyping, user); this.broadcastService.broadcastTypingIndicator(payload.roomId, user.userId, payload.isTyping, user.name); }
  }

  @SubscribeMessage('dashboard:subscribe')
  handleDashboardSubscribe(client: Socket, payload: { dashboardId: string }) {
    this.dashboardService.subscribeToDashboard(payload.dashboardId, client);
    const user = (client as any).data?.user;
    if (user) { const room = this.roomService.getDashboardRoom(user.tenantId, payload.dashboardId); client.join(room); }
  }
}
