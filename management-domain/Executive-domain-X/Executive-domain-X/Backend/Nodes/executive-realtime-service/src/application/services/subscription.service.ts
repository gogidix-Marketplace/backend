import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { IPubSubService } from '../../domain/ports/output/pub-sub.interface';
import { SubscriptionChannel } from '../../domain/enums/subscription-channel.enum';

@Injectable()
export class SubscriptionService implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(SubscriptionService.name);

  constructor(private readonly pubSubService: IPubSubService) {}

  async onModuleInit() { await this.setupSubscriptions(); }

  async onModuleDestroy() { await this.cleanupSubscriptions(); }

  private async setupSubscriptions() {
    try {
      await this.pubSubService.subscribe(SubscriptionChannel.KPI_UPDATE, this.handleKpiUpdate.bind(this));
      await this.pubSubService.subscribe(SubscriptionChannel.KPI_ALERT, this.handleKpiAlert.bind(this));
      await this.pubSubService.subscribe(SubscriptionChannel.EXECUTIVE_DASHBOARD, this.handleDashboardUpdate.bind(this));
      this.logger.log('Redis pub/sub subscriptions established');
    } catch (error) { this.logger.error('Failed to setup subscriptions:', error); }
  }

  private handleKpiUpdate(data: any, _channel: string) { this.logger.debug(`KPI update: tenant=${data.tenantId}`); }
  private handleKpiAlert(data: any, _channel: string) { this.logger.debug(`KPI alert: tenant=${data.tenantId}`); }
  private handleDashboardUpdate(data: any, _channel: string) { this.logger.debug(`Dashboard update: tenant=${data.tenantId}`); }

  private async cleanupSubscriptions() {
    try {
      await this.pubSubService.unsubscribe(SubscriptionChannel.KPI_UPDATE);
      await this.pubSubService.unsubscribe(SubscriptionChannel.KPI_ALERT);
      await this.pubSubService.unsubscribe(SubscriptionChannel.EXECUTIVE_DASHBOARD);
    } catch (error) { this.logger.error('Error cleaning up subscriptions:', error); }
  }
}
