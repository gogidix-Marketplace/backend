import { Controller, Get, Param, Query, UseInterceptors, ClassSerializerInterceptor } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { KpiQueryService } from '../../application/services/kpi-query.service';
import { CurrentTenant } from '../../shared/decorators/tenant.decorator';

@ApiTags('kpi')
@ApiBearerAuth()
@Controller('kpi')
@UseInterceptors(ClassSerializerInterceptor)
export class KpiController {
  constructor(private readonly kpiQueryService: KpiQueryService) {}

  @Get(':id')
  @ApiOperation({ summary: 'Get KPI by ID' })
  async getKpiById(@CurrentTenant() tenantId: string, @Param('id') id: string) {
    const kpi = await this.kpiQueryService.getKpiById(tenantId, id);
    return { success: true, data: kpi.toJSON() };
  }

  @Get()
  @ApiOperation({ summary: 'Get all KPIs with pagination and filtering' })
  async getKpis(
    @CurrentTenant() tenantId: string,
    @Query('page') page?: number,
    @Query('limit') limit?: number,
    @Query('category') category?: string,
    @Query('executiveLevel') executiveLevel?: string,
    @Query('status') status?: string,
    @Query('period') period?: string,
    @Query('sortBy') sortBy?: string,
    @Query('sortOrder') sortOrder?: string,
  ) {
    const result = await this.kpiQueryService.getKpis(tenantId, { page: page ? Number(page) : 1, limit: limit ? Number(limit) : 20, sortBy: sortBy || 'createdAt', sortOrder: (sortOrder as 'asc' | 'desc') || 'desc' });
    return { success: true, ...result };
  }

  @Get('summary')
  @ApiOperation({ summary: 'Get KPI summary by category' })
  async getKpiSummary(@CurrentTenant() tenantId: string) {
    const summary = await this.kpiQueryService.getKpiSummary(tenantId);
    return { success: true, data: summary };
  }

  @Get('dashboard/:level')
  @ApiOperation({ summary: 'Get dashboard KPIs for executive level' })
  async getDashboardKpis(@CurrentTenant() tenantId: string, @Param('level') level: string) {
    const result = await this.kpiQueryService.getDashboardKpis(tenantId, level);
    return { success: true, data: result };
  }

  @Get(':id/trends')
  @ApiOperation({ summary: 'Get KPI trends over time' })
  async getKpiTrends(@CurrentTenant() tenantId: string, @Param('id') id: string, @Query('periods') periods?: number) {
    const trends = await this.kpiQueryService.getKpiTrends(tenantId, id, periods ? Number(periods) : 12);
    return { success: true, data: trends };
  }

  @Get('search')
  @ApiOperation({ summary: 'Search KPIs by name' })
  async searchKpis(@CurrentTenant() tenantId: string, @Query('q') q: string, @Query('limit') limit?: number) {
    const kpis = await this.kpiQueryService.searchKpis(tenantId, q, limit ? Number(limit) : 10);
    return { success: true, data: kpis };
  }
}
