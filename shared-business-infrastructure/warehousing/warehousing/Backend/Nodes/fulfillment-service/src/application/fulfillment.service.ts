import { Injectable, Logger, NotFoundException, BadRequestException } from '@nestjs/common';
import { InjectModel } from '@nestjs/mongoose';
import { Model } from 'mongoose';
import { v4 as uuidv4 } from 'uuid';
import {
  CreateFulfillmentCommand,
  UpdateStatusCommand,
  FulfillmentQueryDto,
  FulfillmentOrderDto,
  ShipmentManifestDto,
} from './dto/fulfillment.dto';
import { Fulfillment, FulfillmentDocument } from '../domain/entities/fulfillment.entity';

@Injectable()
export class FulfillmentService {
  private readonly logger = new Logger(FulfillmentService.name);

  constructor(
    @InjectModel(Fulfillment.name)
    private fulfillmentModel: Model<FulfillmentDocument>,
  ) {}

  async createFulfillmentOrder(command: CreateFulfillmentCommand): Promise<FulfillmentOrderDto> {
    this.logger.log(`Creating fulfillment for order ${command.orderId} for tenant ${command.tenantId}`);

    const existing = await this.fulfillmentModel.findOne({
      tenantId: command.tenantId,
      orderId: command.orderId,
    });

    if (existing) {
      throw new BadRequestException(`Fulfillment already exists for order ${command.orderId}`);
    }

    const fulfillment = new this.fulfillmentModel({
      orderId: command.orderId,
      tenantId: command.tenantId,
      warehouseId: command.warehouseId,
      customerId: command.customerId,
      customerEmail: command.customerEmail,
      shippingAddress: command.shippingAddress,
      billingAddress: command.billingAddress,
      items: command.items.map((item) => ({
        sku: item.sku,
        productName: item.productName,
        quantity: item.quantity,
        unitPrice: item.unitPrice,
        weight: item.weight,
        dimensions: item.dimensions,
        isFragile: item.isFragile,
        requiresColdStorage: item.requiresColdStorage,
        location: item.location,
      })),
      status: 'PENDING',
      priority: command.priority || 'STANDARD',
      notes: command.notes,
    });

    const saved = await fulfillment.save();
    return this.toDto(saved);
  }

  async getFulfillmentOrder(tenantId: string, fulfillmentId: string): Promise<FulfillmentOrderDto> {
    const fulfillment = await this.fulfillmentModel.findOne({
      _id: fulfillmentId,
      tenantId,
    });

    if (!fulfillment) {
      throw new NotFoundException(`Fulfillment ${fulfillmentId} not found`);
    }

    return this.toDto(fulfillment);
  }

  async getFulfillmentByOrderId(tenantId: string, orderId: string): Promise<FulfillmentOrderDto> {
    const fulfillment = await this.fulfillmentModel.findOne({
      tenantId,
      orderId,
    });

    if (!fulfillment) {
      throw new NotFoundException(`Fulfillment for order ${orderId} not found`);
    }

    return this.toDto(fulfillment);
  }

  async queryFulfillmentOrders(query: FulfillmentQueryDto): Promise<{ data: FulfillmentOrderDto[]; total: number }> {
    const filter: any = { tenantId: query.tenantId };

    if (query.warehouseId) filter.warehouseId = query.warehouseId;
    if (query.orderId) filter.orderId = query.orderId;
    if (query.status) filter.status = query.status;
    if (query.customerId) filter.customerId = query.customerId;

    if (query.fromDate || query.toDate) {
      filter.createdAt = {};
      if (query.fromDate) filter.createdAt.$gte = query.fromDate;
      if (query.toDate) filter.createdAt.$lte = query.toDate;
    }

    const page = query.page || 1;
    const limit = query.limit || 20;
    const skip = (page - 1) * limit;

    const sort: any = {};
    if (query.sortBy) {
      sort[query.sortBy] = query.sortOrder === 'DESC' ? -1 : 1;
    } else {
      sort.createdAt = -1;
    }

    const [data, total] = await Promise.all([
      this.fulfillmentModel.find(filter).sort(sort).skip(skip).limit(limit).exec(),
      this.fulfillmentModel.countDocuments(filter),
    ]);

    return {
      data: data.map((f) => this.toDto(f)),
      total,
    };
  }

  async updateStatus(command: UpdateStatusCommand): Promise<FulfillmentOrderDto> {
    const fulfillment = await this.fulfillmentModel.findOne({
      _id: command.fulfillmentId,
      tenantId: command.tenantId,
    });

    if (!fulfillment) {
      throw new NotFoundException(`Fulfillment ${command.fulfillmentId} not found`);
    }

    const validTransitions: Record<string, string[]> = {
      PENDING: ['PROCESSING', 'CANCELLED'],
      PROCESSING: ['PICKED', 'CANCELLED'],
      PICKED: ['PACKED', 'PROCESSING', 'CANCELLED'],
      PACKED: ['SHIPPED', 'CANCELLED'],
      SHIPPED: ['DELIVERED'],
      DELIVERED: [],
      CANCELLED: [],
    };

    if (!validTransitions[fulfillment.status]?.includes(command.status)) {
      throw new BadRequestException(
        `Cannot transition from ${fulfillment.status} to ${command.status}`,
      );
    }

    fulfillment.status = command.status as any;
    if (command.notes) {
      fulfillment.notes = fulfillment.notes
        ? `${fulfillment.notes}\n${command.notes}`
        : command.notes;
    }

    if (command.status === 'CANCELLED') {
      fulfillment.cancelledAt = new Date();
      fulfillment.cancellationReason = command.notes;
    }

    const saved = await fulfillment.save();
    return this.toDto(saved);
  }

  async shipOrder(
    tenantId: string,
    fulfillmentId: string,
    carrierId: string,
    trackingNumber?: string,
  ): Promise<ShipmentManifestDto> {
    const fulfillment = await this.fulfillmentModel.findOne({
      _id: fulfillmentId,
      tenantId,
    });

    if (!fulfillment) {
      throw new NotFoundException(`Fulfillment ${fulfillmentId} not found`);
    }

    if (fulfillment.status !== 'PACKED') {
      throw new BadRequestException('Fulfillment must be PACKED before shipping');
    }

    fulfillment.status = 'SHIPPED';
    fulfillment.carrierId = carrierId;
    fulfillment.carrierName = this.getCarrierName(carrierId);
    fulfillment.trackingNumber = trackingNumber || this.generateTrackingNumber();
    fulfillment.shippedAt = new Date();

    const saved = await fulfillment.save();

    return this.toManifestDto(saved);
  }

  async deliverOrder(
    tenantId: string,
    fulfillmentId: string,
    deliveryProof?: string,
    notes?: string,
  ): Promise<FulfillmentOrderDto> {
    const fulfillment = await this.fulfillmentModel.findOne({
      _id: fulfillmentId,
      tenantId,
    });

    if (!fulfillment) {
      throw new NotFoundException(`Fulfillment ${fulfillmentId} not found`);
    }

    if (fulfillment.status !== 'SHIPPED') {
      throw new BadRequestException('Fulfillment must be SHIPPED before delivery');
    }

    fulfillment.status = 'DELIVERED';
    fulfillment.actualDelivery = new Date();
    fulfillment.deliveryProof = deliveryProof;
    if (notes) {
      fulfillment.notes = fulfillment.notes ? `${fulfillment.notes}\n${notes}` : notes;
    }

    const saved = await fulfillment.save();
    return this.toDto(saved);
  }

  async cancelOrder(tenantId: string, fulfillmentId: string, reason?: string): Promise<FulfillmentOrderDto> {
    const fulfillment = await this.fulfillmentModel.findOne({
      _id: fulfillmentId,
      tenantId,
    });

    if (!fulfillment) {
      throw new NotFoundException(`Fulfillment ${fulfillmentId} not found`);
    }

    if (!['PENDING', 'PROCESSING', 'PICKED'].includes(fulfillment.status)) {
      throw new BadRequestException('Cannot cancel fulfillment in current status');
    }

    fulfillment.status = 'CANCELLED';
    fulfillment.cancelledAt = new Date();
    fulfillment.cancellationReason = reason;

    const saved = await fulfillment.save();
    return this.toDto(saved);
  }

  async getPendingOrders(tenantId: string, warehouseId: string): Promise<FulfillmentOrderDto[]> {
    const fulfillments = await this.fulfillmentModel
      .find({
        tenantId,
        warehouseId,
        status: { $in: ['PENDING', 'PROCESSING'] },
      })
      .sort({ priority: -1, createdAt: 1 })
      .exec();

    return fulfillments.map((f) => this.toDto(f));
  }

  async getShipmentManifest(tenantId: string, fulfillmentId: string): Promise<ShipmentManifestDto> {
    const fulfillment = await this.fulfillmentModel.findOne({
      _id: fulfillmentId,
      tenantId,
    });

    if (!fulfillment) {
      throw new NotFoundException(`Fulfillment ${fulfillmentId} not found`);
    }

    return this.toManifestDto(fulfillment);
  }

  async processBatch(tenantId: string, fulfillmentIds: string[]): Promise<FulfillmentOrderDto[]> {
    const results: FulfillmentOrderDto[] = [];

    for (const id of fulfillmentIds) {
      try {
        const fulfillment = await this.fulfillmentModel.findOne({
          _id: id,
          tenantId,
        });

        if (fulfillment && fulfillment.status === 'PENDING') {
          fulfillment.status = 'PROCESSING';
          const saved = await fulfillment.save();
          results.push(this.toDto(saved));
        }
      } catch (error) {
        this.logger.error(`Error processing fulfillment ${id}: ${error.message}`);
      }
    }

    return results;
  }

  private toDto(doc: FulfillmentDocument): FulfillmentOrderDto {
    return {
      id: doc._id.toString(),
      orderId: doc.orderId,
      tenantId: doc.tenantId,
      warehouseId: doc.warehouseId,
      customerId: doc.customerId,
      customerEmail: doc.customerEmail,
      items: doc.items.map((item) => ({
        sku: item.sku,
        productName: item.productName,
        quantity: item.quantity,
        unitPrice: item.unitPrice,
        weight: item.weight,
        dimensions: item.dimensions,
        isFragile: item.isFragile,
        requiresColdStorage: item.requiresColdStorage,
        location: item.location,
      })),
      shippingAddress: doc.shippingAddress,
      billingAddress: doc.billingAddress,
      status: doc.status,
      priority: doc.priority,
      assignedTo: doc.assignedTo,
      trackingNumber: doc.trackingNumber,
      carrierId: doc.carrierId,
      carrierName: doc.carrierName,
      estimatedDelivery: doc.estimatedDelivery,
      actualDelivery: doc.actualDelivery,
      shippedAt: doc.shippedAt,
      notes: doc.notes,
      createdAt: doc.createdAt,
      updatedAt: doc.updatedAt,
    };
  }

  private toManifestDto(doc: FulfillmentDocument): ShipmentManifestDto {
    const totalWeight = doc.items.reduce((sum, item) => sum + (item.weight || 0) * item.quantity, 0);
    const totalValue = doc.items.reduce((sum, item) => sum + item.unitPrice * item.quantity, 0);

    return {
      fulfillmentId: doc._id.toString(),
      orderId: doc.orderId,
      tenantId: doc.tenantId,
      warehouseId: doc.warehouseId,
      carrierId: doc.carrierId,
      carrierName: doc.carrierName,
      trackingNumber: doc.trackingNumber,
      manifestDate: doc.shippedAt || new Date(),
      packages: [
        {
          packageId: uuidv4(),
          weight: totalWeight,
          dimensions: { length: 10, width: 10, height: 10 },
          items: doc.items.map((item) => ({
            sku: item.sku,
            name: item.productName,
            quantity: item.quantity,
          })),
        },
      ],
      totalWeight,
      totalValue,
      status: doc.status,
    };
  }

  private getCarrierName(carrierId: string): string {
    const carriers: Record<string, string> = {
      UPS: 'United Parcel Service',
      FEDEX: 'Federal Express',
      DHL: 'DHL Express',
      USPS: 'United States Postal Service',
      ONTRAC: 'OnTrac',
      LSO: 'Lone Star Overnight',
    };
    return carriers[carrierId] || carrierId;
  }

  private generateTrackingNumber(): string {
    const prefix = 'FUL';
    const timestamp = Date.now().toString(36).toUpperCase();
    const random = Math.random().toString(36).substring(2, 8).toUpperCase();
    return `${prefix}${timestamp}${random}`;
  }
}
