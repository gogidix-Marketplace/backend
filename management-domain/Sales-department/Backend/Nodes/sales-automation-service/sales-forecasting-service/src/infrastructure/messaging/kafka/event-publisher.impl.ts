import { Inject, Injectable, OnModuleInit, Logger } from '@nestjs/common';
import { ClientKafka } from '@nestjs/microservices';
import { EventPublisherPort } from '../../../domain/ports/out/event-publisher.port';
import { ForecastGeneratedEvent } from '../../../domain/events/forecast-generated.event';
import { ForecastAdjustedEvent } from '../../../domain/events/forecast-adjusted.event';
import { ForecastAccuracyCalculatedEvent } from '../../../domain/events/forecast-accuracy-calculated.event';
import { RollingForecastTriggeredEvent } from '../../../domain/events/rolling-forecast-triggered.event';
import { KafkaConfigService } from '../../config/kafka.config';

@Injectable()
export class KafkaEventPublisherImpl implements EventPublisherPort, OnModuleInit {
  private readonly logger = new Logger(KafkaEventPublisherImpl.name);
  private client: ClientKafka;

  constructor(private readonly kafkaConfig: KafkaConfigService) {
    const options = this.kafkaConfig.getOptions();
    this.client = new ClientKafka(options.options as any);
  }

  async onModuleInit() {
    await this.client.connect();
    this.logger.log('Kafka client connected');
  }

  async publishForecastGenerated(event: ForecastGeneratedEvent): Promise<void> {
    const topic = this.kafkaConfig.getTopics().forecast;
    try {
      await this.client.emit(topic, {
        key: event.forecastId,
        value: JSON.stringify(event.toJSON()),
      });
      this.logger.debug(`Published ForecastGeneratedEvent for forecast ${event.forecastId}`);
    } catch (error) {
      this.logger.error(`Failed to publish ForecastGeneratedEvent: ${(error as Error).message}`);
      throw error;
    }
  }

  async publishForecastAdjusted(event: ForecastAdjustedEvent): Promise<void> {
    const topic = this.kafkaConfig.getTopics().forecast;
    try {
      await this.client.emit(topic, {
        key: event.forecastId,
        value: JSON.stringify(event.toJSON()),
      });
      this.logger.debug(`Published ForecastAdjustedEvent for forecast ${event.forecastId}`);
    } catch (error) {
      this.logger.error(`Failed to publish ForecastAdjustedEvent: ${(error as Error).message}`);
      throw error;
    }
  }

  async publishForecastAccuracyCalculated(
    event: ForecastAccuracyCalculatedEvent,
  ): Promise<void> {
    const topic = this.kafkaConfig.getTopics().forecast;
    try {
      await this.client.emit(topic, {
        key: event.forecastId,
        value: JSON.stringify(event.toJSON()),
      });
      this.logger.debug(
        `Published ForecastAccuracyCalculatedEvent for forecast ${event.forecastId}`,
      );
    } catch (error) {
      this.logger.error(`Failed to publish ForecastAccuracyCalculatedEvent: ${(error as Error).message}`);
      throw error;
    }
  }

  async publishRollingForecastTriggered(event: RollingForecastTriggeredEvent): Promise<void> {
    const topic = this.kafkaConfig.getTopics().forecast;
    try {
      await this.client.emit(topic, {
        key: event.tenantId,
        value: JSON.stringify(event.toJSON()),
      });
      this.logger.debug(`Published RollingForecastTriggeredEvent for tenant ${event.tenantId}`);
    } catch (error) {
      this.logger.error(`Failed to publish RollingForecastTriggeredEvent: ${(error as Error).message}`);
      throw error;
    }
  }
}

