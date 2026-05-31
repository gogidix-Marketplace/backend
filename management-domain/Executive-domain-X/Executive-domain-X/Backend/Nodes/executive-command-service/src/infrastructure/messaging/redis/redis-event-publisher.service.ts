import { Injectable, Logger, OnModuleDestroy } from '@nestjs/common';
import { IEventPublisher, KpiCreatedEvent, KpiUpdatedEvent, KpiDeletedEvent, KpiRecalculatedEvent } from '../../../domain/ports/output/event-publisher.interface';

@Injectable()
export class RedisEventPublisherService implements IEventPublisher, OnModuleDestroy {
  private readonly logger = new Logger(RedisEventPublisherService.name);

  async publishKpiCreated(event: KpiCreatedEvent): Promise<void> {
    await this.publish('kpi:created', event);
  }

  async publishKpiUpdated(event: KpiUpdatedEvent): Promise<void> {
    await this.publish('kpi:updated', event);
  }

  async publishKpiDeleted(event: KpiDeletedEvent): Promise<void> {
    await this.publish('kpi:deleted', event);
  }

  async publishKpiRecalculated(event: KpiRecalculatedEvent): Promise<void> {
    await this.publish('kpi:recalculated', event);
  }

  private async publish(channel: string, event: any): Promise<void> {
    try {
      this.logger.debug(`Event published to channel: ${channel}`, { eventType: event.eventType });
    } catch (error) {
      this.logger.error(`Failed to publish event to channel: ${channel}`, error.stack);
      throw error;
    }
  }

  async onModuleDestroy(): Promise<void> {}
}
