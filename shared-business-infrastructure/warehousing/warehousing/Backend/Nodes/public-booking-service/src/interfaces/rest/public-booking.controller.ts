import {
  Controller,
  Get,
  Post,
  Param,
  Body,
  Query,
  UsePipes,
  ValidationPipe,
} from '@nestjs/common';
import { ApiTags, ApiOperation, ApiResponse } from '@nestjs/swagger';
import { PublicBookingService } from '../../application/public-booking.service';
import {
  WarehousingBookingDto,
  CreateBookingCommand,
  ConfirmBookingCommand,
  CancelBookingCommand,
  BookingQueryDto,
  AvailabilityQueryDto,
} from '../../application/dto/public-booking.dto';

@ApiTags('public-bookings')
@Controller('bookings')
@UsePipes(new ValidationPipe({ whitelist: true, transform: true }))
export class PublicBookingController {
  constructor(private readonly bookingService: PublicBookingService) {}

  @Post
  @ApiOperation({ summary: 'Create a new warehousing booking' })
  @ApiResponse({ status: 201, type: WarehousingBookingDto })
  async create(@Body() command: CreateBookingCommand): Promise<WarehousingBookingDto> {
    return this.bookingService.createBooking(command);
  }

  @Get(':bookingId')
  @ApiOperation({ summary: 'Get booking by ID' })
  @ApiResponse({ status: 200, type: WarehousingBookingDto })
  async getById(
    @Param('bookingId') bookingId: string,
    @Query('tenantId') tenantId: string,
  ): Promise<WarehousingBookingDto> {
    return this.bookingService.getBooking(tenantId, bookingId);
  }

  @Get('reference/:referenceNumber')
  @ApiOperation({ summary: 'Get booking by reference number' })
  @ApiResponse({ status: 200, type: WarehousingBookingDto })
  async getByReference(@Param('referenceNumber') referenceNumber: string): Promise<WarehousingBookingDto> {
    return this.bookingService.getBookingByReference(referenceNumber);
  }

  @Get('customer/:email')
  @ApiOperation({ summary: 'Get bookings by customer email' })
  @ApiResponse({ status: 200, type: [WarehousingBookingDto] })
  async getByEmail(
    @Param('email') email: string,
    @Query('tenantId') tenantId: string,
  ): Promise<WarehousingBookingDto[]> {
    return this.bookingService.getBookingsByEmail(tenantId, email);
  }

  @Post(':bookingId/confirm')
  @ApiOperation({ summary: 'Confirm and pay for booking' })
  @ApiResponse({ status: 200, type: WarehousingBookingDto })
  async confirm(@Body() command: ConfirmBookingCommand): Promise<WarehousingBookingDto> {
    return this.bookingService.confirmBooking(command);
  }

  @Post(':bookingId/cancel')
  @ApiOperation({ summary: 'Cancel booking' })
  @ApiResponse({ status: 200, type: WarehousingBookingDto })
  async cancel(@Body() command: CancelBookingCommand): Promise<WarehousingBookingDto> {
    return this.bookingService.cancelBooking(command);
  }

  @Get('availability/check')
  @ApiOperation({ summary: 'Check storage availability' })
  @ApiResponse({ status: 200, type: 'object' })
  async checkAvailability(@Query() query: AvailabilityQueryDto) {
    return this.bookingService.checkAvailability(query);
  }

  @Get('warehouses/:warehouseId/types')
  @ApiOperation({ summary: 'Get available storage types' })
  @ApiResponse({ status: 200, type: ['object'] })
  async getStorageTypes(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
  ) {
    return this.bookingService.getStorageTypes(tenantId, warehouseId);
  }

  @Get('warehouses/:warehouseId/pricing')
  @ApiOperation({ summary: 'Get storage pricing' })
  @ApiResponse({ status: 200, type: ['object'] })
  async getPricing(
    @Param('warehouseId') warehouseId: string,
    @Query('tenantId') tenantId: string,
  ) {
    return this.bookingService.getPricing(tenantId, warehouseId);
  }

  @Get('estimates/calculate')
  @ApiOperation({ summary: 'Calculate booking estimate' })
  @ApiResponse({ status: 200, type: 'object' })
  async calculateEstimate(@Query() query: {
    tenantId: string;
    warehouseId: string;
    storageType: string;
    volume: number;
    duration: number;
  }) {
    return this.bookingService.calculateEstimate(query);
  }

  @Post(':bookingId/extend')
  @ApiOperation({ summary: 'Extend booking duration' })
  @ApiResponse({ status: 200, type: WarehousingBookingDto })
  async extendBooking(
    @Param('bookingId') bookingId: string,
    @Query('tenantId') tenantId: string,
    @Body() body: { additionalDays: number },
  ): Promise<WarehousingBookingDto> {
    return this.bookingService.extendBooking(tenantId, bookingId, body.additionalDays);
  }
}
