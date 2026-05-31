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
import { PackingService } from '../../application/packing.service';
import {
  PackOrderDto,
  CreatePackOrderCommand,
  AssignPackerCommand,
  PackItemCommand,
  PackQueryDto,
} from '../../application/dto/packing.dto';

@ApiTags('packing')
@Controller('packing')
@UsePipes(new ValidationPipe({ whitelist: true, transform: true }))
export class PackingController {
  constructor(private readonly packingService: PackingService) {}

  @Post('orders')
  @ApiOperation({ summary: 'Create a pack order' })
  @ApiResponse({ status: 201, type: PackOrderDto })
  async createPackOrder(@Body() command: CreatePackOrderCommand): Promise<PackOrderDto> {
    return this.packingService.createPackOrder(command);
  }

  @Get('orders/:packOrderId')
  @ApiOperation({ summary: 'Get pack order by ID' })
  @ApiResponse({ status: 200, type: PackOrderDto })
  async getPackOrder(
    @Param('packOrderId') packOrderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PackOrderDto> {
    return this.packingService.getPackOrder(tenantId, packOrderId);
  }

  @Get('orders/pick/:pickOrderId')
  @ApiOperation({ summary: 'Get pack order by pick order ID' })
  @ApiResponse({ status: 200, type: PackOrderDto })
  async getPackOrderByPickOrderId(
    @Param('pickOrderId') pickOrderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PackOrderDto> {
    return this.packingService.getPackOrderByPickOrderId(tenantId, pickOrderId);
  }

  @Get('orders')
  @ApiOperation({ summary: 'Query pack orders' })
  @ApiResponse({ status: 200, type: [PackOrderDto] })
  async queryPackOrders(@Query() query: PackQueryDto): Promise<{ data: PackOrderDto[]; total: number }> {
    return this.packingService.queryPackOrders(query);
  }

  @Get('orders/available')
  @ApiOperation({ summary: 'Get available pack orders' })
  @ApiResponse({ status: 200, type: [PackOrderDto] })
  async getAvailableOrders(@Query('tenantId') tenantId: string): Promise<PackOrderDto[]> {
    return this.packingService.getAvailableOrders(tenantId);
  }

  @Get('orders/packer/:packerId')
  @ApiOperation({ summary: 'Get pack orders for packer' })
  @ApiResponse({ status: 200, type: [PackOrderDto] })
  async getPackerOrders(
    @Param('packerId') packerId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PackOrderDto[]> {
    return this.packingService.getPackerOrders(tenantId, packerId);
  }

  @Put('orders/:packOrderId/assign')
  @ApiOperation({ summary: 'Assign packer to pack order' })
  @ApiResponse({ status: 200, type: PackOrderDto })
  async assignPacker(@Body() command: AssignPackerCommand): Promise<PackOrderDto> {
    return this.packingService.assignPacker(command);
  }

  @Put('orders/:packOrderId/start')
  @ApiOperation({ summary: 'Start packing' })
  @ApiResponse({ status: 200, type: PackOrderDto })
  async startPack(
    @Param('packOrderId') packOrderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PackOrderDto> {
    return this.packingService.startPack(tenantId, packOrderId);
  }

  @Put('orders/:packOrderId/items')
  @ApiOperation({ summary: 'Pack item' })
  @ApiResponse({ status: 200, type: PackOrderDto })
  async packItem(@Body() command: PackItemCommand): Promise<PackOrderDto> {
    return this.packingService.packItem(command);
  }

  @Put('orders/:packOrderId/complete')
  @ApiOperation({ summary: 'Complete pack order' })
  @ApiResponse({ status: 200, type: PackOrderDto })
  async completePack(
    @Param('packOrderId') packOrderId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<PackOrderDto> {
    return this.packingService.completePack(tenantId, packOrderId);
  }

  @Get('orders/:packOrderId/labels')
  @ApiOperation({ summary: 'Generate shipping labels' })
  @ApiResponse({ status: 200, type: 'object' })
  async generateLabels(
    @Param('packOrderId') packOrderId: string,
    @Query('tenantId') tenantId: string,
  ) {
    return this.packingService.generateShippingLabels(tenantId, packOrderId);
  }
}
