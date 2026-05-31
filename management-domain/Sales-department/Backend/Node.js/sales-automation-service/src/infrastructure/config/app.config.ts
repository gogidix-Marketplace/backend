import { registerAs } from '@nestjs/config';

export const appConfig = registerAs('app', () => ({
  port: parseInt(process.env.PORT, 10) || 3003,
  serviceName: process.env.SERVICE_NAME || 'sales-automation-service',
  environment: process.env.NODE_ENV || 'development',
  corsOrigin: process.env.CORS_ORIGIN || '*',
  defaultTenantId: process.env.DEFAULT_TENANT_ID || 'default',
  enableMultiTenancy: process.env.ENABLE_MULTI_TENANCY === 'true',
}));
