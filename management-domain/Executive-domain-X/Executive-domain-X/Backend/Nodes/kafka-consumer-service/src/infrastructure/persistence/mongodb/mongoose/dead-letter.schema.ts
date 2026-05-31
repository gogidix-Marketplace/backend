import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true, collection: 'dead_letter_queue' })
export class DeadLetterDocument extends Document {
  @Prop({ required: true }) dlqEventId: string;
  @Prop({ required: true }) originalEventId: string;
  @Prop({ required: true }) eventType: string;
  @Prop() tenantId: string;
  @Prop({ type: Object, default: {} }) payload: Record<string, unknown>;
  @Prop() error: string;
  @Prop({ default: 0 }) retryCount: number;
  @Prop({ default: 3 }) maxRetryAttempts: number;
  @Prop({ default: 'PENDING', enum: ['PENDING', 'RESOLVED', 'EXHAUSTED'] }) status: string;
  @Prop({ type: Object, default: {} }) metadata: Record<string, unknown>;
}
export const DeadLetterSchema = SchemaFactory.createForClass(DeadLetterDocument);
DeadLetterSchema.index({ status: 1, retryCount: 1 });
DeadLetterSchema.index({ tenantId: 1 });
