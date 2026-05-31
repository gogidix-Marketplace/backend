import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class ForecastPeriodDocument extends Document {
  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  forecastId: string;

  @Prop({ required: true, enum: ['MONTHLY', 'QUARTERLY', 'ANNUAL'] })
  periodType: string;

  @Prop({ required: true, type: Date })
  periodStartDate: Date;

  @Prop({ required: true, type: Date })
  periodEndDate: Date;

  @Prop({ required: true })
  periodName: string;

  @Prop({ required: true })
  sequence: number;

  @Prop({ required: true })
  forecastAmount: number;

  @Prop({ required: true })
  weightedAmount: number;

  @Prop({ required: true })
  bestCase: number;

  @Prop({ required: true })
  worstCase: number;

  @Prop({ required: true })
  confidence: number;

  @Prop({ required: true })
  dealsCount: number;

  @Prop({ required: true, default: 0 })
  wonDealsCount: number;

  @Prop()
  actualAmount?: number;

  @Prop()
  variance?: number;
}

export const ForecastPeriodSchema = SchemaFactory.createForClass(ForecastPeriodDocument);

// Indexes
ForecastPeriodSchema.index({ tenantId: 1, forecastId: 1, sequence: 1 });
ForecastPeriodSchema.index({ tenantId: 1, periodStartDate: 1, periodEndDate: 1 });
ForecastPeriodSchema.index({ tenantId: 1, forecastId: 1 });
