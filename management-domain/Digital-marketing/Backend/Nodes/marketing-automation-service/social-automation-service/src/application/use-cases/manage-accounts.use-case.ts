import { Injectable, Inject } from '@nestjs/common';
import { SocialAccount } from '../../domain/models/social-account';
import { ISocialAccountRepository, SOCIAL_ACCOUNT_REPOSITORY } from '../../domain/ports/repositories/social-account.repository';
import { ConnectAccountDto, UpdateAccountDto } from '../dtos/account.dto';
import { AccountNotFoundException } from '../../domain/exceptions/domain.exceptions';

@Injectable()
export class ManageAccountsUseCase {
  constructor(
    @Inject(SOCIAL_ACCOUNT_REPOSITORY) private readonly accountRepo: ISocialAccountRepository,
  ) {}

  async connect(dto: ConnectAccountDto, tenantId: string): Promise<SocialAccount> {
    const account = new SocialAccount({ tenantId, platform: dto.platform, platformUserId: dto.platformUserId, username: dto.username, displayName: dto.displayName, avatarUrl: dto.avatarUrl, tokens: { accessToken: dto.accessToken, refreshToken: dto.refreshToken, expiresAt: dto.expiresAt, scopes: dto.scopes } });
    return this.accountRepo.save(account);
  }

  async findAll(tenantId: string, platform?: string): Promise<SocialAccount[]> {
    return this.accountRepo.findByTenantId(tenantId, { platform, isActive: true });
  }

  async findById(id: string): Promise<SocialAccount> {
    const account = await this.accountRepo.findById(id);
    if (!account) throw new AccountNotFoundException(id);
    return account;
  }

  async update(id: string, dto: UpdateAccountDto): Promise<SocialAccount> {
    const account = await this.findById(id);
    const updated = new SocialAccount({ ...account.toPlainObject(), ...dto, updatedAt: new Date() });
    return this.accountRepo.update(updated);
  }

  async disconnect(id: string): Promise<boolean> {
    const account = await this.findById(id);
    account.deactivate();
    await this.accountRepo.update(account);
    return true;
  }
}
