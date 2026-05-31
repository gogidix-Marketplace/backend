import { Controller, Post, Get, Body, Param, UseInterceptors, Logger } from '@nestjs/common';
import { RoutingService } from '@application/services';
import { RouteTicketDto, ReassignTicketDto } from '@application/dto';
import { TransformInterceptor } from '@shared/interceptors';
import { RoutingInputPort } from '@domain/ports/input';

@Controller('api/v1/routing')
@UseInterceptors(TransformInterceptor)
export class RoutingController {
  private readonly logger = new Logger(RoutingController.name);

  constructor(private readonly routingService: RoutingInputPort) {}

  @Post('route')
  async routeTicket(@Body() dto: RouteTicketDto) {
    return this.routingService.routeTicket({
      ticketId: dto.ticketId,
      priority: dto.priority,
      requiredSkills: dto.requiredSkills,
      customerId: dto.customerId,
      language: dto.language,
      teamId: dto.teamId,
      queueId: dto.queueId,
      metadata: dto.metadata,
    });
  }

  @Post('decision')
  async getRoutingDecision(@Body() dto: RouteTicketDto) {
    return this.routingService.getRoutingDecision({
      ticketId: dto.ticketId,
      priority: dto.priority,
      requiredSkills: dto.requiredSkills,
      customerId: dto.customerId,
    });
  }

  @Post('reassign')
  async reassignTicket(@Body() dto: ReassignTicketDto) {
    return this.routingService.reassignTicket(dto.currentAgentId, dto.currentAgentId, dto.reason);
  }

  @Get('stats')
  async getStats() {
    return this.routingService.getRoutingStats();
  }
}
