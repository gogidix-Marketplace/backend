import { Module } from '@nestjs/common';
import { ConfigModule, ConfigService } from '@nestjs/config';
import { MongooseModule } from '@nestjs/mongoose';
import { APP_INTERCEPTOR } from '@nestjs/core';
import configuration from '@infrastructure/config/configuration';
import { PersistenceModule } from '@infrastructure/persistence/mongodb';
import { MessagingModule } from '@infrastructure/messaging/kafka';
import { LoggerModule } from '@shared/logging';
import { LoggingInterceptor, TransformInterceptor } from '@shared/interceptors';
import { SentimentController, HealthController } from '@interfaces/http/controllers/sentiment.controller';
import { SentimentAnalysisService } from '@application/services';

@Module({
  imports: [
    ConfigModule.forRoot({ isGlobal: true, load: [configuration] }),
    MongooseModule.forRootAsync({
      inject: [ConfigService],
      useFactory: (config: ConfigService) => ({
        uri: config.get<string>('config.mongoUri'),
        dbName: config.get<string>('config.mongoDbName'),
      }),
    }),
    LoggerModule,
    PersistenceModule,
    MessagingModule,
  ],
  controllers: [SentimentController, HealthController],
  providers: [
    SentimentAnalysisService,
    { provide: APP_INTERCEPTOR, useClass: LoggingInterceptor },
    { provide: APP_INTERCEPTOR, useClass: TransformInterceptor },
  ],
})
export class AppModule {}
