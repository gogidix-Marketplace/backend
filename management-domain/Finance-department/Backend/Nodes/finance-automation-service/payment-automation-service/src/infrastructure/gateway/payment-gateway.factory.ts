import { Injectable } from '@nestjs/common';
import { ConfigService } from '@nestjs/config';
import {
  PaymentGateway,
  PaymentGatewayFactory as IPaymentGatewayFactory,
} from '../../domain/ports/output/payment-gateway.interface';
import { StripeGateway } from './stripe.gateway';
import { TransferWiseGateway } from './transferwise.gateway';

@Injectable()
export class PaymentGatewayFactory implements IPaymentGatewayFactory {
  private readonly gateways: Map<string, PaymentGateway> = new Map();

  constructor(private readonly configService: ConfigService) {
    this.initializeGateways();
  }

  private initializeGateways(): void {
    // Initialize Stripe
    const stripeApiKey = this.configService.get<string>('STRIPE_API_KEY');
    if (stripeApiKey) {
      const stripeGateway = new StripeGateway(stripeApiKey);
      this.gateways.set('stripe', stripeGateway);
    }

    // Initialize TransferWise
    const transferwiseApiKey = this.configService.get<string>('TRANSFERWISE_API_KEY');
    const transferwiseBaseUrl = this.configService.get<string>(
      'TRANSFERWISE_BASE_URL',
      'https://api.transferwise.com',
    );
    const transferwiseProfileId = this.configService.get<string>('TRANSFERWISE_PROFILE_ID');

    if (transferwiseApiKey && transferwiseProfileId) {
      const transferwiseGateway = new TransferWiseGateway({
        apiKey: transferwiseApiKey,
        baseUrl: transferwiseBaseUrl,
        profileId: transferwiseProfileId,
      });
      this.gateways.set('transferwise', transferwiseGateway);
    }
  }

  createGateway(gatewayType: string, config?: any): PaymentGateway {
    const normalizedType = gatewayType.toLowerCase();

    // Check if already initialized
    if (this.gateways.has(normalizedType)) {
      return this.gateways.get(normalizedType)!;
    }

    // Create on-demand
    switch (normalizedType) {
      case 'stripe':
        const stripeGateway = new StripeGateway(config?.apiKey || this.configService.get<string>('STRIPE_API_KEY')!);
        this.gateways.set(normalizedType, stripeGateway);
        return stripeGateway;

      case 'transferwise':
      case 'wise':
        const transferwiseGateway = new TransferWiseGateway({
          apiKey: config?.apiKey || this.configService.get<string>('TRANSFERWISE_API_KEY')!,
          baseUrl: config?.baseUrl || this.configService.get<string>('TRANSFERWISE_BASE_URL', 'https://api.transferwise.com'),
          profileId: config?.profileId || this.configService.get<string>('TRANSFERWISE_PROFILE_ID')!,
        });
        this.gateways.set(normalizedType, transferwiseGateway);
        return transferwiseGateway;

      default:
        throw new Error(`Unsupported payment gateway type: ${gatewayType}`);
    }
  }

  getAvailableGateways(): string[] {
    return Array.from(this.gateways.keys());
  }

  getGateway(gatewayType: string): PaymentGateway {
    const gateway = this.gateways.get(gatewayType.toLowerCase());
    if (!gateway) {
      throw new Error(`Payment gateway not found: ${gatewayType}`);
    }
    return gateway;
  }
}
