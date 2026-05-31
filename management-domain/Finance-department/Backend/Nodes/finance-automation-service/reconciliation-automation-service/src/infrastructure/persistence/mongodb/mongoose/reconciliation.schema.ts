import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';

@Schema({ timestamps: true })
export class ReconciliationDocument extends Document {
  @Prop({ required: true })
  tenantId: string;

  @Prop({ required: true })
  name: string;

  @Prop()
  description?: string;

  @Prop({ required: true, enum: ['PENDING', 'RUNNING', 'COMPLETED', 'FAILED', 'PARTIAL'] })
  status: string;

  @Prop({ required: true, type: Date })
  startDate: Date;

  @Prop({ type: Date })
  endDate?: Date;

  @Prop({ required: true, enum: ['BANK', 'INTERNAL', 'BOTH'] })
  dataSourceType: string;

  @Prop()
  bankAccountId?: string;

  @Prop()
  internalAccountId?: string;

  @Prop({ type: [String], default: [] })
  ruleIds: string[];

  @Prop({ type: [Object] })
  matches?: Array<{
    id: string;
    bankTransactionId?: string;
    internalTransactionId?: string;
    matchType: string;
    confidence: number;
    matchDate: Date;
    verified?: boolean;
  }>;

  @Prop({ type: [Object] })
  differences?: Array<{
    id: string;
    differenceType: string;
    status: string;
    severity: string;
  }>;

  @Prop({ default: 0 })
  totalTransactionsProcessed: number;

  @Prop({ default: 0 })
  totalMatches: number;

  @Prop({ default: 0 })
  totalDifferences: number;

  @Prop({ default: 0 })
  autoResolvedCount: number;

  @Prop({ default: 0 })
  manualReviewCount: number;

  @Prop({ required: true })
  createdBy: string;

  @Prop()
  completedBy?: string;

  @Prop({ type: Date })
  completedAt?: Date;

  @Prop()
  errorDetails?: string;

  @Prop({ default: false })
  scheduled: boolean;

  @Prop()
  scheduleExpression?: string;

  @Prop({ type: Date })
  lastRunAt?: Date;

  @Prop({ type: Date })
  nextRunAt?: Date;

  @Prop({ type: Object })
  metadata?: Record<string, unknown>;
}

export const ReconciliationSchema = SchemaFactory.createForClass(ReconciliationDocument);

ReconciliationSchema.index({ tenantId: 1, createdAt: -1 });
ReconciliationSchema.index({ tenantId: 1, status: 1 });
ReconciliationSchema.index({ scheduled: 1, nextRunAt: 1 });
