import { Injectable, Logger, OnModuleInit, OnModuleDestroy } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { Kafka, Consumer } from 'kafkajs';
import { PayrollCommandService } from '@application/services/payroll-command.service';

@Injectable()
export class KafkaConsumerService implements OnModuleInit, OnModuleDestroy {
  private readonly logger = new Logger(KafkaConsumerService.name);
  private consumer: Consumer;

  constructor(
    private readonly configService: ConfigService,
    private readonly payrollCommandService: PayrollCommandService,
  ) {
    const kafka = new Kafka({
      clientId: 'payroll-automation-service',
      brokers: (this.configService.get<string>('KAFKA_BROKERS') || 'localhost:9092').split(','),
      retry: { initialRetryTime: 100, retries: 8 },
    });
    this.consumer = kafka.consumer({ groupId: 'payroll-automation-group' });
  }

  async onModuleInit() {
    await this.consumer.connect();
    await this.consumer.subscribe({ topic: 'payroll-commands', fromBeginning: false });
    this.logger.log('Kafka consumer connected');

    await this.consumer.run({
      eachMessage: async ({ topic, partition, message }) => {
        const value = message.value?.toString();
        if (!value) return;

        try {
          const command = JSON.parse(value);
          await this.handleCommand(command);
        } catch (error) {
          this.logger.error('Failed to process Kafka message', error);
        }
      },
    });
  }

  async onModuleDestroy() {
    await this.consumer.disconnect();
  }

  private async handleCommand(command: any): Promise<void> {
    const { type, data } = command;
    switch (type) {
      case 'PROCESS_PAYROLL':
        await this.payrollCommandService.submitPayrollJob(
          data.tenantId, data.employeeIds, data.periodStart, data.periodEnd,
        );
        break;
      case 'CANCEL_PAYROLL':
        await this.payrollCommandService.cancelPayrollJob(data.jobId);
        break;
      default:
        this.logger.warn(`Unknown command type: ${type}`);
    }
  }
}
