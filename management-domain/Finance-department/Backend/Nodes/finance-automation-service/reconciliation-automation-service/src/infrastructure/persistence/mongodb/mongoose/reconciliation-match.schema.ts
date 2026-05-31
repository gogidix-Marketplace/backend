import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class ReconciliationMatchDocument extends Document {
  @Prop({ required: true })
  reconciliationId: string;

  @Prop({ required: true })
  tenantId: string;

  @Prop()
  bankTransactionId?: string;

  @Prop()
  internalTransactionId?: string;

  @Prop({ required: true, enum: ['EXACT', 'FUZZY', 'AI_BASED', 'MANUAL'] })
  matchType: string;

  @Prop({ required: true, min: 0, max: 1 })
  confidence: number;

  @Prop({ required: true, type: Date })
  matchDate: Date;

  @Prop({ required: true })
  matchedBy: string;

  @Prop({ default: false })
  verified?: boolean;

  @Prop()
  verifiedBy?: string;

  @Prop({ type: Date })
  verifiedAt?: Date;

  @Prop()
  notes?: string;

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

export const ReconciliationMatchSchema = SchemaFactory.createForClass(ReconciliationMatchDocument);

ReconciliationMatchSchema.index({ reconciliationId: 1 });
ReconciliationMatchSchema.index({ tenantId: 1, reconciliationId: 1 });
ReconciliationMatchSchema.index({ bankTransactionId: 1, internalTransactionId: 1 });
