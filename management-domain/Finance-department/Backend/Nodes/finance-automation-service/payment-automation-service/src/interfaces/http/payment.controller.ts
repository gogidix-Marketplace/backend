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
  ApiQuery,
  ApiBearerAuth,
} from '@nestjs/swagger';
import { PaymentCommandService, PaymentQueryService } from '../../application/services';
import { ExecutionContextImpl } from '@shared/context';
import {
  CreatePaymentRequestDto,
  UpdatePaymentRequestDto,
} from '../../application/dto/requests';
import { PaymentResponseDto } from '../../application/dto/responses';
import { TenantIdInterceptor } from './interceptors/tenant-id.interceptor';
import { AuthGuard } from './guards/auth.guard';

@ApiTags('Payments')
@Controller('payments')
@UseGuards(AuthGuard)
@UseInterceptors(TenantIdInterceptor)
@ApiBearerAuth()
export class PaymentController {
  constructor(
    private readonly paymentCommandService: PaymentCommandService,
    private readonly paymentQueryService: PaymentQueryService,
  ) {}

  @Post()
  @ApiOperation({ summary: 'Create a new payment' })
  @ApiResponse({
    status: HttpStatus.CREATED,
    description: 'Payment created successfully',
    type: PaymentResponseDto,
  })
  @ApiResponse({
    status: HttpStatus.BAD_REQUEST,
    description: 'Invalid payment data',
  })
  async createPayment(
    @Body() createPaymentDto: CreatePaymentRequestDto,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
    @Query('correlationId') correlationId?: string,
  ): Promise<PaymentResponseDto> {
    await createPaymentDto.validate();

    const context = new ExecutionContextImpl(tenantId, userId, correlationId);

    const payment = await this.paymentCommandService.createPayment(
      createPaymentDto,
      context,
    );

    return PaymentResponseDto.fromEntity(payment);
  }

  @Get(':id')
  @ApiOperation({ summary: 'Get payment by ID' })
  @ApiParam({ name: 'id', description: 'Payment ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payment retrieved successfully',
    type: PaymentResponseDto,
  })
  @ApiResponse({
    status: HttpStatus.NOT_FOUND,
    description: 'Payment not found',
  })
  async getPayment(
    @Param('id') id: string,
  ): Promise<PaymentResponseDto> {
    const payment = await this.paymentQueryService.findById(id);
    if (!payment) {
      throw new Error('Payment not found');
    }
    return PaymentResponseDto.fromEntity(payment);
  }

  @Put(':id')
  @ApiOperation({ summary: 'Update payment' })
  @ApiParam({ name: 'id', description: 'Payment ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payment updated successfully',
    type: PaymentResponseDto,
  })
  async updatePayment(
    @Param('id') id: string,
    @Body() updatePaymentDto: UpdatePaymentRequestDto,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<PaymentResponseDto> {
    await updatePaymentDto.validate();

    const context = new ExecutionContextImpl(tenantId, userId);

    const command = { ...updatePaymentDto, paymentId: id };
    const payment = await this.paymentCommandService.updatePayment(command, context);

    return PaymentResponseDto.fromEntity(payment);
  }

  @Post(':id/process')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Process a payment' })
  @ApiParam({ name: 'id', description: 'Payment ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payment processed successfully',
    type: PaymentResponseDto,
  })
  async processPayment(
    @Param('id') id: string,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<PaymentResponseDto> {
    const context = new ExecutionContextImpl(tenantId, userId);
    const payment = await this.paymentCommandService.processPayment({ paymentId: id }, context);

    return PaymentResponseDto.fromEntity(payment);
  }

  @Post(':id/schedule')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Schedule a payment' })
  @ApiParam({ name: 'id', description: 'Payment ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payment scheduled successfully',
    type: PaymentResponseDto,
  })
  async schedulePayment(
    @Param('id') id: string,
    @Body('scheduledAt') scheduledAt: Date,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<PaymentResponseDto> {
    const context = new ExecutionContextImpl(tenantId, userId);
    const payment = await this.paymentCommandService.schedulePayment(
      { paymentId: id, scheduledAt: new Date(scheduledAt) },
      context,
    );

    return PaymentResponseDto.fromEntity(payment);
  }

  @Post(':id/cancel')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Cancel a payment' })
  @ApiParam({ name: 'id', description: 'Payment ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payment cancelled successfully',
    type: PaymentResponseDto,
  })
  async cancelPayment(
    @Param('id') id: string,
    @Body('reason') reason: string,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<PaymentResponseDto> {
    const context = new ExecutionContextImpl(tenantId, userId);
    const payment = await this.paymentCommandService.cancelPayment(
      { paymentId: id, reason },
      context,
    );

    return PaymentResponseDto.fromEntity(payment);
  }

  @Post(':id/retry')
  @HttpCode(HttpStatus.OK)
  @ApiOperation({ summary: 'Retry a failed payment' })
  @ApiParam({ name: 'id', description: 'Payment ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payment retry initiated successfully',
    type: PaymentResponseDto,
  })
  async retryPayment(
    @Param('id') id: string,
    @Query('tenantId') tenantId?: string,
    @Query('userId') userId?: string,
  ): Promise<PaymentResponseDto> {
    const context = new ExecutionContextImpl(tenantId, userId);
    const payment = await this.paymentCommandService.retryPayment({ paymentId: id }, context);

    return PaymentResponseDto.fromEntity(payment);
  }

  @Get()
  @ApiOperation({ summary: 'List payments with filters' })
  @ApiQuery({ name: 'vendorId', required: false })
  @ApiQuery({ name: 'status', required: false })
  @ApiQuery({ name: 'gateway', required: false })
  @ApiQuery({ name: 'startDate', required: false })
  @ApiQuery({ name: 'endDate', required: false })
  @ApiQuery({ name: 'batchId', required: false })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payments retrieved successfully',
    type: [PaymentResponseDto],
  })
  async listPayments(
    @Query('vendorId') vendorId?: string,
    @Query('status') status?: string,
    @Query('gateway') gateway?: string,
    @Query('startDate') startDate?: string,
    @Query('endDate') endDate?: string,
    @Query('batchId') batchId?: string,
  ): Promise<PaymentResponseDto[]> {
    const filters: any = {};
    if (vendorId) filters.vendorId = vendorId;
    if (status) filters.status = status;
    if (gateway) filters.gateway = gateway;
    if (batchId) filters.batchId = batchId;
    if (startDate) filters.startDate = new Date(startDate);
    if (endDate) filters.endDate = new Date(endDate);

    const payments = await this.paymentQueryService.search(filters);

    return PaymentResponseDto.fromEntities(payments);
  }

  @Get('vendor/:vendorId')
  @ApiOperation({ summary: 'Get payments by vendor ID' })
  @ApiParam({ name: 'vendorId', description: 'Vendor ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payments retrieved successfully',
    type: [PaymentResponseDto],
  })
  async getPaymentsByVendor(
    @Param('vendorId') vendorId: string,
  ): Promise<PaymentResponseDto[]> {
    const payments = await this.paymentQueryService.findByVendorId(vendorId);
    return PaymentResponseDto.fromEntities(payments);
  }

  @Get('batch/:batchId')
  @ApiOperation({ summary: 'Get payments by batch ID' })
  @ApiParam({ name: 'batchId', description: 'Batch ID' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Payments retrieved successfully',
    type: [PaymentResponseDto],
  })
  async getPaymentsByBatch(
    @Param('batchId') batchId: string,
  ): Promise<PaymentResponseDto[]> {
    const payments = await this.paymentQueryService.findByBatchId(batchId);
    return PaymentResponseDto.fromEntities(payments);
  }

  @Get('statistics/summary')
  @ApiOperation({ summary: 'Get payment statistics' })
  @ApiResponse({
    status: HttpStatus.OK,
    description: 'Statistics retrieved successfully',
  })
  async getStatistics(
    @Query('startDate') startDate?: string,
    @Query('endDate') endDate?: string,
  ) {
    const filters: any = {};
    if (startDate) filters.startDate = new Date(startDate);
    if (endDate) filters.endDate = new Date(endDate);

    return this.paymentQueryService.getStatistics(filters);
  }
}
