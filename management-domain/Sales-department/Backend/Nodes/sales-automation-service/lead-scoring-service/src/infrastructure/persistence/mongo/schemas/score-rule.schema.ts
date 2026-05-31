import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export type ScoreRuleDocument = ScoreRuleSchema & Document;

@Schema({ collection: 'score_rules', timestamps: true })
export class ScoreRuleSchema {
  @Prop({ required: true, unique: true, index: true })
  _id: string;

  @Prop({ required: true, index: true })
  tenantId: string;

  @Prop({ required: true, index: true })
  scoreModelId: string;

  @Prop({ required: true })
  name: string;

  @Prop({ required: false })
  description: string;

  @Prop({ required: true, enum: ['condition', 'formula', 'machine_learning', 'external'] })
  ruleType: string;

  @Prop({ type: [Object], required: true })
  conditions: Array<{
    field: string;
    operator: string;
    value: any;
    weight?: number;
  }>;

  @Prop({ type: Object })
  formula: {
    expression: string;
    variables: string[];
  };

  @Prop({ required: true })
  baseScore: number;

  @Prop({ required: true })
  maxScore: number;

  @Prop({ required: true, default: 0 })
  priority: number;

  @Prop({ required: true, default: true, index: true })
  isActive: boolean;

  @Prop({ required: true, default: 'general' })
  category: string;

  @Prop({ type: [String], default: [] })
  tags: string[];

  @Prop({ type: Object, default: {} })
  metadata: Record<string, any>;

  @Prop({ required: true, type: Date })
  createdAt: Date;

  @Prop({ required: true, type: Date })
  updatedAt: Date;
}

export const ScoreRuleSchemaModel = SchemaFactory.createForClass(ScoreRuleSchema);

// Indexes
ScoreRuleSchemaModel.index({ tenantId: 1, scoreModelId: 1 });
ScoreRuleSchemaModel.index({ scoreModelId: 1, isActive: 1 });
ScoreRuleSchemaModel.index({ tenantId: 1, category: 1 });
