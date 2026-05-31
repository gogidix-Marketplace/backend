import { Controller, Get, Post, Put, Body, Param, Query } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { DashboardService } from '../../application/services/dashboard.service';
import { RoomService } from '../../application/services/room.service';
import { PresenceService } from '../../application/services/presence.service';
import { BroadcastService } from '../../application/services/broadcast.service';
import { CurrentTenant, CurrentUser } from '../../shared/decorators/tenant.decorator';

@ApiTags('dashboard')
@ApiBearerAuth()
@Controller('dashboard')
export class DashboardController {
  constructor(
    private readonly dashboardService: DashboardService,
    private readonly roomService: RoomService,
    private readonly presenceService: PresenceService,
    private readonly broadcastService: BroadcastService,
  ) {}

  @Post()
  async createDashboard(@CurrentTenant() tenantId: string, @Body() body: { name: string; description?: string; widgets?: any[]; isPublic?: boolean }, @CurrentUser() user: any) {
    return this.dashboardService.createDashboard({ ...body, executiveId: user.id });
  }

  @Get(':id')
  async getDashboard(@Param('id') id: string) { return this.dashboardService.getDashboard(id); }

  @Get()
  async listDashboards(@CurrentUser() user: any) { return this.dashboardService.getExecutiveDashboards(user.id); }

  @Put(':id/widget/:widgetId')
  async updateWidget(@Param('id') id: string, @Param('widgetId') widgetId: string, @Body() body: any) {
    return this.dashboardService.handleWidgetUpdate(id, widgetId, body);
  }

  @Get('rooms')
  async listRooms(@CurrentTenant() tenantId: string, @Query('type') type?: string) { return this.roomService.listRooms(tenantId, type); }

  @Post('rooms')
  async createRoom(@CurrentTenant() tenantId: string, @Body() body: { name: string; type?: string; maxUsers?: number; persistent?: boolean }) {
    return this.roomService.createRoom(tenantId, body.name, body.type as any, { maxUsers: body.maxUsers, persistent: body.persistent });
  }

  @Get('presence/online')
  async getOnlineUsers(@CurrentTenant() tenantId: string) { return this.presenceService.getOnlineUsers(tenantId); }

  @Get('stats')
  async getStats() { return { broadcast: this.broadcastService.getStats() }; }
}
