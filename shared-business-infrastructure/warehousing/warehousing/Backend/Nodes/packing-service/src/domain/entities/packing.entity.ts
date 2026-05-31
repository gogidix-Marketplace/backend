import {
  Entity,
  PrimaryGeneratedColumn,
  Column,
  CreateDateColumn,
  UpdateDateColumn,
  OneToMany,
  Index,
} from 'typeorm';
import { PackOrderStatus } from '../../application/dto/packing.dto';

@Entity('pack_orders')
@Index(['tenantId', 'status'])
@Index(['tenantId', 'packerId'])
@Index(['tenantId', 'pickOrderId'])
export class PackOrderEntity {
  @PrimaryGeneratedColumn('uuid')
  id: string;

  @Column({ type: 'uuid' })
  tenantId: string;

  @Column({ type: 'varchar', length: 50 })
  packOrderNumber: string;

  @Column({ type: 'uuid' })
  pickOrderId: string;

  @Column({ type: 'uuid', nullable: true })
  orderId: string;

  @Column({
    type: 'enum',
    enum: PackOrderStatus,
    default: PackOrderStatus.PENDING,
  })
  status: PackOrderStatus;

  @Column({ type: 'uuid', nullable: true })
  packerId: string;

  @Column({ type: 'varchar', length: 255, nullable: true })
  packerName: string;

  @Column({ type: 'jsonb' })
  items: any;

  @Column({ type: 'jsonb', nullable: true })
  packages: any;

  @Column({ type: 'jsonb', nullable: true })
  qualityChecks: any;

  @Column({ type: 'jsonb', nullable: true })
  materialsUsed: any;

  @Column({ type: 'decimal', precision: 10, scale: 2, nullable: true })
  totalWeight: number;

  @Column({ type: 'int', nullable: true })
  priority: number;

  @Column({ type: 'text', nullable: true })
  specialInstructions: string;

  @CreateDateColumn()
  createdAt: Date;

  @UpdateDateColumn()
  updatedAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  startedAt: Date;

  @Column({ type: 'timestamp', nullable: true })
  completedAt: Date;
}
