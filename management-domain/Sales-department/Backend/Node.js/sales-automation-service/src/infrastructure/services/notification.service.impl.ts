import { Injectable, Logger } from '@nestjs/common';
import { NotificationService } from '../../../domain/ports/output/email.service.interface';

@Injectable()
export class InMemoryNotificationService implements NotificationService {
  private readonly logger = new Logger(InMemoryNotificationService.name);
  private readonly notifications: Array<{
    id: string;
    recipient: string | string[];
    title: string;
    message: string;
    type: string;
    createdAt: Date;
  }> = [];

  async sendNotification(params: {
    recipient: string | string[];
    title: string;
    message: string;
    type?: 'info' | 'warning' | 'error' | 'success';
    data?: Record<string, any>;
  }): Promise<{ success: boolean; notificationId?: string; error?: string }> {
    try {
      const notificationId = `notif-${Date.now()}-${Math.random().toString(36).substring(7)}`;

      this.logger.log(
        `Sending notification to ${Array.isArray(params.recipient) ? params.recipient.join(', ') : params.recipient}: ${params.title}`,
      );

      // Store notification (in-memory for this implementation)
      this.notifications.push({
        id: notificationId,
        recipient: params.recipient,
        title: params.title,
        message: params.message,
        type: params.type || 'info',
        createdAt: new Date(),
      });

      // In a real implementation, you would integrate with a notification service
      // like Pusher, Firebase Cloud Messaging, OneSignal, etc.

      return {
        success: true,
        notificationId,
      };
    } catch (error) {
      this.logger.error('Failed to send notification', error);
      return {
        success: false,
        error: error instanceof Error ? error.message : 'Unknown error',
      };
    }
  }

  getNotifications(): Array<{
    id: string;
    recipient: string | string[];
    title: string;
    message: string;
    type: string;
    createdAt: Date;
  }> {
    return [...this.notifications];
  }

  clearNotifications(): void {
    this.notifications.length = 0;
  }
}
