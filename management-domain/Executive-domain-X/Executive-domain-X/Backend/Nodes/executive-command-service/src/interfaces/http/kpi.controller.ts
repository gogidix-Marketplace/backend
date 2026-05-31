import { Controller, Post, Put, Delete, Body, Param, HttpCode, HttpStatus, UseInterceptors, ClassSerializerInterceptor } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiBearerAuth } from '@nestjs/swagger';
import { KpiCommandService } from '../../application/services/kpi-command.service';
import { CreateKpiDto } from '../../application/dto/requests/create-kpi.dto';
import { UpdateKpiDto } from '../../application/dto/requests/update-kpi.dto';
import { BatchCreateKpiDto } from '../../application/dto/requests/batch-create-kpi.dto';
import { RecalculateKpiDto } from '../../application/dto/requests/recalculate-kpi.dto';
import { CurrentTenant } from '../../shared/decorators/tenant.decorator';

@ApiTags('kpi')
@ApiBearerAuth()
@Controller('kpi')
@UseInterceptors(ClassSerializerInterceptor)
export class KpiController {
  constructor(private readonly kpiCommandService: KpiCommandService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new KPI' })
  async createKpi(
    @CurrentTenant() tenantId: string,
    @Body() dto: CreateKpiDto,
  ) {
    const kpi = await this.kpiCommandService.createKpi(tenantId, dto);
    return { success: true, data: kpi.toJSON() };
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update an existing KPI' })
  async updateKpi(
    @CurrentTenant() tenantId: string,
    @Param('id') id: string,
    @Body() dto: UpdateKpiDto,
  ) {
    const kpi = await this.kpiCommandService.updateKpi(tenantId, id, dto);
    return { success: true, data: kpi.toJSON() };
  }

  @Delete(':id')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Delete a KPI' })
  async deleteKpi(
    @CurrentTenant() tenantId: string,
    @Param('id') id: string,
  ) {
    await this.kpiCommandService.deleteKpi(tenantId, id);
    return { success: true, message: 'KPI deleted successfully' };
  }

  @Post('batch')
  @ApiOperation({ summary: 'Batch create KPIs' })
  async batchCreateKpis(
    @CurrentTenant() tenantId: string,
    @Body() dto: BatchCreateKpiDto,
  ) {
    const result = await this.kpiCommandService.batchCreateKpis(tenantId, dto.kpis);
    return { success: true, ...result };
  }

  @Post(':id/recalculate')
  @ApiOperation({ summary: 'Recalculate KPI value' })
  async recalculateKpi(
    @CurrentTenant() tenantId: string,
    @Param('id') id: string,
    @Body() dto: RecalculateKpiDto,
  ) {
    const kpi = await this.kpiCommandService.recalculateKpi(tenantId, id, dto.value, dto.metadata);
    return { success: true, data: kpi.toJSON() };
  }
}
