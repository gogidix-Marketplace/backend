import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ timestamps: true, collection: 'event_store' })
export class EventDocument extends Document {
  @Prop({ required: true, unique: true }) eventId: string;
  @Prop({ required: true }) eventType: string;
  @Prop({ default: '1.0' }) eventVersion: string;
  @Prop({ required: true }) aggregateId: string;
  @Prop() aggregateType: string;
  @Prop({ default: 'default' }) tenantId: string;
  @Prop({ type: Object, default: {} }) payload: Record<string, unknown>;
  @Prop({ type: Object, default: {} }) metadata: Record<string, unknown>;
  @Prop() causationId?: string;
  @Prop() correlationId?: string;
  @Prop({ default: 1 }) version: number;
  @Prop({ default: 'PENDING', enum: ['PENDING', 'PROCESSING', 'COMPLETED', 'FAILED'] }) status: string;
  @Prop() error?: string;
  @Prop({ type: Date }) processedAt?: Date;
  @Prop({ default: 0 }) retryCount: number;
}
export const EventSchema = SchemaFactory.createForClass(EventDocument);
EventSchema.index({ aggregateId: 1, version: 1 });
EventSchema.index({ tenantId: 1, eventType: 1 });
EventSchema.index({ status: 1, retryCount: 1 });
