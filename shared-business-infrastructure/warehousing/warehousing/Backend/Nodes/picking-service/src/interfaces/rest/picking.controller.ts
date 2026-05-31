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
import { PickingService } from '../../application/picking.service';
import {
  PickOrderDto,
  CreatePickOrderCommand,
  AssignPickerCommand,
  UpdateItemPickCommand,
  PickQueryDto,
} from '../../application/dto/picking.dto';

@ApiTags('picking')
@Controller('picking')
@UsePipes(new ValidationPipe({ whitelist: true, transform: true }))
export class PickingController {
  constructor(private readonly pickingService: PickingService) {}

  @Post('orders')
  @ApiOperation({ summary: 'Create a pick order' })
  @ApiResponse({ status: 201, type: PickOrderDto })
  async createPickOrder(@Body() command: CreatePickOrderCommand): Promise<PickOrderDto> {
    return this.pickingService.createPickOrder(command);
  }

  @Get('orders/:pickOrderId')
  @ApiOperation({ summary: 'Get pick order by ID' })
  @ApiResponse({ status: 200, type: PickOrderDto })
  async getPickOrder(
    @Param('pickOrderId') pickOrderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PickOrderDto> {
    return this.pickingService.getPickOrder(tenantId, pickOrderId);
  }

  @Get('orders/order/:orderId')
  @ApiOperation({ summary: 'Get pick order by order ID' })
  @ApiResponse({ status: 200, type: PickOrderDto })
  async getPickOrderByOrderId(
    @Param('orderId') orderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PickOrderDto> {
    return this.pickingService.getPickOrderByOrderId(tenantId, orderId);
  }

  @Get('orders')
  @ApiOperation({ summary: 'Query pick orders' })
  @ApiResponse({ status: 200, type: [PickOrderDto] })
  async queryPickOrders(@Query() query: PickQueryDto): Promise<{ data: PickOrderDto[]; total: number }> {
    return this.pickingService.queryPickOrders(query);
  }

  @Get('orders/available')
  @ApiOperation({ summary: 'Get available pick orders' })
  @ApiResponse({ status: 200, type: [PickOrderDto] })
  async getAvailableOrders(@Query('tenantId') tenantId: string): Promise<PickOrderDto[]> {
    return this.pickingService.getAvailableOrders(tenantId);
  }

  @Get('orders/picker/:pickerId')
  @ApiOperation({ summary: 'Get pick orders for picker' })
  @ApiResponse({ status: 200, type: [PickOrderDto] })
  async getPickerOrders(
    @Param('pickerId') pickerId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PickOrderDto[]> {
    return this.pickingService.getPickerOrders(tenantId, pickerId);
  }

  @Put('orders/:pickOrderId/assign')
  @ApiOperation({ summary: 'Assign picker to pick order' })
  @ApiResponse({ status: 200, type: PickOrderDto })
  async assignPicker(@Body() command: AssignPickerCommand): Promise<PickOrderDto> {
    return this.pickingService.assignPicker(command);
  }

  @Put('orders/:pickOrderId/start')
  @ApiOperation({ summary: 'Start picking' })
  @ApiResponse({ status: 200, type: PickOrderDto })
  async startPick(
    @Param('pickOrderId') pickOrderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PickOrderDto> {
    return this.pickingService.startPick(tenantId, pickOrderId);
  }

  @Put('orders/:pickOrderId/items')
  @ApiOperation({ summary: 'Update item pick' })
  @ApiResponse({ status: 200, type: PickOrderDto })
  async updateItemPick(@Body() command: UpdateItemPickCommand): Promise<PickOrderDto> {
    return this.pickingService.updateItemPick(command);
  }

  @Put('orders/:pickOrderId/complete')
  @ApiOperation({ summary: 'Complete pick order' })
  @ApiResponse({ status: 200, type: PickOrderDto })
  async completePick(
    @Param('pickOrderId') pickOrderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PickOrderDto> {
    return this.pickingService.completePick(tenantId, pickOrderId);
  }

  @Put('orders/:pickOrderId/cancel')
  @ApiOperation({ summary: 'Cancel pick order' })
  @ApiResponse({ status: 200, type: PickOrderDto })
  async cancelPick(
    @Param('pickOrderId') pickOrderId: string,
    @Query('tenantId') tenantId: string,
    @Query('reason') reason?: string,
  ): Promise<PickOrderDto> {
    return this.pickingService.cancelPick(tenantId, pickOrderId, reason);
  }
}
