import { Injectable } from '@nestjs/common';
import { ISocialPlatform, SocialPostPayload, SocialPostResult, EngagementData } from '../../../domain/ports/services/social-platform.port';

@Injectable()
export class TwitterAdapter implements ISocialPlatform {
  async publishPost(payload: SocialPostPayload): Promise<SocialPostResult> {
    const { TwitterApi } = require('twitter-api-v2');
    const client = new TwitterApi(payload.accessToken);
    const tweet = await client.v2.tweet(payload.content);
    return { platformPostId: tweet.data.id, platformPostUrl: `https://twitter.com/i/status/${tweet.data.id}` };
  }

  async deletePost(platformPostId: string, accessToken: string): Promise<boolean> {
    const { TwitterApi } = require('twitter-api-v2');
    const client = new TwitterApi(accessToken);
    await client.v2.deleteTweet(platformPostId);
    return true;
  }

  async getEngagement(platformPostId: string, accessToken: string): Promise<EngagementData> {
    return { likes: 0, comments: 0, shares: 0, impressions: 0, clicks: 0 };
  }
}
