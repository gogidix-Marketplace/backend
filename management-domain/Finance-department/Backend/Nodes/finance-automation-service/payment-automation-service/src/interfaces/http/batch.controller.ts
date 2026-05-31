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
  HttpStatus,
  HttpCode,
} from '@nestjs/common';
import {
  ApiTags,
  ApiOperation,
  ApiResponse,
  ApiParam,
  ApiBearerAuth,
} from '@nestjs/swagger';
import { PaymentBatchService } from '../../application/services';
import { ExecutionContextImpl } from '@shared/context';
import {
  CreateBatchRequestDto,
  AddPaymentToBatchRequestDto,
} from '../../application/dto/requests';
import { BatchResponseDto } from '../../application/dto/responses';
import { TenantIdInterceptor } from './interceptors/tenant-id.interceptor';
import { AuthGuard } from './guards/auth.guard';

@ApiTags('Payment Batches')
@Controller('batches')
@UseGuards(AuthGuard)
@UseInterceptors(TenantIdInterceptor)
@ApiBearerAuth()
export class BatchController {
  constructor(private readonly batchService: PaymentBatchService) {}

  @Post()
  @ApiOperation({ summary: 'Create a new payment batch' })
  @ApiResponse({
    status: HttpStatus.CREATED,
    description: 'Batch created successfully',
    type: BatchResponseDto,
  })
  async createBatch(
    @Body() createBatchDto: CreateBatchRequestDto,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<BatchResponseDto> {
    await createBatchDto.validate();

    const context = new ExecutionContextImpl(tenantId, userId);

    const batch = await this.batchService.createBatch(createBatchDto, context);

    return BatchResponseDto.fromEntity(batch);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get batch by ID' })
  @ApiParam({ name: 'id', description: 'Batch ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Batch retrieved successfully',
    type: BatchResponseDto,
  })
  async getBatch(
    @Param('id') id: string,
    @Query('tenantId') tenantId?: string,
  ): Promise<BatchResponseDto> {
    const context = new ExecutionContextImpl(tenantId);
    const batch = await this.batchService.getBatch(id, context);

    return BatchResponseDto.fromEntity(batch);
  }

  @Post(':id/payments')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Add payment to batch' })
  @ApiParam({ name: 'id', description: 'Batch ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payment added to batch successfully',
    type: BatchResponseDto,
  })
  async addPaymentToBatch(
    @Param('id') id: string,
    @Body() addPaymentDto: AddPaymentToBatchRequestDto,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<BatchResponseDto> {
    await addPaymentDto.validate();

    const context = new ExecutionContextImpl(tenantId, userId);

    const command = { ...addPaymentDto, batchId: id };
    const batch = await this.batchService.addPaymentToBatch(command, context);

    return BatchResponseDto.fromEntity(batch);
  }

  @Post(':id/process')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Process a payment batch' })
  @ApiParam({ name: 'id', description: 'Batch ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Batch processed successfully',
    type: BatchResponseDto,
  })
  async processBatch(
    @Param('id') id: string,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<BatchResponseDto> {
    const context = new ExecutionContextImpl(tenantId, userId);
    const batch = await this.batchService.processBatch({ batchId: id }, context);

    return BatchResponseDto.fromEntity(batch);
  }

  @Post(':id/schedule')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Schedule a payment batch' })
  @ApiParam({ name: 'id', description: 'Batch ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Batch scheduled successfully',
    type: BatchResponseDto,
  })
  async scheduleBatch(
    @Param('id') id: string,
    @Body('scheduledAt') scheduledAt: Date,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<BatchResponseDto> {
    const context = new ExecutionContextImpl(tenantId, userId);
    const batch = await this.batchService.scheduleBatch(
      { batchId: id, scheduledAt: new Date(scheduledAt) },
      context,
    );

    return BatchResponseDto.fromEntity(batch);
  }

  @Delete(':id')
  @ApiOperation({ summary: 'Cancel a payment batch' })
  @ApiParam({ name: 'id', description: 'Batch ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Batch cancelled successfully',
    type: BatchResponseDto,
  })
  async cancelBatch(
    @Param('id') id: string,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<BatchResponseDto> {
    const context = new ExecutionContextImpl(tenantId, userId);
    const batch = await this.batchService.cancelBatch({ batchId: id }, context);

    return BatchResponseDto.fromEntity(batch);
  }

  @Get()
  @ApiOperation({ summary: 'List payment batches' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Batches retrieved successfully',
    type: [BatchResponseDto],
  })
  async listBatches(
    @Query('status') status?: string,
    @Query('gateway') gateway?: string,
    @Query('tenantId') tenantId?: string,
  ): Promise<BatchResponseDto[]> {
    const context = new ExecutionContextImpl(tenantId);
    const filters: any = {};
    if (status) filters.status = status;
    if (gateway) filters.gateway = gateway;

    const batches = await this.batchService.listBatches(filters, context);

    return BatchResponseDto.fromEntities(batches);
  }
}
