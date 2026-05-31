import { Controller, Get, Post, Put, Delete, Param, Body, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { LogSourceCommandService } from '@application/services/log-source-command.service';
import { LogSourceQueryService } from '@application/services/log-source-query.service';
import { CreateSourceDto } from '@application/dto/requests/create-source.dto';
import { UpdateSourceDto } from '@application/dto/requests/update-source.dto';

@ApiTags('sources')
@Controller('sources')
export class LogSourceController {
  constructor(
    private readonly commandService: LogSourceCommandService,
    private readonly queryService: LogSourceQueryService,
  ) {}

  @Post()
  @ApiOperation({ summary: 'Create log source' })
  async createSource(@Body() dto: CreateSourceDto) {
    return this.commandService.createSource(dto);
  }

  @Get()
  @ApiOperation({ summary: 'List log sources' })
  async getSources(@Query() filters: any) {
    return this.queryService.getSources(filters);
  }

  @Get('retention/stats')
  @ApiOperation({ summary: 'Get retention statistics' })
  async getRetentionStats() {
    return this.queryService.getRetentionStats();
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get log source by ID' })
  async getSourceById(@Param('id') id: string) {
    return this.queryService.getSourceById(id);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update log source' })
  async updateSource(@Param('id') id: string, @Body() dto: UpdateSourceDto) {
    return this.commandService.updateSource(id, dto);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete log source' })
  async deleteSource(@Param('id') id: string) {
    await this.commandService.deleteSource(id);
    return { success: true, message: 'Log source deleted' };
  }

  @Post(':id/enable')
  @ApiOperation({ summary: 'Enable log source' })
  async enableSource(@Param('id') id: string) {
    return this.commandService.enableSource(id);
  }

  @Post(':id/disable')
  @ApiOperation({ summary: 'Disable log source' })
  async disableSource(@Param('id') id: string) {
    return this.commandService.disableSource(id);
  }
}
