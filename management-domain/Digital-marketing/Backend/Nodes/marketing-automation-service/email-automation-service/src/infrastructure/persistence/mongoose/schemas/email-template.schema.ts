import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'email_templates', timestamps: true })
export class EmailTemplateDocument extends Document {
  @Prop({ required: true }) tenantId: string;
  @Prop({ required: true }) name: string;
  @Prop({ required: true, unique: true }) slug: string;
  @Prop({ required: true }) subject: string;
  @Prop({ required: true }) htmlContent: string;
  @Prop() textContent: string;
  @Prop([String]) variables: string[];
  @Prop() category: string;
  @Prop({ default: true }) isActive: boolean;
  @Prop({ default: 1 }) version: number;
}

export const EmailTemplateSchema = SchemaFactory.createForClass(EmailTemplateDocument);
