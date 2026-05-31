import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class ForecastModelDocument extends Document {
  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  name: string;

  @Prop({ required: true, enum: ['WEIGHTED_PIPELINE', 'HISTORICAL', 'AI_ML', 'HYBRID'] })
  modelType: string;

  @Prop({ required: true })
  description: string;

  @Prop({ required: true })
  version: string;

  @Prop({ required: true, default: true })
  isActive: boolean;

  @Prop({ required: true, default: false })
  isDefault: boolean;

  @Prop({ required: true, type: [{}] })
  parameters: Array<{
    name: string;
    value: number | string | boolean;
    description?: string;
  }>;

  @Prop({ type: Map, of: Number })
  weights: Map<string, number>;

  @Prop({
    required: true,
    type: {
      accuracy: { type: Number },
      mape: { type: Number },
      mae: { type: Number },
      rmse: { type: Number },
      bias: { type: Number },
      lastCalculatedAt: { type: Date },
    },
  })
  metrics: {
    accuracy: number;
    mape: number;
    mae: number;
    rmse: number;
    bias: number;
    lastCalculatedAt: Date;
  };

  @Prop({ required: true })
  createdBy: string;

  @Prop({ type: Date })
  lastCalibratedAt?: Date;
}

export const ForecastModelSchema = SchemaFactory.createForClass(ForecastModelDocument);

// Indexes
ForecastModelSchema.index({ tenantId: 1, modelType: 1 });
ForecastModelSchema.index({ tenantId: 1, isActive: 1 });
ForecastModelSchema.index({ tenantId: 1, isDefault: 1 });
