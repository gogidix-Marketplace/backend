import { Controller, Post, Get, Param, Body, Query, HttpCode, HttpStatus } from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { PayrollCommandService } from '@application/services/payroll-command.service';
import { PayrollQueryService } from '@application/services/payroll-query.service';
import { SubmitPayrollJobDto } from '@application/dto/requests/submit-payroll-job.dto';
import { PayrollStatus } from '@domain/enums/payroll-status.enum';
import { CurrentTenant } from '@shared/decorators/tenant.decorator';

@ApiTags('payroll')
@Controller('payroll')
export class PayrollController {
  constructor(
    private readonly commandService: PayrollCommandService,
    private readonly queryService: PayrollQueryService,
  ) {}

  @Post('process')
  @HttpCode(HttpStatus.ACCEPTED)
  @ApiOperation({ summary: 'Submit a payroll processing job' })
  @ApiResponse({ status: 202, description: 'Job queued' })
  async submitPayrollJob(@Body() dto: SubmitPayrollJobDto) {
    return this.commandService.submitPayrollJob(
      dto.tenantId, dto.employeeIds, new Date(dto.periodStart), new Date(dto.periodEnd), dto.priority,
    );
  }

  @Get('jobs/:jobId')
  @ApiOperation({ summary: 'Get job status' })
  async getJobStatus(@Param('jobId') jobId: string) {
    const result = await this.commandService.getJobStatus(jobId);
    if (!result) return { success: false, error: `Job ${jobId} not found` };
    return { success: true, data: result };
  }

  @Get('queue/stats')
  @ApiOperation({ summary: 'Get queue statistics' })
  async getQueueStats() {
    return this.commandService.getQueueStats();
  }

  @Get()
  @ApiOperation({ summary: 'List payrolls' })
  async listPayrolls(
    @Query('tenantId') tenantId: string,
    @Query('status') status?: PayrollStatus,
    @Query('limit') limit = 50,
    @Query('offset') offset = 0,
  ) {
    return this.queryService.listPayrolls(tenantId, status, Number(limit), Number(offset));
  }

  @Get(':payrollId')
  @ApiOperation({ summary: 'Get payroll by ID' })
  async getPayroll(@Param('payrollId') payrollId: string) {
    return this.queryService.getPayrollById(payrollId);
  }

  @Get(':payrollId/employee/:employeeId')
  @ApiOperation({ summary: 'Get employee payroll details' })
  async getEmployeePayroll(@Param('payrollId') payrollId: string, @Param('employeeId') employeeId: string) {
    return this.queryService.getEmployeePayroll(payrollId, employeeId);
  }
}
