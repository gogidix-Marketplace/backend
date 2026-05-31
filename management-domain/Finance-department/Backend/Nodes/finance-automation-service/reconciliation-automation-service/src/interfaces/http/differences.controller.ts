import {
  Controller,
  Get,
  Post,
  Param,
  Body,
  UseGuards,
  UseInterceptors,
} from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiParam,
  ApiBearerAuth,
} from '@nestjs/swagger';
import { DifferenceResponseDto } from '../../application/dto/responses/reconciliation-response.dto';
import { ResolveDifferenceDto, BulkResolveDifferencesDto } from '../../application/dto/requests/resolve-difference.dto';
import { ReconciliationCommandService } from '../../application/services/reconciliation-command.service';
import { ReconciliationQueryService } from '../../application/services/reconciliation-query.service';
import { TenantInterceptor } from '../../shared/interceptors/tenant.interceptor';
import { UserInterceptor } from '../../shared/interceptors/user.interceptor';

@ApiTags('differences')
@ApiBearerAuth()
@Controller('differences')
@UseInterceptors(TenantInterceptor, UserInterceptor)
export class DifferencesController {
  constructor(
    private readonly commandService: ReconciliationCommandService,
    private readonly queryService: ReconciliationQueryService,
  ) {}

  @Get(':id')
  @ApiOperation({ summary: 'Get difference by ID' })
  @ApiResponse({ status: 200, type: DifferenceResponseDto })
  @ApiParam({ name: 'id', description: 'Difference ID' })
  async findOne(@Param('id') id: string) {
    const tenantId = this.extractTenantId();

    const result = await this.queryService.getDifferenceById({
      differenceId: id as any,
      tenantId,
    });

    if (!result) {
      return { statusCode: 404, message: 'Difference not found' };
    }

    return result;
  }

  @Post(':id/resolve')
  @ApiOperation({ summary: 'Resolve a difference' })
  @ApiResponse({ status: 200 })
  @ApiParam({ name: 'id', description: 'Difference ID' })
  async resolve(@Param('id') id: string, @Body() dto: ResolveDifferenceDto) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    const difference = await this.queryService.getDifferenceById({
      differenceId: id as any,
      tenantId,
    });

    if (!difference) {
      return { statusCode: 404, message: 'Difference not found' };
    }

    await this.commandService.resolveDifference({
      differenceId: id as any,
      reconciliationId: difference.reconciliationId,
      tenantId,
      action: dto.action,
      userId,
      notes: dto.notes,
      resolutionData: dto.resolutionData,
    });

    return { message: 'Difference resolved successfully' };
  }

  @Post('bulk-resolve')
  @ApiOperation({ summary: 'Resolve multiple differences' })
  @ApiResponse({ status: 200 })
  async bulkResolve(@Body() dto: BulkResolveDifferencesDto) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    await this.commandService.bulkResolveDifferences({
      differenceIds: dto.differenceIds as any,
      reconciliationId: dto.reconciliationId as any,
      tenantId,
      action: dto.action,
      userId,
      notes: dto.notes,
    });

    return { message: 'Differences resolved successfully' };
  }

  private extractTenantId(): string {
    return 'default-tenant';
  }

  private extractUserId(): string {
    return 'system-user';
  }
}
