import { Controller, Post, Get, Put, Delete, Patch, Body, Param, Query, UseInterceptors } from '@nestjs/common';
import { AgentManagementInputPort, RoutingInputPort } from '@domain/ports/input';
import { CreateAgentDto, UpdateAgentStatusDto, RouteTicketDto, ReassignTicketDto, CreateTicketDto } from '@application/dto';
import { TransformInterceptor } from '@shared/interceptors';
import { v4 as uuidv4 } from 'uuid';
import { Ticket, RoutingResult } from '@domain/models';
import { TicketPriority, TicketStatus, RoutingStrategy } from '@domain/enums';
import Redis from 'ioredis';

@Controller('api/agents')
@UseInterceptors(TransformInterceptor)
export class AgentController {
  constructor(private readonly agentService: AgentManagementInputPort) {}
  @Post() async create(@Body() dto: CreateAgentDto) { return this.agentService.createAgent(dto); }
  @Get() async getAll() { return this.agentService.getAllAgents(); }
  @Get('available') async getAvailable(@Query('department') dept?: string) { return this.agentService.getAvailableAgents(dept); }
  @Get(':id') async getOne(@Param('id') id: string) { return this.agentService.getAgent(id); }
  @Patch(':id/status') async updateStatus(@Param('id') id: string, @Body() dto: UpdateAgentStatusDto) { return this.agentService.updateAgentStatus(id, dto.status); }
  @Delete(':id') async remove(@Param('id') id: string) { return this.agentService.deleteAgent(id); }
  @Get(':id/stats') async stats(@Param('id') id: string) { return this.agentService.getAgentStats(id); }
  @Post(':id/activity') async activity(@Param('id') id: string) { return { success: true }; }
}

@Controller('api/tickets')
@UseInterceptors(TransformInterceptor)
export class TicketController {
  private readonly redis = new Redis({ host: process.env.REDIS_HOST || 'localhost', port: parseInt(process.env.REDIS_PORT || '6379') });
  constructor(private readonly routingService: RoutingInputPort) {}

  @Post() async create(@Body() dto: CreateTicketDto) {
    const ticket = Ticket.create({ id: dto.id || uuidv4(), customerId: dto.customerId, subject: dto.subject, description: dto.description, priority: (dto.priority as TicketPriority) || TicketPriority.MEDIUM, status: TicketStatus.PENDING, department: dto.department, requiredSkills: dto.requiredSkills || [], createdAt: new Date(), updatedAt: new Date() });
    await this.redis.set(`ticket:${ticket.id}`, JSON.stringify(ticket.toPlainObject()));
    return ticket;
  }

  @Post(':id/route') async route(@Param('id') id: string, @Body() dto: RouteTicketDto) {
    const data = await this.redis.get(`ticket:${id}`);
    if (!data) return { success: false, error: 'Ticket not found' };
    const ticket = Ticket.create(JSON.parse(data));
    return this.routingService.routeTicket(ticket, dto.strategy as RoutingStrategy);
  }

  @Get(':id') async get(@Param('id') id: string) {
    const data = await this.redis.get(`ticket:${id}`);
    return data ? JSON.parse(data) : { success: false, error: 'Not found' };
  }

  @Post('bulk-route') async bulkRoute(@Body() body: { ticketIds: string[]; strategy?: string }) {
    const tickets: Ticket[] = [];
    for (const id of body.ticketIds || []) {
      const data = await this.redis.get(`ticket:${id}`);
      if (data) tickets.push(Ticket.create(JSON.parse(data)));
    }
    return this.routingService.bulkRoute(tickets, body.strategy as RoutingStrategy);
  }

  @Post(':id/reassign') async reassign(@Param('id') id: string, @Body() dto: ReassignTicketDto) {
    return this.routingService.reassignTicket({ ticketId: id, currentAgentId: dto.currentAgentId, reason: dto.reason, force: dto.force || false });
  }
}

@Controller('api/queue')
@UseInterceptors(TransformInterceptor)
export class QueueController {
  constructor(private readonly routingService: RoutingInputPort) {}
  @Post('process') async process(@Body() body: { department?: string }) { await this.routingService.processQueue(body.department); return { success: true }; }
}

@Controller('api/routing')
@UseInterceptors(TransformInterceptor)
export class RoutingController {
  constructor(private readonly routingService: RoutingInputPort) {}
  @Get('stats') async stats() { return this.routingService.getRoutingStats(); }
  @Post('reassign') async reassign(@Body() body: any) { return this.routingService.reassignTicket(body); }
}

@Controller('health')
export class HealthController {
  @Get() check() { return { success: true, service: 'ticket-routing-service', status: 'healthy', timestamp: new Date().toISOString() }; }
}
