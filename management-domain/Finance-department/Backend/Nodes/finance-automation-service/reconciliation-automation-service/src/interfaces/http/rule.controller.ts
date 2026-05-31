import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Query,
  UseGuards,
  UseInterceptors,
} from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiParam,
  ApiQuery,
  ApiBearerAuth,
} from '@nestjs/swagger';
import { RuleResponseDto } from '../../application/dto/responses/reconciliation-response.dto';
import { CreateRuleDto, UpdateRuleDto } from '../../application/dto/requests/create-rule.dto';
import { ReconciliationCommandService } from '../../application/services/reconciliation-command.service';
import { ReconciliationQueryService } from '../../application/services/reconciliation-query.service';
import { TenantInterceptor } from '../../shared/interceptors/tenant.interceptor';
import { UserInterceptor } from '../../shared/interceptors/user.interceptor';

@ApiTags('rules')
@ApiBearerAuth()
@Controller('rules')
@UseInterceptors(TenantInterceptor, UserInterceptor)
export class RuleController {
  constructor(
    private readonly commandService: ReconciliationCommandService,
    private readonly queryService: ReconciliationQueryService,
  ) {}

  @Post()
  @ApiOperation({ summary: 'Create a new reconciliation rule' })
  @ApiResponse({ status: 201, type: RuleResponseDto })
  async create(@Body() dto: CreateRuleDto) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    return this.commandService.createRule({
      tenantId,
      name: dto.name,
      description: dto.description,
      matchType: dto.matchType as any,
      priority: dto.priority,
      conditions: dto.conditions,
      actions: dto.actions,
      confidenceThreshold: dto.confidenceThreshold,
      createdBy: userId,
    });
  }

  @Get()
  @ApiOperation({ summary: 'List reconciliation rules' })
  @ApiResponse({ status: 200, type: [RuleResponseDto] })
  @ApiQuery({ name: 'enabledOnly', required: false, type: Boolean })
  @ApiQuery({ name: 'matchType', required: false })
  async findAll(@Query('enabledOnly') enabledOnly?: boolean, @Query('matchType') matchType?: string) {
    const tenantId = this.extractTenantId();

    return this.queryService.getRulesByTenant({
      tenantId,
      enabledOnly,
      matchType: matchType as any,
    });
  }

  @Get('active')
  @ApiOperation({ summary: 'Get active rules' })
  @ApiResponse({ status: 200, type: [RuleResponseDto] })
  async findActive() {
    const tenantId = this.extractTenantId();

    return this.queryService.getActiveRules({ tenantId });
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get rule by ID' })
  @ApiResponse({ status: 200, type: RuleResponseDto })
  @ApiParam({ name: 'id', description: 'Rule ID' })
  async findOne(@Param('id') id: string) {
    const tenantId = this.extractTenantId();

    const result = await this.queryService.getRuleById({
      ruleId: id as any,
      tenantId,
    });

    if (!result) {
      return { statusCode: 404, message: 'Rule not found' };
    }

    return result;
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update a rule' })
  @ApiResponse({ status: 200, type: RuleResponseDto })
  @ApiParam({ name: 'id', description: 'Rule ID' })
  async update(@Param('id') id: string, @Body() dto: UpdateRuleDto) {
    const tenantId = this.extractTenantId();

    await this.commandService.updateRule({
      ruleId: id as any,
      tenantId,
      name: dto.name,
      description: dto.description,
      matchType: dto.matchType as any,
      priority: dto.priority,
      conditions: dto.conditions,
      actions: dto.actions,
      confidenceThreshold: dto.confidenceThreshold,
    });

    return { message: 'Rule updated successfully' };
  }

  @Post(':id/enable')
  @ApiOperation({ summary: 'Enable a rule' })
  @ApiResponse({ status: 200 })
  @ApiParam({ name: 'id', description: 'Rule ID' })
  async enable(@Param('id') id: string) {
    const tenantId = this.extractTenantId();

    await this.commandService.enableRule({
      ruleId: id as any,
      tenantId,
    });

    return { message: 'Rule enabled successfully' };
  }

  @Post(':id/disable')
  @ApiOperation({ summary: 'Disable a rule' })
  @ApiResponse({ status: 200 })
  @ApiParam({ name: 'id', description: 'Rule ID' })
  async disable(@Param('id') id: string) {
    const tenantId = this.extractTenantId();

    await this.commandService.disableRule({
      ruleId: id as any,
      tenantId,
    });

    return { message: 'Rule disabled successfully' };
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Delete a rule' })
  @ApiResponse({ status: 200 })
  @ApiParam({ name: 'id', description: 'Rule ID' })
  async delete(@Param('id') id: string) {
    const tenantId = this.extractTenantId();

    await this.commandService.deleteRule({
      ruleId: id as any,
      tenantId,
    });

    return { message: 'Rule deleted successfully' };
  }

  private extractTenantId(): string {
    return 'default-tenant';
  }

  private extractUserId(): string {
    return 'system-user';
  }
}
