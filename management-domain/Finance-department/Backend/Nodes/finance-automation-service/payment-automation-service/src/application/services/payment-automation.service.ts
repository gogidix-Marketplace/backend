import { Injectable, Logger } from '@nestjs/common';
import { Cron, CronExpression } from '@nestjs/schedule';
import { Payment, PaymentStatus, PaymentBatch } from '../../domain/models';
import {
  PaymentRepository,
  PaymentBatchRepository,
  EventPublisher,
} from '../../domain/ports';
import {
  PaymentInitiatedEvent,
  PaymentCompletedEvent,
  PaymentFailedEvent,
} from '../../domain/events';
import { ExecutionContextImpl } from '@shared/context';

@Injectable()
export class PaymentAutomationService {
  private readonly logger = new Logger(PaymentAutomationService.name);
  private readonly maxConcurrentProcessing = 10;

  constructor(
    private readonly paymentRepository: PaymentRepository,
    private readonly batchRepository: PaymentBatchRepository,
    private readonly eventPublisher: EventPublisher,
  ) {}

  @Cron(CronExpression.EVERY_HOUR)
  async processScheduledPayments(): Promise<void> {
    this.logger.log('Starting scheduled payment processing...');
    try {
      const now = new Date();
      const scheduledPayments = await this.paymentRepository.findScheduled(now);

      this.logger.log(`Found ${scheduledPayments.length} scheduled payments to process`);

      for (const payment of scheduledPayments) {
        try {
          payment.process();
          await this.paymentRepository.save(payment);

          const context = new ExecutionContextImpl(
            payment.tenantId,
            'system',
            undefined,
            `scheduled-${payment.id}`,
          );

          const event = PaymentInitiatedEvent.fromPayment(payment, context.correlationId);
          await this.eventPublisher.publish(event);

          this.logger.log(`Payment ${payment.id} marked for processing`);
        } catch (error) {
          this.logger.error(
            `Error processing scheduled payment ${payment.id}: ${error.message}`,
          );
        }
      }

      this.logger.log('Completed scheduled payment processing');
    } catch (error) {
      this.logger.error(`Error in scheduled payment processing: ${error.message}`);
    }
  }

  @Cron(CronExpression.EVERY_5_MINUTES)
  async processScheduledBatches(): Promise<void> {
    this.logger.log('Starting scheduled batch processing...');
    try {
      const now = new Date();
      const pendingBatches = await this.batchRepository.findPending();

      const dueBatches = pendingBatches.filter(
        (b) => !b.scheduledAt || b.scheduledAt <= now,
      );

      this.logger.log(`Found ${dueBatches.length} scheduled batches to process`);

      for (const batch of dueBatches) {
        try {
          this.logger.log(`Starting batch ${batch.id} processing`);
          batch.startProcessing();
          await this.batchRepository.save(batch);

          await this.processBatchPayments(batch);
        } catch (error) {
          this.logger.error(`Error processing batch ${batch.id}: ${error.message}`);
          batch.fail();
          await this.batchRepository.save(batch);
        }
      }

      this.logger.log('Completed scheduled batch processing');
    } catch (error) {
      this.logger.error(`Error in scheduled batch processing: ${error.message}`);
    }
  }

  @Cron(CronExpression.EVERY_DAY_AT_MIDNIGHT)
  async retryFailedPayments(): Promise<void> {
    this.logger.log('Starting failed payment retry processing...');
    try {
      const retryablePayments = await this.paymentRepository.findFailed(true);

      this.logger.log(`Found ${retryablePayments.length} retryable payments`);

      for (const payment of retryablePayments) {
        try {
          if (payment.canRetry()) {
            payment.retry();
            await this.paymentRepository.save(payment);

            const context = new ExecutionContextImpl(
              payment.tenantId,
              'system',
              `retry-${payment.id}`,
            );

            const event = PaymentInitiatedEvent.fromPayment(payment, context.correlationId);
            await this.eventPublisher.publish(event);

            this.logger.log(`Payment ${payment.id} queued for retry (${payment.retryCount}/${payment.maxRetries})`);
          }
        } catch (error) {
          this.logger.error(`Error retrying payment ${payment.id}: ${error.message}`);
        }
      }

      this.logger.log('Completed failed payment retry processing');
    } catch (error) {
      this.logger.error(`Error in failed payment retry processing: ${error.message}`);
    }
  }

  @Cron(CronExpression.EVERY_DAY_AT_1AM)
  async cleanupOldPayments(): Promise<void> {
    this.logger.log('Starting old payment cleanup...');
    try {
      const cutoffDate = new Date();
      cutoffDate.setFullYear(cutoffDate.getFullYear() - 1);

      const allPayments = await this.paymentRepository.findAll();
      const oldCompletedPayments = allPayments.filter(
        (p) => p.isCompleted() && p.completedAt && p.completedAt < cutoffDate,
      );

      this.logger.log(`Found ${oldCompletedPayments.length} old completed payments for archival`);

      // This would trigger an archival process
      // For now, just log the findings

      this.logger.log('Completed old payment cleanup');
    } catch (error) {
      this.logger.error(`Error in old payment cleanup: ${error.message}`);
    }
  }

  private async processBatchPayments(batch: PaymentBatch): Promise<void> {
    const paymentIds = batch.paymentIds;
    const maxConcurrent = Math.min(batch.maxConcurrent, this.maxConcurrentProcessing);

    for (let i = 0; i < paymentIds.length; i += maxConcurrent) {
      const chunk = paymentIds.slice(i, i + maxConcurrent);
      await Promise.all(
        chunk.map(async (paymentId) => {
          const payment = await this.paymentRepository.findById(paymentId);
          if (!payment) return;

          try {
            payment.process();
            await this.paymentRepository.save(payment);

            const context = new ExecutionContextImpl(
              payment.tenantId,
              'system',
              `batch-${batch.id}`,
            );

            const event = PaymentInitiatedEvent.fromPayment(payment, context.correlationId);
            await this.eventPublisher.publish(event);
          } catch (error) {
            this.logger.error(`Error processing payment ${paymentId}: ${error.message}`);
          }
        }),
      );
    }

    const context = new ExecutionContextImpl(batch.tenantId, 'system', `batch-${batch.id}`);
    const summary = batch.getSummary(0, 0);

    this.logger.log(
      `Batch ${batch.id} processing initiated. Total payments: ${summary.totalPayments}`,
    );
  }

  async processPendingAutomationRules(payment: Payment): Promise<void> {
    this.logger.log(`Processing automation rules for payment ${payment.id}`);
    // Automation rules would be evaluated here
    // This is a placeholder for rule evaluation logic
  }

  async reconcilePayment(paymentId: string, externalData: any): Promise<void> {
    const payment = await this.paymentRepository.findById(paymentId);
    if (!payment) {
      throw new Error(`Payment ${paymentId} not found`);
    }

    // Reconciliation logic would go here
    // Compare payment data with external data

    payment.markAsReconciled(true);
    await this.paymentRepository.save(payment);

    this.logger.log(`Payment ${paymentId} reconciled successfully`);
  }

  async getAutomationMetrics(): Promise<{
    scheduledPaymentsProcessed: number;
    batchesProcessed: number;
    retriedPayments: number;
    successRate: number;
  }> {
    const pendingPayments = await this.paymentRepository.findPending();
    const processingPayments = await this.paymentRepository.findByStatus(
      PaymentStatus.PROCESSING,
    );
    const completedPayments = await this.paymentRepository.findByStatus(
      PaymentStatus.COMPLETED,
    );
    const failedPayments = await this.paymentRepository.findByStatus(PaymentStatus.FAILED);

    const total = completedPayments.length + failedPayments.length;
    const successRate = total > 0 ? (completedPayments.length / total) * 100 : 0;

    return {
      scheduledPaymentsProcessed: processingPayments.length,
      batchesProcessed: await this.batchRepository.count({ status: 'PROCESSING' }),
      retriedPayments: failedPayments.filter((p) => p.retryCount > 0).length,
      successRate,
    };
  }
}
