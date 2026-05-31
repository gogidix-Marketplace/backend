import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class ForecastAccuracyDocument extends Document {
  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  forecastId: string;

  @Prop()
  periodId?: string;

  @Prop({ required: true })
  modelUsed: string;

  @Prop({ required: true, type: [{}] })
  metrics: Array<{
    metric: string;
    value: number;
    description?: string;
  }>;

  @Prop({ required: true })
  overallAccuracy: number;

  @Prop({ required: true })
  sampleSize: number;

  @Prop({ required: true, type: Date })
  comparisonStartDate: Date;

  @Prop({ required: true, type: Date })
  comparisonEndDate: Date;

  @Prop({ required: true })
  calculatedBy: string;

  @Prop({ required: true, enum: ['IMPROVING', 'DECLINING', 'STABLE'], default: 'STABLE' })
  trend: string;

  @Prop()
  previousAccuracy?: number;

  @Prop()
  notes?: string;
}

export const ForecastAccuracySchema = SchemaFactory.createForClass(ForecastAccuracyDocument);

// Indexes
ForecastAccuracySchema.index({ tenantId: 1, forecastId: 1, calculatedAt: -1 });
ForecastAccuracySchema.index({ tenantId: 1, forecastId: 1, periodId: 1 });
ForecastAccuracySchema.index({ tenantId: 1, overallAccuracy: 1 });
