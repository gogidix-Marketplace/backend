import { BaseEntity } from '@shared/base/base.entity';
import { LogLevel } from '../enums/log-level.enum';
import { LogSourceType } from '../enums/log-source-type.enum';

export class LogEntry extends BaseEntity {
  constructor(
    public timestamp: Date,
    public level: LogLevel,
    public message: string,
    public source: {
      service: string;
      host: string;
      type: LogSourceType;
      component?: string;
      environment?: string;
    },
    public context?: Record<string, unknown>,
    public tags?: string[],
    public correlationId?: string,
    public userId?: string,
    public requestId?: string,
    public stackTrace?: string,
    public metadata?: Record<string, unknown>,
    public raw?: string,
    props?: { id?: string; createdAt?: Date; updatedAt?: Date },
  ) {
    super(props);
  }
}
