import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class TransactionDifferenceDocument extends Document {
  @Prop({ required: true })
  reconciliationId: string;

  @Prop({ required: true })
  tenantId: string;

  @Prop()
  bankTransactionId?: string;

  @Prop()
  internalTransactionId?: string;

  @Prop({
    required: true,
    enum: [
      'AMOUNT_MISMATCH',
      'MISSING_BANK_TRANSACTION',
      'MISSING_INTERNAL_TRANSACTION',
      'DUPLICATE_TRANSACTION',
      'TIMING_DIFFERENCE',
      'CURRENCY_MISMATCH',
      'COUNTERPARTY_MISMATCH',
      'REFERENCE_MISMATCH',
    ],
  })
  differenceType: string;

  @Prop({
    required: true,
    enum: ['PENDING_REVIEW', 'AUTO_RESOLVED', 'MANUALLY_RESOLVED', 'IGNORED', 'ESCALATED'],
  })
  status: string;

  @Prop()
  bankAmount?: number;

  @Prop()
  internalAmount?: number;

  @Prop()
  amountDifference?: number;

  @Prop()
  currency?: string;

  @Prop({ type: Date })
  bankDate?: Date;

  @Prop({ type: Date })
  internalDate?: Date;

  @Prop()
  description?: string;

  @Prop({ required: true, type: Date })
  detectedAt: Date;

  @Prop({ type: Date })
  resolvedAt?: Date;

  @Prop()
  resolvedBy?: string;

  @Prop()
  resolutionNotes?: string;

  @Prop({ type: Object })
  autoResolution?: {
    applied: boolean;
    rule?: string;
    action?: string;
    timestamp?: Date;
  };

  @Prop({ required: true, enum: ['LOW', 'MEDIUM', 'HIGH', 'CRITICAL'] })
  severity: string;

  @Prop()
  assignee?: string;

  @Prop({ type: Date })
  dueDate?: Date;

  @Prop({ type: Object })
  bankTransaction?: {
    id: string;
    amount: number;
    currency: string;
    date: Date;
    reference?: string;
    description?: string;
    counterparty?: string;
  };

  @Prop({ type: Object })
  internalTransaction?: {
    id: string;
    amount: number;
    currency: string;
    date: Date;
    reference?: string;
    description?: string;
    counterparty?: string;
  };

  @Prop({ type: Object })
  metadata?: Record<string, unknown>;
}

export const TransactionDifferenceSchema = SchemaFactory.createForClass(TransactionDifferenceDocument);

TransactionDifferenceSchema.index({ reconciliationId: 1 });
TransactionDifferenceSchema.index({ tenantId: 1, status: 1 });
TransactionDifferenceSchema.index({ tenantId: 1, severity: 1 });
TransactionDifferenceSchema.index({ dueDate: 1, status: 1 });
