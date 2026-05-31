import { Injectable } from '@nestjs/common';
import { ISocialPlatform, SocialPostPayload, SocialPostResult, EngagementData } from '../../../domain/ports/services/social-platform.port';
import axios from 'axios';

@Injectable()
export class InstagramAdapter implements ISocialPlatform {
  async publishPost(payload: SocialPostPayload): Promise<SocialPostResult> {
    const response = await axios.post(`https://graph.facebook.com/v18.0/me/media`, { caption: payload.content, image_url: payload.mediaUrls?.[0] }, { headers: { Authorization: `Bearer ${payload.accessToken}` } });
    const containerId = response.data.id;
    const publishResponse = await axios.post(`https://graph.facebook.com/v18.0/me/media_publish`, { creation_id: containerId }, { headers: { Authorization: `Bearer ${payload.accessToken}` } });
    return { platformPostId: publishResponse.data.id };
  }

  async deletePost(platformPostId: string, accessToken: string): Promise<boolean> { return false; }
  async getEngagement(platformPostId: string, accessToken: string): Promise<EngagementData> {
    try {
      const response = await axios.get(`https://graph.facebook.com/v18.0/${platformPostId}/insights`, { headers: { Authorization: `Bearer ${accessToken}` }, params: { metric: 'impressions,reach,engagement' } });
      return { likes: 0, comments: 0, shares: 0, impressions: 0, clicks: 0 };
    } catch { return { likes: 0, comments: 0, shares: 0, impressions: 0, clicks: 0 }; }
  }
}
