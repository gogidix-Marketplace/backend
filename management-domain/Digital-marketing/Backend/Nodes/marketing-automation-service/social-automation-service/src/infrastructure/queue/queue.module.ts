import { Module } from '@nestjs/common';
import { QUEUE_SERVICE_PORT } from '../../../domain/ports/services/queue-service.port';
import { BullQueueAdapter } from './bull-queue.adapter';

@Module({
  providers: [{ provide: 'QUEUE_SERVICE_PORT' useClass: BullQueueAdapter }],
  exports: [QUEUE_SERVICE_PORT],
})
export class QueueModule {}
