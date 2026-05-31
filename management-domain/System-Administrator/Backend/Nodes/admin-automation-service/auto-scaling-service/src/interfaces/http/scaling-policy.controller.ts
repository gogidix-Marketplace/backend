import { Controller, Get, Post, Put, Delete, Param, Body, Query } from '@nestjs/common';
import { ApiTags, ApiOperation } from '@nestjs/swagger';
import { ScalingPolicyCommandService } from '@application/services/scaling-policy-command.service';
import { ScalingPolicyQueryService } from '@application/services/scaling-policy-query.service';
import { CreatePolicyDto } from '@application/dto/requests/create-policy.dto';
import { UpdatePolicyDto } from '@application/dto/requests/update-policy.dto';

@ApiTags('policies')
@Controller('policies')
export class ScalingPolicyController {
  constructor(
    private readonly commandService: ScalingPolicyCommandService,
    private readonly queryService: ScalingPolicyQueryService,
  ) {}

  @Post()
  @ApiOperation({ summary: 'Create scaling policy' })
  async createPolicy(@Body() dto: CreatePolicyDto) {
    return this.commandService.createPolicy(dto);
  }

  @Get()
  @ApiOperation({ summary: 'List scaling policies' })
  async getPolicies(@Query() filters: any) {
    return this.queryService.getPolicies(filters);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get scaling policy by ID' })
  async getPolicyById(@Param('id') id: string) {
    return this.queryService.getPolicyById(id);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update scaling policy' })
  async updatePolicy(@Param('id') id: string, @Body() dto: UpdatePolicyDto) {
    return this.commandService.updatePolicy(id, dto);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete scaling policy' })
  async deletePolicy(@Param('id') id: string) {
    await this.commandService.deletePolicy(id);
    return { success: true, message: 'Policy deleted' };
  }

  @Post(':id/enable')
  @ApiOperation({ summary: 'Enable scaling policy' })
  async enablePolicy(@Param('id') id: string) {
    return this.commandService.enablePolicy(id);
  }

  @Post(':id/disable')
  @ApiOperation({ summary: 'Disable scaling policy' })
  async disablePolicy(@Param('id') id: string) {
    return this.commandService.disablePolicy(id);
  }
}
