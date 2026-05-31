import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'complaint_records', timestamps: true })
export class ComplaintRecordDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true, index: true }) email: string;
  @Prop() complaintType: string;
  @Prop() userAgent: string;
  @Prop() providerMessageId: string;
  @Prop({ required: true }) provider: string;
}

export const ComplaintRecordSchema = SchemaFactory.createForClass(ComplaintRecordDocument);
