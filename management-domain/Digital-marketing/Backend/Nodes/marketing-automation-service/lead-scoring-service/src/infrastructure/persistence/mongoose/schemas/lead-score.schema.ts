import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'lead_scores', timestamps: true })
export class LeadScoreDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true, index: true }) leadId: string;
  @Prop({ default: 0 }) score: number;
  @Prop({ default: 0 }) previousScore: number;
  @Prop({ default: 100 }) maxScore: number;
  @Prop({ default: 0 }) minScore: number;
  @Prop({ type: Object, default: {} }) categoryScores: Record<string, number>;
  @Prop([Object]) history: any[];
  @Prop([Object]) flags: any[];
  @Prop() lastEventAt: Date;
  @Prop() lastCalculatedAt: Date;
  @Prop() expiresAt: Date;
  @Prop({ type: Object }) metadata: any;
}

export const LeadScoreSchema = SchemaFactory.createForClass(LeadScoreDocument);
LeadScoreSchema.index({ tenantId: 1, leadId: 1 }, { unique: true });
