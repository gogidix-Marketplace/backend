import { Module, Global } from '@nestjs/common';
import { WinstonModule } from 'nest-winston';
import * as winston from 'winston';
@Global()
@Module({ imports: [WinstonModule.forRoot({ transports: [
  new winston.transports.Console({ format: winston.format.combine(winston.format.timestamp(), winston.format.colorize(), winston.format.printf(({ timestamp, level, message, context }) => `${timestamp} [${context || 'App'}] ${level}: ${message}`)) }),
  new winston.transports.File({ filename: 'logs/error.log', level: 'error' }),
  new winston.transports.File({ filename: 'logs/combined.log' }),
] })], exports: [WinstonModule] })
export class LoggerModule {}
