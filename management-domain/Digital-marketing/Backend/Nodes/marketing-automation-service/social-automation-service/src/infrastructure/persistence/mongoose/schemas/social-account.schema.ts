import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'social_accounts', timestamps: true })
export class SocialAccountDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true, enum: ['twitter', 'linkedin', 'facebook', 'instagram'] }) platform: string;
  @Prop({ required: true }) platformUserId: string;
  @Prop({ required: true }) username: string;
  @Prop() displayName: string;
  @Prop() avatarUrl: string;
  @Prop({ type: Object, required: true }) tokens: any;
  @Prop({ default: true }) isActive: boolean;
  @Prop({ default: false }) isVerified: boolean;
  @Prop() followerCount: number;
  @Prop() followingCount: number;
  @Prop({ type: Object }) metadata: any;
  @Prop() lastSyncedAt: Date;
}

export const SocialAccountSchema = SchemaFactory.createForClass(SocialAccountDocument);
SocialAccountSchema.index({ platformUserId: 1, platform: 1 }, { unique: true });
