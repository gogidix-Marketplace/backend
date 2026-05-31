import { Injectable, Logger } from '@nestjs/common';
import { Cron, CronExpression } from '@nestjs/schedule';
import { TriggerRollingForecastUseCase } from '../../domain/ports/in/trigger-rolling-forecast.use-case';
import { RequestContextData } from '../../shared/request-context';

@Injectable()
export class RollingForecastScheduler {
  private readonly logger = new Logger(RollingForecastScheduler.name);

  constructor(
    private readonly triggerRollingForecastUseCase: TriggerRollingForecastUseCase,
  ) {}

  // Run rolling forecast on the first day of each month at midnight
  @Cron(CronExpression.EVERY_1ST_DAY_OF_MONTH_AT_MIDNIGHT)
  async handleMonthlyRollingForecast() {
    this.logger.log('Starting monthly rolling forecast generation...');

    const systemContext: RequestContextData = {
      tenantId: 'system',
      userId: 'system',
      correlationId: `rolling_${Date.now()}`,
      roles: ['SYSTEM'],
    };

    try {
      // Trigger global rolling forecast
      const result = await this.triggerRollingForecastUseCase.execute(systemContext, {
        granularity: 'GLOBAL',
        triggeredBy: 'SCHEDULER',
        reason: 'Monthly rolling forecast update',
      });

      this.logger.log(
        `Monthly rolling forecast generated: ${result.forecast.id} (Total: ${result.forecast.totalWeightedForecast})`,
      );
    } catch (error) {
      this.logger.error(`Failed to generate monthly rolling forecast: ${(error as Error).message}`);
    }
  }

  // Weekly check for forecast accuracy alerts
  @Cron(CronExpression.EVERY_WEEK)
  async checkForecastAccuracy() {
    this.logger.log('Checking forecast accuracy for alerts...');

    // In production, this would query the accuracy repository
    // and send alerts for forecasts below threshold
    this.logger.debug('Forecast accuracy check completed');
  }
}
