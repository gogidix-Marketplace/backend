import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'segments', timestamps: true })
export class SegmentDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true }) name: string;
  @Prop() description: string;
  @Prop([Object]) criteria: any[];
  @Prop({ default: 'and' }) criteriaLogic: string;
  @Prop() minScore: number;
  @Prop() maxScore: number;
  @Prop({ default: 0 }) memberCount: number;
  @Prop({ default: true }) isActive: boolean;
  @Prop({ default: false }) autoAssign: boolean;
}

export const SegmentSchema = SchemaFactory.createForClass(SegmentDocument);
