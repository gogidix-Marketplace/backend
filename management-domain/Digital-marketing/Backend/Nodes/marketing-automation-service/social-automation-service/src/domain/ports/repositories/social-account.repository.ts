import { SocialAccount } from '../models/social-account';
export const SOCIAL_ACCOUNT_REPOSITORY = Symbol('SOCIAL_ACCOUNT_REPOSITORY');
export interface ISocialAccountRepository {
  save(account: SocialAccount): Promise<SocialAccount>;
  findById(id: string): Promise<SocialAccount | null>;
  findByTenantId(tenantId: string, options?: { platform?: string; isActive?: boolean }): Promise<SocialAccount[]>;
  findByPlatformUserId(platformUserId: string, platform: string): Promise<SocialAccount | null>;
  update(account: SocialAccount): Promise<SocialAccount>;
  delete(id: string): Promise<boolean>;
}
