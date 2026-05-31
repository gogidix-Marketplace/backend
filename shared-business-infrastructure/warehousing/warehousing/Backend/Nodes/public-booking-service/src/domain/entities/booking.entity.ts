import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  Index,
} from 'typeorm';

export enum BookingStatus {
  PENDING = 'PENDING',
  CONFIRMED = 'CONFIRMED',
  PAID = 'PAID',
  ACTIVE = 'ACTIVE',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  EXPIRED = 'EXPIRED',
}

export enum StorageType {
  STANDARD = 'STANDARD',
  CLIMATE_CONTROLLED = 'CLIMATE_CONTROLLED',
  HAZARDOUS = 'HAZARDOUS',
  COLD_STORAGE = 'COLD_STORAGE',
  HIGH_SECURITY = 'HIGH_SECURITY',
}

@Entity('bookings')
@Index(['tenantId', 'id'])
@Index(['tenantId', 'referenceNumber'])
@Index(['tenantId', 'warehouseId'])
@Index(['tenantId', 'status'])
@Index(['tenantId', 'createdAt'])
export class Booking {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'varchar', length: 100 })
  tenantId: string;

  @Column({ type: 'varchar', length: 50, unique: true })
  referenceNumber: string;

  @Column({ type: 'uuid' })
  warehouseId: string;

  @Column({ type: 'varchar', length: 255, nullable: true })
  warehouseName: string;

  @Column({
    type: 'enum',
    enum: StorageType,
    default: StorageType.STANDARD,
  })
  storageType: StorageType;

  @Column({ type: 'jsonb' })
  customer: {
    name: string;
    email: string;
    phone?: string;
    company?: string;
  };

  @Column({ type: 'jsonb' })
  items: Array<{
    description: string;
    quantity: number;
    volume: number;
    weight: number;
    category?: string;
    hazardous?: boolean;
  }>;

  @Column({ type: 'date' })
  startDate: Date;

  @Column({ type: 'date' })
  endDate: Date;

  @Column({ type: 'decimal', precision: 10, scale: 2 })
  totalVolume: number;

  @Column({ type: 'decimal', precision: 10, scale: 2 })
  totalWeight: number;

  @Column({ type: 'int' })
  durationDays: number;

  @Column({ type: 'decimal', precision: 12, scale: 2 })
  baseAmount: number;

  @Column({ type: 'decimal', precision: 12, scale: 2 })
  taxAmount: number;

  @Column({ type: 'decimal', precision: 12, scale: 2 })
  totalAmount: number;

  @Column({ type: 'varchar', length: 3, default: 'USD' })
  currency: string;

  @Column({
    type: 'enum',
    enum: BookingStatus,
    default: BookingStatus.PENDING,
  })
  status: BookingStatus;

  @Column({ type: 'text', nullable: true })
  specialInstructions: string;

  @Column({ type: 'varchar', length: 50, nullable: true })
  promotionalCode: string;

  @Column({ type: 'decimal', precision: 5, scale: 2, nullable: true })
  discountApplied: number;

  @Column({ type: 'uuid', nullable: true })
  paymentId: string;

  @Column({ type: 'varchar', length: 50, nullable: true })
  paymentMethod: string;

  @Column({ type: 'timestamp', nullable: true })
  confirmedAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  paidAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  cancelledAt: Date;

  @Column({ type: 'text', nullable: true })
  cancellationReason: string;

  @Column({ type: 'boolean', default: false })
  refundRequested: boolean;

  @Column({ type: 'uuid', nullable: true })
  refundId: string;

  @Column({ type: 'int', default: 0 })
  extensionCount: number;

  @Column({ type: 'int', default: 0 })
  totalExtendedDays: number;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  expiresAt: Date;
}
