import { Injectable, Logger } from '@nestjs/common';
import { Cron, CronExpression } from '@nestjs/schedule';
import { ApplyScoreDecayHandler } from '../../application/handlers/command-handlers/apply-score-decay.handler';
import { ApplyScoreDecayCommand } from '../../application/commands/apply-score-decay.command';

@Injectable()
export class ScoreDecayScheduler {
  private readonly logger = new Logger(ScoreDecayScheduler.name);
  private readonly tenantIds: string[] = [];

  constructor(
    private readonly applyScoreDecayHandler: ApplyScoreDecayHandler,
  ) {
    // Load tenant IDs from environment or configuration
    if (process.env.TENANT_IDS) {
      this.tenantIds = process.env.TENANT_IDS.split(',');
    }
  }

  @Cron(CronExpression.EVERY_DAY_AT_MIDNIGHT)
  async handleDailyScoreDecay(): Promise<void> {
    this.logger.log('Starting daily score decay process...');

    for (const tenantId of this.tenantIds) {
      try {
        const command = new ApplyScoreDecayCommand(tenantId);
        await this.applyScoreDecayHandler.execute(command);
        this.logger.log(`Score decay applied for tenant: ${tenantId}`);
      } catch (error) {
        this.logger.error(`Failed to apply score decay for tenant ${tenantId}`, error);
      }
    }

    this.logger.log('Daily score decay process completed');
  }

  @Cron(CronExpression.EVERY_HOUR)
  async handleHourlyScoreDecay(): Promise<void> {
    this.logger.debug('Starting hourly score decay check...');

    for (const tenantId of this.tenantIds) {
      try {
        const command = new ApplyScoreDecayCommand(tenantId, 0.05, 7);
        await this.applyScoreDecayHandler.execute(command);
        this.logger.debug(`Score decay applied for tenant: ${tenantId}`);
      } catch (error) {
        this.logger.error(`Failed to apply score decay for tenant ${tenantId}`, error);
      }
    }
  }
}
