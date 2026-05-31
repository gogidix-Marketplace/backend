import { Injectable, Logger } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import { INotificationService } from '@domain/ports/output/notification.interface';
import axios from 'axios';

@Injectable()
export class NotificationService implements INotificationService {
  private readonly logger = new Logger(NotificationService.name);
  private webhookUrl: string;

  constructor(private readonly configService: ConfigService) {
    this.webhookUrl = this.configService.get<string>('NOTIFICATION_WEBHOOK_URL') || '';
  }

  async notifyScalingEvent(event: any): Promise<void> {
    if (!this.webhookUrl) return;
    try {
      await axios.post(this.webhookUrl, {
        type: 'scaling_event', policyName: event.policyName,
        eventType: event.eventType, status: event.status,
        previousCapacity: event.previousCapacity, newCapacity: event.newCapacity,
        timestamp: event.startedAt,
      }, { headers: { 'Content-Type': 'application/json' }, timeout: 10000 });
    } catch (error) {
      this.logger.error('Error sending notification', error);
    }
  }

  async notifyPolicyAlert(policyName: string, message: string): Promise<void> {
    if (!this.webhookUrl) return;
    try {
      await axios.post(this.webhookUrl, {
        type: 'policy_alert', policyName, message, timestamp: new Date().toISOString(),
      }, { headers: { 'Content-Type': 'application/json' }, timeout: 10000 });
    } catch (error) {
      this.logger.error('Error sending policy alert', error);
    }
  }
}
