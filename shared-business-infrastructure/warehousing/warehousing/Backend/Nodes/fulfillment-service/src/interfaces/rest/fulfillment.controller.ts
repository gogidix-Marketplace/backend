import {
  Controller,
  Get,
  Post,
  Put,
  Param,
  Body,
  Query,
  UsePipes,
  ValidationPipe,
} from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { FulfillmentService } from '../../application/fulfillment.service';
import {
  FulfillmentOrderDto,
  CreateFulfillmentCommand,
  UpdateStatusCommand,
  FulfillmentQueryDto,
  ShipmentManifestDto,
} from '../../application/dto/fulfillment.dto';

@ApiTags('fulfillment')
@Controller('fulfillment')
@UsePipes(new ValidationPipe({ whitelist: true, transform: true }))
export class FulfillmentController {
  constructor(private readonly fulfillmentService: FulfillmentService) {}

  @Post('orders')
  @ApiOperation({ summary: 'Create fulfillment order' })
  @ApiResponse({ status: 201, type: FulfillmentOrderDto })
  async createFulfillment(@Body() command: CreateFulfillmentCommand): Promise<FulfillmentOrderDto> {
    return this.fulfillmentService.createFulfillmentOrder(command);
  }

  @Get('orders/:fulfillmentId')
  @ApiOperation({ summary: 'Get fulfillment order by ID' })
  @ApiResponse({ status: 200, type: FulfillmentOrderDto })
  async getFulfillment(
    @Param('fulfillmentId') fulfillmentId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<FulfillmentOrderDto> {
    return this.fulfillmentService.getFulfillmentOrder(tenantId, fulfillmentId);
  }

  @Get('orders/order/:orderId')
  @ApiOperation({ summary: 'Get fulfillment by order ID' })
  @ApiResponse({ status: 200, type: FulfillmentOrderDto })
  async getFulfillmentByOrderId(
    @Param('orderId') orderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<FulfillmentOrderDto> {
    return this.fulfillmentService.getFulfillmentByOrderId(tenantId, orderId);
  }

  @Get('orders')
  @ApiOperation({ summary: 'Query fulfillment orders' })
  @ApiResponse({ status: 200, type: [FulfillmentOrderDto] })
  async queryOrders(@Query() query: FulfillmentQueryDto): Promise<{ data: FulfillmentOrderDto[]; total: number }> {
    return this.fulfillmentService.queryFulfillmentOrders(query);
  }

  @Put('orders/:fulfillmentId/status')
  @ApiOperation({ summary: 'Update fulfillment status' })
  @ApiResponse({ status: 200, type: FulfillmentOrderDto })
  async updateStatus(@Body() command: UpdateStatusCommand): Promise<FulfillmentOrderDto> {
    return this.fulfillmentService.updateStatus(command);
  }

  @Post('orders/:fulfillmentId/ship')
  @ApiOperation({ summary: 'Mark order as shipped' })
  @ApiResponse({ status: 200, type: ShipmentManifestDto })
  async shipOrder(
    @Param('fulfillmentId') fulfillmentId: string,
    @Query('tenantId') tenantId: string,
    @Body() body: { carrierId: string; trackingNumber?: string },
  ): Promise<ShipmentManifestDto> {
    return this.fulfillmentService.shipOrder(tenantId, fulfillmentId, body.carrierId, body.trackingNumber);
  }

  @Post('orders/:fulfillmentId/deliver')
  @ApiOperation({ summary: 'Mark order as delivered' })
  @ApiResponse({ status: 200, type: FulfillmentOrderDto })
  async deliverOrder(
    @Param('fulfillmentId') fulfillmentId: string,
    @Query('tenantId') tenantId: string,
    @Body() body: { deliveryProof?: string; notes?: string },
  ): Promise<FulfillmentOrderDto> {
    return this.fulfillmentService.deliverOrder(tenantId, fulfillmentId, body.deliveryProof, body.notes);
  }

  @Post('orders/:fulfillmentId/cancel')
  @ApiOperation({ summary: 'Cancel fulfillment order' })
  @ApiResponse({ status: 200, type: FulfillmentOrderDto })
  async cancelOrder(
    @Param('fulfillmentId') fulfillmentId: string,
    @Query('tenantId') tenantId: string,
    @Body() body: { reason?: string },
  ): Promise<FulfillmentOrderDto> {
    return this.fulfillmentService.cancelOrder(tenantId, fulfillmentId, body.reason);
  }

  @Get('warehouses/:warehouseId/pending')
  @ApiOperation({ summary: 'Get pending fulfillment orders' })
  @ApiResponse({ status: 200, type: [FulfillmentOrderDto] })
  async getPendingOrders(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<FulfillmentOrderDto[]> {
    return this.fulfillmentService.getPendingOrders(tenantId, warehouseId);
  }

  @Get('orders/:fulfillmentId/manifest')
  @ApiOperation({ summary: 'Get shipment manifest' })
  @ApiResponse({ status: 200, type: ShipmentManifestDto })
  async getManifest(
    @Param('fulfillmentId') fulfillmentId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<ShipmentManifestDto> {
    return this.fulfillmentService.getShipmentManifest(tenantId, fulfillmentId);
  }

  @Post('batch/process')
  @ApiOperation({ summary: 'Process batch fulfillment orders' })
  @ApiResponse({ status: 200, type: [FulfillmentOrderDto] })
  async processBatch(
    @Query('tenantId') tenantId: string,
    @Body() body: { fulfillmentIds: string[] },
  ): Promise<FulfillmentOrderDto[]> {
    return this.fulfillmentService.processBatch(tenantId, body.fulfillmentIds);
  }
}
