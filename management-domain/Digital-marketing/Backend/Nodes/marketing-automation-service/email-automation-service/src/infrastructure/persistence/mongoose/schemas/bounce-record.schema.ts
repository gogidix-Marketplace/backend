import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'bounce_records', timestamps: true })
export class BounceRecordDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true, index: true }) email: string;
  @Prop({ required: true }) bounceType: string;
  @Prop() bounceSubType: string;
  @Prop() diagnosticCode: string;
  @Prop() providerMessageId: string;
  @Prop({ required: true }) provider: string;
}

export const BounceRecordSchema = SchemaFactory.createForClass(BounceRecordDocument);
