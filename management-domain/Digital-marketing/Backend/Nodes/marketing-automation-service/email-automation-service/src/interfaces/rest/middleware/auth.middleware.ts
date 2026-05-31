import { Injectable, NestMiddleware } from '@nestjs/common';
import { Request, Response, NextFunction } from 'express';

@Injectable()
export class AuthMiddleware implements NestMiddleware {
  use(req: Request, res: Response, next: NextFunction) {
    const apiKey = req.headers['x-api-key'];
    const validKey = process.env.API_KEY;
    if (validKey && apiKey !== validKey) {
      res.status(401).json({ error: 'Invalid API key' });
      return;
    }
    next();
  }
}
