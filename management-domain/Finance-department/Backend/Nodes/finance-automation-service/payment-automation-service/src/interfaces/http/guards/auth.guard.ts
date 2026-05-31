import {
  Injectable,
  CanActivate,
  ExecutionContext,
  UnauthorizedException,
} from '@nestjs/common';
import { Observable } from 'rxjs';

@Injectable()
export class AuthGuard implements CanActivate {
  canActivate(
    context: ExecutionContext,
  ): boolean | Promise<boolean> | Observable<boolean> {
    const request = context.switchToHttp().getRequest();
    const authHeader = request.headers.authorization;

    if (!authHeader) {
      throw new UnauthorizedException('Authorization header is missing');
    }

    if (!authHeader.startsWith('Bearer ')) {
      throw new UnauthorizedException('Invalid authorization header format');
    }

    const token = authHeader.substring(7);

    // In a real implementation, you would validate the token here
    // For now, we'll just check if it's not empty
    if (!token || token === '') {
      throw new UnauthorizedException('Invalid token');
    }

    // Attach user info to request (in real implementation, decode from token)
    request.user = {
      id: this.extractUserIdFromToken(token),
      roles: ['USER'],
    };

    return true;
  }

  private extractUserIdFromToken(token: string): string {
    // In a real implementation, decode the JWT and extract user ID
    // For now, return a placeholder
    return 'user-from-token';
  }
}
