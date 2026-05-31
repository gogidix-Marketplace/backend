import { Controller, Get, Delete, Param, Query, UseInterceptors } from '@nestjs/common';
import { QueueManagementService } from '@application/services';
import { TransformInterceptor } from '@shared/interceptors';
import { QueueManagementInputPort } from '@domain/ports/input';

@Controller('api/v1/queues')
@UseInterceptors(TransformInterceptor)
export class QueueController {
  constructor(private readonly queueService: QueueManagementInputPort) {}

  @Get()
  async getQueues() {
    return this.queueService.getQueues();
  }

  @Get(':id')
  async getQueue(@Param('id') id: string) {
    return this.queueService.getQueue(id);
  }

  @Get(':id/stats')
  async getQueueStats(@Param('id') id: string) {
    return this.queueService.getQueueStats(id);
  }

  @Delete(':id')
  async clearQueue(@Param('id') id: string) {
    return this.queueService.clearQueue(id);
  }
}
