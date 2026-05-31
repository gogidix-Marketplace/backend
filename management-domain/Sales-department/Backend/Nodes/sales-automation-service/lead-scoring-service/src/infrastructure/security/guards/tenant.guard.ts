import { Injectable, CanActivate, ExecutionContext, UnauthorizedException } from '@nestjs/common';
import { RequestContextHelper } from '../request-context';

@Injectable()
export class TenantGuard implements CanActivate {
  canActivate(context: ExecutionContext): boolean {
    const request = context.switchToHttp().getRequest();
    const tenantId = RequestContextHelper.getTenantId(request);

    if (!tenantId || tenantId === 'default') {
      throw new UnauthorizedException('Tenant identification is required');
    }

    return true;
  }
}
