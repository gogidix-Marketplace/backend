import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class ReconciliationRuleDocument extends Document {
  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  name: string;

  @Prop()
  description?: string;

  @Prop({ required: true, enum: ['EXACT', 'FUZZY', 'AI_BASED', 'MANUAL'] })
  matchType: string;

  @Prop({ required: true, min: 1, max: 100 })
  priority: number;

  @Prop({ required: true, default: true })
  enabled: boolean;

  @Prop({ required: true, type: [Object] })
  conditions: Array<{
    field: string;
    operator: string;
    value: unknown;
    weight?: number;
  }>;

  @Prop({ required: true, type: [Object] })
  actions: Array<{
    type: string;
    params?: Record<string, unknown>;
  }>;

  @Prop({ min: 0, max: 1 })
  confidenceThreshold?: number;

  @Prop({ required: true })
  createdBy: string;
}

export const ReconciliationRuleSchema = SchemaFactory.createForClass(ReconciliationRuleDocument);

ReconciliationRuleSchema.index({ tenantId: 1, priority: -1 });
ReconciliationRuleSchema.index({ tenantId: 1, enabled: 1 });
