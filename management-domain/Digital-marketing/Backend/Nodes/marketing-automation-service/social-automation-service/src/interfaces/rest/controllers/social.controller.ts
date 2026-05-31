import { Controller, Get, Post, Put, Delete, Body, Param, Query, Req } from '@nestjs/common';
import { SocialOrchestrationService } from '../../../application/services/social-orchestration.service';
import { SchedulePostDto, UpdatePostDto } from '../../../application/dtos/post.dto';
import { ConnectAccountDto, UpdateAccountDto } from '../../../application/dtos/account.dto';
import { GetAnalyticsDto } from '../../../application/dtos/analytics.dto';

@Controller('api/v1/social')
export class SocialController {
  constructor(private readonly service: SocialOrchestrationService) {}

  @Post('posts')
  async schedulePost(@Body() dto: SchedulePostDto, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.schedulePost(dto, tenantId);
  }

  @Get('posts')
  async getPosts(@Req() req: any, @Query('status') status?: string, @Query('platform') platform?: string) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return [];
  }

  @Get('posts/:id')
  async getPost(@Param('id') id: string) { return {}; }

  @Put('posts/:id')
  async updatePost(@Param('id') id: string, @Body() dto: UpdatePostDto) { return {}; }

  @Delete('posts/:id')
  async deletePost(@Param('id') id: string) { return { deleted: true }; }

  @Post('posts/:id/publish')
  async publishNow(@Param('id') id: string) { return this.service.publishPost(id); }

  @Post('accounts')
  async connectAccount(@Body() dto: ConnectAccountDto, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.connectAccount(dto, tenantId);
  }

  @Get('accounts')
  async getAccounts(@Req() req: any, @Query('platform') platform?: string) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.getAccounts(tenantId, platform);
  }

  @Get('accounts/:id')
  async getAccount(@Param('id') id: string) { return this.service.getAccount(id); }

  @Put('accounts/:id')
  async updateAccount(@Param('id') id: string, @Body() dto: UpdateAccountDto) { return this.service.updateAccount(id, dto); }

  @Delete('accounts/:id')
  async disconnectAccount(@Param('id') id: string) { return this.service.disconnectAccount(id); }

  @Get('analytics')
  async getAnalytics(@Req() req: any, @Query() dto: GetAnalyticsDto) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.getAnalytics(tenantId, dto);
  }
}
