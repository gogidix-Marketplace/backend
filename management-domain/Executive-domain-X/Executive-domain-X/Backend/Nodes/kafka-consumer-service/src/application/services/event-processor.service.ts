import { Injectable, Logger } from '@nestjs/common';
import { DomainEvent, EventProps } from '../../domain/models/event.entity';
import { IEventStoreRepository } from '../../domain/repositories/event-store.interface';

@Injectable()
export class EventProcessorService {
  private readonly logger = new Logger(EventProcessorService.name);

  constructor(private readonly eventStore: IEventStoreRepository) {}

  async processEvent(eventData: any): Promise<{ success: boolean; message: string }> {
    this.logger.log(`Processing event: ${eventData.eventType} for aggregate: ${eventData.aggregateId}`);

    const event = new DomainEvent({
      eventId: eventData.eventId || crypto.randomUUID(),
      eventType: eventData.eventType,
      aggregateId: eventData.aggregateId,
      aggregateType: eventData.aggregateType || this.inferAggregateType(eventData.eventType),
      tenantId: eventData.tenantId || 'default',
      payload: eventData.payload || eventData.data || {},
      metadata: eventData.metadata || {},
      correlationId: eventData.correlationId,
      version: 1,
      status: 'PENDING',
      retryCount: 0,
      timestamp: new Date(),
      createdAt: new Date(),
    } as EventProps);

    await this.eventStore.save(event);

    switch (eventData.eventType) {
      case 'EXECUTIVE_CREATED': return this.processExecutiveCreated(eventData);
      case 'EXECUTIVE_UPDATED': return this.processExecutiveUpdated(eventData);
      case 'KPI_UPDATED': return this.processKpiUpdated(eventData);
      case 'STRATEGY_UPDATED': return this.processStrategyUpdated(eventData);
      case 'APPROVAL_CREATED': return this.processApprovalCreated(eventData);
      case 'DECISION_MADE': return this.processDecisionMade(eventData);
      default: return { success: false, message: `Unknown event type: ${eventData.eventType}` };
    }
  }

  private async processExecutiveCreated(data: any) { this.logger.log(`Executive created: ${data.aggregateId}`); return { success: true, message: 'Executive created successfully' }; }
  private async processExecutiveUpdated(data: any) { this.logger.log(`Executive updated: ${data.aggregateId}`); return { success: true, message: 'Executive updated successfully' }; }
  private async processKpiUpdated(data: any) { this.logger.log(`KPI updated: ${data.aggregateId}`); return { success: true, message: 'KPI updated successfully' }; }
  private async processStrategyUpdated(data: any) { this.logger.log(`Strategy updated: ${data.aggregateId}`); return { success: true, message: 'Strategy updated successfully' }; }
  private async processApprovalCreated(data: any) { this.logger.log(`Approval created: ${data.aggregateId}`); return { success: true, message: 'Approval created successfully' }; }
  private async processDecisionMade(data: any) { this.logger.log(`Decision made: ${data.aggregateId}`); return { success: true, message: 'Decision processed successfully' }; }

  private inferAggregateType(eventType: string): string {
    const map: Record<string, string> = { EXECUTIVE_CREATED: 'EXECUTIVE', EXECUTIVE_UPDATED: 'EXECUTIVE', KPI_UPDATED: 'KPI', STRATEGY_UPDATED: 'STRATEGY', APPROVAL_CREATED: 'APPROVAL', DECISION_MADE: 'DECISION' };
    return map[eventType] || 'UNKNOWN';
  }

  async getProcessingStats(tenantId?: string) { return this.eventStore.getStats(tenantId); }
}

