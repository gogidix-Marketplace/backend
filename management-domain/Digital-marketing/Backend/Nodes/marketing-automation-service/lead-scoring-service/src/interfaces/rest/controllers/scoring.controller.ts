import { Controller, Get, Post, Put, Delete, Body, Param, Query, Req } from '@nestjs/common';
import { ScoringOrchestrationService } from '../../../application/services/scoring-orchestration.service';
import { CreateScoringRuleDto, UpdateScoringRuleDto } from '../../../application/dtos/scoring-rule.dto';
import { CreateSegmentDto, UpdateSegmentDto } from '../../../application/dtos/segment.dto';

@Controller('api/v1/scoring')
export class ScoringController {
  constructor(private readonly service: ScoringOrchestrationService) {}

  @Get('scores')
  async getScores(@Req() req: any, @Query('minScore') minScore?: string, @Query('maxScore') maxScore?: string) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.calculateScore({ leadId: '', eventData: {} }, tenantId).catch(() => []);
  }

  @Get('scores/:leadId')
  async getScore(@Param('leadId') leadId: string, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.calculateScore({ leadId, eventData: {} }, tenantId);
  }

  @Post('rules')
  async createRule(@Body() dto: CreateScoringRuleDto, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.createRule(dto, tenantId);
  }

  @Get('rules')
  async getRules(@Req() req: any, @Query('category') category?: string) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.getRules(tenantId, category);
  }

  @Get('rules/:id')
  async getRule(@Param('id') id: string) { return this.service.getRule(id); }

  @Put('rules/:id')
  async updateRule(@Param('id') id: string, @Body() dto: UpdateScoringRuleDto) { return this.service.updateRule(id, dto); }

  @Delete('rules/:id')
  async deleteRule(@Param('id') id: string) { return this.service.deleteRule(id); }

  @Post('segments')
  async createSegment(@Body() dto: CreateSegmentDto, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.createSegment(dto, tenantId);
  }

  @Get('segments')
  async getSegments(@Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.getSegments(tenantId);
  }

  @Get('segments/:id')
  async getSegment(@Param('id') id: string) { return this.service.getSegment(id); }

  @Put('segments/:id')
  async updateSegment(@Param('id') id: string, @Body() dto: UpdateSegmentDto) { return this.service.updateSegment(id, dto); }

  @Delete('segments/:id')
  async deleteSegment(@Param('id') id: string) { return this.service.deleteSegment(id); }
}
