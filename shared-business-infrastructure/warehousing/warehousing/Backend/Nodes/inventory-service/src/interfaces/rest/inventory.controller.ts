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
import { InventoryService } from '../../application/inventory.service';
import {
  InventoryItemDto,
  CreateInventoryItemCommand,
  UpdateStockCommand,
  AdjustStockCommand,
  InventoryQueryDto,
  StockMovementDto,
  TransferStockCommand,
} from '../../application/dto/inventory.dto';

@ApiTags('inventory')
@Controller('inventory')
@UsePipes(new ValidationPipe({ whitelist: true, transform: true }))
export class InventoryController {
  constructor(private readonly inventoryService: InventoryService) {}

  @Post('items')
  @ApiOperation({ summary: 'Create inventory item' })
  @ApiResponse({ status: 201, type: InventoryItemDto })
  async createItem(@Body() command: CreateInventoryItemCommand): Promise<InventoryItemDto> {
    return this.inventoryService.createInventoryItem(command);
  }

  @Get('items/:itemId')
  @ApiOperation({ summary: 'Get inventory item by ID' })
  @ApiResponse({ status: 200, type: InventoryItemDto })
  async getItem(
    @Param('itemId') itemId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<InventoryItemDto> {
    return this.inventoryService.getInventoryItem(tenantId, itemId);
  }

  @Get('items/sku/:sku')
  @ApiOperation({ summary: 'Get inventory item by SKU' })
  @ApiResponse({ status: 200, type: InventoryItemDto })
  async getItemBySku(
    @Param('sku') sku: string,
    @Query('tenantId') tenantId: string,
    @Query('warehouseId') warehouseId: string,
  ): Promise<InventoryItemDto> {
    return this.inventoryService.getInventoryItemBySku(tenantId, sku, warehouseId);
  }

  @Get('items')
  @ApiOperation({ summary: 'Query inventory items' })
  @ApiResponse({ status: 200, type: [InventoryItemDto] })
  async queryItems(@Query() query: InventoryQueryDto): Promise<{ data: InventoryItemDto[]; total: number }> {
    return this.inventoryService.queryInventoryItems(query);
  }

  @Put('items/:itemId/stock')
  @ApiOperation({ summary: 'Update stock quantity' })
  @ApiResponse({ status: 200, type: InventoryItemDto })
  async updateStock(@Body() command: UpdateStockCommand): Promise<InventoryItemDto> {
    return this.inventoryService.updateStock(command);
  }

  @Post('items/:itemId/adjust')
  @ApiOperation({ summary: 'Adjust stock quantity' })
  @ApiResponse({ status: 200, type: InventoryItemDto })
  async adjustStock(@Body() command: AdjustStockCommand): Promise<InventoryItemDto> {
    return this.inventoryService.adjustStock(command);
  }

  @Post('transfer')
  @ApiOperation({ summary: 'Transfer stock between warehouses' })
  @ApiResponse({ status: 200, type: StockMovementDto })
  async transferStock(@Body() command: TransferStockCommand): Promise<StockMovementDto> {
    return this.inventoryService.transferStock(command);
  }

  @Get('warehouses/:warehouseId/low-stock')
  @ApiOperation({ summary: 'Get low stock items' })
  @ApiResponse({ status: 200, type: [InventoryItemDto] })
  async getLowStock(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
    @Query('threshold') threshold?: number,
  ): Promise<InventoryItemDto[]> {
    return this.inventoryService.getLowStockItems(tenantId, warehouseId, threshold);
  }

  @Get('warehouses/:warehouseId/movements')
  @ApiOperation({ summary: 'Get stock movements' })
  @ApiResponse({ status: 200, type: [StockMovementDto] })
  async getStockMovements(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
    @Query('itemId') itemId?: string,
    @Query('startDate') startDate?: string,
    @Query('endDate') endDate?: string,
  ): Promise<StockMovementDto[]> {
    return this.inventoryService.getStockMovements(
      tenantId,
      warehouseId,
      itemId,
      startDate ? new Date(startDate) : undefined,
      endDate ? new Date(endDate) : undefined,
    );
  }

  @Get('warehouses/:warehouseId/reservations')
  @ApiOperation({ summary: 'Get reserved stock' })
  @ApiResponse({ status: 200, type: [InventoryItemDto] })
  async getReservedStock(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<InventoryItemDto[]> {
    return this.inventoryService.getReservedStock(tenantId, warehouseId);
  }
}
