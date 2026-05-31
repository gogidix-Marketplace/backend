import { Global, Module } from '@nestjs/common';
import { LoggerService } from '@shared/logging/logger.service';
import { ConfigService } from '@nestjs/config';

@Global()
@Module({
  providers: [
    {
      provide: LoggerService,
      useFactory: (configService: ConfigService) => {
        return new LoggerService(
          configService.get<string>('config.logging.level', 'info'),
          configService.get<string>('config.logging.filePath', 'logs'),
        );
      },
      inject: [ConfigService],
    },
  ],
  exports: [LoggerService],
})
export class LoggerModule {}
