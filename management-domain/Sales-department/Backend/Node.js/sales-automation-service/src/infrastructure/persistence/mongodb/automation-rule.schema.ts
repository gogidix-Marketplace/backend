import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class AutomationRuleDocument extends Document {
  @Prop({ required: true })
  name: string;

  @Prop()
  description: string;

  @Prop({ required: true, enum: ['DRAFT', 'ACTIVE', 'PAUSED', 'ARCHIVED'], default: 'DRAFT' })
  status: string;

  @Prop({ required: true, type: Object })
  trigger: Record<string, any>;

  @Prop({ required: true, type: [Object] })
  actions: Array<Record<string, any>>;

  @Prop({ type: Object })
  leadScoring?: Record<string, any>;

  @Prop({ type: Object })
  dealStageRule?: Record<string, any>;

  @Prop({ type: [String], default: [] })
  tags: string[];

  @Prop({ default: 0 })
  priority: number;

  @Prop({ default: 0 })
  executionCount: number;

  @Prop()
  lastExecutedAt: Date;

  @Prop()
  lastExecutionStatus: string;

  @Prop()
  category: string;

  @Prop()
  templateId: string;

  @Prop({ default: false })
  isEnabled: boolean;

  @Prop({ type: Object })
  schedule?: Record<string, any>;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ default: 1 })
  version: number;
}

export const AutomationRuleSchema = SchemaFactory.createForClass(AutomationRuleDocument);

AutomationRuleSchema.index({ tenantId: 1, status: 1 });
AutomationRuleSchema.index({ tenantId: 1, category: 1 });
AutomationRuleSchema.index({ tenantId: 1, tags: 1 });
AutomationRuleSchema.index({ tenantId: 1, isEnabled: 1 });
