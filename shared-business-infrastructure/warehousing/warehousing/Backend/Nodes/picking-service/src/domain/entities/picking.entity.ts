import {
  Entity,
  Column,
  PrimaryGeneratedColumn,
  CreateDateColumn,
  UpdateDateColumn,
  OneToMany,
  ManyToOne,
  JoinColumn,
  Index,
} from 'typeorm';

export enum PickOrderStatus {
  PENDING = 'pending',
  ASSIGNED = 'assigned',
  IN_PROGRESS = 'in_progress',
  COMPLETED = 'completed',
  CANCELLED = 'cancelled',
}

export enum PickItemStatus {
  PENDING = 'pending',
  PICKED = 'picked',
  SKIPPED = 'skipped',
  SHORT = 'short',
}

export enum PickPriority {
  LOW = 'low',
  MEDIUM = 'medium',
  HIGH = 'high',
  URGENT = 'urgent',
}

export enum PickType {
  SINGLE = 'single',
  BATCH = 'batch',
  ZONE = 'zone',
  WAVE = 'wave',
}

@Entity('pick_items')
@Index(['pickOrderId'])
@Index(['zone'])
export class PickItem {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'uuid' })
  pickOrderId: string;

  @ManyToOne(() => PickOrder, (order) => order.items)
  @JoinColumn({ name: 'pickOrderId' })
  pickOrder: PickOrder;

  @Column()
  sku: string;

  @Column()
  productName: string;

  @Column({ type: 'int' })
  quantity: number;

  @Column()
  zone: string;

  @Column()
  aisle: string;

  @Column()
  shelf: string;

  @Column({ nullable: true })
  bin: string;

  @Column({
    type: 'enum',
    enum: PickItemStatus,
    default: PickItemStatus.PENDING,
  })
  status: PickItemStatus;

  @Column({ type: 'int', nullable: true })
  pickedQuantity: number;

  @Column({ type: 'timestamp', nullable: true })
  pickedAt: Date;

  @Column({ nullable: true })
  notes: string;

  @Column({ type: 'int', default: 0 })
  sortOrder: number;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}

@Entity('pick_orders')
@Index(['tenantId'])
@Index(['orderId'])
@Index(['pickerId'])
@Index(['status'])
@Index(['pickType'])
@Index(['batchId'])
@Index(['tenantId', 'status'])
@Index(['tenantId', 'pickerId', 'status'])
export class PickOrder {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'uuid' })
  tenantId: string;

  @Column({ nullable: true })
  orderId: string;

  @Column({ unique: true })
  pickOrderNumber: string;

  @Column({
    type: 'enum',
    enum: PickOrderStatus,
    default: PickOrderStatus.PENDING,
  })
  status: PickOrderStatus;

  @Column({
    type: 'enum',
    enum: PickType,
    default: PickType.SINGLE,
  })
  pickType: PickType;

  @Column({
    type: 'enum',
    enum: PickPriority,
    default: PickPriority.MEDIUM,
  })
  priority: PickPriority;

  @OneToMany(() => PickItem, (item) => item.pickOrder, { cascade: true })
  items: PickItem[];

  @Column({ type: 'uuid', nullable: true })
  pickerId: string;

  @Column({ nullable: true })
  pickerName: string;

  @Column({ nullable: true })
  warehouseId: string;

  @Column({ nullable: true })
  assignedZone: string;

  @Column({ type: 'uuid', nullable: true })
  batchId: string;

  @Column({ type: 'int', nullable: true })
  waveNumber: number;

  @Column({ type: 'int' })
  estimatedTimeMinutes: number;

  @Column({ type: 'int', nullable: true })
  actualTimeMinutes: number;

  @Column({ type: 'int' })
  totalItems: number;

  @Column({ type: 'int', default: 0 })
  pickedItems: number;

  @Column({ type: 'timestamp', nullable: true })
  assignedAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  startedAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  completedAt: Date;

  @Column({ nullable: true })
  notes: string;

  @Column({ nullable: true })
  cancellationReason: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}

@Entity('pick_batches')
@Index(['tenantId'])
@Index(['status'])
export class PickBatch {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'uuid' })
  tenantId: string;

  @Column({ unique: true })
  batchNumber: string;

  @Column({ type: 'uuid', array: true })
  pickOrderIds: string[];

  @Column({ type: 'int' })
  totalItems: number;

  @Column({ type: 'uuid', nullable: true })
  pickerId: string;

  @Column({
    type: 'enum',
    enum: PickOrderStatus,
    default: PickOrderStatus.PENDING,
  })
  status: PickOrderStatus;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}

@Entity('zone_optimizations')
@Index(['tenantId'])
@Index(['zoneId'])
export class ZoneOptimization {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'uuid' })
  tenantId: string;

  @Column()
  zoneId: string;

  @Column()
  zoneName: string;

  @Column({ type: 'int' })
  pendingPicks: number;

  @Column({ type: 'int' })
  estimatedTimeMinutes: number;

  @Column({ type: 'uuid', nullable: true })
  suggestedPickerId: string;

  @Column({ type: 'decimal', precision: 5, scale: 2, nullable: true })
  priorityScore: number;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;
}
