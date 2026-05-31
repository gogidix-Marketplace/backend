import {Injectable, Logger, Inject} from '@nestjs/common';
import { Cron } from '@nestjs/schedule';
import { ILogSearch } from '@domain/ports/output/log-search.interface';
import { ILogSourceRepository } from '@domain/repositories/log-source-repository.interface';
import { IEventPublisher } from '@domain/ports/output/event-publisher.interface';
import { RetentionAppliedEvent } from '@domain/events/retention-applied.event';

@Injectable()
export class LogRetentionService {
  private readonly logger = new Logger(LogRetentionService.name);

  constructor(
    @Inject('ILogSearch')
    private readonly logSearch: ILogSearch,
    @Inject('ILogSourceRepository')
    private readonly sourceRepository: ILogSourceRepository,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
  ) {}

  @Cron('0 3 * * *')
  async applyRetentionPolicies(): Promise<void> {
    this.logger.log('Applying log retention policies');
    const sources = await this.sourceRepository.findWithRetention();

    for (const source of sources) {
      try {
        const deletedIndices = await this.logSearch.deleteOldLogs(source.retention.days);
        await this.eventPublisher.publish(new RetentionAppliedEvent(source.name, deletedIndices));
        this.logger.log(`Retention applied for ${source.name}: ${deletedIndices} indices deleted`);
      } catch (error) {
        this.logger.error(`Error applying retention for ${source.name}`, error);
      }
    }
  }

  async updateRetentionPolicy(sourceId: string, retentionConfig: any): Promise<void> {
    await this.sourceRepository.findByIdAndUpdate(sourceId, { retention: retentionConfig });
  }
}
