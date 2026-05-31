import { AggregateRoot } from '@shared/base/base.entity';
import { LogCollectionType } from '../enums/log-collection-type.enum';
import { LogSourceType } from '../enums/log-source-type.enum';
import { LogFormat } from '../enums/log-format.enum';
import { SourceStatus } from '../enums/source-status.enum';

export class LogSource extends AggregateRoot {
  constructor(
    public name: string,
    public description: string,
    public enabled: boolean,
    public type: LogCollectionType,
    public sourceType: LogSourceType,
    public config: Record<string, unknown>,
    public parsing: { enabled: boolean; format: LogFormat; timestampFormat?: string; fieldMapping?: Record<string, string> },
    public retention: { enabled: boolean; days: number; archive: boolean; archiveLocation?: string },
    public status: SourceStatus,
    public lastCollectedAt?: Date,
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }

  activate(): void {
    this.status = SourceStatus.ACTIVE;
    this.lastCollectedAt = new Date();
  }

  markError(): void {
    this.status = SourceStatus.ERROR;
  }

  disable(): void {
    this.enabled = false;
    this.status = SourceStatus.INACTIVE;
  }
}
