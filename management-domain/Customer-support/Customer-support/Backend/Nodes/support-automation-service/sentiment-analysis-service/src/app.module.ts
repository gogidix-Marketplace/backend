import { Module } from '@nestjs/common';
import { ConfigModule } from '@nestjs/config';
import { APP_INTERCEPTOR } from '@nestjs/core';
import configuration from '@infrastructure/config/configuration';
import { MessagingModule } from '@infrastructure/messaging/kafka';
import { LoggerModule } from '@shared/logging';
import { LoggingInterceptor, TransformInterceptor } from '@shared/interceptors';
import { SentimentController, HealthController } from '@interfaces/http/controllers/sentiment.controller';
import { SentimentService } from '@application/services';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, load: [configuration] }),
    LoggerModule,
    MessagingModule,
  ],
  controllers: [SentimentController, HealthController],
  providers: [
    SentimentService,
    { provide: APP_INTERCEPTOR, useClass: LoggingInterceptor },
    { provide: APP_INTERCEPTOR, useClass: TransformInterceptor },
  ],
})
export class AppModule {}
