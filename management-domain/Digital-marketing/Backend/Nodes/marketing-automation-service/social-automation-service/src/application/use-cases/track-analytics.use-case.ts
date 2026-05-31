import { Injectable, Inject } from '@nestjs/common';
import { PostAnalytics } from '../../domain/models/post-analytics';
import { IPostAnalyticsRepository, POST_ANALYTICS_REPOSITORY } from '../../domain/ports/repositories/post-analytics.repository';
import { IScheduledPostRepository, SCHEDULED_POST_REPOSITORY } from '../../domain/ports/repositories/scheduled-post.repository';
import { ISocialPlatform, SOCIAL_PLATFORM_PORT } from '../../domain/ports/services/social-platform.port';
import { ISocialAccountRepository, SOCIAL_ACCOUNT_REPOSITORY } from '../../domain/ports/repositories/social-account.repository';
import { GetAnalyticsDto } from '../dtos/analytics.dto';

@Injectable()
export class TrackAnalyticsUseCase {
  constructor(
    @Inject(POST_ANALYTICS_REPOSITORY) private readonly analyticsRepo: IPostAnalyticsRepository,
    @Inject(SCHEDULED_POST_REPOSITORY) private readonly postRepo: IScheduledPostRepository,
    @Inject(SOCIAL_ACCOUNT_REPOSITORY) private readonly accountRepo: ISocialAccountRepository,
    @Inject(SOCIAL_PLATFORM_PORT) private readonly platform: ISocialPlatform,
  ) {}

  async getAnalytics(tenantId: string, dto: GetAnalyticsDto): Promise<PostAnalytics[]> {
    if (dto.accountId) {
      return this.analyticsRepo.findByAccountAndDateRange(dto.accountId, dto.startDate ?? new Date(Date.now() - 30 * 86400000), dto.endDate ?? new Date());
    }
    return this.analyticsRepo.findByTenantId(tenantId, { platform: dto.platform, startDate: dto.startDate, endDate: dto.endDate });
  }

  async syncPostMetrics(postId: string): Promise<void> {
    const post = await this.postRepo.findById(postId);
    if (!post || !post.platformPostId) return;
    const account = await this.accountRepo.findById(post.accountId);
    if (!account) return;

    const engagement = await this.platform.getEngagement(post.platformPostId, account.tokens.accessToken);
    post.updateMetrics({ likes: engagement.likes, comments: engagement.comments, shares: engagement.shares, impressions: engagement.impressions, clicks: engagement.clicks, engagementRate: engagement.impressions > 0 ? ((engagement.likes + engagement.comments + engagement.shares) / engagement.impressions) * 100 : 0 });
    await this.postRepo.update(post);
  }
}
