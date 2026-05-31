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
import { StorageService } from '../../application/storage.service';
import {
  StorageSpaceDto,
  CreateStorageSpaceCommand,
  UpdateStorageSpaceCommand,
  StorageQueryDto,
  AllocateStorageCommand,
  ReleaseStorageCommand,
} from '../../application/dto/storage.dto';

@ApiTags('storage')
@Controller('storage')
@UsePipes(new ValidationPipe({ whitelist: true, transform: true }))
export class StorageController {
  constructor(private readonly storageService: StorageService) {}

  @Post('spaces')
  @ApiOperation({ summary: 'Create storage space' })
  @ApiResponse({ status: 201, type: StorageSpaceDto })
  async createSpace(@Body() command: CreateStorageSpaceCommand): Promise<StorageSpaceDto> {
    return this.storageService.createStorageSpace(command);
  }

  @Get('spaces/:spaceId')
  @ApiOperation({ summary: 'Get storage space by ID' })
  @ApiResponse({ status: 200, type: StorageSpaceDto })
  async getSpace(
    @Param('spaceId') spaceId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<StorageSpaceDto> {
    return this.storageService.getStorageSpace(tenantId, spaceId);
  }

  @Get('spaces')
  @ApiOperation({ summary: 'Query storage spaces' })
  @ApiResponse({ status: 200, type: [StorageSpaceDto] })
  async querySpaces(@Query() query: StorageQueryDto): Promise<{ data: StorageSpaceDto[]; total: number }> {
    return this.storageService.queryStorageSpaces(query);
  }

  @Get('warehouses/:warehouseId/available')
  @ApiOperation({ summary: 'Get available storage spaces' })
  @ApiResponse({ status: 200, type: [StorageSpaceDto] })
  async getAvailableSpaces(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
    @Query('spaceType') spaceType?: string,
  ): Promise<StorageSpaceDto[]> {
    return this.storageService.getAvailableSpaces(tenantId, warehouseId, spaceType as any);
  }

  @Get('warehouses/:warehouseId/utilization')
  @ApiOperation({ summary: 'Get warehouse utilization' })
  @ApiResponse({ status: 200, type: 'object' })
  async getUtilization(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
  ) {
    return this.storageService.getWarehouseUtilization(tenantId, warehouseId);
  }

  @Put('spaces/:spaceId')
  @ApiOperation({ summary: 'Update storage space' })
  @ApiResponse({ status: 200, type: StorageSpaceDto })
  async updateSpace(@Body() command: UpdateStorageSpaceCommand): Promise<StorageSpaceDto> {
    return this.storageService.updateStorageSpace(command);
  }

  @Post('spaces/:spaceId/allocate')
  @ApiOperation({ summary: 'Allocate storage space' })
  @ApiResponse({ status: 200, type: StorageSpaceDto })
  async allocate(@Body() command: AllocateStorageCommand): Promise<StorageSpaceDto> {
    return this.storageService.allocateStorage(command);
  }

  @Post('spaces/:spaceId/release')
  @ApiOperation({ summary: 'Release storage space' })
  @ApiResponse({ status: 200, type: StorageSpaceDto })
  async release(@Body() command: ReleaseStorageCommand): Promise<StorageSpaceDto> {
    return this.storageService.releaseStorage(command);
  }

  @Get('warehouses/:warehouseId/zones')
  @ApiOperation({ summary: 'Get warehouse zones' })
  @ApiResponse({ status: 200, type: Object })
  async getZones(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
  ) {
    return this.storageService.getWarehouseZones(tenantId, warehouseId);
  }

  @Get('zones/:zoneId/capacity')
  @ApiOperation({ summary: 'Get zone capacity' })
  @ApiResponse({ status: 200, type: 'object' })
  async getZoneCapacity(
    @Param('zoneId') zoneId: string,
    @Query('tenantId') tenantId: string,
  ) {
    return this.storageService.getZoneCapacity(tenantId, zoneId);
  }
}
