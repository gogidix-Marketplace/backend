import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  Index,
} from 'typeorm';

export enum StorageSpaceType {
  PALLET = 'PALLET',
  SHELF = 'SHELF',
  BIN = 'BIN',
  FLOOR = 'FLOOR',
  COLD_STORAGE = 'COLD_STORAGE',
  HAZARDOUS = 'HAZARDOUS',
  OVERSIZE = 'OVERSIZE',
}

export enum StorageSpaceStatus {
  AVAILABLE = 'AVAILABLE',
  OCCUPIED = 'OCCUPIED',
  RESERVED = 'RESERVED',
  MAINTENANCE = 'MAINTENANCE',
  INACTIVE = 'INACTIVE',
}

@Entity('storage_spaces')
@Index(['tenantId', 'warehouseId'])
@Index(['tenantId', 'zoneId'])
@Index(['tenantId', 'status'])
export class StorageSpace {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'uuid' })
  @Index()
  tenantId: string;

  @Column({ type: 'uuid' })
  @Index()
  warehouseId: string;

  @Column({ type: 'uuid', nullable: true })
  zoneId: string | null;

  @Column({ type: 'varchar', length: 100 })
  name: string;

  @Column({ type: 'varchar', length: 50, nullable: true })
  code: string | null;

  @Column({
    type: 'enum',
    enum: StorageSpaceType,
    default: StorageSpaceType.PALLET,
  })
  spaceType: StorageSpaceType;

  @Column({
    type: 'enum',
    enum: StorageSpaceStatus,
    default: StorageSpaceStatus.AVAILABLE,
  })
  status: StorageSpaceStatus;

  @Column({ type: 'decimal', precision: 10, scale: 2, default: 0 })
  capacityVolume: number;

  @Column({ type: 'decimal', precision: 10, scale: 2, default: 0 })
  capacityWeight: number;

  @Column({ type: 'decimal', precision: 10, scale: 2, default: 0 })
  usedVolume: number;

  @Column({ type: 'decimal', precision: 10, scale: 2, default: 0 })
  usedWeight: number;

  @Column({ type: 'decimal', precision: 10, scale: 2, nullable: true })
  length: number;

  @Column({ type: 'decimal', precision: 10, scale: 2, nullable: true })
  width: number;

  @Column({ type: 'decimal', precision: 10, scale: 2, nullable: true })
  height: number;

  @Column({ type: 'int', nullable: true })
  maxStackHeight: number;

  @Column({ type: 'decimal', precision: 5, scale: 2, nullable: true })
  temperatureMin: number;

  @Column({ type: 'decimal', precision: 5, scale: 2, nullable: true })
  temperatureMax: number;

  @Column({ type: 'decimal', precision: 5, scale: 2, nullable: true })
  humidityMin: number;

  @Column({ type: 'decimal', precision: 5, scale: 2, nullable: true })
  humidityMax: number;

  @Column({ type: 'jsonb', nullable: true })
  dimensions: {
    length: number;
    width: number;
    height: number;
    unit: string;
  };

  @Column({ type: 'jsonb', nullable: true })
  location: {
    aisle: string;
    rack: string;
    level: string;
    position: string;
  };

  @Column({ type: 'jsonb', nullable: true })
  attributes: Record<string, any>;

  @Column({ type: 'uuid', nullable: true })
  currentInventoryId: string | null;

  @Column({ type: 'uuid', nullable: true })
  currentProductId: string | null;

  @Column({ type: 'int', default: 0 })
  priority: number;

  @Column({ type: 'boolean', default: true })
  isActive: boolean;

  @Column({ type: 'text', nullable: true })
  notes: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;

  @Column({ type: 'uuid', nullable: true })
  createdBy: string | null;

  @Column({ type: 'uuid', nullable: true })
  updatedBy: string | null;

  get availableVolume(): number {
    return Math.max(0, this.capacityVolume - this.usedVolume);
  }

  get availableWeight(): number {
    return Math.max(0, this.capacityWeight - this.usedWeight);
  }

  get utilizationPercent(): number {
    if (this.capacityVolume === 0) return 0;
    return Math.round((this.usedVolume / this.capacityVolume) * 100);
  }

  get isAvailable(): boolean {
    return this.status === StorageSpaceStatus.AVAILABLE && this.isActive;
  }

  get isFullyOccupied(): boolean {
    return this.usedVolume >= this.capacityVolume || this.usedWeight >= this.capacityWeight;
  }
}
