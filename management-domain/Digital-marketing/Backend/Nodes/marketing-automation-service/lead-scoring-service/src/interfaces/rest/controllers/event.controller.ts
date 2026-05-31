import { Controller, Post, Body, Req } from '@nestjs/common';
import { ScoringOrchestrationService } from '../../../application/services/scoring-orchestration.service';

@Controller('api/v1/events')
export class EventController {
  constructor(private readonly service: ScoringOrchestrationService) {}

  @Post()
  async submitEvent(@Body() body: { eventType: string; leadId: string; eventData: Record<string, any> }, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.processEvent(body.eventType, body.leadId, body.eventData, tenantId);
  }

  @Post('batch')
  async submitBatch(@Body() body: { events: Array<{ eventType: string; leadId: string; eventData: Record<string, any> }> }, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    const results = [];
    for (const event of body.events) {
      results.push(await this.service.processEvent(event.eventType, event.leadId, event.eventData, tenantId));
    }
    return results;
  }
}
