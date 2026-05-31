export interface SocialPostPayload {
  content: string;
  mediaUrls?: string[];
  mediaBuffers?: Buffer[];
  hashtags?: string[];
  accessToken: string;
}

export interface SocialPostResult {
  platformPostId: string;
  platformPostUrl?: string;
}

export interface EngagementData {
  likes: number;
  comments: number;
  shares: number;
  impressions: number;
  clicks: number;
}

export const SOCIAL_PLATFORM_PORT = Symbol('SOCIAL_PLATFORM_PORT');
export interface ISocialPlatform {
  publishPost(payload: SocialPostPayload): Promise<SocialPostResult>;
  deletePost(platformPostId: string, accessToken: string): Promise<boolean>;
  getEngagement(platformPostId: string, accessToken: string): Promise<EngagementData>;
}
