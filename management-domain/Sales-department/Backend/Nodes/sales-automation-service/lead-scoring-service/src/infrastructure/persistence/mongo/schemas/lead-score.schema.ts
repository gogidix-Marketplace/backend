import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type LeadScoreDocument = LeadScoreSchema & Document;

@Schema({ collection: 'lead_scores', timestamps: true })
export class LeadScoreSchema {
  @Prop({ required: true, unique: true, index: true })
  _id: string;

  @Prop({ required: true, index: true })
  leadId: string;

  @Prop({ required: true, index: true })
  tenantId: string;

  @Prop({ required: true, index: true })
  scoreModelId: string;

  @Prop({ required: true })
  totalScore: number;

  @Prop({ required: true, enum: ['A', 'B', 'C', 'D'] })
  grade: string;

  @Prop({ required: true, enum: ['demographic', 'behavioral', 'firmographic', 'engagement', 'custom', 'combined'] })
  scoreType: string;

  @Prop({ type: Object, required: true })
  breakdown: {
    demographicScore: number;
    behavioralScore: number;
    engagementScore: number;
    firmographicScore: number;
    customScores: Record<string, number>;
  };

  @Prop({ type: [Object], required: true })
  attributes: Array<{
    name: string;
    value: any;
    weight: number;
    contribution: number;
  }>;

  @Prop({ required: true, type: Date })
  lastScoredAt: Date;

  @Prop({ required: true })
  scoreDecayRate: number;

  @Prop({ required: true, default: 0 })
  decayApplied: number;

  @Prop({ type: Date })
  lastDecayAppliedAt: Date;

  @Prop({ type: String })
  variantId: string;

  @Prop({ required: true, default: false })
  isControlGroup: boolean;

  @Prop({ type: Object, default: {} })
  metadata: Record<string, any>;

  @Prop({ required: true, type: Date })
  createdAt: Date;

  @Prop({ required: true, type: Date })
  updatedAt: Date;
}

export const LeadScoreSchemaModel = SchemaFactory.createForClass(LeadScoreSchema);

// Indexes
LeadScoreSchemaModel.index({ leadId: 1, tenantId: 1 }, { unique: true });
LeadScoreSchemaModel.index({ tenantId: 1, totalScore: -1 });
LeadScoreSchemaModel.index({ tenantId: 1, grade: 1 });
LeadScoreSchemaModel.index({ scoreModelId: 1 });
LeadScoreSchemaModel.index({ variantId: 1 });
LeadScoreSchemaModel.index({ lastScoredAt: 1 });
