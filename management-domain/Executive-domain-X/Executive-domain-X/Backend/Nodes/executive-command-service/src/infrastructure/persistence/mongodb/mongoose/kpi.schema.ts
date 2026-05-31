import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true, collection: 'kpi_metrics' })
export class KpiDocument extends Document {
  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  name: string;

  @Prop({ required: true, enum: ['FINANCIAL', 'OPERATIONAL', 'CUSTOMER', 'EMPLOYEE', 'ALL'] })
  category: string;

  @Prop({ default: 'ALL', enum: ['CEO', 'CFO', 'CTO', 'COO', 'ALL'] })
  executiveLevel: string;

  @Prop({ required: true })
  value: number;

  @Prop({ required: true })
  unit: string;

  @Prop({ required: true })
  period: string;

  @Prop()
  target?: number;

  @Prop()
  previousValue?: number;

  @Prop()
  percentChange?: number;

  @Prop({ default: 'ON_TRACK', enum: ['ON_TRACK', 'AT_RISK', 'BEHIND', 'AHEAD'] })
  status: string;

  @Prop({ enum: ['UP', 'DOWN', 'STABLE'] })
  trend?: string;

  @Prop({ type: [String], default: [] })
  dataSources: string[];

  @Prop({ type: Object, default: {} })
  metadata: Record<string, unknown>;

  @Prop({ default: true })
  visible: boolean;

  @Prop({ default: true })
  isCalculated: boolean;

  @Prop({ type: Date })
  lastCalculatedAt: Date;
}

export const KpiSchema = SchemaFactory.createForClass(KpiDocument);

KpiSchema.index({ tenantId: 1, category: 1, period: -1 });
KpiSchema.index({ tenantId: 1, executiveLevel: 1, category: 1, period: -1 });
KpiSchema.index({ tenantId: 1, period: -1 });
KpiSchema.index({ tenantId: 1, name: 1 });
