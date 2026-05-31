import { Controller, Post, Get, Delete, Put, Body, Param, Query, Req } from '@nestjs/common';
import { EmailOrchestrationService } from '../../../application/services/email-orchestration.service';
import { SendEmailDto, SendBatchEmailDto } from '../../../application/dtos/send-email.dto';
import { CreateEmailTemplateDto, UpdateEmailTemplateDto } from '../../../application/dtos/email-template.dto';

@Controller('api/v1/email')
export class EmailController {
  constructor(private readonly service: EmailOrchestrationService) {}

  @Post('send')
  async sendEmail(@Body() dto: SendEmailDto, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.sendEmail(dto, tenantId);
  }

  @Post('send/batch')
  async sendBatch(@Body() dto: SendBatchEmailDto, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.sendBatchEmail(dto, tenantId);
  }

  @Get('status/:id')
  async getStatus(@Param('id') id: string) {
    return this.service.processQueueJob(id);
  }

  @Post('templates')
  async createTemplate(@Body() dto: CreateEmailTemplateDto, @Req() req: any) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.createTemplate(dto, tenantId);
  }

  @Get('templates')
  async getTemplates(@Req() req: any, @Query('category') category?: string) {
    const tenantId = req.headers['x-tenant-id'] ?? 'default';
    return this.service.getTemplates(tenantId, category);
  }

  @Get('templates/:id')
  async getTemplate(@Param('id') id: string) {
    return this.service.getTemplate(id);
  }

  @Put('templates/:id')
  async updateTemplate(@Param('id') id: string, @Body() dto: UpdateEmailTemplateDto) {
    return this.service.updateTemplate(id, dto);
  }

  @Delete('templates/:id')
  async deleteTemplate(@Param('id') id: string) {
    return this.service.deleteTemplate(id);
  }
}
