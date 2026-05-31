import { Injectable } from '@nestjs/common';
import { ISocialPlatform, SocialPostPayload, SocialPostResult, EngagementData } from '../../../domain/ports/services/social-platform.port';
import axios from 'axios';

@Injectable()
export class LinkedinAdapter implements ISocialPlatform {
  async publishPost(payload: SocialPostPayload): Promise<SocialPostResult> {
    const response = await axios.post('https://api.linkedin.com/v2/ugcPosts', { author: `urn:li:person:${payload.accessToken}`, lifecycleState: 'PUBLISHED', specificContent: { 'com.linkedin.ugc.ShareContent': { shareCommentary: { text: payload.content }, shareMediaCategory: 'NONE' } }, visibility: { 'com.linkedin.ugc.MemberNetworkVisibility': 'PUBLIC' } }, { headers: { Authorization: `Bearer ${payload.accessToken}`, 'Content-Type': 'application/json' } });
    return { platformPostId: response.data.id };
  }

  async deletePost(platformPostId: string, accessToken: string): Promise<boolean> { return false; }
  async getEngagement(platformPostId: string, accessToken: string): Promise<EngagementData> { return { likes: 0, comments: 0, shares: 0, impressions: 0, clicks: 0 }; }
}
