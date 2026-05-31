import {Injectable, Logger, Inject} from '@nestjs/common';
import { ILogIngestionCommand } from '@domain/ports/input/log-ingestion.command';
import { ILogSearch } from '@domain/ports/output/log-search.interface';
import { IEventPublisher } from '@domain/ports/output/event-publisher.interface';
import { LogEntry } from '@domain/models/log-entry.entity';
import { LogLevel } from '@domain/enums/log-level.enum';
import { LogSourceType } from '@domain/enums/log-source-type.enum';
import { LogIngestedEvent } from '@domain/events/log-ingested.event';
import * as os from 'os';

@Injectable()
export class LogIngestionService implements ILogIngestionCommand {
  private readonly logger = new Logger(LogIngestionService.name);

  constructor(
    @Inject('ILogSearch')
    private readonly logSearch: ILogSearch,
    @Inject('IEventPublisher')
    private readonly eventPublisher: IEventPublisher,
  ) {}

  async ingestLog(logData: any, sourceName: string): Promise<void> {
    const logEntry = new LogEntry(
      new Date(logData.timestamp || Date.now()),
      (logData.level || 'info') as LogLevel,
      logData.message || '',
      {
        service: sourceName,
        host: logData.host || os.hostname(),
        type: (logData.sourceType || 'application') as LogSourceType,
        component: logData.component,
        environment: logData.environment,
      },
      logData.context,
      logData.tags || [sourceName],
      logData.correlationId,
      logData.userId,
      logData.requestId,
      logData.stackTrace,
      logData.metadata,
    );

    const logId = await this.logSearch.indexLog(logEntry);
    await this.eventPublisher.publish(new LogIngestedEvent(logId, sourceName, logEntry.level));
  }

  async ingestBulkLogs(logs: any[], sourceName: string): Promise<{ success: number; failed: number }> {
    let success = 0;
    let failed = 0;
    for (const logData of logs) {
      try {
        await this.ingestLog(logData, sourceName);
        success++;
      } catch (error) {
        this.logger.error('Error ingesting log', error);
        failed++;
      }
    }
    return { success, failed };
  }
}
