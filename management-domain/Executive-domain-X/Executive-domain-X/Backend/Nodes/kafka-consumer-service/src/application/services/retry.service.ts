import {Injectable, Logger, OnModuleInit, OnModuleDestroy, Inject} from '@nestjs/common';
import { IDeadLetterQueueRepository } from '../../domain/repositories/dead-letter-queue.interface';
import { EventProcessorService } from './event-processor.service';

@Injectable()
export class RetryService implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(RetryService.name);
  private isRunning = false;
  private interval: ReturnType<typeof setInterval> | null = null;
  private retryInterval = parseInt(process.env.DLQ_RETRY_INTERVAL || '60000');
  private batchSize = parseInt(process.env.DLQ_RETRY_BATCH_SIZE || '10');
  private processingStats = { totalProcessed: 0, successfulRetries: 0, failedRetries: 0, lastRunTime: null as Date | null };

  constructor(
    @Inject('IDeadLetterQueueRepository')
    private readonly dlqRepository: IDeadLetterQueueRepository,
    private readonly eventProcessor: EventProcessorService,
  ) {}

  async onModuleInit() { await this.start(); }
  async onModuleDestroy() { await this.stop(); }

  async start() {
    if (this.isRunning) return;
    this.isRunning = true;
    this.interval = setInterval(async () => { try { await this.runRetryCycle(); } catch (e) { this.logger.error('Retry cycle error:', e); } }, this.retryInterval);
    this.logger.log(`DLQ retry service started (interval: ${this.retryInterval}ms)`);
  }

  async stop() {
    this.isRunning = false;
    if (this.interval) { clearInterval(this.interval); this.interval = null; }
  }

  async runRetryCycle() {
    this.processingStats.lastRunTime = new Date();
    const events = await this.dlqRepository.getRetryableEvents(undefined, this.batchSize);
    if (events.length === 0) return;
    this.logger.log(`Processing ${events.length} events from DLQ`);
    for (const event of events) {
      try {
        const result = await this.eventProcessor.processEvent({ eventId: event.props.originalEventId, eventType: event.props.eventType, tenantId: event.props.tenantId, payload: event.props.payload, metadata: event.props.metadata });
        if (result.success) { await this.dlqRepository.markAsResolved(event.props.dlqEventId); this.processingStats.successfulRetries++; }
        else { await this.dlqRepository.markForRetry(event.props.dlqEventId); this.processingStats.failedRetries++; }
      } catch (e) { this.logger.error(`Error retrying event ${event.props.dlqEventId}:`, e); await this.dlqRepository.markForRetry(event.props.dlqEventId); this.processingStats.failedRetries++; }
      this.processingStats.totalProcessed++;
    }
  }

  getStats() { return { isRunning: this.isRunning, retryInterval: this.retryInterval, batchSize: this.batchSize, ...this.processingStats }; }
}
