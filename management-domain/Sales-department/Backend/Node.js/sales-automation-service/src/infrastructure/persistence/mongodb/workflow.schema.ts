import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class WorkflowDocument extends Document {
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

  @Prop({ default: 0 })
  executionCount: number;

  @Prop()
  lastExecutedAt: Date;

  @Prop()
  lastExecutionStatus: string;

  @Prop({ type: [String], default: [] })
  tags: string[];

  @Prop({ default: 0 })
  priority: number;

  @Prop({ default: true })
  isEnabled: boolean;

  @Prop({ type: [Object], default: [] })
  executionHistory: Array<Record<string, any>>;

  @Prop({ default: 1 })
  maxConcurrentExecutions: number;

  @Prop()
  timeoutMs: number;

  @Prop({ required: true })
  tenantId: string;

  @Prop({ default: 1 })
  version: number;
}

export const WorkflowSchema = SchemaFactory.createForClass(WorkflowDocument);

WorkflowSchema.index({ tenantId: 1, status: 1 });
WorkflowSchema.index({ tenantId: 1, isEnabled: 1 });
WorkflowSchema.index({ tenantId: 1, tags: 1 });
WorkflowSchema.index({ 'trigger.configuration.type': 1 });
