import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document, Types } from 'mongoose';

@Schema({ timestamps: true, collection: 'dashboards' })
export class DashboardDocument extends Document {
  @Prop({ required: true, unique: true }) dashboardId: string;
  @Prop({ required: true }) name: string;
  @Prop({ default: '' }) description: string;
  @Prop({ required: true, type: Types.ObjectId }) executiveId: Types.ObjectId;
  @Prop({ type: [{ widgetId: String, type: { type: String, enum: ['KPI','CHART','METRIC','TREND','GAUGE'] }, title: String, data: Object, position: { x: Number, y: Number, width: Number, height: Number }, config: Object }] }) widgets: any[];
  @Prop({ default: false }) isPublic: boolean;
  @Prop({ type: [{ userId: Types.ObjectId, permissions: { type: String, enum: ['VIEW','EDIT'], default: 'VIEW' } }] }) sharedWith: any[];
  @Prop({ type: Date, default: Date.now }) lastUpdated: Date;
}
export const DashboardSchema = SchemaFactory.createForClass(DashboardDocument);
DashboardSchema.index({ executiveId: 1, lastUpdated: -1 });
