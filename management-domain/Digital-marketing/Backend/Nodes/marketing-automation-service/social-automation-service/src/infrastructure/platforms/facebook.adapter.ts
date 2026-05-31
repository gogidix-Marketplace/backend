import { Injectable } from '@nestjs/common';
import { ISocialPlatform, SocialPostPayload, SocialPostResult, EngagementData } from '../../../domain/ports/services/social-platform.port';
import axios from 'axios';

@Injectable()
export class FacebookAdapter implements ISocialPlatform {
  async publishPost(payload: SocialPostPayload): Promise<SocialPostResult> {
    const response = await axios.post(`https://graph.facebook.com/v18.0/me/feed`, { message: payload.content }, { headers: { Authorization: `Bearer ${payload.accessToken}` } });
    return { platformPostId: response.data.id, platformPostUrl: `https://facebook.com/${response.data.id}` };
  }

  async deletePost(platformPostId: string, accessToken: string): Promise<boolean> {
    await axios.delete(`https://graph.facebook.com/v18.0/${platformPostId}`, { headers: { Authorization: `Bearer ${accessToken}` } });
    return true;
  }

  async getEngagement(platformPostId: string, accessToken: string): Promise<EngagementData> {
    try {
      const response = await axios.get(`https://graph.facebook.com/v18.0/${platformPostId}/insights`, { headers: { Authorization: `Bearer ${accessToken}` }, params: { metric: 'post_impressions,post_clicks' } });
      return { likes: 0, comments: 0, shares: 0, impressions: 0, clicks: 0 };
    } catch { return { likes: 0, comments: 0, shares: 0, impressions: 0, clicks: 0 }; }
  }
}
