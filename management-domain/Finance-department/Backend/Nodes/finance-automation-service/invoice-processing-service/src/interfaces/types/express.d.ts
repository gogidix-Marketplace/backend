import 'express';

declare global {
  namespace Express {
    interface Request {
      tenantId?: string;
      userId?: string;
      organizationId?: string;
      correlationId?: string;
      roles?: string | string[];
    }
  }
}

export {};
