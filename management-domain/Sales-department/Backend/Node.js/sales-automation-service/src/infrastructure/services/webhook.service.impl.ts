import { Injectable, Logger } from '@nestjs/common';
import axios, { AxiosError } from 'axios';
import { WebhookService } from '../../../domain/ports/output/email.service.interface';

@Injectable()
export class HttpWebhookService implements WebhookService {
  private readonly logger = new Logger(HttpWebhookService.name);

  async sendWebhook(params: {
    url: string;
    method?: 'GET' | 'POST' | 'PUT' | 'PATCH' | 'DELETE';
    headers?: Record<string, string>;
    body?: any;
    timeout?: number;
  }): Promise<{ success: boolean; data?: any; error?: string }> {
    try {
      this.logger.log(`Sending webhook to ${params.url} with method ${params.method || 'POST'}`);

      const response = await axios({
        url: params.url,
        method: params.method || 'POST',
        headers: {
          'Content-Type': 'application/json',
          ...params.headers,
        },
        data: params.body,
        timeout: params.timeout || 30000,
      });

      this.logger.debug(`Webhook sent successfully to ${params.url}`);

      return {
        success: true,
        data: response.data,
      };
    } catch (error) {
      const axiosError = error as AxiosError;

      this.logger.error(`Failed to send webhook to ${params.url}`, {
        message: axiosError.message,
        status: axiosError.response?.status,
        data: axiosError.response?.data,
      });

      return {
        success: false,
        error: axiosError.message || 'Webhook request failed',
      };
    }
  }
}
