import {Injectable, Logger, Inject} from '@nestjs/common';
import { EventBus } from '@nestjs/cqrs';
import { InjectQueue } from '@nestjs/bull';
import { Queue } from 'bull';
import { IPayrollCommand } from '@domain/ports/input/payroll.command';
import { IEventPublisher } from '@domain/ports/output/event-publisher.interface';
import { PayrollStartedEvent } from '@domain/events/payroll-started.event';

@Injectable()
export class PayrollCommandService implements IPayrollCommand {
  private readonly logger = new Logger(PayrollCommandService.name);

  constructor(
    @InjectQueue('payroll-processing') private readonly payrollQueue: Queue,
    private readonly eventBus: EventBus,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
  ) {}

  async submitPayrollJob(tenantId: string, employeeIds: string[], periodStart: Date, periodEnd: Date, priority = 5): Promise<{ jobId: string; payrollId: string }> {
    const payrollId = `payroll-${tenantId}-${Date.now()}`;

    const job = await this.payrollQueue.add('process-payroll', {
      tenantId,
      payrollId,
      employeeIds,
      periodStart,
      periodEnd,
    }, {
      priority,
      delay: 0,
      attempts: 3,
      backoff: { type: 'exponential', delay: 2000 },
      removeOnComplete: 100,
      removeOnFail: 50,
    });

    this.logger.log(`Payroll job created: ${job.id} for payroll: ${payrollId}`);

    await this.eventPublisher.publish(new PayrollStartedEvent(
      payrollId, tenantId, employeeIds, periodStart, periodEnd,
    ));

    return { jobId: String(job.id), payrollId };
  }

  async cancelPayrollJob(jobId: string): Promise<void> {
    const job = await this.payrollQueue.getJob(jobId);
    if (job) {
      await job.remove();
      this.logger.log(`Cancelled payroll job: ${jobId}`);
    }
  }

  async getJobStatus(jobId: string): Promise<any> {
    const job = await this.payrollQueue.getJob(jobId);
    if (!job) return null;

    const state = await job.getState();
    return {
      jobId: job.id,
      state,
      progress: job.progress(),
      data: job.data,
      processedOn: job.processedOn,
      finishedOn: job.finishedOn,
      failedReason: job.failedReason,
    };
  }

  async getQueueStats(): Promise<{ waiting: number; active: number; completed: number; failed: number }> {
    const [waiting, active, completed, failed] = await Promise.all([
      this.payrollQueue.getWaitingCount(),
      this.payrollQueue.getActiveCount(),
      this.payrollQueue.getCompletedCount(),
      this.payrollQueue.getFailedCount(),
    ]);
    return { waiting, active, completed, failed };
  }
}
