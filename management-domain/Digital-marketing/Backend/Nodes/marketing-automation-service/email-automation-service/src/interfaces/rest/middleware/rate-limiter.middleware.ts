import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';

@Injectable()
export class RateLimiterMiddleware implements NestMiddleware {
  private requests: Map<string, { count: number; resetTime: number }> = new Map();

  use(req: Request, res: Response, next: NextFunction) {
    const key = req.ip ?? 'unknown';
    const now = Date.now();
    const windowMs = parseInt(process.env.RATE_LIMIT_WINDOW_MS ?? '60000');
    const maxRequests = parseInt(process.env.RATE_LIMIT_MAX ?? '100');
    const entry = this.requests.get(key);
    if (!entry || now > entry.resetTime) {
      this.requests.set(key, { count: 1, resetTime: now + windowMs });
    } else {
      entry.count++;
      if (entry.count > maxRequests) {
        res.status(429).json({ error: 'Too many requests' });
        return;
      }
    }
    next();
  }
}
