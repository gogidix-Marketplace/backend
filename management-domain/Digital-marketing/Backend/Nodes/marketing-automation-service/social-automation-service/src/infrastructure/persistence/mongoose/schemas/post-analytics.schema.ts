import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'post_analytics', timestamps: true })
export class PostAnalyticsDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true }) accountId: string;
  @Prop() postId: string;
  @Prop({ required: true }) date: Date;
  @Prop({ required: true, enum: ['twitter', 'linkedin', 'facebook', 'instagram'] }) platform: string;
  @Prop({ type: Object, default: {} }) metrics: any;
}

export const PostAnalyticsSchema = SchemaFactory.createForClass(PostAnalyticsDocument);
PostAnalyticsSchema.index({ accountId: 1, date: -1 });
