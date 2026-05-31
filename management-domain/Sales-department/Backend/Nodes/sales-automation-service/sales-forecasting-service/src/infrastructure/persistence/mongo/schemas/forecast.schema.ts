import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';

@Schema({ timestamps: true })
export class ForecastDocument {
  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  name: string;

  @Prop({ required: true })
  description: string;

  @Prop({ required: true, enum: ['WEIGHTED_PIPELINE', 'HISTORICAL', 'AI_ML', 'HYBRID'] })
  model: string;

  @Prop({ required: true, enum: ['MONTHLY', 'QUARTERLY', 'ANNUAL'] })
  period: string;

  @Prop({ required: true, enum: ['DRAFT', 'ACTIVE', 'ARCHIVED'], default: 'DRAFT' })
  status: string;

  @Prop({ required: true, enum: ['GLOBAL', 'TERRITORY', 'TEAM', 'REP'] })
  granularity: string;

  @Prop()
  granularityId?: string;

  @Prop({ required: true, type: [{}] })
  dataPoints: Array<{
    period: string;
    amount: number;
    confidence: number;
    weightedAmount: number;
    bestCase: number;
    worstCase: number;
    dealsCount: number;
  }>;

  @Prop({
    required: true,
    type: {
      byStage: { type: Map, of: Number },
      byProduct: { type: Map, of: Number },
      byRep: { type: Map, of: Number },
      byTerritory: { type: Map, of: Number },
    },
  })
  breakdown: {
    byStage: Map<string, number>;
    byProduct: Map<string, number>;
    byRep: Map<string, number>;
    byTerritory: Map<string, number>;
  };

  @Prop({ required: true })
  totalForecast: number;

  @Prop({ required: true })
  totalWeightedForecast: number;

  @Prop({ required: true })
  currency: string;

  @Prop({ required: true, type: Date })
  startDate: Date;

  @Prop({ required: true, type: Date })
  endDate: Date;

  @Prop({ required: true })
  generatedBy: string;

  @Prop({ required: true })
  lastUpdatedBy: string;

  @Prop({ required: true, default: 1 })
  version: number;

  @Prop()
  previousForecastId?: string;

  @Prop({ required: true, default: 85 })
  confidenceLevel: number;

  @Prop({ type: Map, of: Types.ObjectId })
  metadata: Map<string, any>;
}

export const ForecastSchema = SchemaFactory.createForClass(ForecastDocument);

// Indexes
ForecastSchema.index({ tenantId: 1, createdAt: -1 });
ForecastSchema.index({ tenantId: 1, status: 1 });
ForecastSchema.index({ tenantId: 1, model: 1 });
ForecastSchema.index({ tenantId: 1, granularity: 1, granularityId: 1 });
ForecastSchema.index({ name: 1, tenantId: 1 }, { unique: false });

