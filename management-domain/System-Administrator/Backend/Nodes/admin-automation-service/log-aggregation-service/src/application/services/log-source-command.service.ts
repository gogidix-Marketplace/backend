import { Injectable, Logger } from '@nestjs/common';
import { ILogSourceCommand } from '@domain/ports/input/log-source.command';
import { ILogSourceRepository } from '@domain/repositories/log-source-repository.interface';
import { LogSource } from '@domain/models/log-source.entity';
import { SourceStatus } from '@domain/enums/source-status.enum';
import { LogCollectionType } from '@domain/enums/log-collection-type.enum';
import { LogSourceType } from '@domain/enums/log-source-type.enum';
import { LogFormat } from '@domain/enums/log-format.enum';

@Injectable()
export class LogSourceCommandService implements ILogSourceCommand {
  private readonly logger = new Logger(LogSourceCommandService.name);

  constructor(private readonly sourceRepository: ILogSourceRepository) {}

  async createSource(data: any): Promise<any> {
    const source = new LogSource(
      data.name, data.description, data.enabled ?? true,
      data.type as LogCollectionType, data.sourceType as LogSourceType,
      data.config,
      data.parsing || { enabled: true, format: LogFormat.JSON },
      data.retention || { enabled: true, days: 30, archive: false },
      SourceStatus.ACTIVE,
    );
    return this.sourceRepository.save(source);
  }

  async updateSource(id: string, data: any): Promise<any> {
    return this.sourceRepository.findByIdAndUpdate(id, data);
  }

  async deleteSource(id: string): Promise<void> {
    await this.sourceRepository.findByIdAndDelete(id);
  }

  async enableSource(id: string): Promise<any> {
    const source = await this.sourceRepository.findByIdAndUpdate(id, { enabled: true, status: SourceStatus.ACTIVE });
    return source;
  }

  async disableSource(id: string): Promise<any> {
    const source = await this.sourceRepository.findByIdAndUpdate(id, { enabled: false, status: SourceStatus.INACTIVE });
    return source;
  }
}
