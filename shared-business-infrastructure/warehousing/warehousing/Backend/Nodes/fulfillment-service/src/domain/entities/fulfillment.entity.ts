import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export interface FulfillmentDocument extends Fulfillment, Document {
  createdAt: Date;
  updatedAt: Date;
}

@Schema({ timestamps: true, collection: 'fulfillments' })
export class Fulfillment {
  @Prop({ required: true, index: true })
  orderId: string;

  @Prop({ required: true, index: true })
  tenantId: string;

  @Prop({ required: true, index: true })
  warehouseId: string;

  @Prop({ required: true })
  customerId: string;

  @Prop({ required: true })
  customerEmail: string;

  @Prop({ type: Object, required: true })
  shippingAddress: {
    name: string;
    company?: string;
    addressLine1: string;
    addressLine2?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    phone?: string;
  };

  @Prop({ type: Object })
  billingAddress?: {
    name: string;
    company?: string;
    addressLine1: string;
    addressLine2?: string;
    city: string;
    state: string;
    postalCode: string;
    country: string;
    phone?: string;
  };

  @Prop({
    type: [
      {
        sku: String,
        productName: String,
        quantity: Number,
        unitPrice: Number,
        weight: Number,
        dimensions: Object,
        isFragile: Boolean,
        requiresColdStorage: Boolean,
        location: String,
        pickedAt: Date,
        pickedBy: String,
        packedAt: Date,
        packedBy: String,
      },
    ],
    required: true,
  })
  items: FulfillmentItem[];

  @Prop({
    enum: ['PENDING', 'PROCESSING', 'PICKED', 'PACKED', 'SHIPPED', 'DELIVERED', 'CANCELLED'],
    default: 'PENDING',
    index: true,
  })
  status: string;

  @Prop({
    enum: ['STANDARD', 'EXPRESS', 'PRIORITY', 'SAME_DAY'],
    default: 'STANDARD',
  })
  priority: string;

  @Prop()
  assignedTo?: string;

  @Prop()
  trackingNumber?: string;

  @Prop()
  carrierId?: string;

  @Prop()
  carrierName?: string;

  @Prop()
  estimatedDelivery?: Date;

  @Prop()
  actualDelivery?: Date;

  @Prop()
  shippedAt?: Date;

  @Prop()
  notes?: string;

  @Prop()
  deliveredBy?: string;

  @Prop()
  deliveryProof?: string;

  @Prop()
  cancelledAt?: Date;

  @Prop()
  cancelledBy?: string;

  @Prop()
  cancellationReason?: string;
}

interface FulfillmentItem {
  sku: string;
  productName: string;
  quantity: number;
  unitPrice: number;
  weight?: number;
  dimensions?: {
    length: number;
    width: number;
    height: number;
  };
  isFragile?: boolean;
  requiresColdStorage?: boolean;
  location?: string;
  pickedAt?: Date;
  pickedBy?: string;
  packedAt?: Date;
  packedBy?: string;
}

export const FulfillmentSchema = SchemaFactory.createForClass(Fulfillment);

FulfillmentSchema.index({ tenantId: 1, orderId: 1 }, { unique: true });
FulfillmentSchema.index({ tenantId: 1, status: 1 });
FulfillmentSchema.index({ tenantId: 1, warehouseId: 1, status: 1 });
FulfillmentSchema.index({ tenantId: 1, createdAt: -1 });
