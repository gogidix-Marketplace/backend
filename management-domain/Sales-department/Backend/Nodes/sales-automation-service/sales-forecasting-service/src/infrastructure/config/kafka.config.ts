import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { ClientProvider, Transport } from '@nestjs/microservices';

@Injectable()
export class KafkaConfigService {
  constructor(private readonly configService: ConfigService) {}

  getOptions(): ClientProvider {
    const brokers = this.configService
      .get<string>('KAFKA_BROKERS', 'localhost:9092')
      .split(',');

    return {
      transport: Transport.KAFKA,
      options: {
        client: {
          clientId: this.configService.get<string>(
            'KAFKA_CLIENT_ID',
            'sales-forecasting-service',
          ),
          brokers,
        },
        consumer: {
          groupId: this.configService.get<string>(
            'KAFKA_CONSUMER_GROUP',
            'sales-forecasting-group',
          ),
        },
      },
    };
  }

  getTopics() {
    return {
      forecast: this.configService.get<string>(
        'KAFKA_FORECAST_TOPIC',
        'sales.forecast.events',
      ),
      pipelineUpdate: this.configService.get<string>(
        'KAFKA_PIPELINE_UPDATE_TOPIC',
        'sales.pipeline.updated',
      ),
      opportunity: this.configService.get<string>(
        'KAFKA_OPPORTUNITY_TOPIC',
        'sales.opportunity.events',
      ),
    };
  }
}
