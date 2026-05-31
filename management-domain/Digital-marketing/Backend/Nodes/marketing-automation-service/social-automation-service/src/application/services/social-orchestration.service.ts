import { Injectable } from '@nestjs/common';
import { SchedulePostUseCase } from '../use-cases/schedule-post.use-case';
import { PublishPostUseCase } from '../use-cases/publish-post.use-case';
import { ManageAccountsUseCase } from '../use-cases/manage-accounts.use-case';
import { TrackAnalyticsUseCase } from '../use-cases/track-analytics.use-case';
import { SchedulePostDto, UpdatePostDto } from '../dtos/post.dto';
import { ConnectAccountDto, UpdateAccountDto } from '../dtos/account.dto';
import { GetAnalyticsDto } from '../dtos/analytics.dto';

@Injectable()
export class SocialOrchestrationService {
  constructor(
    private readonly schedulePostUseCase: SchedulePostUseCase,
    private readonly publishPostUseCase: PublishPostUseCase,
    private readonly manageAccountsUseCase: ManageAccountsUseCase,
    private readonly trackAnalyticsUseCase: TrackAnalyticsUseCase,
  ) {}

  async schedulePost(dto: SchedulePostDto, tenantId: string) { return this.schedulePostUseCase.execute(dto, tenantId); }
  async publishPost(postId: string) { return this.publishPostUseCase.execute(postId); }
  async connectAccount(dto: ConnectAccountDto, tenantId: string) { return this.manageAccountsUseCase.connect(dto, tenantId); }
  async getAccounts(tenantId: string, platform?: string) { return this.manageAccountsUseCase.findAll(tenantId, platform); }
  async getAccount(id: string) { return this.manageAccountsUseCase.findById(id); }
  async updateAccount(id: string, dto: UpdateAccountDto) { return this.manageAccountsUseCase.update(id, dto); }
  async disconnectAccount(id: string) { return this.manageAccountsUseCase.disconnect(id); }
  async getAnalytics(tenantId: string, dto: GetAnalyticsDto) { return this.trackAnalyticsUseCase.getAnalytics(tenantId, dto); }
  async syncPostMetrics(postId: string) { return this.trackAnalyticsUseCase.syncPostMetrics(postId); }
}
