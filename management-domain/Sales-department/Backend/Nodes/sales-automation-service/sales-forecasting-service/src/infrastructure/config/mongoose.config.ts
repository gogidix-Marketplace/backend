import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ConfigModule, ConfigService } from '@nestjs/config';

export const getMongooseConfig = () => ({
  imports: [
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        uri: configService.get<string>('MONGODB_URI'),
        dbName: configService.get<string>('MONGODB_DB_NAME'),
      }),
    }),
  ],
});

export const getMongooseTestingConfig = () => ({
  imports: [
    MongooseModule.forRootAsync({
      imports: [ConfigModule],
      inject: [ConfigService],
      useFactory: (configService: ConfigService) => ({
        uri: configService.get<string>('MONGODB_TEST_URI') || 'mongodb://localhost:27017/sales-forecasting-test',
        dbName: configService.get<string>('MONGODB_TEST_DB_NAME') || 'sales-forecasting-test',
      }),
    }),
  ],
});
