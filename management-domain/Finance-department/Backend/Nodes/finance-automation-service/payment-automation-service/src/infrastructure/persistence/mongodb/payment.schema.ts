import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';

@Schema({ timestamps: true })
export class PaymentDocument extends Document {
  @Prop({ required: true })
  status: string;

  @Prop({ required: true })
  amount: number;

  @Prop({ required: true })
  currency: string;

  @Prop({ required: true })
  paymentMethod: string;

  @Prop({ required: true })
  gateway: string;

  @Prop({ required: true })
  vendorId: string;

  @Prop({ required: true })
  vendorName: string;

  @Prop({ required: true })
  accountId: string;

  @Prop()
  scheduledAt?: Date;

  @Prop()
  processedAt?: Date;

  @Prop()
  completedAt?: Date;

  @Prop()
  failedAt?: Date;

  @Prop()
  cancelledAt?: Date;

  @Prop({ type: Object })
  metadata?: Record<string, any>;

  @Prop()
  externalReference?: string;

  @Prop({ type: Object })
  gatewayResponse?: any;

  @Prop()
  failureReason?: string;

  @Prop({ default: 0 })
  retryCount: number;

  @Prop({ default: 3 })
  maxRetries: number;

  @Prop()
  batchId?: string;

  @Prop({ enum: ['PENDING', 'MATCHED', 'UNMATCHED'], default: 'PENDING' })
  reconciliationStatus: string;

  @Prop()
  reconciledAt?: Date;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ default: 1 })
  version: number;
}

export const PaymentSchema = SchemaFactory.createForClass(PaymentDocument);

PaymentSchema.index({ vendorId: 1, createdAt: -1 });
PaymentSchema.index({ status: 1, tenantId: 1 });
PaymentSchema.index({ batchId: 1 });
PaymentSchema.index({ scheduledAt: 1 });
PaymentSchema.index({ tenantId: 1, createdAt: -1 });

@Schema({ timestamps: true })
export class PaymentBatchDocument extends Document {
  @Prop({ required: true })
  name: string;

  @Prop()
  description: string;

  @Prop({ required: true })
  status: string;

  @Prop({ type: [String], default: [] })
  paymentIds: string[];

  @Prop({ required: true })
  gateway: string;

  @Prop()
  scheduledAt?: Date;

  @Prop()
  processingStartedAt?: Date;

  @Prop()
  completedAt?: Date;

  @Prop({ required: true })
  totalAmount: number;

  @Prop({ required: true })
  currency: string;

  @Prop({ type: Object })
  metadata?: Record<string, any>;

  @Prop({ default: 0 })
  priority: number;

  @Prop({ default: 10 })
  maxConcurrent: number;

  @Prop({ required: true })
  createdBy: string;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ default: 1 })
  version: number;
}

export const PaymentBatchSchema = SchemaFactory.createForClass(PaymentBatchDocument);

PaymentBatchSchema.index({ status: 1, tenantId: 1 });
PaymentBatchSchema.index({ scheduledAt: 1 });
PaymentBatchSchema.index({ tenantId: 1, createdAt: -1 });

@Schema({ timestamps: true })
export class PaymentRuleDocument extends Document {
  @Prop({ required: true })
  name: string;

  @Prop()
  description: string;

  @Prop({ required: true })
  ruleType: string;

  @Prop({ type: Array })
  conditions: any[];

  @Prop({ type: Array })
  actions: any[];

  @Prop({ default: 0 })
  priority: number;

  @Prop({ default: true })
  isActive: boolean;

  @Prop()
  effectiveFrom?: Date;

  @Prop()
  effectiveTo?: Date;

  @Prop({ type: Object })
  metadata?: Record<string, any>;

  @Prop({ default: 0 })
  matchCount: number;

  @Prop()
  lastMatchedAt?: Date;

  @Prop({ required: true })
  createdBy: string;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ default: 1 })
  version: number;
}

export const PaymentRuleSchema = SchemaFactory.createForClass(PaymentRuleDocument);

PaymentRuleSchema.index({ isActive: 1, tenantId: 1 });
PaymentRuleSchema.index({ ruleType: 1 });
PaymentRuleSchema.index({ tenantId: 1, createdAt: -1 });

@Schema({ timestamps: true })
export class VendorPaymentDocument extends Document {
  @Prop({ required: true, unique: true })
  vendorId: string;

  @Prop({ required: true })
  vendorName: string;

  @Prop({ required: true })
  vendorCode: string;

  @Prop({ required: true })
  email: string;

  @Prop()
  phone?: string;

  @Prop()
  taxId?: string;

  @Prop({ type: Object })
  bankDetails: {
    bankName: string;
    accountNumber: string;
    routingNumber?: string;
    swiftCode?: string;
    iban?: string;
    accountType?: string;
    address?: {
      street: string;
      city: string;
      state: string;
      postalCode: string;
      country: string;
    };
  };

  @Prop({ type: Object })
  paymentSettings: {
    paymentTerms: number;
    autoPayEnabled: boolean;
    preferredPaymentMethod: string;
    minimumPaymentAmount?: number;
    maximumPaymentAmount?: number;
    requireApprovalAbove?: number;
    currency: string;
  };

  @Prop({ default: true })
  isActive: boolean;

  @Prop({ default: 0 })
  totalPaidAmount: number;

  @Prop({ default: 0 })
  totalPaymentCount: number;

  @Prop()
  lastPaymentDate?: Date;

  @Prop()
  nextPaymentDate?: Date;

  @Prop({ type: Object })
  metadata?: Record<string, any>;

  @Prop()
  creditScore?: number;

  @Prop({ enum: ['EXCELLENT', 'GOOD', 'AVERAGE', 'POOR'] })
  paymentRating?: string;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ default: 1 })
  version: number;
}

export const VendorPaymentSchema = SchemaFactory.createForClass(VendorPaymentDocument);

VendorPaymentSchema.index({ vendorId: 1 });
VendorPaymentSchema.index({ vendorCode: 1 });
VendorPaymentSchema.index({ isActive: 1, tenantId: 1 });
VendorPaymentSchema.index({ tenantId: 1, createdAt: -1 });
