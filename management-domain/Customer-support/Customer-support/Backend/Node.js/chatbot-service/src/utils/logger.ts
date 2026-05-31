/**
 * Winston Logger Configuration
 */

import winston from 'winston';
import DailyRotateFile from 'winston-daily-rotate-file';
import path from 'path';
import config from '../config';

const logFormat = winston.format.combine(
  winston.format.timestamp({ format: 'YYYY-MM-DD HH:mm:ss' }),
  winston.format.errors({ stack: true }),
  winston.format.splat(),
  winston.format.json()
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
  })
);

const transportOptions: DailyRotateFile.TransportOptions = {
  dirname: path.join(process.cwd(), config.logging.filePath),
  filename: 'chatbot-%DATE%.log',
  datePattern: 'YYYY-MM-DD',
  maxSize: '20m',
  maxFiles: '14d',
  format: logFormat,
};

const errorTransportOptions: DailyRotateFile.TransportOptions = {
  dirname: path.join(process.cwd(), config.logging.filePath),
  filename: 'chatbot-error-%DATE%.log',
  datePattern: 'YYYY-MM-DD',
  maxSize: '20m',
  maxFiles: '30d',
  level: 'error',
  format: logFormat,
};

export const logger = winston.createLogger({
  level: config.logging.level,
  format: logFormat,
  transports: [
    new DailyRotateFile(transportOptions),
    new DailyRotateFile(errorTransportOptions),
  ],
  exitOnError: false,
});

if (config.nodeEnv !== 'production') {
  logger.add(new winston.transports.Console({
    format: consoleFormat,
  }));
}

export default logger;
