import { Injectable, Logger } from '@nestjs/common';
import { IPubSubService } from '../../../domain/ports/output/pub-sub.interface';

@Injectable()
export class RedisPubSubService implements IPubSubService {
  private readonly logger = new Logger(RedisPubSubService.name);

  async subscribe(channel: string, _handler: (message: any, channel: string) => void): Promise<void> {
    this.logger.debug(`Subscribed to channel: ${channel}`);
  }

  async unsubscribe(channel: string): Promise<void> {
    this.logger.debug(`Unsubscribed from channel: ${channel}`);
  }

  async publish(channel: string, _message: any): Promise<void> {
    this.logger.debug(`Published to channel: ${channel}`);
  }
}
