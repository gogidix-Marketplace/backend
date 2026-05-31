import { CanActivate, ExecutionContext, Injectable } from '@nestjs/common';
import { Reflector } from '@nestjs/core';
import { RequestContextHolder } from '../../shared/context/request-context';

/**
 * Guard to check if user has required roles
 */
@Injectable()
export class RoleGuard implements CanActivate {
  constructor(private readonly reflector: Reflector) {}

  canActivate(context: ExecutionContext): boolean {
    const requiredRoles = this.reflector.get<string[]>('roles', context.getHandler());

    if (!requiredRoles || requiredRoles.length === 0) {
      return true;
    }

    const contextData = RequestContextHolder.getContext();

    if (!contextData) {
      return false;
    }

    return requiredRoles.some(role => contextData.roles.includes(role));
  }
}

/**
 * Decorator to set required roles for a route
 */
import { SetMetadata } from '@nestjs/common';

export const Roles = (...roles: string[]) => SetMetadata('roles', roles);
