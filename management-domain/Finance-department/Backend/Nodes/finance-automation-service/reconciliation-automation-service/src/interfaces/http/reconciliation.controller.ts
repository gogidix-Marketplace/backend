import {
  Controller,
  Get,
  Post,
  Put,
  Delete,
  Body,
  Param,
  Query,
  HttpCode,
  HttpStatus,
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
import { AuditLogResponseDto, ReconciliationListResponseDto, ReconciliationResponseDto, ReconciliationStatsResponseDto } from '../../application/dto/responses/reconciliation-response.dto';
import { CreateReconciliationDto, ScheduleReconciliationDto, SearchReconciliationsDto } from '../../application/dto/requests/create-reconciliation.dto';
import { ReconciliationCommandService } from '../../application/services/reconciliation-command.service';
import { ReconciliationQueryService } from '../../application/services/reconciliation-query.service';
import { ResolveDifferenceDto, BulkResolveDifferencesDto } from '../../application/dto/requests/resolve-difference.dto';
import { DifferenceResponseDto } from '../../application/dto/responses/reconciliation-response.dto';
import { TenantInterceptor } from '../../shared/interceptors/tenant.interceptor';
import { UserInterceptor } from '../../shared/interceptors/user.interceptor';

@ApiTags('reconciliation')
@ApiBearerAuth()
@Controller('reconciliations')
@UseInterceptors(TenantInterceptor, UserInterceptor)
export class ReconciliationController {
  constructor(
    private readonly commandService: ReconciliationCommandService,
    private readonly queryService: ReconciliationQueryService,
  ) {}

  @Post()
  @ApiOperation({ summary: 'Create a new reconciliation' })
  @ApiResponse({ status: 201, type: ReconciliationResponseDto })
  async create(@Body() dto: CreateReconciliationDto) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    const result = await this.commandService.startReconciliation({
      tenantId,
      name: dto.name,
      description: dto.description,
      dataSourceType: dto.dataSourceType,
      bankAccountId: dto.bankAccountId,
      internalAccountId: dto.internalAccountId,
      ruleIds: dto.ruleIds || [],
      startDate: dto.startDate ? new Date(dto.startDate) : undefined,
      endDate: dto.endDate ? new Date(dto.endDate) : undefined,
      createdBy: userId,
      metadata: dto.metadata,
    });

    return {
      id: result.reconciliationId,
      status: result.status,
      startedAt: result.startedAt,
    };
  }

  @Post('schedule')
  @ApiOperation({ summary: 'Schedule a recurring reconciliation' })
  @ApiResponse({ status: 201, type: ReconciliationResponseDto })
  async schedule(@Body() dto: ScheduleReconciliationDto) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    await this.commandService.scheduleReconciliation({
      tenantId,
      name: dto.name,
      description: dto.description,
      dataSourceType: dto.dataSourceType,
      bankAccountId: dto.bankAccountId,
      internalAccountId: dto.internalAccountId,
      ruleIds: dto.ruleIds || [],
      scheduleExpression: dto.scheduleExpression,
      timezone: dto.timezone,
      createdBy: userId,
      metadata: dto.metadata,
    });

    return { message: 'Reconciliation scheduled successfully' };
  }

  @Get()
  @ApiOperation({ summary: 'List reconciliations' })
  @ApiResponse({ status: 200, type: ReconciliationListResponseDto })
  @ApiQuery({ name: 'page', required: false, type: Number })
  @ApiQuery({ name: 'limit', required: false, type: Number })
  async findAll(@Query() query: SearchReconciliationsDto) {
    const tenantId = this.extractTenantId();

    const result = await this.queryService.searchReconciliations({
      tenantId,
      searchTerm: query.searchTerm,
      status: query.status as any,
      dataSourceType: query.dataSourceType,
      startDate: query.startDate ? new Date(query.startDate) : undefined,
      endDate: query.endDate ? new Date(query.endDate) : undefined,
      page: query.page || 1,
      limit: query.limit || 20,
    });

    return result;
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get reconciliation by ID' })
  @ApiResponse({ status: 200, type: ReconciliationResponseDto })
  @ApiParam({ name: 'id', description: 'Reconciliation ID' })
  async findOne(@Param('id') id: string) {
    const tenantId = this.extractTenantId();

    const result = await this.queryService.getReconciliationById({
      reconciliationId: id as any,
      tenantId,
    });

    if (!result) {
      return { statusCode: HttpStatus.NOT_FOUND, message: 'Reconciliation not found' };
    }

    return result;
  }

  @Post(':id/cancel')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Cancel a running reconciliation' })
  @ApiResponse({ status: 200 })
  @ApiParam({ name: 'id', description: 'Reconciliation ID' })
  async cancel(@Param('id') id: string, @Body('reason') reason?: string) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    await this.commandService.cancelReconciliation({
      reconciliationId: id as any,
      tenantId,
      userId,
      reason,
    });

    return { message: 'Reconciliation cancelled successfully' };
  }

  @Get(':id/stats')
  @ApiOperation({ summary: 'Get reconciliation statistics' })
  @ApiResponse({ status: 200, type: ReconciliationStatsResponseDto })
  @ApiParam({ name: 'id', description: 'Reconciliation ID' })
  async getStats(@Param('id') id: string) {
    const tenantId = this.extractTenantId();

    return this.queryService.getReconciliationStats({
      tenantId,
      period: 'all',
    });
  }

  @Get(':id/matches')
  @ApiOperation({ summary: 'Get matches for a reconciliation' })
  @ApiResponse({ status: 200, type: [DifferenceResponseDto] })
  @ApiParam({ name: 'id', description: 'Reconciliation ID' })
  async getMatches(
    @Param('id') id: string,
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 50,
  ) {
    const tenantId = this.extractTenantId();

    return this.queryService.getMatchesByReconciliation({
      reconciliationId: id as any,
      tenantId,
      page,
      limit,
    });
  }

  @Get(':id/differences')
  @ApiOperation({ summary: 'Get differences for a reconciliation' })
  @ApiResponse({ status: 200, type: [DifferenceResponseDto] })
  @ApiParam({ name: 'id', description: 'Reconciliation ID' })
  async getDifferences(
    @Param('id') id: string,
    @Query('status') status?: string,
    @Query('severity') severity?: string,
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 50,
  ) {
    const tenantId = this.extractTenantId();

    return this.queryService.getDifferencesByReconciliation({
      reconciliationId: id as any,
      tenantId,
      status: status as any,
      severity,
      page,
      limit,
    });
  }

  @Get(':id/audit-trail')
  @ApiOperation({ summary: 'Get audit trail for a reconciliation' })
  @ApiResponse({ status: 200, type: [AuditLogResponseDto] })
  @ApiParam({ name: 'id', description: 'Reconciliation ID' })
  async getAuditTrail(
    @Param('id') id: string,
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 50,
  ) {
    const tenantId = this.extractTenantId();

    return this.queryService.getReconciliationAuditTrail({
      reconciliationId: id as any,
      tenantId,
      page,
      limit,
    });
  }

  @Get('differences/unresolved')
  @ApiOperation({ summary: 'Get all unresolved differences' })
  @ApiResponse({ status: 200, type: [DifferenceResponseDto] })
  @ApiQuery({ name: 'severity', required: false })
  @ApiQuery({ name: 'overdueOnly', required: false, type: Boolean })
  async getUnresolvedDifferences(
    @Query('severity') severity?: string,
    @Query('overdueOnly') overdueOnly?: boolean,
    @Query('page') page: number = 1,
    @Query('limit') limit: number = 50,
  ) {
    const tenantId = this.extractTenantId();

    return this.queryService.getUnresolvedDifferences({
      tenantId,
      severity,
      overdueOnly,
      page,
      limit,
    });
  }

  private extractTenantId(): string {
    return 'default-tenant';
  }

  private extractUserId(): string {
    return 'system-user';
  }
}
