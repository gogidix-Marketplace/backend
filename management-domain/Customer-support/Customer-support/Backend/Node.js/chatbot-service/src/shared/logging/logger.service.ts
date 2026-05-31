import { Injectable, LoggerService as NestLoggerService, LogLevel } from '@nestjs/common';
import winston from 'winston';
import DailyRotateFile from 'winston-daily-rotate-file';
import path from 'path';

@Injectable()
export class LoggerService extends NestLoggerService {
  private logger: winston.Logger;

  constructor(logLevel: string = 'info', logFilePath: string = 'logs') {
    super();

    const logFormat = winston.format.combine(
      winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
      winston.format.errors({ stack: true }),
      winston.format.splat(),
      winston.format.json(),
    );

    const consoleFormat = winston.format.combine(
      winston.format.colorize(),
      winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
      winston.format.printf(({ timestamp, level, message, ...meta }) => {
        let msg = `${timestamp} [${level}]: ${message}`;
        if (Object.keys(meta).length > 0 && meta.stack) {
          msg += `\n${meta.stack}`;
        } else if (Object.keys(meta).length > 0) {
          msg += `\n${JSON.stringify(meta, null, 2)}`;
        }
        return msg;
      }),
    );

    this.logger = winston.createLogger({
      level: logLevel,
      format: logFormat,
      transports: [
        new DailyRotateFile({
          dirname: path.join(process.cwd(), logFilePath),
          filename: 'chatbot-%DATE%.log',
          datePattern: 'YYYY-MM-DD',
          maxSize: '20m',
          maxFiles: '14d',
          format: logFormat,
        }),
        new DailyRotateFile({
          dirname: path.join(process.cwd(), logFilePath),
          filename: 'chatbot-error-%DATE%.log',
          datePattern: 'YYYY-MM-DD',
          maxSize: '20m',
          maxFiles: '30d',
          level: 'error',
          format: logFormat,
        }),
      ],
      exitOnError: false,
    });

    if (process.env.NODE_ENV !== 'production') {
      this.logger.add(new winston.transports.Console({ format: consoleFormat }));
    }
  }

  log(message: string, context?: string) {
    this.logger.info(message, { context });
  }

  error(message: string, trace?: string, context?: string) {
    this.logger.error(message, { trace, context });
  }

  warn(message: string, context?: string) {
    this.logger.warn(message, { context });
  }

  debug(message: string, context?: string) {
    this.logger.debug(message, { context });
  }

  verbose(message: string, context?: string) {
    this.logger.verbose(message, { context });
  }
}
