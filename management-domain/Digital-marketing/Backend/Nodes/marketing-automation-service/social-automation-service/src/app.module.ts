import { Module } from '@nestjs/common';
import { MongooseModule } from '@nestjs/mongoose';
import { InfrastructureModule } from './infrastructure/infrastructure.module';
import { InterfacesModule } from './interfaces/interfaces.module';
import { SharedModule } from './shared/shared.module';
import { SocialOrchestrationService } from './application/services/social-orchestration.service';
import { SchedulePostUseCase } from './application/use-cases/schedule-post.use-case';
import { PublishPostUseCase } from './application/use-cases/publish-post.use-case';
import { ManageAccountsUseCase } from './application/use-cases/manage-accounts.use-case';
import { TrackAnalyticsUseCase } from './application/use-cases/track-analytics.use-case';

@Module({
  imports: [
    MongooseModule.forRoot(process.env.MONGODB_URI ?? 'mongodb://localhost:27017/social_automation'),
    InfrastructureModule, InterfacesModule, SharedModule,
  ],
  providers: [SocialOrchestrationService, SchedulePostUseCase, PublishPostUseCase, ManageAccountsUseCase, TrackAnalyticsUseCase],
})
export class AppModule {}
