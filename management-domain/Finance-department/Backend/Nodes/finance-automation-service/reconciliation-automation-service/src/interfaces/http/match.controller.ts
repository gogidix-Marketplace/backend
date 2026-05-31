import {
  Controller,
  Post,
  Delete,
  Body,
  Param,
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
import { MatchResponseDto } from '../../application/dto/responses/reconciliation-response.dto';
import { VerifyMatchDto } from '../../application/dto/requests/verify-match.dto';
import { ReconciliationCommandService } from '../../application/services/reconciliation-command.service';
import { ReconciliationQueryService } from '../../application/services/reconciliation-query.service';
import { TenantInterceptor } from '../../shared/interceptors/tenant.interceptor';
import { UserInterceptor } from '../../shared/interceptors/user.interceptor';

@ApiTags('matches')
@ApiBearerAuth()
@Controller('matches')
@UseInterceptors(TenantInterceptor, UserInterceptor)
export class MatchController {
  constructor(
    private readonly commandService: ReconciliationCommandService,
    private readonly queryService: ReconciliationQueryService,
  ) {}

  @Post(':id/verify')
  @ApiOperation({ summary: 'Verify a match' })
  @ApiResponse({ status: 200, type: MatchResponseDto })
  @ApiParam({ name: 'id', description: 'Match ID' })
  async verify(@Param('id') id: string, @Body() dto: VerifyMatchDto) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    const match = await this.queryService.getMatchById({
      matchId: id as any,
      tenantId,
    });

    if (!match) {
      return { statusCode: 404, message: 'Match not found' };
    }

    await this.commandService.verifyMatch({
      matchId: id as any,
      reconciliationId: match.reconciliationId,
      tenantId,
      userId,
      notes: dto.notes,
    });

    return { message: 'Match verified successfully' };
  }

  @Post(':id/unverify')
  @ApiOperation({ summary: 'Unverify a match' })
  @ApiResponse({ status: 200 })
  @ApiParam({ name: 'id', description: 'Match ID' })
  async unverify(@Param('id') id: string) {
    const tenantId = this.extractTenantId();
    const userId = this.extractUserId();

    const match = await this.queryService.getMatchById({
      matchId: id as any,
      tenantId,
    });

    if (!match) {
      return { statusCode: 404, message: 'Match not found' };
    }

    await this.commandService.unverifyMatch({
      matchId: id as any,
      reconciliationId: match.reconciliationId,
      tenantId,
      userId,
    });

    return { message: 'Match unverified successfully' };
  }

  private extractTenantId(): string {
    return 'default-tenant';
  }

  private extractUserId(): string {
    return 'system-user';
  }
}
