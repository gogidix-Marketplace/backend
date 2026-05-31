import { Injectable, Inject } from '@nestjs/common';
import { ScheduledPost } from '../../domain/models/scheduled-post';
import { IScheduledPostRepository, SCHEDULED_POST_REPOSITORY } from '../../domain/ports/repositories/scheduled-post.repository';
import { IQueueService, QUEUE_SERVICE_PORT } from '../../domain/ports/services/queue-service.port';
import { SchedulePostDto } from '../dtos/post.dto';

@Injectable()
export class SchedulePostUseCase {
  constructor(
    @Inject(SCHEDULED_POST_REPOSITORY) private readonly postRepo: IScheduledPostRepository,
    @Inject(QUEUE_SERVICE_PORT) private readonly queueService: IQueueService,
  ) {}

  async execute(dto: SchedulePostDto, tenantId: string): Promise<ScheduledPost> {
    const post = new ScheduledPost({ tenantId, accountId: dto.accountId, platform: dto.platform, content: dto.content, mediaUrls: dto.mediaUrls, hashtags: dto.hashtags, scheduledAt: dto.scheduledAt, metadata: dto.metadata });
    post.schedule();
    const saved = await this.postRepo.save(post);
    const delay = dto.scheduledAt.getTime() - Date.now();
    if (delay > 0) {
      await this.queueService.addJob('social-publishing', { postId: saved.id }, { delay, attempts: 3 });
    }
    return saved;
  }
}
