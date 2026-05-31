import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Kafka, Producer } from 'kafkajs';
import { IEventPublisher } from '@domain/ports/output/event-publisher.interface';

@Injectable()
export class KafkaEventPublisher implements IEventPublisher, OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaEventPublisher.name);
  private producer: Producer;

  constructor(private readonly configService: ConfigService) {
    const kafka = new Kafka({
      clientId: 'payroll-automation-service',
      brokers: (this.configService.get<string>('KAFKA_BROKERS') || 'localhost:9092').split(','),
      retry: { initialRetryTime: 100, retries: 8 },
    });
    this.producer = kafka.producer();
  }

  async onModuleInit() {
    await this.producer.connect();
    this.logger.log('Kafka producer connected');
  }

  async onModuleDestroy() {
    await this.producer.disconnect();
  }

  async publish(event: any): Promise<void> {
    const topic = this.getTopicForEvent(event.constructor.name);
    await this.producer.send({
      topic,
      messages: [{ key: event.tenantId || 'default', value: JSON.stringify(event), timestamp: Date.now().toString() }],
    });
    this.logger.debug(`Published event to ${topic}: ${event.constructor.name}`);
  }

  async publishAll(events: any[]): Promise<void> {
    for (const event of events) {
      await this.publish(event);
    }
  }

  private getTopicForEvent(eventName: string): string {
    const map: Record<string, string> = {
      PayrollStartedEvent: 'payroll-started',
      PayrollCompletedEvent: 'payroll-completed',
      PayrollFailedEvent: 'payroll-failed',
      EmployeePayrollCalculatedEvent: 'employee-payroll-calculated',
    };
    return map[eventName] || 'payroll-events';
  }
}
