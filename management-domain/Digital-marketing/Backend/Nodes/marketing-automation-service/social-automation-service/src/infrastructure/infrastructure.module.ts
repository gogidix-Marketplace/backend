import { Module } from '@nestjs/common';
import { DatabaseModule } from './persistence/database.module';
import { PlatformsModule } from './platforms/platforms.module';
import { QueueModule } from './queue/queue.module';
import { SchedulerModule } from './scheduler/scheduler.module';
import { ScheduledPostRepositoryImpl } from './persistence/mongoose/scheduled-post.repository.impl';
import { SocialAccountRepositoryImpl } from './persistence/mongoose/social-account.repository.impl';
import { PostAnalyticsRepositoryImpl } from './persistence/mongoose/post-analytics.repository.impl';
import { SCHEDULED_POST_REPOSITORY } from '../domain/ports/repositories/scheduled-post.repository';
import { SOCIAL_ACCOUNT_REPOSITORY } from '../domain/ports/repositories/social-account.repository';
import { POST_ANALYTICS_REPOSITORY } from '../domain/ports/repositories/post-analytics.repository';

@Module({
  imports: [DatabaseModule, PlatformsModule, QueueModule, SchedulerModule],
  providers: [
    { provide: 'SCHEDULED_POST_REPOSITORY' useClass: ScheduledPostRepositoryImpl },
    { provide: 'SOCIAL_ACCOUNT_REPOSITORY' useClass: SocialAccountRepositoryImpl },
    { provide: 'POST_ANALYTICS_REPOSITORY' useClass: PostAnalyticsRepositoryImpl },
  ],
  exports: [SCHEDULED_POST_REPOSITORY, SOCIAL_ACCOUNT_REPOSITORY, POST_ANALYTICS_REPOSITORY, PlatformsModule, QueueModule, SchedulerModule],
})
export class InfrastructureModule {}
