import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'scheduled_posts', timestamps: true })
export class ScheduledPostDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true }) accountId: string;
  @Prop({ required: true, enum: ['twitter', 'linkedin', 'facebook', 'instagram'] }) platform: string;
  @Prop({ required: true }) content: string;
  @Prop([String]) mediaUrls: string[];
  @Prop([String]) hashtags: string[];
  @Prop({ required: true }) scheduledAt: Date;
  @Prop() publishedAt: Date;
  @Prop({ default: 'draft', enum: ['draft', 'scheduled', 'publishing', 'published', 'failed'] }) status: string;
  @Prop() platformPostId: string;
  @Prop() platformPostUrl: string;
  @Prop() failureReason: string;
  @Prop({ type: Object }) metrics: any;
  @Prop({ type: Object }) metadata: any;
  @Prop({ default: 0 }) retryCount: number;
  @Prop({ default: 3 }) maxRetries: number;
}

export const ScheduledPostSchema = SchemaFactory.createForClass(ScheduledPostDocument);
ScheduledPostSchema.index({ status: 1, scheduledAt: 1 });
