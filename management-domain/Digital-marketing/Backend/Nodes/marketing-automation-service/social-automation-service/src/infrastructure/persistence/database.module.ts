import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { ScheduledPostDocument, ScheduledPostSchema } from '../schemas/scheduled-post.schema';
import { SocialAccountDocument, SocialAccountSchema } from '../schemas/social-account.schema';
import { PostAnalyticsDocument, PostAnalyticsSchema } from '../schemas/post-analytics.schema';

@Module({
  imports: [
    MongooseModule.forFeature([
      { name: ScheduledPostDocument.name, schema: ScheduledPostSchema },
      { name: SocialAccountDocument.name, schema: SocialAccountSchema },
      { name: PostAnalyticsDocument.name, schema: PostAnalyticsSchema },
    ]),
  ],
  exports: [MongooseModule],
})
export class DatabaseModule {}
