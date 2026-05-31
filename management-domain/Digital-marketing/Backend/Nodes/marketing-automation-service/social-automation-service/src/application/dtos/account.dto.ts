export class ConnectAccountDto {
  platform!: 'twitter' | 'linkedin' | 'facebook' | 'instagram';
  accessToken!: string;
  refreshToken?: string;
  expiresAt?: Date;
  platformUserId!: string;
  username!: string;
  displayName?: string;
  avatarUrl?: string;
  scopes?: string[];
}
export class UpdateAccountDto {
  displayName?: string;
  avatarUrl?: string;
  isActive?: boolean;
}
