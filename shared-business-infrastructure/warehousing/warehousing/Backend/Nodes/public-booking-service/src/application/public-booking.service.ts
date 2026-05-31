import { Injectable, NotFoundException, BadRequestException, ConflictException } from '@nestjs/common';
import { InjectRepository } from '@nestjs/typeorm';
import { Repository, DataSource, Between, MoreThanOrEqual, LessThanOrEqual, In } from 'typeorm';
import { v4 as uuidv4 } from 'uuid';
import {
  WarehousingBookingDto,
  CreateBookingCommand,
  ConfirmBookingCommand,
  CancelBookingCommand,
  AvailabilityQueryDto,
  AvailabilityResultDto,
  StorageTypeDto,
  PricingDto,
  BookingEstimateDto,
  BookingStatus,
  StorageType,
  CustomerInfoDto,
  BookingItemDto,
} from './dto/public-booking.dto';
import { Booking } from '../../domain/entities/booking.entity';

@Injectable()
export class PublicBookingService {
  private readonly PRICING_CONFIG: Record<StorageType, PricingDto> = {
    [StorageType.STANDARD]: {
      storageType: StorageType.STANDARD,
      name: 'Standard Storage',
      basePricePerDay: 25,
      pricePerCubicMeterPerDay: 0.15,
      minimumDays: 1,
      maximumDays: 365,
      discountWeekly: 0.05,
      discountMonthly: 0.15,
      taxRate: 0.08,
      features: ['24/7 Access', 'Basic Security', 'Ground Floor'],
    },
    [StorageType.CLIMATE_CONTROLLED]: {
      storageType: StorageType.CLIMATE_CONTROLLED,
      name: 'Climate Controlled Storage',
      basePricePerDay: 40,
      pricePerCubicMeterPerDay: 0.25,
      minimumDays: 1,
      maximumDays: 365,
      discountWeekly: 0.07,
      discountMonthly: 0.20,
      taxRate: 0.08,
      features: ['Temperature Control', 'Humidity Control', '24/7 Access', 'Enhanced Security'],
    },
    [StorageType.HAZARDOUS]: {
      storageType: StorageType.HAZARDOUS,
      name: 'Hazardous Material Storage',
      basePricePerDay: 75,
      pricePerCubicMeterPerDay: 0.50,
      minimumDays: 7,
      maximumDays: 90,
      discountWeekly: 0.03,
      discountMonthly: 0.10,
      taxRate: 0.08,
      features: ['Specialized Containment', 'Safety Compliance', 'Restricted Access', 'Fire Suppression'],
    },
    [StorageType.COLD_STORAGE]: {
      storageType: StorageType.COLD_STORAGE,
      name: 'Cold Storage',
      basePricePerDay: 55,
      pricePerCubicMeterPerDay: 0.35,
      minimumDays: 1,
      maximumDays: 180,
      discountWeekly: 0.05,
      discountMonthly: 0.12,
      taxRate: 0.08,
      features: ['Sub-Zero Temperature', 'Temperature Monitoring', 'Backup Power', 'Quick Access'],
    },
    [StorageType.HIGH_SECURITY]: {
      storageType: StorageType.HIGH_SECURITY,
      name: 'High Security Storage',
      basePricePerDay: 100,
      pricePerCubicMeterPerDay: 0.60,
      minimumDays: 1,
      maximumDays: 365,
      discountWeekly: 0.08,
      discountMonthly: 0.18,
      taxRate: 0.08,
      features: ['Biometric Access', '24/7 CCTV', 'Motion Sensors', 'Individual Alarms', 'Insurance Options'],
    },
  };

  constructor(
    @InjectRepository(Booking)
    private readonly bookingRepository: Repository<Booking>,
    private readonly dataSource: DataSource,
  ) {}

  async createBooking(command: CreateBookingCommand): Promise<WarehousingBookingDto> {
    const startDate = new Date(command.startDate);
    const endDate = new Date(command.endDate);
    const durationDays = Math.ceil((endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24));

    if (durationDays < 1) {
      throw new BadRequestException('Booking duration must be at least 1 day');
    }

    const pricing = this.PRICING_CONFIG[command.storageType];
    if (durationDays < pricing.minimumDays || durationDays > pricing.maximumDays) {
      throw new BadRequestException(
        `Booking duration must be between ${pricing.minimumDays} and ${pricing.maximumDays} days for ${pricing.name}`,
      );
    }

    const totalVolume = command.items.reduce((sum, item) => sum + item.volume * item.quantity, 0);
    const totalWeight = command.items.reduce((sum, item) => sum + item.weight * item.quantity, 0);

    const estimate = this.calculateBookingAmount(command.storageType, totalVolume, durationDays);

    const referenceNumber = await this.generateReferenceNumber();

    const booking = this.bookingRepository.create({
      tenantId: command.tenantId,
      referenceNumber,
      warehouseId: command.warehouseId,
      storageType: command.storageType,
      customer: command.customer,
      items: command.items,
      startDate,
      endDate,
      totalVolume,
      totalWeight,
      durationDays,
      baseAmount: estimate.baseAmount,
      taxAmount: estimate.taxAmount,
      totalAmount: estimate.totalAmount,
      currency: 'USD',
      status: BookingStatus.PENDING,
      specialInstructions: command.specialInstructions,
      promotionalCode: command.promotionalCode,
      expiresAt: new Date(Date.now() + 24 * 60 * 60 * 1000),
    });

    const savedBooking = await this.bookingRepository.save(booking);
    return this.toDto(savedBooking);
  }

  async getBooking(tenantId: string, bookingId: string): Promise<WarehousingBookingDto> {
    const booking = await this.bookingRepository.findOne({
      where: { tenantId, id: bookingId },
    });

    if (!booking) {
      throw new NotFoundException(`Booking ${bookingId} not found`);
    }

    return this.toDto(booking);
  }

  async getBookingByReference(referenceNumber: string): Promise<WarehousingBookingDto> {
    const booking = await this.bookingRepository.findOne({
      where: { referenceNumber },
    });

    if (!booking) {
      throw new NotFoundException(`Booking with reference ${referenceNumber} not found`);
    }

    return this.toDto(booking);
  }

  async getBookingsByEmail(tenantId: string, email: string): Promise<WarehousingBookingDto[]> {
    const bookings = await this.bookingRepository
      .createQueryBuilder('booking')
      .where('booking.tenantId = :tenantId', { tenantId })
      .andWhere('booking.customer ->> :emailKey = :email', { emailKey: 'email', email })
      .orderBy('booking.createdAt', 'DESC')
      .getMany();

    return bookings.map((b) => this.toDto(b));
  }

  async confirmBooking(command: ConfirmBookingCommand): Promise<WarehousingBookingDto> {
    const booking = await this.bookingRepository.findOne({
      where: { tenantId: command.tenantId, id: command.bookingId },
    });

    if (!booking) {
      throw new NotFoundException(`Booking ${command.bookingId} not found`);
    }

    if (booking.status !== BookingStatus.PENDING) {
      throw new BadRequestException(`Cannot confirm booking in ${booking.status} status`);
    }

    if (booking.expiresAt && new Date() > booking.expiresAt) {
      booking.status = BookingStatus.EXPIRED;
      await this.bookingRepository.save(booking);
      throw new ConflictException('Booking has expired. Please create a new booking.');
    }

    booking.status = BookingStatus.CONFIRMED;
    booking.confirmedAt = new Date();
    booking.paymentMethod = command.paymentMethodId;

    if (command.paymentIntentId) {
      booking.paymentId = command.paymentIntentId;
      booking.status = BookingStatus.PAID;
      booking.paidAt = new Date();
    }

    const savedBooking = await this.bookingRepository.save(booking);
    return this.toDto(savedBooking);
  }

  async cancelBooking(command: CancelBookingCommand): Promise<WarehousingBookingDto> {
    const booking = await this.bookingRepository.findOne({
      where: { tenantId: command.tenantId, id: command.bookingId },
    });

    if (!booking) {
      throw new NotFoundException(`Booking ${command.bookingId} not found`);
    }

    if (booking.status === BookingStatus.COMPLETED || booking.status === BookingStatus.CANCELLED) {
      throw new BadRequestException(`Cannot cancel booking in ${booking.status} status`);
    }

    booking.status = BookingStatus.CANCELLED;
    booking.cancelledAt = new Date();
    booking.cancellationReason = command.cancellationReason;
    booking.refundRequested = command.requestRefund ?? false;

    const savedBooking = await this.bookingRepository.save(booking);
    return this.toDto(savedBooking);
  }

  async checkAvailability(query: AvailabilityQueryDto): Promise<AvailabilityResultDto> {
    const { tenantId, warehouseId, storageType, startDate, endDate, requiredVolume } = query;

    const bookings = await this.bookingRepository.find({
      where: {
        tenantId,
        warehouseId,
        storageType,
        status: In([BookingStatus.CONFIRMED, BookingStatus.PAID, BookingStatus.ACTIVE]),
      },
    });

    const overlappingBookings = bookings.filter((b) => {
      const bookingStart = new Date(b.startDate);
      const bookingEnd = new Date(b.endDate);
      const queryStart = new Date(startDate);
      const queryEnd = new Date(endDate);

      return queryStart <= bookingEnd && queryEnd >= bookingStart;
    });

    const usedVolume = overlappingBookings.reduce((sum, b) => sum + Number(b.totalVolume), 0);

    const totalCapacity = 10000;
    const availableVolume = Math.max(0, totalCapacity - usedVolume);
    const utilizationPercentage = (usedVolume / totalCapacity) * 100;

    const available = availableVolume >= requiredVolume;

    let alternativeDates = undefined;
    if (!available) {
      alternativeDates = await this.findAlternativeDates(tenantId, warehouseId, storageType, requiredVolume);
    }

    return {
      available,
      availableVolume,
      totalVolume: totalCapacity,
      utilizationPercentage,
      alternativeDates,
    };
  }

  async getStorageTypes(tenantId: string, warehouseId: string): Promise<StorageTypeDto[]> {
    const storageTypes: StorageTypeDto[] = [];

    for (const [type, pricing] of Object.entries(this.PRICING_CONFIG)) {
      const bookings = await this.bookingRepository.count({
        where: {
          tenantId,
          warehouseId,
          storageType: type as StorageType,
          status: In([BookingStatus.CONFIRMED, BookingStatus.PAID, BookingStatus.ACTIVE]),
        },
      });

      const totalVolume = 10000;
      const usedVolume = bookings * 100;
      const availableVolume = Math.max(0, totalVolume - usedVolume);

      storageTypes.push({
        code: type,
        name: pricing.name,
        description: pricing.features?.join(', '),
        availableVolume,
        totalVolume,
        available: availableVolume > 0,
        basePricePerDay: pricing.basePricePerDay,
        pricePerCubicMeter: pricing.pricePerCubicMeterPerDay,
      });
    }

    return storageTypes;
  }

  async getPricing(tenantId: string, warehouseId: string): Promise<PricingDto[]> {
    return Object.values(this.PRICING_CONFIG);
  }

  async calculateEstimate(query: {
    tenantId: string;
    warehouseId: string;
    storageType: StorageType;
    volume: number;
    duration: number;
  }): Promise<BookingEstimateDto> {
    return this.calculateBookingAmount(query.storageType, query.volume, query.duration);
  }

  async extendBooking(tenantId: string, bookingId: string, additionalDays: number): Promise<WarehousingBookingDto> {
    if (additionalDays < 1) {
      throw new BadRequestException('Additional days must be at least 1');
    }

    const booking = await this.bookingRepository.findOne({
      where: { tenantId, id: bookingId },
    });

    if (!booking) {
      throw new NotFoundException(`Booking ${bookingId} not found`);
    }

    if (booking.status !== BookingStatus.CONFIRMED && booking.status !== BookingStatus.PAID && booking.status !== BookingStatus.ACTIVE) {
      throw new BadRequestException(`Cannot extend booking in ${booking.status} status`);
    }

    const pricing = this.PRICING_CONFIG[booking.storageType];
    const newDuration = booking.durationDays + additionalDays;

    if (newDuration > pricing.maximumDays) {
      throw new BadRequestException(`Maximum booking duration is ${pricing.maximumDays} days`);
    }

    const additionalAmount = this.calculateBookingAmount(booking.storageType, Number(booking.totalVolume), additionalDays);

    const newEndDate = new Date(booking.endDate);
    newEndDate.setDate(newEndDate.getDate() + additionalDays);

    booking.endDate = newEndDate;
    booking.durationDays = newDuration;
    booking.baseAmount = Number(booking.baseAmount) + additionalAmount.baseAmount;
    booking.taxAmount = Number(booking.taxAmount) + additionalAmount.taxAmount;
    booking.totalAmount = Number(booking.totalAmount) + additionalAmount.totalAmount;
    booking.extensionCount += 1;
    booking.totalExtendedDays += additionalDays;

    const savedBooking = await this.bookingRepository.save(booking);
    return this.toDto(savedBooking);
  }

  private calculateBookingAmount(storageType: StorageType, volume: number, duration: number): BookingEstimateDto {
    const pricing = this.PRICING_CONFIG[storageType];

    const baseAmount = pricing.basePricePerDay * duration;
    const volumeCharge = volume * pricing.pricePerCubicMeterPerDay * duration;

    let durationDiscount = 0;
    if (duration >= 30) {
      durationDiscount = (baseAmount + volumeCharge) * pricing.discountMonthly;
    } else if (duration >= 7) {
      durationDiscount = (baseAmount + volumeCharge) * pricing.discountWeekly;
    }

    const subtotal = baseAmount + volumeCharge - durationDiscount;
    const taxAmount = subtotal * pricing.taxRate;
    const totalAmount = subtotal + taxAmount;

    const appliedDiscounts: string[] = [];
    if (duration >= 30) {
      appliedDiscounts.push(`Monthly discount: ${pricing.discountMonthly * 100}%`);
    } else if (duration >= 7) {
      appliedDiscounts.push(`Weekly discount: ${pricing.discountWeekly * 100}%`);
    }

    return {
      baseAmount: Math.round(baseAmount * 100) / 100,
      volumeCharge: Math.round(volumeCharge * 100) / 100,
      durationDiscount: Math.round(durationDiscount * 100) / 100,
      subtotal: Math.round(subtotal * 100) / 100,
      taxAmount: Math.round(taxAmount * 100) / 100,
      totalAmount: Math.round(totalAmount * 100) / 100,
      currency: 'USD',
      breakdown: {
        dailyRate: pricing.basePricePerDay,
        volumeRate: pricing.pricePerCubicMeterPerDay,
        days: duration,
        volume,
        appliedDiscounts,
      },
    };
  }

  private async generateReferenceNumber(): Promise<string> {
    const prefix = 'BK';
    const timestamp = Date.now().toString(36).toUpperCase();
    const random = uuidv4().substring(0, 6).toUpperCase();
    return `${prefix}-${timestamp}-${random}`;
  }

  private async findAlternativeDates(
    tenantId: string,
    warehouseId: string,
    storageType: StorageType,
    requiredVolume: number,
  ): Promise<Array<{ startDate: string; endDate: string; availableVolume: number }>> {
    const alternatives: Array<{ startDate: string; endDate: string; availableVolume: number }> = [];

    const now = new Date();
    for (let i = 1; i <= 3; i++) {
      const startDate = new Date(now);
      startDate.setDate(startDate.getDate() + i * 7);

      const endDate = new Date(startDate);
      endDate.setDate(endDate.getDate() + 7);

      alternatives.push({
        startDate: startDate.toISOString().split('T')[0],
        endDate: endDate.toISOString().split('T')[0],
        availableVolume: requiredVolume,
      });
    }

    return alternatives;
  }

  private toDto(booking: Booking): WarehousingBookingDto {
    return {
      id: booking.id,
      tenantId: booking.tenantId,
      referenceNumber: booking.referenceNumber,
      warehouseId: booking.warehouseId,
      warehouseName: booking.warehouseName,
      storageType: booking.storageType,
      customer: booking.customer as CustomerInfoDto,
      items: booking.items as BookingItemDto[],
      startDate: booking.startDate.toISOString().split('T')[0],
      endDate: booking.endDate.toISOString().split('T')[0],
      totalVolume: Number(booking.totalVolume),
      totalWeight: Number(booking.totalWeight),
      durationDays: booking.durationDays,
      baseAmount: Number(booking.baseAmount),
      taxAmount: Number(booking.taxAmount),
      totalAmount: Number(booking.totalAmount),
      currency: booking.currency,
      status: booking.status,
      specialInstructions: booking.specialInstructions,
      createdAt: booking.createdAt.toISOString(),
      updatedAt: booking.updatedAt.toISOString(),
      confirmedAt: booking.confirmedAt?.toISOString(),
      cancelledAt: booking.cancelledAt?.toISOString(),
      cancellationReason: booking.cancellationReason,
    };
  }
}
