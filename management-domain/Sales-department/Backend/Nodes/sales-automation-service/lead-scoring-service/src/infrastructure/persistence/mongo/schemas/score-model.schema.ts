import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type ScoreModelDocument = ScoreModelSchema & Document;

@Schema({ collection: 'score_models', timestamps: true })
export class ScoreModelSchema {
  @Prop({ required: true, unique: true, index: true })
  _id: string;

  @Prop({ required: true, index: true })
  tenantId: string;

  @Prop({ required: true })
  name: string;

  @Prop({ required: false })
  description: string;

  @Prop({ required: true, enum: ['demographic', 'behavioral', 'combined', 'custom', 'machine_learning'] })
  modelType: string;

  @Prop({ required: true, enum: ['draft', 'active', 'paused', 'archived'], index: true })
  status: string;

  @Prop({ required: true })
  version: number;

  @Prop({ type: Object, required: true })
  scoringConfig: {
    enableScoreDecay: boolean;
    decayRate: number;
    decayPeriodDays: number;
    minimumScoreThreshold: number;
    autoReScoreEnabled: boolean;
    reScoreIntervalDays: number;
    gradeConfigs: Array<{
      grade: string;
      minScore: number;
      maxScore: number;
      label: string;
      description: string;
      recommendedAction: string;
    }>;
  };

  @Prop({ type: [String], default: [] })
  ruleIds: string[];

  @Prop({ type: [String], default: [] })
  attributeIds: string[];

  @Prop({ type: [Object], default: [] })
  variants: Array<{
    id: string;
    name: string;
    description: string;
    percentage: number;
    isActive: boolean;
    createdAt: Date;
  }>;

  @Prop({ required: true, default: false, index: true })
  isDefault: boolean;

  @Prop({ required: true, default: false })
  isABTestEnabled: boolean;

  @Prop({ type: Date })
  testStartDate: Date;

  @Prop({ type: Date })
  testEndDate: Date;

  @Prop({ type: Object, default: {} })
  metadata: Record<string, any>;

  @Prop({ required: true })
  createdBy: string;

  @Prop({ required: true })
  updatedBy: string;

  @Prop({ required: true, type: Date })
  createdAt: Date;

  @Prop({ required: true, type: Date })
  updatedAt: Date;
}

export const ScoreModelSchemaModel = SchemaFactory.createForClass(ScoreModelSchema);

// Indexes
ScoreModelSchemaModel.index({ tenantId: 1, status: 1 });
ScoreModelSchemaModel.index({ tenantId: 1, isDefault: 1 });
ScoreModelSchemaModel.index({ tenantId: 1, name: 1, version: 1 }, { unique: true });
