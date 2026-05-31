import { Controller, Get } from '@nestjs/common';
import { ApiTags, ApiBearerAuth } from '@nestjs/swagger';
import { EventProcessorService } from '../../application/services/event-processor.service';
import { RetryService } from '../../application/services/retry.service';

@ApiTags('events')
@ApiBearerAuth()
@Controller('events')
export class EventController {
  constructor(private readonly eventProcessor: EventProcessorService, private readonly retryService: RetryService) {}

  @Get('stats')
  getStats() { return this.eventProcessor.getProcessingStats(); }

  @Get('retry/stats')
  getRetryStats() { return this.retryService.getStats(); }
}
