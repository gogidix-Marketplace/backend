import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true })
export class AuditLogDocument extends Document {
  @Prop({ required: true })
  reconciliationId: string;

  @Prop({ required: true })
  action: string;

  @Prop({ required: true })
  userId: string;

  @Prop({ required: true, type: Date })
  timestamp: Date;

  @Prop({ type: Object })
  details?: Record<string, unknown>;

  @Prop()
  ipAddress?: string;
}

export const AuditLogSchema = SchemaFactory.createForClass(AuditLogDocument);

AuditLogSchema.index({ reconciliationId: 1, timestamp: -1 });
AuditLogSchema.index({ userId: 1, timestamp: -1 });
