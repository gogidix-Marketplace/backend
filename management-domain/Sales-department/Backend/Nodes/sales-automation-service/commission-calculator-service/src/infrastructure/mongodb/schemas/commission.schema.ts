import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';

/**
 * MongoDB schema for Commission
 */
@Schema({ timestamps: true, versionKey: false })
export class CommissionDocument extends Document {
  @Prop({ required: true })
  salesRepId: string;

  @Prop({ required: true })
  salesRepName: string;

  @Prop({ required: true })
  periodId: string;

  @Prop({ required: true })
  ruleId: string;

  @Prop({ required: true })
  ruleName: string;

  @Prop({
    required: true,
    enum: ['DRAFT', 'PENDING', 'CALCULATED', 'APPROVED', 'PAID', 'CLAWED_BACK', 'CANCELLED'],
  })
  status: string;

  @Prop({ required: true, type: Number })
  salesAmount: number;

  @Prop({ required: true, type: Number })
  commissionRate: number;

  @Prop({ required: true, type: Number })
  calculatedAmount: number;

  @Prop({ required: true, default: 0, type: Number })
  adjustedAmount: number;

  @Prop({ required: true, type: Number })
  finalAmount: number;

  @Prop({ required: true, default: 'USD' })
  currency: string;

  @Prop({ required: true, type: Date })
  transactionDate: Date;

  @Prop({ type: Date })
  settlementDate?: Date;

  @Prop({ type: Date })
  paymentDate?: Date;

  @Prop({ type: Array, default: [] })
  splits: Array<{
    salesRepId: string;
    salesRepName: string;
    percentage: number;
    role: string;
  }>;

  @Prop({ type: String })
  adjustmentReason?: string;

  @Prop({ required: true, default: 0, type: Number })
  clawbackAmount: number;

  @Prop({ type: String })
  clawbackReason?: string;

  @Prop({ type: Date })
  clawbackDate?: Date;

  @Prop({ type: Number })
  quotaAttained?: number;

  @Prop({ type: Number })
  quotaTarget?: number;

  @Prop({ type: Number })
  acceleratorApplied?: number;

  @Prop({ type: Array, default: [] })
  productSales: Array<{
    productId: string;
    productName: string;
    amount: number;
    commission: number;
  }>;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  organizationId: string;

  @Prop({ type: String })
  approvedBy?: string;

  @Prop({ type: Date })
  approvedAt?: Date;

  @Prop({ type: String })
  paidVia?: string;

  @Prop({ type: String })
  paymentReference?: string;

  @Prop({ required: true, default: 0 })
  version: number;
}

export const CommissionSchema = SchemaFactory.createForClass(CommissionDocument);

// Indexes for efficient queries
CommissionSchema.index({ salesRepId: 1, tenantId: 1 });
CommissionSchema.index({ periodId: 1 });
CommissionSchema.index({ status: 1, tenantId: 1 });
CommissionSchema.index({ transactionDate: 1 });
CommissionSchema.index({ settlementDate: 1 });
CommissionSchema.index({ tenantId: 1, organizationId: 1 });
CommissionSchema.index({ salesRepId: 1, periodId: 1 });
