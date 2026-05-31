import { Module } from '@nestjs/common';
import { SCHEDULER_SERVICE_PORT } from '../../../domain/ports/services/scheduler-service.port';
import { CronSchedulerAdapter } from './cron-scheduler.adapter';

@Module({
  providers: [{ provide: 'SCHEDULER_SERVICE_PORT' useClass: CronSchedulerAdapter }],
  exports: [SCHEDULER_SERVICE_PORT],
})
export class SchedulerModule {}
