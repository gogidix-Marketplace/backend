export interface TokenInfo {
  accessToken: string;
  refreshToken?: string;
  expiresAt?: Date;
  scopes?: string[];
}

export interface SocialAccountProps {
  id?: string;
  tenantId: string;
  platform: 'twitter' | 'linkedin' | 'facebook' | 'instagram';
  platformUserId: string;
  username: string;
  displayName?: string;
  avatarUrl?: string;
  tokens: TokenInfo;
  isActive: boolean;
  isVerified: boolean;
  followerCount?: number;
  followingCount?: number;
  metadata?: Record<string, any>;
  lastSyncedAt?: Date;
  createdAt?: Date;
  updatedAt?: Date;
}

export class SocialAccount {
  private readonly props: SocialAccountProps;

  constructor(props: SocialAccountProps) {
    this.props = {
      ...props,
      isActive: props.isActive ?? true,
      isVerified: props.isVerified ?? false,
      createdAt: props.createdAt ?? new Date(),
      updatedAt: new Date(),
    };
  }

  get id(): string | undefined { return this.props.id; }
  get tenantId(): string { return this.props.tenantId; }
  get platform(): string { return this.props.platform; }
  get platformUserId(): string { return this.props.platformUserId; }
  get username(): string { return this.props.username; }
  get displayName(): string | undefined { return this.props.displayName; }
  get avatarUrl(): string | undefined { return this.props.avatarUrl; }
  get tokens(): TokenInfo { return this.props.tokens; }
  get isActive(): boolean { return this.props.isActive; }
  get isVerified(): boolean { return this.props.isVerified; }
  get followerCount(): number | undefined { return this.props.followerCount; }
  get followingCount(): number | undefined { return this.props.followingCount; }
  get metadata(): Record<string, any> | undefined { return this.props.metadata; }
  get lastSyncedAt(): Date | undefined { return this.props.lastSyncedAt; }
  get createdAt(): Date | undefined { return this.props.createdAt; }
  get updatedAt(): Date | undefined { return this.props.updatedAt; }

  updateTokens(tokens: TokenInfo): void { this.props.tokens = tokens; this.props.updatedAt = new Date(); }
  activate(): void { this.props.isActive = true; this.props.updatedAt = new Date(); }
  deactivate(): void { this.props.isActive = false; this.props.updatedAt = new Date(); }
  verify(): void { this.props.isVerified = true; this.props.updatedAt = new Date(); }
  markSynced(): void { this.props.lastSyncedAt = new Date(); this.props.updatedAt = new Date(); }
  isTokenExpired(): boolean { return this.props.tokens.expiresAt ? new Date() >= this.props.tokens.expiresAt : false; }

  toPlainObject(): SocialAccountProps { return { ...this.props }; }
}
