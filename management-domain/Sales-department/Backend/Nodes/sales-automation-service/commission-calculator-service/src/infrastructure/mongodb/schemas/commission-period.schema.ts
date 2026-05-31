import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

/**
 * MongoDB schema for CommissionPeriod
 */
@Schema({ timestamps: true, versionKey: false })
export class CommissionPeriodDocument extends Document {
  @Prop({ required: true })
  name: string;

  @Prop({
    required: true,
    enum: ['MONTHLY', 'QUARTERLY', 'ANNUAL', 'CUSTOM'],
  })
  periodType: string;

  @Prop({ required: true, type: Date })
  startDate: Date;

  @Prop({ required: true, type: Date })
  endDate: Date;

  @Prop({
    required: true,
    enum: ['OPEN', 'CALCULATING', 'CLOSED', 'LOCKED'],
    default: 'OPEN',
  })
  status: string;

  @Prop({ type: Date })
  processingDate?: Date;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  organizationId: string;

  @Prop({ required: true, type: Number })
  fiscalYear: number;

  @Prop({ type: Number })
  fiscalQuarter?: number;

  @Prop({ type: Number })
  fiscalMonth?: number;

  @Prop({ required: true, default: 0 })
  version: number;
}

export const CommissionPeriodSchema = SchemaFactory.createForClass(CommissionPeriodDocument);

// Indexes
CommissionPeriodSchema.index({ tenantId: 1, organizationId: 1 });
CommissionPeriodSchema.index({ status: 1, tenantId: 1 });
CommissionPeriodSchema.index({ startDate: 1, endDate: 1 });
CommissionPeriodSchema.index({ fiscalYear: 1, fiscalMonth: 1 });
