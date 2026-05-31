import { Prop, Schema, SchemaFactory } from '@nestjs/mongoose';
import { Document } from 'mongoose';

export interface InventoryItemDocument extends InventoryItem, Document {
  createdAt: Date;
  updatedAt: Date;
}

@Schema({ timestamps: true, collection: 'inventory_items' })
export class InventoryItem {
  @Prop({ required: true, index: true })
  tenantId: string;

  @Prop({ required: true, index: true })
  warehouseId: string;

  @Prop({ required: true, index: true })
  sku: string;

  @Prop()
  name: string;

  @Prop({ required: true, default: 0 })
  quantity: number;

  @Prop({ required: true, default: 0 })
  reservedQuantity: number;

  @Prop({ default: 10 })
  reorderPoint: number;

  @Prop({ default: 0 })
  reorderQuantity: number;

  @Prop({ default: 0 })
  unitCost: number;

  createdAt: Date;
  updatedAt: Date;
}

export const InventoryItemSchema = SchemaFactory.createForClass(InventoryItem);

InventoryItemSchema.index({ tenantId: 1, warehouseId: 1, sku: 1 }, { unique: true });
InventoryItemSchema.index({ tenantId: 1, warehouseId: 1 });
InventoryItemSchema.index({ quantity: 1 });

export interface StockMovementDocument extends StockMovement, Document {
  createdAt: Date;
}

@Schema({ timestamps: { createdAt: true, updatedAt: false }, collection: 'stock_movements' })
export class StockMovement {
  @Prop({ required: true, index: true })
  tenantId: string;

  @Prop({ required: true, index: true })
  itemId: string;

  @Prop({ required: true })
  sku: string;

  @Prop({ required: true, index: true })
  warehouseId: string;

  @Prop({ required: true, enum: ['received', 'shipped', 'adjusted', 'transferred_in', 'transferred_out', 'reserved', 'released'] })
  movementType: string;

  @Prop({ required: true })
  quantity: number;

  @Prop({ required: true })
  previousQuantity: number;

  @Prop({ required: true })
  newQuantity: number;

  @Prop()
  reference: string;

  @Prop()
  reason: string;
}

export const StockMovementSchema = SchemaFactory.createForClass(StockMovement);

StockMovementSchema.index({ tenantId: 1, warehouseId: 1, createdAt: -1 });
StockMovementSchema.index({ tenantId: 1, itemId: 1, createdAt: -1 });
