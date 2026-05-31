import { Controller, Get, Post, Put, Delete, Patch, Body, Param, UseInterceptors } from '@nestjs/common';
import { AgentManagementService } from '@application/services';
import { CreateAgentDto, UpdateAgentDto, UpdateAgentStatusDto } from '@application/dto';
import { TransformInterceptor } from '@shared/interceptors';
import { AgentManagementInputPort } from '@domain/ports/input';

@Controller('api/v1/agents')
@UseInterceptors(TransformInterceptor)
export class AgentController {
  constructor(private readonly agentService: AgentManagementInputPort) {}

  @Post()
  async createAgent(@Body() dto: CreateAgentDto) {
    return this.agentService.createAgent(dto);
  }

  @Get()
  async getAllAgents() {
    return this.agentService.getAllAgents();
  }

  @Get('available')
  async getAvailableAgents() {
    return this.agentService.getAvailableAgents();
  }

  @Get(':id')
  async getAgent(@Param('id') id: string) {
    return this.agentService.getAgent(id);
  }

  @Put(':id')
  async updateAgent(@Param('id') id: string, @Body() dto: UpdateAgentDto) {
    return this.agentService.getAgent(id);
  }

  @Patch(':id/status')
  async updateStatus(@Param('id') id: string, @Body() dto: UpdateAgentStatusDto) {
    return this.agentService.updateAgentStatus(id, dto.status);
  }

  @Delete(':id')
  async deleteAgent(@Param('id') id: string) {
    return this.agentService.deleteAgent(id);
  }

  @Get(':id/stats')
  async getAgentStats(@Param('id') id: string) {
    return this.agentService.getAgentStats(id);
  }
}
