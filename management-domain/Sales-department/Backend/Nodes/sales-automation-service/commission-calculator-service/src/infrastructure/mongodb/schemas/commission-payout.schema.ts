import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

/**
 * MongoDB schema for CommissionPayout
 */
@Schema({ timestamps: true, versionKey: false })
export class CommissionPayoutDocument extends Document {
  @Prop({ required: true })
  salesRepId: string;

  @Prop({ required: true })
  salesRepName: string;

  @Prop({ required: true })
  periodId: string;

  @Prop({ required: true })
  periodName: string;

  @Prop({ type: [String], default: [] })
  commissionIds: string[];

  @Prop({
    required: true,
    enum: ['PENDING', 'APPROVED', 'PROCESSING', 'COMPLETED', 'FAILED', 'CANCELLED'],
    default: 'PENDING',
  })
  status: string;

  @Prop({ required: true, type: Number })
  grossAmount: number;

  @Prop({ required: true, default: 0, type: Number })
  adjustments: number;

  @Prop({ required: true, default: 0, type: Number })
  taxWithholdings: number;

  @Prop({ required: true, default: 0, type: Number })
  deductions: number;

  @Prop({ required: true, type: Number })
  netAmount: number;

  @Prop({ required: true, default: 'USD' })
  currency: string;

  @Prop({ required: true, type: Date })
  scheduledDate: Date;

  @Prop({ type: Date })
  processedDate?: Date;

  @Prop({ type: Date })
  completedDate?: Date;

  @Prop({ type: String })
  paymentMethod?: string;

  @Prop({ type: String })
  paymentReference?: string;

  @Prop({ type: String })
  failureReason?: string;

  @Prop({ type: String })
  approvedBy?: string;

  @Prop({ type: Date })
  approvedAt?: Date;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  organizationId: string;

  @Prop({ type: String })
  notes?: string;

  @Prop({ required: true, default: 0 })
  version: number;
}

export const CommissionPayoutSchema = SchemaFactory.createForClass(CommissionPayoutDocument);

// Indexes
CommissionPayoutSchema.index({ salesRepId: 1, tenantId: 1 });
CommissionPayoutSchema.index({ periodId: 1 });
CommissionPayoutSchema.index({ status: 1, tenantId: 1 });
CommissionPayoutSchema.index({ scheduledDate: 1 });
CommissionPayoutSchema.index({ tenantId: 1, organizationId: 1 });
