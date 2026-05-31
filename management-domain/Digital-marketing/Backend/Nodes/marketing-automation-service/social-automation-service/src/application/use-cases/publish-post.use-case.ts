import { Injectable, Inject } from '@nestjs/common';
import { IScheduledPostRepository, SCHEDULED_POST_REPOSITORY } from '../../domain/ports/repositories/scheduled-post.repository';
import { ISocialAccountRepository, SOCIAL_ACCOUNT_REPOSITORY } from '../../domain/ports/repositories/social-account.repository';
import { ISocialPlatform, SOCIAL_PLATFORM_PORT } from '../../domain/ports/services/social-platform.port';
import { PostNotFoundException } from '../../domain/exceptions/domain.exceptions';
import { PublishingFailedException } from '../../domain/exceptions/domain.exceptions';

@Injectable()
export class PublishPostUseCase {
  constructor(
    @Inject(SCHEDULED_POST_REPOSITORY) private readonly postRepo: IScheduledPostRepository,
    @Inject(SOCIAL_ACCOUNT_REPOSITORY) private readonly accountRepo: ISocialAccountRepository,
    @Inject(SOCIAL_PLATFORM_PORT) private readonly platform: ISocialPlatform,
  ) {}

  async execute(postId: string): Promise<void> {
    const post = await this.postRepo.findById(postId);
    if (!post) throw new PostNotFoundException(postId);
    post.markAsPublishing();
    await this.postRepo.update(post);

    const account = await this.accountRepo.findById(post.accountId);
    if (!account) throw new PublishingFailedException(`Account ${post.accountId} not found`);

    try {
      const result = await this.platform.publishPost({ content: post.content, mediaUrls: post.mediaUrls, hashtags: post.hashtags, accessToken: account.tokens.accessToken });
      post.markAsPublished(result.platformPostId, result.platformPostUrl);
      await this.postRepo.update(post);
    } catch (error) {
      post.markAsFailed(error.message);
      await this.postRepo.update(post);
      throw new PublishingFailedException(error.message);
    }
  }
}
