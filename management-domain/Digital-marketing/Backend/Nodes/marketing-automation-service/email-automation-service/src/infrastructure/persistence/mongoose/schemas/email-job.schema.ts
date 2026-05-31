import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

@Schema({ collection: 'email_jobs', timestamps: true })
export class EmailJobDocument extends Document {
  @Prop() tenantId: string;
  @Prop() to: any;
  @Prop([String]) cc: string[];
  @Prop([String]) bcc: string[];
  @Prop({ required: true }) subject: string;
  @Prop() htmlBody: string;
  @Prop() textBody: string;
  @Prop() templateId: string;
  @Prop() templateData: any;
  @Prop({ required: true }) fromEmail: string;
  @Prop() fromName: string;
  @Prop() replyTo: string;
  @Prop() provider: string;
  @Prop({ default: 'pending' }) status: string;
  @Prop() providerMessageId: string;
  @Prop({ default: 0 }) attempts: number;
  @Prop({ default: 3 }) maxAttempts: number;
  @Prop() nextRetryAt: Date;
  @Prop() scheduledAt: Date;
  @Prop() sentAt: Date;
  @Prop() failureReason: string;
  @Prop() metadata: any;
  @Prop([String]) tags: string[];
}

export const EmailJobSchema = SchemaFactory.createForClass(EmailJobDocument);
