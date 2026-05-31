export interface EmailService {
  sendEmail(params: {
    to: string | string[];
    cc?: string | string[];
    bcc?: string | string[];
    subject: string;
    template?: string;
    templateData?: Record<string, any>;
    body?: string;
  }): Promise<{ success: boolean; messageId?: string; error?: string }>;
}

export interface NotificationService {
  sendNotification(params: {
    recipient: string | string[];
    title: string;
    message: string;
    type?: 'info' | 'warning' | 'error' | 'success';
    data?: Record<string, any>;
  }): Promise<{ success: boolean; notificationId?: string; error?: string }>;
}

export interface WebhookService {
  sendWebhook(params: {
    url: string;
    method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
    headers?: Record<string, string>;
    body?: any;
    timeout?: number;
  }): Promise<{ success: boolean; data?: any; error?: string }>;
}
