import { Injectable, Logger } from '@nestjs/common';
import { Cron, CronExpression } from '@nestjs/schedule';
import { ReconciliationCommandService } from '../../application/services/reconciliation-command.service';
import { StartReconciliationCommand } from '../../domain/ports/input/reconciliation.command';
import { ReconciliationRepository } from '../persistence/mongodb/repositories/reconciliation.repository.impl';

@Injectable()
export class SchedulerService {
  private readonly logger = new Logger(SchedulerService.name);

  constructor(
    private readonly reconciliationRepository: ReconciliationRepository,
    private readonly reconciliationCommandService: ReconciliationCommandService,
  ) {}

  @Cron(CronExpression.EVERY_MINUTE)
  async handleScheduledReconciliations(): Promise<void> {
    try {
      this.logger.debug('Checking for scheduled reconciliations...');

      const scheduledReconciliations = await this.reconciliationRepository.findScheduledReconciliations();

      for (const reconciliation of scheduledReconciliations) {
        try {
          this.logger.log(`Starting scheduled reconciliation: ${reconciliation.id}`);

          await this.reconciliationCommandService.startReconciliation(
            new StartReconciliationCommand(
              reconciliation.tenantId,
              reconciliation.name,
              reconciliation.props.dataSourceType,
              reconciliation.props.createdBy,
              reconciliation.props.ruleIds,
              reconciliation.props.bankAccountId,
              reconciliation.props.internalAccountId,
              reconciliation.props.description,
              new Date(),
              undefined,
              reconciliation.props.metadata,
            ),
          );

          this.logger.log(`Scheduled reconciliation started successfully: ${reconciliation.id}`);
        } catch (error) {
          this.logger.error(`Failed to start scheduled reconciliation: ${reconciliation.id}`, error.stack);
        }
      }
    } catch (error) {
      this.logger.error('Error in scheduled reconciliation check', error.stack);
    }
  }

  @Cron(CronExpression.EVERY_HOUR)
  async checkOverdueDifferences(): Promise<void> {
    this.logger.debug('Checking for overdue differences...');
  }

  @Cron(CronExpression.EVERY_DAY_AT_MIDNIGHT)
  async generateDailyReport(): Promise<void> {
    this.logger.log('Generating daily reconciliation report...');
  }
}
